package com.owsega.hackernews.data.model;

import android.os.Parcel;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

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

}
