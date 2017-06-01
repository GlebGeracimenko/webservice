package com.gleb.webservices.service;

import java.util.Date;
import java.util.List;

import com.gleb.webservices.bo.BOBlogPost;

public interface BlogService {
    public List<BOBlogPost> getPosts(Date startDate, int count);
}
