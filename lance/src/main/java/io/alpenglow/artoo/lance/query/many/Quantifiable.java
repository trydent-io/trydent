package io.alpenglow.artoo.lance.query.many;

import io.alpenglow.artoo.lance.func.TryPredicate;
import io.alpenglow.artoo.lance.literator.Cursor;
import io.alpenglow.artoo.lance.query.One;
import io.alpenglow.artoo.lance.Queryable;
import io.alpenglow.artoo.lance.query.func.Every;
import io.alpenglow.artoo.lance.query.func.Some;
import io.alpenglow.artoo.lance.query.func.None;

public interface Quantifiable<T> extends Queryable<T> {
  default <R> One<Boolean> every(final Class<R> type) {
    return every(type::isInstance);
  }

  default One<Boolean> every(final TryPredicate<? super T> where) {
    return () -> cursor().map(new Every<>(where)).keepNull();
  }

  default <R> One<Boolean> none(final Class<R> type) {
    return none(type::isInstance);
  }

  default One<Boolean> none(final TryPredicate<? super T> where) {
    return () -> cursor().map(new None<>(where)).keepNull();
  }

  default One<Boolean> some() { return this.some(t -> true); }

  default One<Boolean> some(final TryPredicate<? super T> where) {
    return () -> cursor().map(new Some<>(where)).keepNull().or(() -> Cursor.open(false));
  }
}


