package io.alpenglow.artoo.lance.literator.cursor.routine;

import io.alpenglow.artoo.lance.func.TryFunction;
import io.alpenglow.artoo.lance.literator.Literator;
import io.alpenglow.artoo.lance.literator.cursor.routine.concat.Concat;
import io.alpenglow.artoo.lance.literator.cursor.routine.convert.Convert;
import io.alpenglow.artoo.lance.literator.cursor.routine.join.Join;
import io.alpenglow.artoo.lance.literator.cursor.routine.sort.Sort;

import java.util.Iterator;
import java.util.List;

public sealed interface Routine<T, R> permits Join, Concat, Convert, Sort {

  static <T> Convert<T, List<T>> list() { return new Convert.Listable<>(); }

  static <T> Convert<T, T[]> array(final Class<T> type) {
    return new Convert.Arrayable<>(type);
  }

  TryFunction<T[], R> onArray();
  TryFunction<Literator<T>, R> onLiterator();
  TryFunction<Iterator<T>, R> onIterator();

  @SuppressWarnings("unchecked")
  default TryFunction<T, R> onSelf() {
    return it -> (R) it;
  }
}
