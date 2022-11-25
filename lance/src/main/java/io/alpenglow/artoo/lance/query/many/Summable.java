package io.alpenglow.artoo.lance.query.many;

import io.alpenglow.artoo.lance.func.TryFunction;
import io.alpenglow.artoo.lance.query.One;
import io.alpenglow.artoo.lance.Queryable;
import io.alpenglow.artoo.lance.query.func.Sum;

public interface Summable<T> extends Queryable<T> {
  default <N extends Number> One<N> sum(final TryFunction<? super T, ? extends N> select) {
    return () -> cursor().map(new Sum<T, N, N>(select)).keepNull();
  }

  default One<T> sum() {
    return () -> cursor().map(new Sum<T, Number, T>(it -> it instanceof Number n ? n : null)).keepNull();
  }
}
