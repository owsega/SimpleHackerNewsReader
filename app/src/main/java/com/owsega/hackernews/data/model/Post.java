package com.owsega.hackernews.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Post implements Parcelable {

    public static final Parcelable.Creator<Post> CREATOR = new Parcelable.Creator<Post>() {
        public Post createFromParcel(Parcel source) {
            return new Post(source);
        }

        public Post[] newArray(int size) {
            return new Post[size];
        }
    };
    public String by;
    public Long id;
    public Long time;
    public ArrayList<Long> kids;
    public String url;
    public Long score;
    public String title;
    public String text;

    private Post(Parcel in) {
        this.by = in.readString();
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.time = (Long) in.readValue(Long.class.getClassLoader());
        this.kids = (ArrayList<Long>) in.readSerializable();
        this.url = in.readString();
        this.score = (Long) in.readValue(Long.class.getClassLoader());
        this.title = in.readString();
        this.text = in.readString();
        int tmpStoryType = in.readInt();
    }

    @Override
    public int hashCode() {
        int result = by != null ? by.hashCode() : 0;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (time != null ? time.hashCode() : 0);
        result = 31 * result + (kids != null ? kids.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (score != null ? score.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (text != null ? text.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Post story = (Post) o;

        if (by != null ? !by.equals(story.by) : story.by != null) return false;
        if (id != null ? !id.equals(story.id) : story.id != null) return false;
        if (kids != null ? !kids.equals(story.kids) : story.kids != null) return false;
        if (score != null ? !score.equals(story.score) : story.score != null) return false;
        if (time != null ? !time.equals(story.time) : story.time != null) return false;
        if (title != null ? !title.equals(story.title) : story.title != null) return false;
        if (text != null ? !text.equals(story.text) : story.text != null) return false;
        return url != null ? url.equals(story.url) : story.url == null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.by);
        dest.writeValue(this.id);
        dest.writeValue(this.time);
        dest.writeSerializable(this.kids);
        dest.writeString(this.url);
        dest.writeValue(this.score);
        dest.writeString(this.title);
        dest.writeString(this.text);
    }

}
