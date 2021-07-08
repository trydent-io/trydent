package io.artoo.lance.query.internal;

import io.artoo.lance.func.Func;

public record Tail<T, R, F extends Func.Uni<T, Tail<T, R, F>>>(F func, R returning) {}
