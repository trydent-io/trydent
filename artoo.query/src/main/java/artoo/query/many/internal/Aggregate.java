package artoo.query.many.internal;

import artoo.cursor.Cursor;
import artoo.func.$2.Func;
import artoo.func.Cons;
import artoo.func.Pred;
import artoo.query.Queryable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public final class Aggregate<T, A, R> implements Queryable<A> {
  private final Queryable<T> queryable;
  private final Cons<? super T> peek;
  private final A seed;
  private final Pred<? super T> where;
  private final artoo.func.Func<? super T, ? extends R> select;
  private final Func<? super A, ? super R, ? extends A> aggregate;

  @Contract(pure = true)
  public Aggregate(
    final Queryable<T> queryable,
    final Cons<? super T> peek,
    final A seed,
    final Pred<? super T> where,
    final artoo.func.Func<? super T, ? extends R> select,
    final Func<? super A, ? super R, ? extends A> aggregate
  ) {
    this.queryable = queryable;
    this.peek = peek;
    this.seed = seed;
    this.where = where;
    this.select = select;
    this.aggregate = aggregate;
  }

  @NotNull
  @Override
  public final Iterator<A> iterator() {
    var aggregated = seed;
    for (final var it : queryable) {
      peek.accept(it);
      if (where.test(it)) {
        final var apply = select.apply(it);
        aggregated = aggregate.apply(aggregated, apply);
      }
    }
    return Cursor.of(aggregated);
  }
}