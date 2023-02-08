package re.artoo.lance.query.cursor.mapper;

import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.Head;

@SuppressWarnings("SwitchStatementWithTooFewBranches")
public final class Flat<T> implements Cursor<T> {
  private final Head<Head<T>> source;
  private Head<T> current;
  private int index = 0;

  public Flat(final Head<Head<T>> source) {
    this.source = source;
  }

  @Override
  public boolean hasNext() {
    /*
     * this is a bit tricky to catch it at first glance:
     * at the very beginning, we need to check if the fetcher has a fetcher within,
     * then we take it by setting the current flatten fetcher,
     * otherwise we take the next one
     */
    boolean hasNext = (current != null && current.hasNext()) || source.hasNext();

    if (hasNext && (current == null || !current.hasNext())) {
      current = source.next();
    }
    return hasNext;
  }
  @Override
  public <R> R scroll(TryIntFunction1<? super T, ? extends R> fetch) throws Throwable {
    /*
     * if we don't have a current flatten fetcher,
     * then we check if we have one within (see above) and if so, we fetch a value from it,
     * otherwise we just fetch a value from it
     */
    return fetch.invoke(index++, switch (current) {
      case null -> hasNext() ? current.scroll() : null;
      default -> current.hasNext() || hasNext() ? current.scroll() : null;
    });
  }

}
