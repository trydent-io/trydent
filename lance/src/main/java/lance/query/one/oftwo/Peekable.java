package lance.query.one.oftwo;

import lance.func.Cons;
import lance.query.One;
import lance.Queryable;
import lance.query.func.Peek;
import lance.tuple.Pair;

public interface Peekable<A, B> extends Queryable.OfTwo<A, B> {
  default One.OfTwo<A, B> peek(Cons.TryBiConsumer<? super A, ? super B> peek) {
    return peek((index, first, second) -> peek.tryAccept(first, second));
  }

  default One.OfTwo<A, B> peek(Cons.TryTriConsumer<? super Integer, ? super A, ? super B> peek) {
    return () -> cursor().map(new Peek<Pair<A, B>, Pair<A, B>>((index, record) -> peek.tryAccept(index, record.first(), record.second())));
  }

  default One.OfTwo<A, B> exceptionally(Cons.TryConsumer<? super Throwable> catch$) {
    return () -> cursor().exceptionally(catch$);
  }
}

