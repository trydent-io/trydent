package io.artoo.ddd.forum.post.domain;

import io.artoo.ddd.core.Domain;
import io.artoo.ddd.core.Id;
import io.artoo.ddd.forum.member.Member;
import io.artoo.ddd.forum.post.Post;

import static io.artoo.ddd.forum.post.domain.Command.*;

public sealed interface Command permits Post {
  sealed interface Act extends Domain.Act {}

  record CreateArticle(Post.Title title, Post.Text text) implements Act {}
  record SignArticle(Id memberId, Member.FullName fullName) implements Act {}
}