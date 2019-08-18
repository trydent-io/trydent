package oak.collect.query;

import oak.collect.query.Projection.IndexFunction1;
import oak.func.con.Consumer1;
import oak.func.fun.Function1;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Objects.requireNonNull;

public interface Projection<T, M extends Iterable<T>> extends Functor<T, M> {
  static <S, R> Queryable<R> select(final Queryable<S> some, final Function1<S, R> map) {
    return new SelectMany<>(
      requireNonNull(some, "Some is null"),
      requireNonNull(map, "Map is null")
    );
  }

  static <S, R> Queryable<R> selectMany(final Queryable<S> some, final IndexManyFunction1<S, R> indexMap) {
    return new SelectManyMany<>(
      new SelectIndex<>(
        requireNonNull(some, "Some is null"),
        requireNonNull(indexMap, "IndexMap is null")
      )
    );
  }

  static <S, R> Queryable<R> selectMany(final Queryable<S> some, final Queryable.ManyFunction<S, R> flatMap) {
    return new SelectManyMany<>(
      new SelectMany<>(
        requireNonNull(some, "Some is null"),
        requireNonNull(flatMap, "FlatMap is null")
      )
    );
  }

  static <S> Queryable<S> look(final Queryable<S> some, final Consumer1<S> peek) {
    return new LookMany<>(
      requireNonNull(some, "Some is null"),
      requireNonNull(peek, "Peek is null")
    );
  }

  static <S, R> Maybe<R> select(final Maybe<S> maybe, final Function1<S, R> map) {
    return new SelectMaybe<>(
      requireNonNull(maybe, "Maybe is null"),
      requireNonNull(map, "Map is null")
    );
  }

  static <S, R> Maybe<R> selectJust(final Maybe<S> maybe, final Maybe.MaybeFunction1<S, R> flatMap) {
    return new SelectJust<>(
      new SelectMaybe<>(
        requireNonNull(maybe, "Maybe is null"),
        requireNonNull(flatMap, "FlatMap is null")
      )
    );
  }

  static <S> Maybe<S> look(final Maybe<S> maybe, final Consumer1<S> peek) {
    return new LookMaybe<>(
      requireNonNull(maybe, "Maybe is null"),
      requireNonNull(peek, "Peek is null")
    );
  }

  static <S, R> Queryable<R> selectIndex(final Queryable<S> many, final IndexFunction1<S, R> indexMap) {
    return new SelectIndex<>(
      requireNonNull(many, "Some is null"),
      requireNonNull(indexMap, "IndexMap is null")
    );
  }

  @FunctionalInterface
  interface IndexFunction1<S, R> {
    R apply(int index, S value);
  }

  @FunctionalInterface
  interface IndexManyFunction1<S, R> extends IndexFunction1<S, Queryable<R>> {}

  final class LookMany<T> implements Queryable<T> {
    private final Queryable<T> some;
    private final Consumer1<T> peek;

    LookMany(final Queryable<T> some, final Consumer1<T> peek) {
      this.some = some;
      this.peek = peek;
    }

    @Override
    @SuppressWarnings("unchecked")
    public final Iterator<T> iterator() {
      for (final var value : some) peek.accept(value);
      return some.iterator();
    }
  }
}

final class LookMaybe<S> implements Maybe<S> {
  private final Maybe<S> maybe;
  private final Consumer1<S> peek;

  @Contract(pure = true)
  LookMaybe(final Maybe<S> maybe, final Consumer1<S> peek) {
    this.maybe = maybe;
    this.peek = peek;
  }

  @Override
  public final S get() {
    if (maybe.iterator().hasNext()) peek.accept(maybe.iterator().next());
    return maybe.iterator().next();
  }
}

final class SelectIndex<S, R> implements Queryable<R> {
  private final Queryable<S> some;
  private final IndexFunction1<S, R> indexMap;

  @Contract(pure = true)
  SelectIndex(final Queryable<S> some, final IndexFunction1<S, R> indexMap) {
    this.some = some;
    this.indexMap = indexMap;
  }

  @NotNull
  @Override
  public final Iterator<R> iterator() {
    final var idx = new AtomicInteger(0);
    return some
      .select(it -> indexMap.apply(idx.getAndIncrement(), it))
      .iterator();
  }
}

final class SelectJust<R, S> implements Maybe<R> {
  private final Maybe<Maybe<R>> maybes;

  @Contract(pure = true)
  SelectJust(final Maybe<Maybe<R>> maybes) {
    this.maybes = maybes;
  }

  @Nullable
  @Override
  public final R get() {
    return maybes.iterator().hasNext()
      ? maybes.iterator().next().iterator().next()
      : null;
  }
}

final class SelectMany<T, R> implements Queryable<R> {
  private final Queryable<T> some;
  private final Function1<T, R> map;

  @Contract(pure = true)
  SelectMany(final Queryable<T> some, final Function1<T, R> map) {
    this.some = some;
    this.map = map;
  }

  @NotNull
  @Override
  public final Iterator<R> iterator() {
    final var mapped = new ArrayList<R>();
    for (final var value : some) {
      mapped.add(map.apply(value));
    }
    return mapped.iterator();
  }
}

final class SelectManyMany<R> implements Queryable<R> {
  private final Queryable<Queryable<R>> projection;

  SelectManyMany(final Queryable<Queryable<R>> projection) {
    this.projection = projection;
  }

  @Override
  public final Iterator<R> iterator() {
    final var flatMapped = new ArrayList<R>();
    for (final var some : projection) {
      for (final var it : some) {
        flatMapped.add(it);
      }
    }
    return flatMapped.iterator();
  }
}

final class SelectMaybe<S, R> implements Maybe<R> {
  private final Maybe<S> maybe;
  private final Function1<S, R> map;

  SelectMaybe(final Maybe<S> maybe, Function1<S, R> map) {
    this.maybe = maybe;
    this.map = map;
  }

  @Override
  public final R get() {
    return maybe.iterator().hasNext()
      ? map.apply(maybe.iterator().next())
      : null;
  }
}