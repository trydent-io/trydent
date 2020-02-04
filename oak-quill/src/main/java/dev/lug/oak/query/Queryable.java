package dev.lug.oak.query;

import dev.lug.oak.func.con.Consumer1;
import dev.lug.oak.func.fun.Function1;
import dev.lug.oak.func.fun.Function2;
import dev.lug.oak.func.fun.Function3;
import dev.lug.oak.func.fun.IntFunction2;
import dev.lug.oak.func.pre.IntPredicate2;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface Queryable<T> extends Iterable<T> {
  default void eventually(final Consumer1<T> consuming) {
    for (final var value : this) consuming.accept(value);
  }

  enum P {
    ;

    @Contract(value = "_ -> param1", pure = true)
    public static <T, R> IntFunction2<T, R> ith(final IntFunction2<T, R> index) { return index; }

    @Contract(value = "_ -> param1", pure = true)
    public static <T, R> AsJust<T, R> just(final AsJust<T, R> just) { return just; }

    @Contract(value = "_ -> param1", pure = true)
    public static <T1, T2, R> AsJust2<T1, T2, R> just(final AsJust2<T1, T2, R> just2) { return just2; }

    @Contract(value = "_ -> param1", pure = true)
    public static <T1, T2, T3, R> Function3<T1, T2, T3, R> just(final Function3<T1, T2, T3, R> just3) { return just3; }

    @NotNull
    @Contract(pure = true)
    @SuppressWarnings("unchecked")
    public static <T, R, Q extends Queryable<R>> Queryable.AsQueryable<T, R, Q> array(final FromArray<T, R> array) {
      return it -> (Q) Many.from(array.apply(it));
    }

    public static <T, R, Q extends Queryable<R>> AsQueryable<T, R, Q> many(final AsQueryable<T, R, Q> many) {
      return many;
    }

    @Contract(value = "_ -> param1", pure = true)
    public static <T, U extends Tuple, Q extends Queryable<U>> Function1<T, Q> tuples2(final Function1<T, Q> many) { return many; }

    @Contract(value = "_ -> param1", pure = true)
    public static <V, R1, R2, T extends Tuple2<R1, R2>> Function1<V, T> tuple2(final Function1<V, T> tuple2) { return tuple2; }

    @Contract(value = "_ -> param1", pure = true)
    public static <V, R1, R2, R3, T extends Tuple3<R1, R2, R3>> Function1<V, T> tuple3(final Function1<V, T> tuple3) { return tuple3; }
  }

  enum F {
    ;

    @Contract(value = "_ -> param1", pure = true)
    public static <T> IntPredicate2<T> ith(final IntPredicate2<T> index) { return index; }
  }

  interface AsJust<T, R> extends Function1<T, R> {}
  interface AsJust2<T1, T2, R> extends Function2<T1, T2, R> {}
  interface AsJust3<T1, T2, T3, R> extends Function3<T1, T2, T3, R> {}

  interface AsQueryable<T, R, Q extends Queryable<R>> extends Function1<T, Q> {}

  interface FromArray<T, R> extends Function1<T, R[]> {}

  interface Just2AsManyTuple2<V1, V2, S extends Queryable> extends Function2<V1, V2, S> {}

  interface JustAsTuple2<V, T extends Tuple> extends Function1<V, T> {}
  interface Just2AsTuple2<V1, V2, T extends Tuple> extends Function2<V1, V2, T> {}
  interface JustAsTuple3<V, T extends Tuple> extends Function1<V, T> {}
  interface Just3AsTuple3<V1, V2, V3, T extends Tuple> extends Function3<V1, V2, V3, T> {}
}
