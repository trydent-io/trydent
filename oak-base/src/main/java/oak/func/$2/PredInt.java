package oak.func.$2;

import oak.func.Functional;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface PredInt<T> extends Pred<Integer, T>, Func<Integer, T, Boolean>, Functional.Pre {
  @NotNull
  @Contract(pure = true)
  static <T> PredInt<T> tautology() {
    return (index, it) -> true;
  }

  @NotNull
  @Contract(pure = true)
  static <T> PredInt<T> not(final PredInt<T> pred) {
    return (index, it) -> !pred.apply(index, it);
  }

  boolean verify(final int index, final T it);

  @Override
  default Boolean apply(Integer index, T it) {
    return verify(index, it);
  }

  @Override
  default boolean test(Integer index, T it) {
    return verify(index, it);
  }
}