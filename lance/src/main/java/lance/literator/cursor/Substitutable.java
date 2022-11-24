package lance.literator.cursor;

import lance.func.Cons;
import lance.func.Func;
import lance.func.Suppl;
import lance.literator.Cursor;
import lance.literator.Literator;
import lance.literator.cursor.routine.Routine;
import lance.scope.Late;
import lance.scope.Let;

import java.util.Iterator;

public interface Substitutable<T> extends Literator<T> {
  default <C extends Cursor<T>> Cursor<T> or(final Suppl.MaybeSupplier<? extends C> alternative) {
    return new Or<>(this, Let.lazy(alternative));
  }

  default <E extends RuntimeException> Cursor<T> or(final String message, final Func.TryBiFunction<? super String, ? super Throwable, ? extends E> exception) {
    return new Er<>(this, message, exception);
  }

  default Cursor<T> exceptionally(Cons.TryConsumer<? super Throwable> catch$) {
    return new Catch<>(this, catch$);
  }
}

abstract class As<T> implements Transformable<T> {
  protected final Literator<T> source;

  protected As(final Literator<T> source) {this.source = source;}

  @Override
  public final <R> R as(final Routine<T, R> routine) {
    return switch (source) {
      case Cursor<T> cursor -> cursor.as(routine);
      default -> Cursor.<T>nothing().as(routine);
    };
  }
}

final class Or<T, C extends Cursor<T>> extends As<T> implements Cursor<T> {
  private final Late<Literator<T>> reference = Late.init();
  private final Let<? extends C> other;

  Or(final Literator<T> source, final Let<? extends C> other) {
    super(source);
    this.other = other;
  }

  @Override
  public T fetch() {
    return hasNext() ? reference.let(Literator::fetch) : null;
  }

  @Override
  public boolean hasNext() {
    other.get(otherwise -> reference.set(() -> source.hasNext() ? source : otherwise));

    return reference.let(Iterator::hasNext);
  }
}

final class Er<T, E extends RuntimeException> extends As<T> implements Cursor<T> {
  private final String message;
  private final Func.TryBiFunction<? super String, ? super Throwable, ? extends E> exception;

  Er(final Literator<T> source, final String message, final Func.TryBiFunction<? super String, ? super Throwable, ? extends E> exception) {
    super(source);
    this.message = message;
    this.exception = exception;
  }

  @Override
  public T fetch() {
    try {
      if (hasNext()) {
        return source.fetch();
      } else {
        throw exception.apply(message, null);
      }
    } catch (Throwable throwable) {
      throw exception.apply(message, throwable);
    }
  }

  @Override
  public boolean hasNext() {
    return source.hasNext();
  }
}

final class Catch<T> extends As<T> implements Cursor<T> {
  private final Cons.TryConsumer<? super Throwable> catch$;

  Catch(Literator<T> source, Cons.TryConsumer<? super Throwable> catch$) {
    super(source);
    this.catch$ = catch$;
  }

  @Override
  public T fetch() {
    try {
      return source.fetch();
    } catch (Throwable throwable) {
      catch$.accept(throwable);
      return null;
    }
  }

  @Override
  public boolean hasNext() {
    return source.hasNext();
  }
}
