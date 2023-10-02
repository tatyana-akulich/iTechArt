package by.itechart.util;

import java.util.Random;

public class RandomGenerator {
    public static int generateInt(int bound) {
        return new Random().nextInt(bound) + 1;
    }
}
