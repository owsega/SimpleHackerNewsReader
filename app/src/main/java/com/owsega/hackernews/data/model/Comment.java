package com.owsega.hackernews.data.model;

import java.util.ArrayList;

public class Comment {

    public String text;
    public Long time;
    public String by;
    public Long id;
    public ArrayList<Long> kids;
    public ArrayList<Comment> comments;
    public int depth = 0;
}
