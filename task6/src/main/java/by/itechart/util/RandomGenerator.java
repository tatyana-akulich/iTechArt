package by.itechart.util;

import java.util.Random;

public class RandomGenerator {
    public static int generateInt(int fromIncluding, int toIncluding) {
        return new Random().nextInt(toIncluding + 1 - fromIncluding) + fromIncluding;
    }
}
