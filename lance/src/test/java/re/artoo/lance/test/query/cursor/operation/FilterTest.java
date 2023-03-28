package re.artoo.lance.test.query.cursor.operation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import re.artoo.lance.query.cursor.operation.Filter;
import re.artoo.lance.query.cursor.operation.Open;
import re.artoo.lance.query.cursor.operation.PresenceOnly;

import static org.assertj.core.api.Assertions.assertThat;
import static re.artoo.lance.query.cursor.operation.Filter.presenceOnly;


class FilterTest {
  @Test
  @DisplayName("should filter elements")
  void shouldFilterElements() throws Throwable {
    var filter =
      new Filter<>(
        new Open<>("Luke", null, "I'm", "your", null, "father", null, null, null, "fool"),
        (index, element) -> element != null && element.length() == 4
      );

    assertThat(filter.fetch()).isEqualTo("Luke");
    assertThat(filter.fetch()).isEqualTo("your");
    assertThat(filter.fetch()).isEqualTo("fool");
    assertThat(filter.canFetch()).isFalse();
  }

  @Test
  @DisplayName("should filter with present-only elements")
  void shouldFilterWithPresentOnlyElements() throws Throwable {
    var filter =
      new Filter<>(
        new Filter<>(
          new Open<>("Luke", null, "I'm", "your", null, "father", null, null, null, "fool"), // all null elements will be ignored by the presence-only cursor
          presenceOnly()
        ),
        (index, element) -> element.length() == 4
      );

    assertThat(filter.fetch()).isEqualTo("Luke");
    assertThat(filter.fetch()).isEqualTo("your");
    assertThat(filter.fetch()).isEqualTo("fool");
    assertThat(filter.canFetch()).isFalse();
  }

  @Test
  @DisplayName("should filter numbers less than condition with index")
  void shouldFilterNumberLessThanOperation() throws Throwable {
    var filter =
      new Filter<>(
        new Filter<>(
          new Open<>(0, null, 30, null, 20, null, 15, 90, 85, 40, 75, null),
          presenceOnly()
        ),
        (index, number) -> number <= index * 10
      );

    assertThat(filter.fetch()).isEqualTo(0);
    assertThat(filter.fetch()).isEqualTo(20);
    assertThat(filter.fetch()).isEqualTo(15);
    assertThat(filter.fetch()).isEqualTo(40);
    assertThat(filter.canFetch()).isFalse();
  }

  @Test
  @DisplayName("should filter numbers less than condition with index and exclude nulls in the end")
  void shouldFilterNumberLessThanOperationButNulls() throws Throwable {
    var filter =
      new Filter<>(
        new Open<>(0, 30, 20, 15, 90, 85, 40, 75),
        (index, number) -> number <= index * 10
      );

    assertThat(filter.fetch()).isEqualTo(0);
    assertThat(filter.fetch()).isEqualTo(20);
    assertThat(filter.fetch()).isEqualTo(15);
    assertThat(filter.fetch()).isEqualTo(40);
    assertThat(filter.canFetch()).isFalse();
  }

  @Test
  @DisplayName("should coalesce cursor")
  void shouldCoalesceCursor() throws Throwable {
    var presenceOnly = new Filter<>(new Open<>(1, null, 2, null, 3, null, 4), presenceOnly());

    assertThat(presenceOnly.fetch()).isEqualTo(1);
    assertThat(presenceOnly.fetch()).isEqualTo(2);
    assertThat(presenceOnly.fetch()).isEqualTo(3);
    assertThat(presenceOnly.fetch()).isEqualTo(4);
    assertThat(presenceOnly.hasNext()).isFalse();
  }
}