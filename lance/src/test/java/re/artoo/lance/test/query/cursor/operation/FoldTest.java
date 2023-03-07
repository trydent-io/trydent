package re.artoo.lance.test.query.cursor.operation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import re.artoo.lance.query.cursor.operation.Open;
import re.artoo.lance.query.cursor.operation.Fold;

import static org.assertj.core.api.Assertions.assertThat;

class FoldTest {
  @Test
  @DisplayName("should reduce all the elements with an sequential value")
  void shouldReduceWithInitial() throws Throwable {
    var cursor =
      new Fold<>(
        new Open<>(1, 2, 3, 4),
        0,
        (index, acc, element) -> acc + element
      );

    assertThat(cursor.fetch()).isEqualTo(1);
    assertThat(cursor.fetch()).isEqualTo(3);
    assertThat(cursor.fetch()).isEqualTo(6);
    assertThat(cursor.fetch()).isEqualTo(10);
    assertThat(cursor.canFetch()).isFalse();
  }
}
