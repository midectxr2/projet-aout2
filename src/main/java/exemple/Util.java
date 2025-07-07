package exemple;

public class Util {
    public static int theAnswer() {
        /* Think for 7.5 million years
         * (but in tenths of micro-seconds, we're in 2020 !). */
        try {
            Thread.sleep(750);
        }
        catch (InterruptedException e) {
            /* Someone woke us up ? Whatever, we know the answer anyway. */
        }
        return 42;
    }
}
