package io.alpenglow.artoo.lance.query.many.oftwo;

import io.alpenglow.artoo.lance.func.TryBiPredicate;
import io.alpenglow.artoo.lance.func.TryTriPredicate;
import io.alpenglow.artoo.lance.query.Many;
import io.alpenglow.artoo.lance.Queryable;
import io.alpenglow.artoo.lance.query.func.Skip;
import io.alpenglow.artoo.lance.query.func.Take;
import io.alpenglow.artoo.lance.tuple.Pair;

public interface Partitionable<A, B> extends Queryable.OfTwo<A, B> {
  default Many.OfTwo<A, B> skip(final int until) {
    return skipWhile((index, first, second) -> index < until);
  }

  default Many.OfTwo<A, B> skipWhile(final TryBiPredicate<? super A, ? super B> where) {
    return skipWhile((index, first, second) -> where.test(first, second));
  }

  default Many.OfTwo<A, B> skipWhile(final TryTriPredicate<? super Integer, ? super A, ? super B> where) {
    return () -> cursor().map(new Skip<Pair<A, B>, Pair<A, B>>((index, pair) -> where.tryTest(index, pair.first(), pair.second())));
  }

  default Many.OfTwo<A, B> take(final int until) {
    return takeWhile((index, first, second) -> index < until);
  }

  default Many.OfTwo<A, B> takeWhile(final TryBiPredicate<? super A, ? super B> where) {
    return takeWhile((index, first, second) -> where.test(first, second));
  }

  default Many.OfTwo<A, B> takeWhile(final TryTriPredicate<? super Integer, ? super A, ? super B> where) {
    return () -> cursor().map(new Take<Pair<A, B>, Pair<A, B>>((index, pair) -> where.tryTest(index, pair.first(), pair.second())));
  }
}
