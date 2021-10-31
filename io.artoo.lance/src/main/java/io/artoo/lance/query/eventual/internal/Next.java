package io.artoo.lance.query.eventual.internal;

import io.artoo.lance.func.Func;

public interface Next<T> {
  void await();
  <P> Next<P> select(final Func.MaybeFunction<? super T, ? extends P> select);
}
