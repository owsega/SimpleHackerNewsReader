package com.owsega.hackernews.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.owsega.hackernews.R;
import com.owsega.hackernews.data.model.Post;
import com.owsega.hackernews.util.StringUtil;
import com.owsega.hackernews.view.fragment.PostFragment.OnPostSelectedListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * {@link Adapter} that can display a {@link Post} and makes a call to the
 * specified {@link OnPostSelectedListener}.
 */
public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private final List<Post> posts;
    private final OnPostSelectedListener postSelectedListener;

    public PostAdapter(List<Post> items, OnPostSelectedListener listener) {
        posts = items;
        postSelectedListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.post = posts.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != postSelectedListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    postSelectedListener.onPostSelected(holder.post);
                }
            }
        });

        holder.titleView.setText(holder.post.title);

        String anchor = String.format(Locale.getDefault(), "%d. ▲ ", position);
        holder.anchorView.setText(anchor);

        String subtitle = String.format(Locale.getDefault(), "%s by %s %s | %s",
                StringUtil.singularPluralPoints(holder.post.score),
                holder.post.by,
                DateUtils.getRelativeTimeSpanString(holder.post.time,
                        System.currentTimeMillis() / 1000, DateUtils.FORMAT_ABBREV_RELATIVE),
                StringUtil.singularPluralComments(holder.post.kids));
        holder.subtitleView.setText(subtitle);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    /**
     * replace items in the adapter with new ones
     */
    public void setItems(ArrayList<Post> items) {
        posts.clear();
        posts.addAll(items);
        notifyDataSetChanged();
        Log.e("seyi", "stories length " + posts.size());
    }

    /**
     * add a Post to the list
     */
    public void addItem(Post item) {
        posts.add(item);
        notifyItemInserted(posts.size() - 1);
        Log.e("seyi", "item inserted new length " + posts.size());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public Post post;

        @BindView(R.id.anchor)
        TextView anchorView;
        @BindView(R.id.title)
        TextView titleView;
        @BindView(R.id.subtitle)
        TextView subtitleView;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @Override
        public String toString() {
            return super.toString() + post.title;
        }
    }
}
