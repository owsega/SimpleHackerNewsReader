package com.owsega.hackernews.data.model;

import org.junit.Test;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotSame;
import static junit.framework.Assert.assertTrue;

public class PostTest {

    @Test
    public void testPostEquality() {
        Post post = new Post(1234L);
        Post post1 = new Post(1245L);
        Post post2 = new Post(1245L);
        Post post3 = new Post(1234L);
        post3.id = null;

        assertFalse(post.equals(post1));
        assertFalse(post.equals(""));
        assertFalse(post3.equals(post));
        assertTrue(post1.equals(post2));
        assertNotSame(post1, post2);
        assertTrue(post1.hashCode() == post2.hashCode());
        post2.text = "another Text";
        assertFalse(post1.hashCode() == post2.hashCode());
        assertFalse(post.hashCode() == post1.hashCode());
    }

    @Test
    public void testPostOrdering() {
        Post post = new Post(1234L);
        post.time = 123L;
        Post post1 = new Post(1245L);
        post1.time = 125L;
        Post post2 = new Post(1232L);
        post2.time = 123L;

        assertTrue(post.compareTo(post1) < 0);
        assertTrue(post.compareTo(post2) == 0);
        assertTrue(post1.compareTo(post) > 0);
    }

}
