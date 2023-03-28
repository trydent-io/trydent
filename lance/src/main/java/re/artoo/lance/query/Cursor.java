package re.artoo.lance.query;

import re.artoo.lance.query.cursor.Appendor;
import re.artoo.lance.query.cursor.Iterable;
import re.artoo.lance.query.cursor.*;
import re.artoo.lance.query.cursor.operation.Empty;
import re.artoo.lance.query.cursor.operation.Open;

import java.util.List;

public non-sealed interface Cursor<ELEMENT> extends Mappator<ELEMENT>, Folderator<ELEMENT>, Reduciator<ELEMENT>, Alternator<ELEMENT>, Appendor<ELEMENT>, Collector<ELEMENT>, Joinable<ELEMENT>, Filterator<ELEMENT> {
  @SafeVarargs
  static <T> Cursor<T> open(T... elements) {
    return new Open<>(elements);
  }

  @SuppressWarnings("unchecked")
  static <T> Cursor<T> empty() {
    return (Cursor<T>) Empty.Default;
  }

  static <VALUE> Cursor<VALUE> maybe(VALUE value) {
    return value == null ? empty() : open(value);
  }

  static <T> Cursor<T> from(List<T> collection) {
    return new Iterable<>(collection);
  }

  static Throwable exception(String message, Throwable cause) {
    return new Exception(message, cause);
  }

  static Throwable exception(Throwable cause) {
    return new Exception(cause);
  }

  static Throwable exception(String message) {
    return new Exception(message);
  }

  final class Exception extends RuntimeException {
    public Exception() {
      super();
    }

    public Exception(String message) {
      super(message);
    }

    public Exception(String message, Throwable cause) {
      super(message, cause);
    }

    public Exception(Throwable cause) {
      super(cause);
    }
  }
}
