package com.owsega.hackernews.data.model;

import junit.framework.Assert;

import org.junit.Test;

import java.util.ArrayList;

public class CommentTest {

    @Test
    public void testComment() {
        Comment comment = new Comment();
        comment.id = 123L;

        Comment comment1 = new Comment();
        comment1.id = 123L;
        comment1.text = "text";
        comment1.time = 13898938933L;
        comment1.by = "user";
        comment1.comments = new ArrayList<>();
        comment1.kids = new ArrayList<>();
        comment1.depth = 1;
        comment1.text = "text";

        Comment comment2 = new Comment();
        comment2.id = 143L;
        comment2.text = "text";
        comment2.time = 13898938933L;
        comment2.by = "user";
        comment2.comments = new ArrayList<>();
        comment2.kids = new ArrayList<>();
        comment2.depth = 1;
        comment2.text = "text";

        // test equals
        Assert.assertFalse(comment == comment1);
        Assert.assertTrue(comment.equals(comment1));
        Assert.assertFalse(comment1.equals(comment2));
        Assert.assertFalse(comment1.equals("sdfdf"));
    }

}
