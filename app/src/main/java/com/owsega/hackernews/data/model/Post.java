package com.owsega.hackernews.data.model;

import java.util.ArrayList;

public class Post {

    public Long id;
    public String by;
    public Long time;
    public ArrayList<Long> kids;
    public String url;
    public Long score;
    public String title;
    public String text;
}
