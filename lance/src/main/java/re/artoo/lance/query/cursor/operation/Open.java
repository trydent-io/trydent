package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.query.Cursor;

public record Open<ELEMENT>(ELEMENT[] elements, Index index) implements Cursor<ELEMENT> {
  @SafeVarargs
  public Open(ELEMENT... elements) {
    this(elements, new Index());
  }

  @Override
  public boolean hasNext() {
    return index.value <= elements.length;
  }

  @Override
  public Next<ELEMENT> fetch() {
    return Next.of(index.value, elements[index.value++]);
  }

  private static class Index {
    private int value = 0;
  }
}
