package io.alpenglow.artoo.lance.query.cursor.routine.join;

import io.alpenglow.artoo.lance.func.TryPredicate2;
import io.alpenglow.artoo.lance.func.TryFunction1;
import io.alpenglow.artoo.lance.query.Cursor;
import io.alpenglow.artoo.lance.query.Repeatable;
import io.alpenglow.artoo.lance.tuple.Pair;

import java.util.Iterator;
import java.util.Objects;

final class Inner<A, B> implements Join<A, Cursor<Pair<A, B>>> {
  private final Cursor<B> cursor;
  private final TryPredicate2<? super A, ? super B> pred;

  Inner(final Cursor<B> cursor) {
    this(
      cursor,
      Objects::equals
    );
  }
  Inner(final Cursor<B> cursor, final TryPredicate2<? super A, ? super B> pred) {this.cursor = cursor;
    this.pred = pred;
  }

  @Override
  public TryFunction1<A[], Cursor<Pair<A, B>>> onArray() {
    return ts -> cursor.to(new Nested<>(ts, pred));
  }

  @Override
  public TryFunction1<Repeatable<A>, Cursor<Pair<A, B>>> onLiterator() {
    return null;
  }

  @Override
  public TryFunction1<Iterator<A>, Cursor<Pair<A, B>>> onIterator() {
    return null;
  }
}