package lance.scope.random;

import lance.func.Func;
import lance.literator.Cursor;
import lance.query.One;

import java.util.concurrent.atomic.AtomicLong;

sealed public interface Scramble extends One<Scramble.Seed> {
  record Seed(long value, long mul, long mask) {}

  <R> R apply(Func.TryTriFunction<? super AtomicLong, ? super Long, ? super Long, ? extends R> func);

  final class Initial implements Scramble {
    private static final long multiplier = 0x5DEECE66DL;
    private static final long mask = (1L << 48) - 1;

    private final AtomicLong seed;

    public Initial(final long seed) {
      this(new AtomicLong((seed ^ multiplier) * mask));
    }
    private Initial(final AtomicLong seed) {this.seed = seed;}

    @Override
    public <R> R apply(final Func.TryTriFunction<? super AtomicLong, ? super Long, ? super Long, ? extends R> func) {
      return func.apply(seed, multiplier, mask);
    }

    @Override
    public Cursor<Seed> cursor() {
      return Cursor.open(new Seed(seed.get(), multiplier, mask));
    }
  }
}
