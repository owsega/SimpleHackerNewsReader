package com.owsega.hackernews.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.owsega.hackernews.R;
import com.owsega.hackernews.data.DataProvider;
import com.owsega.hackernews.data.model.Post;
import com.owsega.hackernews.view.adapter.PostAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;


public class PostFragment extends Fragment implements OnRefreshListener {

    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.list)
    RecyclerView listPosts;

    @BindView(R.id.progressbar)
    ProgressBar progressBar;
    DataProvider dataProvider;
    ArrayList<Subscription> subscriptions;
    private Unbinder unbinder;
    private OnPostSelectedListener postSelectedListener;
    private PostAdapter listAdapter;

    public PostFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPostSelectedListener) {
            postSelectedListener = (OnPostSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnPostSelectedListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);

        dataProvider = DataProvider.getInstance();
        subscriptions = new ArrayList<>();
        listAdapter = new PostAdapter(postSelectedListener);

        loadTopStories();
    }

    private void loadTopStories() {
        subscriptions.add(dataProvider.getTopStories()
                .onBackpressureBuffer()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(dataProvider.getScheduler())
                .subscribe(new Subscriber<Post>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        hideLoadingViews();
                        Toast.makeText(getContext(),
                                R.string.error_posts_loading,
                                Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Post post) {
                        hideLoadingViews();
                        listAdapter.addItem(post);
                    }
                }));
    }

    private void hideLoadingViews() {
        progressBar.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_list, container, false);
        unbinder = ButterKnife.bind(PostFragment.this, view);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);

        listPosts.setAdapter(listAdapter);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        for (Subscription subscription : subscriptions) subscription.unsubscribe();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        postSelectedListener = null;
    }

    @Override
    public void onRefresh() {
        for (Subscription subscription : subscriptions) subscription.unsubscribe();
        listAdapter.setItems(new ArrayList<Post>());
        loadTopStories();
    }

    /**
     * callback to handle events when a post is selected
     */
    public interface OnPostSelectedListener {
        void onPostSelected(Post post);
    }
}
