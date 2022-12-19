package io.alpenglow.artoo.lance.query.cursor;

import io.alpenglow.artoo.lance.query.Cursor;
import io.alpenglow.artoo.lance.query.cursor.routine.Routine;

public sealed interface Convertor<FROM> extends Fetcher<FROM> permits Cursor, As {
  default <TO, CURSOR extends Cursor<TO>> CURSOR to(Routine<FROM, CURSOR> routine) {
    return this.as(routine);
  }
  <TO> TO as(Routine<FROM, TO> routine);
}
