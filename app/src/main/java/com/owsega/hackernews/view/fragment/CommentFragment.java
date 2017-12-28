package com.owsega.hackernews.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.owsega.hackernews.R;
import com.owsega.hackernews.data.DataProvider;
import com.owsega.hackernews.data.model.Comment;
import com.owsega.hackernews.data.model.Post;
import com.owsega.hackernews.view.adapter.CommentAdapter;
import com.owsega.hackernews.view.adapter.CommentAdapter.OnCommentSelectedListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

import static com.owsega.hackernews.view.activity.PostDetailsActivity.EXTRA_POST;

/**
 * A fragment holding the view for Comments to a Post
 */
public class CommentFragment extends Fragment implements OnCommentSelectedListener {

    @BindView(R.id.list)
    RecyclerView commentList;

    @BindView(R.id.progressbar)
    ProgressBar progressBar;

    DataProvider dataProvider;
    ArrayList<Subscription> subscriptions;
    Post post;
    private Unbinder unbinder;
    private CommentAdapter listAdapter;

    public CommentFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        dataProvider = DataProvider.getInstance();
        subscriptions = new ArrayList<>();
        post = getArguments().getParcelable(EXTRA_POST);
        loadComments();

        getActivity().setTitle(post.title);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_details, container, false);
        unbinder = ButterKnife.bind(this, view);

        listAdapter = new CommentAdapter(new ArrayList<Comment>(), this);
        commentList.setAdapter(listAdapter);

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

    private void loadComments() {
        if (post.kids != null) subscriptions.add(dataProvider.getPostComments(post.kids, 0)
                .onBackpressureBuffer()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(dataProvider.getScheduler())
                .subscribe(new Subscriber<Comment>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        hideProgressBar();
                        Toast.makeText(getContext(),
                                R.string.error_comments_load,
                                Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Comment comment) {
                        hideProgressBar();
                        listAdapter.addItem(comment);
                    }
                }));
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onCommentSelected(Comment comment) {
        //todo Load children later
    }
}
