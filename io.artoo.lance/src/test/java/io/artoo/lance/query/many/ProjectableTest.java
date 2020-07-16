package io.artoo.lance.query.many;

import io.artoo.lance.query.Many;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static io.artoo.lance.query.Many.from;
import static java.util.stream.IntStream.*;
import static org.assertj.core.api.Assertions.assertThat;

class ProjectableTest {
  @Test
  @DisplayName("should select with index")
  void shouldSelectWithIndex() {
    final var select = from("apple", "banana", "mango", "orange", "passionfruit", "grape")
      .select((index, fruit) -> String.format("%d - %s", index, fruit));

    assertThat(select).containsExactly(
      "0 - apple",
      "1 - banana",
      "2 - mango",
      "3 - orange",
      "4 - passionfruit",
      "5 - grape"
    );
  }

  @Test
  @DisplayName("should say every fruit is a fruit")
  void shouldSelect() {
    final var select = from("apple", "banana", "mango", "orange", "passionfruit", "grape")
      .select(fruit -> fruit + " is a fruit.");

    assertThat(select).containsExactly(
      "apple is a fruit.",
      "banana is a fruit.",
      "mango is a fruit.",
      "orange is a fruit.",
      "passionfruit is a fruit.",
      "grape is a fruit."
    );
  }

  @Test
  @DisplayName("should split every word")
  void shouldDoASelection() {
    final var manySelected = from("an apple a day", "the quick brown fox").selectMany(phrase -> from(phrase.split(" ")));

    assertThat(manySelected).containsExactly(
      "an",
      "apple",
      "a",
      "day",
      "the",
      "quick",
      "brown",
      "fox"
    );
  }
}
