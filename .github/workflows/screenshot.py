#!/usr/bin/env python3
# -*- coding: utf-8 -*-
import base64
import datetime
import io
import logging
import os
import random
import re
import subprocess
from pathlib import Path
import tempfile

from pyvirtualdisplay.smartdisplay import SmartDisplay, DisplayTimeoutError

module_path = os.path.realpath(os.path.dirname(os.path.realpath(__file__)))

# FIXME: introduce grader configuration variables from the global config file ?

# Gradle binary to use if there is no gradle wrapper. It is supposed to be
# already available
GRADLE_BIN = ("~/.gradle/gradlew")

"""
Timeout value for gradle run and gradle tests.
"""
TIMEOUT = 30  # seconds

"""
Screen resolution of the virtual display.
"""
SCREENW, SCREENH = 1280, 720

"""
Shell to use by default.
"""
DEFAULT_SHELL = "/bin/sh"

"""
Regex definition of an allowed archive name, with groups matching on the
filename (without extension) and extension.
"""
ARCH_FORMAT = re.compile(
    r"^(.*)\.(zip|tar|tar\.gz|tar\.bz2|tar\.lzma|tar\.xz)$",
    flags=re.IGNORECASE)

"""
List of messages to be printed.
"""
# TODO Should use enums
MSGS = {
    "BAD_AR_NAME": ("<p>Mauvaise archive : %s<br/>"
                    "Le travail doit être livré sous la forme d’une "
                    "archive (.zip ou un format libre) portant, en majuscules "
                    "uniquement, votre nom.</p>"),
    "INT_DIR": "<p>L'archive doit posséder un dossier racine "
               "(dossier interne).</p>",
    "EMPTY_ARCHIVE": "<p>L'archive est vide.</p>",
    "BAD_NAME": "<p>Le dossier interne n'a pas le même nom que l'archive.</p>",
    "NO_REPORT": "<p>Pas de rapport ou pas au format pdf.</p>",
    "CLASSES": "<p>Présence de fichiers <code>.class</code>:</p>",
    "NO_BUILD": "<p>Pas de fichier <code>build.gradle</code>.</p>",
    "ERR_GRADLE": "<p>Erreur pour la commande gradle %s.</p>",
    "ERR_BUILD": "<p>Erreur pour la commande gradle build lors de la "
                 "compilation des classes principales.</p>",
    "ERR_BUILD_TEST": "<p>Erreur pour la commande gradle build lors de la "
                      "compilation des classes de test.</p>",
    "NOTE_BUILD_TEST": "<p>Note: L'erreur ci-dessus se produit également pour "
                       "la commande gradle build car celle-ci exécute "
                       "automatiquement les tests après la compilation.</p>",
    "ERR_CLEAN": "<p>La commande gradle clean ne supprime pas les .class.</p>",
    "SHELL": "Spawning a shell in the student's directory. 'exit' when done.",
    "NO_WRAPPER": "<p>Attention, pas de Gradle Wrapper (fichier "
                  "<code>gradlew</code>) trouvé.<br/>"
                  "Cela pourrait poser un problème sur les machines des "
                  "salles info selon la configuration de votre projet.<br/>"
                  "Assurez-vous que les fichiers <code>build.gradle</code> "
		  "(et <code>settings.gradle</code> si présent) soient dans "
		  "le même répertoire que le Gradle Wrapper (fichier "
		  "<code>gradlew</code>).<br/>"
                  "Pour ces tests, le système va tenter de générer un wrapper "
                  "automatiquement</p>",
    "WRAPPER_NO_EXEC": "<p>Attention, votre fichier <code>gradlew</code> "
                       "n'est pas exécutable. Le problème est arrangé ici "
                       "pour vous, mais vous devrez arranger le problème "
                       "le jour de la défense.</p>",
    "WRAPPER_CRLF": "<p>Votre fichier <code>gradlew</code> semble utiliser "
                    "des fins de lignes Windows (CRLF, \\r\\n). Il devrait "
                    "utiliser LF (\\n). Ce n'est pas normal, il a dû être "
                    "modifié entre sa génération par gradle et la création de "
                    "votre archive.<br/>Cela va très probablement faire "
                    "planter son exécution.</p>",
    "ERR_WRAPPER_GEN": "<p>Erreur lors de la génération du Gradle wrapper "
                       "manquant.</p>",
    "MULT_BUILD": "<p>Il y a plus d'un fichier <code>build.gradle</code>. Les "
                  "tests s'effectueront pour chaque fichier.",
    "BUILD_OK": "<p>Le fichier %s n'a pas donné d'erreurs.",
    "ERR_EXTRACT": "<p>L'archive est corrompue. Il n'est pas possible de "
                   "l'extraire.",
    "ERR_TIMEOUT": "<p>La commande gradle %s a été stoppée après %d secondes "
                   "car elle prenait trop de temps.</p>",
    "ERR_NO_DISPLAY": "<p>Rien n'est apparu à l'écran dans les %d "
                      "secondes après avoir lancé la "
                      "commande <code>gradle %s</code>, contrairement à ce qui "
                      "était attendu.</p>",
    "INFO_SCREENSHOT": "<p>Pour information, voici une capture d'écran "
                       "automatique de votre application. La résolution "
                       "maximale de l'écran est %dx%d.</p>"
                       "<p><img src=\"@@PLUGINFILE@@/%s\" alt=\"screenshot\" "
                       "class=\"img-fluid atto_image_button_middle\"></p>"
                       "<p>Notez que cet aperçu est expérimental. L'affichage "
                       "réel pourrait être différent.</p>"
}

