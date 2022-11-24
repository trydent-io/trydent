package lance.query.func;

import lance.func.Func;
import lance.func.Pred;

import java.util.ArrayList;
import java.util.Collection;

public final class Distinct<T> implements Func.TryFunction<T, T> {
  private final Pred.TryPredicate<? super T> where;
  private final Collection<T> collected;

  public Distinct(final Pred.TryPredicate<? super T> where) {
    assert where != null;
    this.where = where;
    this.collected = new ArrayList<>();
  }

  @Override
  public final T tryApply(final T element) throws Throwable {
    if (where.tryTest(element) && !collected.contains(element)) {
      collected.add(element);
      return element;
    } else if (where.tryTest(element) && collected.contains(element)) {
      return null;
    } else {
      return element;
    }
  }
}
