package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.query.Cursor;

public enum Empty implements Cursor<Object> {
  Default;

  @Override
  public Object fetch() {
    return null;
  }

  @Override
  public boolean canFetch() {
    return false;
  }

}