package exemple;
import exemple.Util;

import java.util.Random;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.Collectors;

/* You don't have to understand any of this. */
public class Stat
{
    public static void statistics(int n, Supplier<Number> sample)
    {
        /* Sample in parallel and accumulate the results in a List. */
        List<Double> values = Stream.generate(sample)
            .parallel().limit(n)
            .map(Number::doubleValue)
            .collect(Collectors.toList());

        /* Compute the statistic descriptors. */
        double mean = values.stream().reduce(0.0, Double::sum) / n;
        double stddev = Math.sqrt(values.stream()
                .map(x -> x - mean)
                .map(x -> x * x)
                .reduce(0.0, Double::sum) / n);

        /* Display the descriptive statistics. */
        System.out.println("Sample size: " + n);
        System.out.println("Mean: " + mean);
        System.out.println("Standard deviation: " + stddev);
        /* We could add range, quartiles and whatnots. */
    }

    public static void main(String[] argv)
    {
        Random rnd = new Random();
        System.out.println("Let's see if Deep Thought is consistant:");
        statistics(20, Util::theAnswer);
        System.out.println("");
        System.out.println("Sample random :");
        statistics(100000, rnd::nextDouble);
    }
}
