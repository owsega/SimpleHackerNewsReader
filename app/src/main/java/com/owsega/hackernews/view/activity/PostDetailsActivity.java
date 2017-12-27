package com.owsega.hackernews.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.owsega.hackernews.R;
import com.owsega.hackernews.view.fragment.CommentFragment;

public class PostDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_POST = "com.owsega.hackernews.view.activity.EXTRA_POST";
    private static final String DETAILS_FRAGMENT = "DETAILS_FRAGMENT";
    CommentFragment commentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            commentFragment = new CommentFragment();
            commentFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content, commentFragment, DETAILS_FRAGMENT)
                    .commit();
        } else {
            commentFragment = (CommentFragment) getSupportFragmentManager()
                    .findFragmentByTag(DETAILS_FRAGMENT);
        }
    }
}
