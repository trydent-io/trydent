package io.alpenglow.artoo.lance.query.many.oftwo;

import io.alpenglow.artoo.lance.func.TryBiPredicate;
import io.alpenglow.artoo.lance.literator.Cursor;
import io.alpenglow.artoo.lance.query.One;
import io.alpenglow.artoo.lance.Queryable;
import io.alpenglow.artoo.lance.query.func.Every;
import io.alpenglow.artoo.lance.query.func.Some;

public interface Quantifiable<A, B> extends Queryable.OfTwo<A, B> {
  default <X, Y> One<Boolean> all(final Class<X> type1, final Class<Y> type2) {
    return all((first, second) -> type1.isInstance(first) && type2.isInstance(type2));
  }

  default One<Boolean> all(final TryBiPredicate<? super A, ? super B> where) {
    return () -> cursor().map(new Every<>(pair -> where.tryTest(pair.first(), pair.second()))).keepNull();
  }

  default One<Boolean> any() { return this.any((first, second) -> true); }

  default One<Boolean> any(final TryBiPredicate<? super A, ? super B> where) {
    return () -> cursor().map(new Some<>(pair -> where.tryTest(pair.first(), pair.second()))).keepNull().or(() -> Cursor.open(false));
  }
}

