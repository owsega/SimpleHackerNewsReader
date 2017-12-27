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
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;
import rx.Scheduler;
import rx.observers.TestSubscriber;
import rx.schedulers.Schedulers;

public class DataProviderTest {

    private MockWebServer mockServer;
    private DataProvider dataProvider;
    private Scheduler scheduler;

    @Before
    public void setUp() {
        mockServer = new MockWebServer();
        scheduler = Schedulers.immediate();
        dataProvider = new DataProvider(mockHackerNews(), scheduler);
    }

    private HackerNews mockHackerNews() {
        mockServer.setDispatcher(getDispatcher());
        return getHackerNews(mockServer);
    }

    private Dispatcher getDispatcher() {
        return new Dispatcher() {

            @Override
            public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
                System.out.println("requestPath " + request.getPath());
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
    }

    private HackerNews getHackerNews(MockWebServer server) {
        return new RestAdapter.Builder()
                .setEndpoint(server.url("/").toString())
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
    public void testGetScheduler() throws Exception {
        DataProvider provider = DataProvider.getInstance();
        Assert.assertEquals(scheduler, dataProvider.getScheduler());
        Assert.assertNotSame(provider, dataProvider);
        Assert.assertNotSame(provider.getScheduler(), dataProvider.getScheduler());
    }

    @Test
    public void testGetTopStories() throws Exception {
        Post post = dataProvider.getTopStories().toBlocking().first();
        Assert.assertEquals(Long.valueOf(16012968), post.id);
        Assert.assertEquals(1, post.kids.size());
    }

    @Test
    public void testGetCommentItem() throws Exception {
        List<Long> ids = Arrays.asList(16013867L, 16013867L);
        int i = 0;
        Iterator<Comment> it = dataProvider.getPostComments(ids, 0).toBlocking().getIterator();
        for (; it.hasNext(); i++) {
            Comment comment = it.next();
            Assert.assertEquals(ids.get(i), comment.id);
            Assert.assertEquals(0, comment.depth);
        }
    }

    @Test
    public void testNestedComments() throws Exception {
        List<Long> ids = Collections.singletonList(16012968L);
        MockWebServer mockWebServer = new MockWebServer();
        mockWebServer.enqueue(new MockResponse().setResponseCode(200)
                .setBody("{\"by\":\"da02\",\"descendants\":1,\"id\":16012968,\"kids\":[1514351152],\"score\":106,\"time\":1514351152,\"title\":\"Symbols Found in Ice Age Caves Across Europe (2015)\",\"type\":\"story\",\"url\":\"https://digventures.com/2015/12/these-32-symbols-are-found-in-ice-age-caves-across-europe-but-what-do-they-mean/\"}"));
        mockWebServer.enqueue(new MockResponse().setResponseCode(200)
                .setBody("{\"by\":\"da02\",\"descendants\":0,\"id\":1514351152,\"kids\":[],\"score\":106,\"time\":1514351152,\"title\":\"Symbols Found in Ice Age Caves Across Europe (2015)\",\"type\":\"story\",\"url\":\"https://digventures.com/2015/12/these-32-symbols-are-found-in-ice-age-caves-across-europe-but-what-do-they-mean/\"}"));
        DataProvider dataProvider = new DataProvider(getHackerNews(mockWebServer), scheduler);
        Iterator<Comment> it = dataProvider.getPostComments(ids, 0).toBlocking().getIterator();
        for (; it.hasNext(); ) {
            Comment comment = it.next();
            Assert.assertEquals(ids.get(0), comment.id);
        }
    }

    @Test
    public void testEmptyPost() throws Exception {
        MockWebServer mockWebServer = new MockWebServer();
        mockWebServer.enqueue(new MockResponse().setBody("[16012968]"));
        mockWebServer.enqueue(new MockResponse().setResponseCode(200)
                .setBody("{\"by\":\"da02\",\"descendants\":0,\"id\":16012968,\"kids\":[],\"score\":106,\"time\":1514351152,\"title\":\"Symbols Found in Ice Age Caves Across Europe (2015)\",\"type\":\"story\",\"url\":\"https://digventures.com/2015/12/these-32-symbols-are-found-in-ice-age-caves-across-europe-but-what-do-they-mean/\"}"));
        DataProvider dataProvider = new DataProvider(getHackerNews(mockWebServer), scheduler);
        Post post = dataProvider.getTopStories().toBlocking().first();
        Assert.assertEquals(Long.valueOf(16012968L), post.id);
        Assert.assertEquals(0, post.kids.size());
    }

    public void testPoorNetworkConditions() throws Exception {
        MockWebServer mockWebServer = new MockWebServer();
        mockWebServer.enqueue(new MockResponse().setResponseCode(404));
        mockWebServer.enqueue(new MockResponse()
                .setBody("[16012968, 1343344]")
                .throttleBody(10, 1, TimeUnit.MINUTES));
        mockWebServer.enqueue(new MockResponse().setResponseCode(404));
        mockWebServer.enqueue(new MockResponse().setResponseCode(502));
        DataProvider dataProvider = new DataProvider(getHackerNews(mockWebServer), scheduler);

        TestSubscriber<Post> subscriber = new TestSubscriber<>();
        dataProvider.getTopStories().subscribe(subscriber);
        Assert.assertTrue(subscriber.getOnErrorEvents().size() == 0);
        try {
            Iterator<Post> posts = dataProvider.getTopStories().toBlocking().getIterator();
            for (; posts.hasNext(); ) {
                System.out.println("dslfkjldfdf");
                Assert.assertNotNull(posts.next());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.getClass());
            Assert.assertTrue(e instanceof IOException);
        }
    }
}