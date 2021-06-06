package com.example.register.model;

import java.util.List;

public class PostFetch {
    private List<Post> posts;
    private boolean isEnd;

    public void setEnd(boolean end) {
        isEnd = end;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public boolean isEnd() {
        return isEnd;
    }
}
