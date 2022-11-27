package io.alpenglow.artoo.lance.query.func;

import io.alpenglow.artoo.lance.func.TryFunction1;

public final class NotOfType<T, R> implements TryFunction1<T, T> {
  private final Class<? extends R> type;

  public NotOfType(final Class<? extends R> type) {
    assert type != null;
    this.type = type;
  }

  @Override
  public final T tryApply(final T element) {
    return !type.isInstance(element) ? element : null;
  }
}
