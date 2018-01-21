package com.owsega.hackernews.data;

import com.google.gson.GsonBuilder;
import com.owsega.hackernews.data.model.Comment;
import com.owsega.hackernews.data.model.Post;

import java.util.ArrayList;
import java.util.List;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;
import rx.Observable;
import rx.Scheduler;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Retrieves and provides data (Model) in a way the views (activity/fragments) need it.
 */
public class DataProvider {

    private static DataProvider instance;  // todo use injection not singleton
    private final List<Post> posts;
    private HackerNews hackerNews;
    private Scheduler subscribeScheduler;

    public DataProvider(HackerNews hackerNews, Scheduler scheduler) {
        this.hackerNews = hackerNews;
        this.subscribeScheduler = scheduler;
        posts = new ArrayList<>();
    }

    public static DataProvider getInstance() {
        if (instance == null) {
            instance = new DataProvider(
                    new RestAdapter.Builder()
                            .setEndpoint(HackerNews.BASE_URL)
                            .setConverter(new GsonConverter(new GsonBuilder().create()))
                            .build()
                            .create(HackerNews.class),
                    Schedulers.io());
        }
        return instance;
    }

    public Scheduler getScheduler() {
        return subscribeScheduler;
    }

    public Observable<Post> getTopStories() {
        return hackerNews.getTopStories().concatMap(new Func1<List<Long>, Observable<? extends Post>>() {
            @Override
            public Observable<? extends Post> call(List<Long> longs) {
                return getPostsFromIds(longs);
            }
        });
    }

    private Observable<Post> getPostsFromIds(List<Long> storyIds) {
        return Observable.from(storyIds)
                .concatMap(new Func1<Long, Observable<Post>>() {
                    @Override
                    public Observable<Post> call(Long aLong) {
                        return hackerNews.getStoryItem(String.valueOf(aLong));
                    }
                }).flatMap(new Func1<Post, Observable<Post>>() {
                    @Override
                    public Observable<Post> call(Post post) {
                        return post.title != null ? Observable.just(post) : Observable.<Post>empty();
                    }
                });
    }

    public Observable<Comment> getPostComments(final List<Long> commentIds, final int depth) {
        return Observable.from(commentIds)
                .concatMap(new Func1<Long, Observable<Comment>>() {
                    @Override
                    public Observable<Comment> call(Long aLong) {
                        return hackerNews.getCommentItem(String.valueOf(aLong));
                    }
                }).concatMap(new Func1<Comment, Observable<Comment>>() {
                    @Override
                    public Observable<Comment> call(Comment comment) {
                        comment.depth = depth;
                        if (comment.kids == null || comment.kids.isEmpty()) {
                            return Observable.just(comment);
                        } else {
                            return Observable.just(comment)
                                    .mergeWith(getPostComments(comment.kids, depth + 1));
                        }
                    }
                }).filter(new Func1<Comment, Boolean>() {
                    @Override
                    public Boolean call(Comment comment) {
                        return (comment.by != null && !comment.by.trim().isEmpty()
                                && comment.text != null && !comment.text.trim().isEmpty());
                    }
                });
    }

    public List<Post> getPosts() {
        return posts;
    }
}
