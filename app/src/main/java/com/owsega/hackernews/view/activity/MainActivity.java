package com.owsega.hackernews.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.widget.TextView;
import android.widget.Toast;

import com.owsega.hackernews.R;
import com.owsega.hackernews.data.DataProvider;
import com.owsega.hackernews.data.model.Post;

import java.util.ArrayList;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public class MainActivity extends AppCompatActivity {

    ArrayList<Subscription> subscriptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DataProvider dataProvider = new DataProvider();

        subscriptions = new ArrayList<>();
        subscriptions.add(dataProvider.getTopStories()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(dataProvider.getScheduler())
                .subscribe(new Subscriber<Post>() {
                    @Override
                    public void onCompleted() {
                        Toast.makeText(MainActivity.this, "completed", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(MainActivity.this,
                                "There was a problem loading the top stories " + e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(Post post) {
                        if (post != null && post.text != null) {
                            ((TextView) MainActivity.this.findViewById(R.id.textView))
                                    .setText(Html.fromHtml(post.text));
                        }
                    }
                }));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        for (Subscription subscription : subscriptions) subscription.unsubscribe();
    }
}
