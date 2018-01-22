package com.owsega.hackernews.data.model;

import android.os.Parcel;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotSame;
import static junit.framework.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class PostTest {

    @Test
    public void testPostParcelling() {
        Post post = new Post(1234L);
        post.id = null;
        post.title = "title";
        post.time = 13898938933L;
        post.by = "user";
        post.url = "url";
        post.kids = new ArrayList<>();
        post.score = 89L;
        post.text = "text";

        // test parcelling
        Parcel parcel = Parcel.obtain();
        post.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        Post parceled = Post.CREATOR.createFromParcel(parcel);
        Assert.assertEquals(0, post.describeContents());
        Assert.assertEquals(post, parceled);
    }

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
        post2.title = "title";
        post2.time = 13898938933L;
        post2.by = "user";
        post2.url = "url";
        post2.kids = new ArrayList<>();
        post2.score = 89L;
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
