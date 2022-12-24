package io.alpenglow.artoo.lance.test.query.many;

import io.alpenglow.artoo.lance.query.Many;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static io.alpenglow.artoo.lance.query.Many.from;
import static io.alpenglow.artoo.lance.test.Test.PACKAGES;
import static org.assertj.core.api.Assertions.assertThat;

public class SummableTest {
  @Test
  @DisplayName("should sum all float sequence")
  public void shouldSumFloatSequence() throws Throwable {
    final var sum = Many.from(43.68F, 1.25F, 583.7F, 6.5F).sum().cursor().fetch();

    assertThat(sum).isEqualTo(635.13F);
  }

  @Test
  @DisplayName("should sum all nullable any sequence ignoring null-ones")
  public void shouldSumNullableFloatSequence() throws Throwable {
    final var sum = Many.fromAny(null, "0", 92.83F, null, 100.0F, 37.46F, 81.1F).sum().cursor().fetch();

    assertThat(sum).isEqualTo(311.39F);
  }

  @Test
  @DisplayName("should sum all double sequence")
  public void shouldSumDoubleSequence() throws Throwable {
    final var sum = from(43.68D, 1.25D, 583.7D, 6.5D).sum().cursor().fetch();

    assertThat(sum).isEqualTo(635.13D);
  }

  @Test
  @DisplayName("should sum all nullable double sequence ignoring null-ones")
  public void shouldSumNullableDoubleSequence() throws Throwable {
    final var sum = Many.fromAny(null, 0, 92.83D, null, 100.0D, 37.46D, 81.1D).sum().cursor().fetch();

    assertThat(sum).isEqualTo(311.39D);
  }

  @Test
  @DisplayName("should sum all by selecting package-weight")
  public void shouldSumBySelecting() throws Throwable {
    final var sum = Many.from(PACKAGES).sum(io.alpenglow.artoo.lance.test.Test.Package::weight).cursor().fetch();

    assertThat(sum).isEqualTo(83.7F);
  }

  @Test
  @DisplayName("should sum by declaring the generic type")
  public void shouldSumWithGenericSpecified() throws Throwable {
    final var sum = Many.fromAny(25.2f, "Coho Vineyard", "Lucerne Publishing", BigInteger.valueOf(12)).sum().cursor().fetch();

    assertThat(sum).isEqualTo(BigInteger.valueOf(37));
  }

  @Test
  @DisplayName("should sum by ignoring the null element")
  public void shouldSumIgnoringNull() throws Throwable {
    final var sum = Many.fromAny(null, "Coho Vineyard", "Lucerne Publishing", 12L).sum().cursor().fetch();

    assertThat(sum).isEqualTo(12L);
  }

  @Test
  @DisplayName("should fail if there are no numbers")
  public void shouldFailIfNoNumbers() throws Throwable {
    final var sum = Many.from("Coho Vineyard", "Wingtip Toys", "Adventure Works").sum().cursor().fetch();

    assertThat(sum).isNull();
  }
}