logger = logging.getLogger(__name__)


def run_gradle(command, timeout=300, allowed_exit_codes=[0], wrapper=False,
               expected_display=False):
    """
    Runs the given gradle command (gradle {command}). Timeouts after given time
    (in seconds). Uses the gradle wrapper (gradlew) if asked. If the command
    succeeds or timeouts, returns True. returns False otherwise.
    """
    gradle_cmd = "./gradlew" if wrapper else GRADLE_BIN

    # We use console plain to avoid the color formatting and fancy terminal
    # output to mess up our output message to students.p
    cmd = (  # f'firejail --quiet --profile={firejail_profile} '
        # f'--timeout=00:00:{2 * timeout} '
        f'{gradle_cmd} --no-daemon --console plain '
        f'{command} '
    )
    with SmartDisplay(manage_global_env=False, size=(SCREENW, SCREENH)) as disp:
        logger.debug(f'Run process command "{cmd}"')
        gradle = subprocess.Popen(
            cmd,
            env=disp.env(),
            shell=True,
            stdout=subprocess.PIPE,
            stderr=subprocess.STDOUT)
        msg = ""
        if expected_display:
            try:
                # Wait for something to be displayed. The library waits and
                # crops the output.
                img = disp.waitgrab(timeout=timeout)
                timestamp = int(datetime.datetime.now().timestamp())
                # Take a screenshot of the app output and add it to the
                # message.
                # Generate a unique filename.
                suffix = base64.urlsafe_b64encode(random.getrandbits(
                    24).to_bytes(3, "little")).decode("utf8")
                filename = f"screenshot-{timestamp}-{suffix}.png"
                msg += MSGS["INFO_SCREENSHOT"] % (
                    SCREENW, SCREENH, filename)
                data = io.BytesIO()
                img.save(data, format='PNG')
                # create a temporary directory to store the image
                img_path = Path(tempfile.mkdtemp())
                img_path.mkdir(parents=True, exist_ok=True)
                img.save("/tmp/screen.png")
                print("Saved at:", str(img_path / filename))
                
                # As soon as we get something displayed on the output,
                # we signal it is good and return.
                gradle.kill()
                return True, msg
            except DisplayTimeoutError:
                msg += MSGS["ERR_NO_DISPLAY"] % (timeout, command)
        try:
            out, _ = gradle.communicate(None, timeout)
            res = gradle.poll()
            if res not in allowed_exit_codes:
                msg += "\n<pre>" + out.decode("UTF8") + "</pre>"
        except subprocess.TimeoutExpired:
            gradle.kill()
            out, _ = gradle.communicate()
            msg += MSGS["ERR_TIMEOUT"] % (command, timeout)
            msg += "\n<pre>" + out.decode("UTF8") + "</pre>"
            gradle.wait()
            res = 124  # To emulate the timeout command
        return res in allowed_exit_codes, msg


def main():
    res, msg = run_gradle("run", wrapper=True, expected_display=True)
    if not res:
        print(msg)

if __name__ == '__main__':
    main()
