package huzpsb.ll4j.utils.random;

import java.util.concurrent.ThreadLocalRandom;

public abstract class RandomSeedGenerator {
    public static final class Builtin {
        public static final RandomSeedGenerator CONSTANT = new ConstantRandomGenerator(1145141919810L), RANDOM = new RandomGenerator();
    }

    public abstract long generateSeed();

    public static class ConstantRandomGenerator extends RandomSeedGenerator {
        private final long seed;

        public ConstantRandomGenerator(long seed) {
            this.seed = seed;
        }

        @Override
        public long generateSeed() {
            return seed;
        }
    }

    public static class RandomGenerator extends RandomSeedGenerator {
        @Override
        public long generateSeed() {
            return ThreadLocalRandom.current().nextLong();
        }
    }
}
