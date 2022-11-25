package io.alpenglow.artoo.lance.query.many.oftwo;

import io.alpenglow.artoo.lance.func.TryBiFunction;
import io.alpenglow.artoo.lance.func.TryTriFunction;
import io.alpenglow.artoo.lance.func.tail.Select;
import io.alpenglow.artoo.lance.query.Many;
import io.alpenglow.artoo.lance.Queryable;
import io.alpenglow.artoo.lance.tuple.Pair;

public interface Projectable<A, B> extends Queryable.OfTwo<A, B> {
  default <R> Many<R> select(TryTriFunction<? super Integer, ? super A, ? super B, ? extends R> select) {
    return () -> cursor().map(rec(Select.with((index, record) -> select.apply(index, record.first(), record.second()))));
  }

  default <R> Many<R> select(TryBiFunction<? super A, ? super B, ? extends R> select) {
    return select((index, first, second) -> select.tryApply(first, second));
  }

  default <R, Q extends Queryable<R>> Many<R> selection(TryTriFunction<? super Integer, ? super A, ? super B, ? extends Q> select) {
    return () -> cursor().map(rec(Select.with(((i, pair) -> select.tryApply(i, pair.first(), pair.second()))))).flatMap(Queryable::cursor);
  }

  default <R, Q extends Queryable<R>> Many<R> selection(TryBiFunction<? super A, ? super B, ? extends Q> select) {
    return selection((i, first, second) -> select.tryApply(first, second));
  }

  default <P extends Pair<A, B>> Many.OfTwo<A, B> to(TryBiFunction<? super A, ? super B, ? extends P> func) {
    return () -> cursor().map(pair -> func.tryApply(pair.first(), pair.second()));
  }

  default <Q extends Queryable.OfTwo<A, B>> Many.OfTwo<A, B> too(TryBiFunction<? super A, ? super B, ? extends Q> func) {
    return () -> cursor().flatMap(pair -> func.tryApply(pair.first(), pair.second()).cursor());
  }

}