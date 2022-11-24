package lance.query.many;

import lance.func.Cons;
import lance.query.Many;
import lance.Queryable;
import lance.query.func.Peek;

public interface Peekable<T> extends Queryable<T> {
  default Many<T> peek(Cons.TryConsumer<? super T> peek) {
    return peek((index, it) -> peek.tryAccept(it));
  }

  default Many<T> peek(Cons.TryBiConsumer<? super Integer, ? super T> peek) {
    return () -> cursor().map(new Peek<T, T>(peek));
  }

  default Many<T> exceptionally(Cons.TryConsumer<? super Throwable> catch$) {
    return () -> cursor().exceptionally(catch$);
  }
}

