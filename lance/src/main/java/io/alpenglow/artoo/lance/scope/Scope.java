package io.alpenglow.artoo.lance.scope;

import io.alpenglow.artoo.lance.func.TryConsumer;
import io.alpenglow.artoo.lance.func.TryFunction;
import io.alpenglow.artoo.lance.func.TrySupplier;
import io.alpenglow.artoo.lance.literator.Cursor;
import io.alpenglow.artoo.lance.query.One;

public abstract class Scope<T, $this extends Scope<T, $this>> implements One<T>, TrySupplier<T> {
  protected int value;

  @Override
  public final Cursor<T> cursor() {
    return Cursor.maybe(get());
  }

  public final <R, $new extends Scope<R, $new>> $new let(final TryFunction<? super T, ? extends $new> let) {
    return let.apply(get());
  }

  @SuppressWarnings("unchecked")
  public $this apply(final TryConsumer<Scope<T, $this>> apply) {
    return ($this) new Scope<T, $this>() {

      {
        apply.accept(this);
      }


      @Override
      public T tryGet() throws Throwable {
        return Scope.this.tryGet();
      }
    };
  }

  public static void main(String[] args) {
    class Test extends Scope<Integer, Test> {
      @Override
      public Integer tryGet() {
        return 12;
      }
    }
  }
}