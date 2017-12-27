package com.owsega.hackernews.data;

import com.google.gson.GsonBuilder;
import com.owsega.hackernews.data.model.Comment;
import com.owsega.hackernews.data.model.Post;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;
import rx.Observer;
import rx.schedulers.Schedulers;

public class DataProviderTest {

    private MockWebServer mockServer;
    private DataProvider dataProvider;

    @Before
    public void setUp() {
        mockServer = new MockWebServer();
        dataProvider = new DataProvider(mockHackerNews(), Schedulers.io());
    }

    private HackerNews mockHackerNews() {
        Dispatcher dispatcher = new Dispatcher() {

            @Override
            public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
                switch (request.getPath()) {
                    case "/topstories.json":
                        return new MockResponse().setResponseCode(200)
                                .setBody("[16012968]");
                    case "/item/16012968.json":
                        return new MockResponse().setResponseCode(200)
                                .setBody("{\"by\":\"da02\",\"descendants\":57,\"id\":16012968,\"kids\":[16013867],\"score\":106,\"time\":1514351152,\"title\":\"Symbols Found in Ice Age Caves Across Europe (2015)\",\"type\":\"story\",\"url\":\"https://digventures.com/2015/12/these-32-symbols-are-found-in-ice-age-caves-across-europe-but-what-do-they-mean/\"}");
                    case "/item/16013867.json":
                        return new MockResponse().setResponseCode(200)
                                .setBody("{\"by\":\"jbotz\",\"id\":16013867,\"parent\":16012968,\"text\":\"For more substance and less self-promotion, see her master&#x27;s thesis here: <a href=\\\"http:&#x2F;&#x2F;citeseerx.ist.psu.edu&#x2F;viewdoc&#x2F;download?doi=10.1.1.929.671&amp;rep=rep1&amp;type=pdf\\\" rel=\\\"nofollow\\\">http:&#x2F;&#x2F;citeseerx.ist.psu.edu&#x2F;viewdoc&#x2F;download?doi=10.1.1.929...</a><p>There&#x27;s also a better graphical overview of the signs here: <a href=\\\"https:&#x2F;&#x2F;frontiers-of-anthropology.blogspot.com.br&#x2F;2014&#x2F;08&#x2F;geometric-signs-from-genevieve-von.html?m=1\\\" rel=\\\"nofollow\\\">https:&#x2F;&#x2F;frontiers-of-anthropology.blogspot.com.br&#x2F;2014&#x2F;08&#x2F;ge...</a>\",\"time\":1514368452,\"type\":\"comment\"}");
                }
                return new MockResponse().setResponseCode(404);
            }
        };
        mockServer.setDispatcher(dispatcher);
        return new RestAdapter.Builder()
                .setEndpoint(mockServer.url("/v0/").toString())
                .setConverter(new GsonConverter(new GsonBuilder().create()))
                .build()
                .create(HackerNews.class);
    }

    @After
    public void tearDown() {
        try {
            mockServer.shutdown();
        } catch (IOException ignored) {
        }
    }

    @Test
    public void testGetTopStories() throws Exception {
        dataProvider.getTopStories().subscribe(new Observer<Post>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(Post post) {
                Assert.assertEquals(Long.valueOf(16012968L), post.id);
                Assert.assertEquals(1, post.kids.size());
            }
        });
    }

    @Test
    public void testGetCommentItem() throws Exception {
        List<Long> ids = Arrays.asList(16013867L, 16013867L);
        dataProvider.getPostComments(ids, 0).subscribe(new Observer<Comment>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(Comment comment) {
                Assert.assertEquals(Long.valueOf(16013867), comment.id);
                Assert.assertEquals(1, comment.depth);
            }
        });
    }


}