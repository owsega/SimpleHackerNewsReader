package com.owsega.hackernews.data.model;

import junit.framework.Assert;

import org.junit.Test;

import java.util.ArrayList;

public class PostTest {

    @Test
    public void testPostEquality() {
        Post post = new Post(1234L);
        post.id = null;
        post.title = "title";
        post.time = 13898938933L;
        post.by = "user";
        post.url = "url";
        post.kids = new ArrayList<>();
        post.score = 89L;
        post.text = "text";
        Post post1 = new Post(1245L);
        Post post2 = new Post(1245L);
        Post post3 = new Post(1234L);
        post3.id = null;
        post3.title = "title";
        post3.time = 13898938933L;
        post3.by = "user";
        post3.url = "another_url";
        post3.kids = new ArrayList<>();
        post3.score = 89L;
        post3.text = "text";

        Assert.assertFalse(post.equals(post1));
        Assert.assertFalse(post3.equals(post));
        Assert.assertTrue(post1.equals(post2));
        Assert.assertNotSame(post1, post2);
        Assert.assertTrue(post1.hashCode() == post2.hashCode());
        Assert.assertFalse(post.hashCode() == post1.hashCode());
    }

    @Test
    public void testPostOrdering() {
        Post post = new Post(1234L);
        post.time = 123L;
        Post post1 = new Post(1245L);
        post1.time = 125L;

        Assert.assertTrue(post.compareTo(post1) < 0);
    }

}
