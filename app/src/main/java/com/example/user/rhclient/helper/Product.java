package com.example.user.rhclient.helper;

public class Product {

    private int id;
    private String title;
    private String description;
    private int upvotes;
    private String thumbnailUrl;

    public Product(int id, String title, String description, int upvotes, String  thumbnailUrl) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.upvotes = upvotes;
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getUpvotes() {
        return upvotes;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public int getId() {
        return id;
    }
}
