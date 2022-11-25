package io.alpenglow.artoo.lance.tuple;

import io.alpenglow.artoo.lance.func.TryBiConsumer;
import io.alpenglow.artoo.lance.func.TryBiFunction;

import static io.alpenglow.artoo.lance.tuple.Type.firstOf;
import static io.alpenglow.artoo.lance.tuple.Type.has;
import static io.alpenglow.artoo.lance.tuple.Type.secondOf;


public interface Pair<A, B> extends Tuple {
  default A first() { return firstOf(this); }
  default B second() { return secondOf(this); }

  default <R extends Record> R to(final TryBiFunction<? super A, ? super B, ? extends R> to) {
    return to.apply(first(), second());
  }

  default <T> T as(final TryBiFunction<? super A, ? super B, ? extends T> as) {
    return as.apply(first(), second());
  }

  default boolean is(final A value1, final B value2) {
    return has(first(), value1) && has(second(), value2);
  }

  default <R extends Record & Pair<A, B>> R map(TryBiFunction<? super A, ? super B, ? extends R> map) {
    return map.apply(first(), second());
  }

  default <R extends Record & Pair<A, B>, F extends Record & Single<R>> R flatMap(TryBiFunction<? super A, ? super B, ? extends F> func) {
    return func.apply(first(), second()).first();
  }

  default Pair<A, B> peek(TryBiConsumer<? super A, ? super B> cons) {
    cons.accept(first(), second());
    return this;
  }
}