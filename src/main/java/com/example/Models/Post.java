package com.example.Models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class Post {
    int id;
   
    String description;
    long upvotes;
    long downvotes;
    Date creation_time;
    int user_id;

    public void setId(int id) {
        this.id = id;
    }
   
    public int getId() {
        return id;
    }
    
    public String getDescription() {
        return description;
    }
    public long getUpvotes() {
        return upvotes;
    }
    public void setUpvotes(long upvotes) {
        this.upvotes = upvotes;
    }
    public long getDownvotes() {
        return downvotes;
    }
    public void setDownvotes(long downvotes) {
        this.downvotes = downvotes;
    }
    public Date getCreation_time() {
        return creation_time;
    }
    public void setCreation_time(Date creation_time) {
        this.creation_time = creation_time;
    }
    public int getUser_id() {
        return user_id;
    }
    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public static Post mapResultSetToPost(ResultSet rs) throws SQLException {
        Post post = new Post();
        post.setId(rs.getInt("id"));
        post.setDescription(rs.getString("description"));
        post.setUpvotes(rs.getLong("upvotes"));
        post.setDownvotes(rs.getLong("downvotes"));
        post.setCreation_time(rs.getDate("creation_time"));
        post.setUser_id(rs.getInt("user_id"));
        return post;
    }
    


    
    
}
