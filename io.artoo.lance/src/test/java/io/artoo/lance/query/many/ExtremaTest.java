package io.artoo.lance.query.many;

import io.artoo.lance.query.Many;
import org.junit.jupiter.api.Test;

import static io.artoo.lance.query.TestData.Pet;
import static org.assertj.core.api.Assertions.assertThat;

class ExtremaTest {
  @Test
  void shouldFindMaxByDefault() {
    final var max = Many.from(1, 2, 3, 4).max().yield();

    assertThat(max).isEqualTo(4);
  }

  @Test
  void shouldFindMinByDefault() {
    final var min = Many.from(1, 2, 3, 4).min().yield();

    assertThat(min).isEqualTo(1);
  }

  @Test
  void shouldNotFindMaxByDefaultWithRecords() {
    final var max = Many.from(new Pet("Pluto", 1), new Pet("Fuffy", 2), new Pet("Cerberos", 3)).max();

    assertThat(max).isEmpty();
  }

  @Test
  void shouldNotFindMinByDefaultWithRecords() {
    final var min = Many.from(new Pet("Pluto", 1), new Pet("Fuffy", 2), new Pet("Cerberos", 3)).min();

    assertThat(min).isEmpty();
  }

  @Test
  void shouldFindMaxBySelecting() {
    final var max = Many.from(new Pet("Pluto", 33), new Pet("Fuffy", 22), new Pet("Cerberos", 41)).max(Pet::age).yield();

    assertThat(max).isEqualTo(41);
  }

  @Test
  void shouldFindMinBySelecting() {
    final var min = Many.from(new Pet("Pluto", 33), new Pet("Fuffy", 22), new Pet("Cerberos", 41)).min(Pet::age).yield();


    assertThat(min).isEqualTo(22);
  }
}
