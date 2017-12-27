package com.owsega.hackernews.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.text.Html;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.owsega.hackernews.R;
import com.owsega.hackernews.data.model.Comment;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * {@link Adapter} that can display a {@link Comment} and makes a call to the
 * specified {@link OnCommentSelectedListener}.
 */
public class CommentAdapter extends Adapter<CommentAdapter.ViewHolder> {

    private final List<Comment> comments;
    private final OnCommentSelectedListener commentSelectedListener;

    public CommentAdapter(List<Comment> items, OnCommentSelectedListener listener) {
        comments = items;
        commentSelectedListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_comment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.comment = comments.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != commentSelectedListener) {
                    commentSelectedListener.onCommentSelected(holder.comment);
                }
            }
        });

        String title = String.format(Locale.getDefault(), "%s %s",
                holder.comment.by,
                DateUtils.getRelativeTimeSpanString(holder.comment.time,
                        System.currentTimeMillis() / 1000, DateUtils.FORMAT_ABBREV_RELATIVE));
        holder.titleView.setText(title);
        holder.subtitleView.setText(Html.fromHtml(holder.comment.text.trim()));
        holder.anchorView.setText("▲  ");
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    /**
     * add a comment to the list
     */
    public void addItem(Comment item) {
        comments.add(item);
        notifyItemInserted(comments.size() - 1);
    }

    public interface OnCommentSelectedListener {
        void onCommentSelected(Comment comment);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public Comment comment;

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
    }
}
