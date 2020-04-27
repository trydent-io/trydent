package trydent.query.internal;

import trydent.func.$2.ConsInt;
import trydent.func.$2.FuncInt;
import trydent.query.Queryable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;

public final class SelectQueryable<T, R, Q extends Queryable<R>> implements Queryable<R> {
  private final Queryable<T> queryable;
  private final ConsInt<? super T> peek;
  private final FuncInt<? super T, ? extends Q> select;

  @Contract(pure = true)
  public SelectQueryable(final Queryable<T> queryable, final FuncInt<? super T, ? extends Q> select) {
    this(queryable, null, select);
  }
  @Contract(pure = true)
  public SelectQueryable(final Queryable<T> queryable, final ConsInt<? super T> peek, final FuncInt<? super T, ? extends Q> select) {
    this.queryable = queryable;
    this.peek = peek;
    this.select = select;
  }

  @NotNull
  @Override
  public final Iterator<R> iterator() {
    final var array = new ArrayList<R>();
    var index = 0;
    for (var cursor = queryable.iterator(); cursor.hasNext(); index++) {
      var it = cursor.next();
      peek.applyInt(index, it);
      if (it != null) {
        final var queried = select.applyInt(index, it);
        if (queried != null) {
          for (final var selected : queried) {
            if (selected != null) array.add(selected);
          }
        }
      }
    }
    return array.iterator();
  }
}