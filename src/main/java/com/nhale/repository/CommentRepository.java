package com.nhale.repository;

import com.nhale.pojos.Comment;

public interface CommentRepository {
    Comment addComment(Comment comment);
}
