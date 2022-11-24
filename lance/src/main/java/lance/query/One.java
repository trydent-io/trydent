package lance.query;

import lance.Queryable;
import lance.func.Func;
import lance.func.Suppl;
import lance.literator.Cursor;
import lance.query.one.Elseable;
import lance.query.one.Filterable;
import lance.query.one.Peekable;
import lance.query.one.Projectable;

enum None implements One<Object> {
  Empty;

  @Override
  public final Cursor<Object> cursor() {
    return Cursor.nothing();
  }
}

public interface One<T> extends Projectable<T>, Peekable<T>, Filterable<T>, Elseable<T> {
  static <T> One<T> maybe(final T element) {
    return element != null ? One.lone(element) : One.none();
  }

  static <L> One<L> lone(final L element) {
    return new Lone<>(element);
  }

  static <T> One<T> from(final Suppl.MaybeSupplier<T> supply) {
    return new Sone<>(supply);
  }

  static <T> One<T> gone(final String message, final Func.TryFunction<? super String, ? extends RuntimeException> error) {
    return new Gone<>(message, error);
  }

  @SuppressWarnings("unchecked")
  static <L> One<L> none() {
    return (One<L>) None.Empty;
  }

  static <A extends AutoCloseable, T, O extends One<T>> One<T> done(Suppl.MaybeSupplier<? extends A> going, Func.TryFunction<? super A, ? extends O> then) {
    return new Done<>(going, then);
  }

  interface OfTwo<A, B> extends Queryable.OfTwo<A, B> {}
}

final class Lone<T> implements One<T> {
  private final T element;

  public Lone(final T element) {
    this.element = element;
  }

  @Override
  public Cursor<T> cursor() {
    return Cursor.open(element);
  }
}

final class Sone<T> implements One<T> {
  private final Suppl.MaybeSupplier<T> suppl;

  public Sone(final Suppl.MaybeSupplier<T> suppl) {
    this.suppl = suppl;
  }

  @Override
  public Cursor<T> cursor() {
    try {
      return Cursor.open(suppl.tryGet());
    } catch (Throwable cause) {
      return One
        .<T>gone("Can't get returning", it -> new IllegalStateException(it, cause))
        .cursor();
    }
  }
}

final class Gone<T> implements One<T> {
  private final String message;
  private final Func.TryFunction<? super String, ? extends RuntimeException> error;

  Gone(final String message, final Func.TryFunction<? super String, ? extends RuntimeException> error) {
    this.message = message;
    this.error = error;
  }

  @Override
  public Cursor<T> cursor() {
    throw error.apply(message);
  }
}

final class Done<A extends AutoCloseable, T, O extends One<T>> implements One<T> {
  private final Suppl.MaybeSupplier<? extends A> doing;
  private final Func.TryFunction<? super A, ? extends O> then;

  Done(final Suppl.MaybeSupplier<? extends A> doing, final Func.TryFunction<? super A, ? extends O> then) {
    this.doing = doing;
    this.then = then;
  }

  @Override
  public final Cursor<T> cursor() {
    try (final var auto = doing.tryGet()) {
      return then.tryApply(auto).cursor();
    } catch (Throwable e) {
      return Cursor.nothing();
    }
  }
}
