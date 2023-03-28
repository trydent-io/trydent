package re.artoo.lance.tuple;

import re.artoo.lance.func.TryConsumer5;
import re.artoo.lance.func.TryFunction5;
import re.artoo.lance.func.TryPredicate5;
import re.artoo.lance.tuple.record.OfFive;

import static re.artoo.lance.tuple.Type.componentOf;

public interface Quintuple<A, B, C, D, E> extends Tuple {
  default A first() { return componentOf(this, 0);}
  default B second() { return componentOf(this, 1); }
  default C third() { return componentOf(this, 2); }
  default D forth() { return componentOf(this, 3); }
  default E fifth() { return componentOf(this, 4); }
  default <T> T select(final TryFunction5<? super A, ? super B, ? super C, ? super D, ? super E, ? extends T> select) {
    return select.apply(first(), second(), third(), forth(), fifth());
  }
  default Quintuple<E, A, B, C, D> shift() { return select((a, b, c, d, e) -> Tuple.of(e, a, b, c, d)); }
  @SuppressWarnings("unchecked")
  default Quintuple<A, B, C, D, E> where(TryPredicate5<? super A, ? super B, ? super C, ? super D, ? super E> predicate) {
    return predicate.test(first(), second(), third(), forth(), fifth()) ? this : (Quintuple<A, B, C, D, E>) OfFive.Empty.Default;
  }
  default Quintuple<A, B, C, D, E> peek(TryConsumer5<? super A, ? super B, ? super C, ? super D, ? super E> consumer) {
    consumer.accept(first(), second(), third(), forth(), fifth());
    return this;
  }
}