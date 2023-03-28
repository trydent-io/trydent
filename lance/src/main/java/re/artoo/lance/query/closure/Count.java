package re.artoo.lance.query.closure;

import re.artoo.lance.func.TryPredicate1;
import re.artoo.lance.query.Closure;
import re.artoo.lance.scope.Expectation;

import java.util.Objects;

public final class Count<T> implements Closure<T, Integer>, Expectation {
  private int counted;
  private final TryPredicate1<? super T> where;
  public Count(final TryPredicate1<? super T> where) {
    this.counted = 0;
    this.where = expect(where, Objects::nonNull);
  }
  @Override
  public Integer invoke(T element) throws Throwable {
    return where.invoke(element) ? ++counted : counted;
  }
}