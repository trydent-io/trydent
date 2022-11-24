package lance.query.func;

import lance.func.Func;
import lance.func.Pred;

public final class Single<T> implements Func.TryFunction<T, T> {
  enum NoSingle {Found}

  private Object single = null;
  private final Pred.TryPredicate<? super T> where;

  public Single(final Pred.TryPredicate<? super T> where) {
    assert where != null;
    this.where = where;
  }

  @Override
  public final T tryApply(final T element) throws Throwable {
    if (where.tryTest(element) && single == null) {
      single = element;
    } else if (where.tryTest(element)) {
      single = NoSingle.Found;
    }

    //noinspection unchecked
    return NoSingle.Found.equals(single)
      ? null
      : (T) single;
  }
}
