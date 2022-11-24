package lance.query.many;

import lance.func.Pred;
import lance.query.Many;
import lance.Queryable;
import lance.query.func.Skip;
import lance.query.func.Take;

public interface Partitionable<T> extends Queryable<T> {
  default Many<T> skip(final int until) {
    return skipWhile((index, it) -> index < until);
  }

  default Many<T> skipWhile(final Pred.TryPredicate<? super T> where) {
    return skipWhile((index, it) -> where.test(it));
  }

  default Many<T> skipWhile(final Pred.TryBiPredicate<? super Integer, ? super T> where) {
    return () -> cursor().map(new Skip<T, T>(where));
  }

  default Many<T> take(final int until) {
    return takeWhile((index, it) -> index < until);
  }

  default Many<T> takeWhile(final Pred.TryPredicate<? super T> where) {
    return takeWhile((index, param) -> where.test(param));
  }

  default Many<T> takeWhile(final Pred.TryBiPredicate<? super Integer, ? super T> where) {
    return () -> cursor().map(new Take<T, T>(where));
  }
}
