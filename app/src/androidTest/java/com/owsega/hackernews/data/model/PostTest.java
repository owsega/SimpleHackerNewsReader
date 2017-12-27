package com.owsega.hackernews.data.model;

import android.os.Parcel;

import junit.framework.Assert;

import org.junit.Test;

public class PostTest {

    @Test
    public void testPost() {
        Post post = new Post(1234L);
        Post post1 = new Post(1245L);
        Post post2 = new Post(1245L);

        // test equals
        Assert.assertFalse(post.equals(post1));
        Assert.assertTrue(post1.equals(post2));
        Assert.assertNotSame(post1, post2);

        // test parcelling
        Parcel parcel = Parcel.obtain();
        post.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        Post parceled = Post.CREATOR.createFromParcel(parcel);
        Assert.assertEquals(post, parceled);
    }

}
