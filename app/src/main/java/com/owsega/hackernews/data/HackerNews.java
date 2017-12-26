package com.owsega.hackernews.data;

import com.owsega.hackernews.data.model.Comment;
import com.owsega.hackernews.data.model.Post;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

public interface HackerNews {

    /**
     * Hacker news api url
     */
    String BASE_URL = "https://hacker-news.firebaseio.com/v0/";

    /**
     * Get top stories
     */
    @GET("/topstories.json")
    Observable<List<Long>> getTopStories();

    /**
     * Get the details of a story
     */
    @GET("/item/{id}.json")
    Observable<Post> getStoryItem(@Path("id") String id);

    /**
     * Get the details for a comment
     */
    @GET("/item/{id}.json")
    Observable<Comment> getCommentItem(@Path("id") String id);

}
