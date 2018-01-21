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

        Assert.assertFalse(post.equals(post1));
        Assert.assertTrue(post1.equals(post2));
        Assert.assertNotSame(post1, post2);
        Assert.assertTrue(post1.hashCode() == post2.hashCode());
        Assert.assertFalse(post.hashCode() == post1.hashCode());
    }

}
