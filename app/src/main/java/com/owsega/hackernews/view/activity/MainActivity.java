package com.owsega.hackernews.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.owsega.hackernews.R;
import com.owsega.hackernews.data.model.Post;
import com.owsega.hackernews.view.fragment.PostFragment;
import com.owsega.hackernews.view.fragment.PostFragment.OnPostSelectedListener;

public class MainActivity extends AppCompatActivity implements
        OnPostSelectedListener {

    private static final String POST_FRAGMENT = "POST_FRAGMENT";

    PostFragment postFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            postFragment = new PostFragment();
            postFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content, postFragment, POST_FRAGMENT)
                    .commit();
        } else {
            postFragment = (PostFragment) getSupportFragmentManager()
                    .findFragmentByTag(POST_FRAGMENT);
        }

    }

    @Override
    public void onPostSelected(Post post) {
        // start Details Activity for the selected post
        Toast.makeText(this, "Display " + post.id, Toast.LENGTH_SHORT).show();
    }
}
