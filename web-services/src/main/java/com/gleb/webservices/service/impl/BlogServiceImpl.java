package com.gleb.webservices.service.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.gleb.webservices.bo.BOBlogPost;
import com.gleb.webservices.service.BlogService;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class BlogServiceImpl implements BlogService {

    @Value("${services.blog.mysqlhost}")
    private String mysqlHost;
    @Value("${services.blog.mysqlport}")
    private String mysqlPort;
    @Value("${services.blog.mysqlusername}")
    private String mysqlUsername;
    @Value("${services.blog.mysqluserpassword}")
    private String mysqlPassword;
    @Value("${services.blog.mysqldbname}")
    private String mysqlDBName;

    public List<BOBlogPost> readDataBase(Date date, int limit) {
        ResultSet resultSet = null;
        Connection connect = null;
        PreparedStatement preparedStatement = null;
        try {
            // This will load the MySQL driver, each DB has its own driver
            Class.forName("com.mysql.jdbc.Driver");
            // Setup the connection with the DB
            connect = DriverManager.getConnection("jdbc:mysql://" + mysqlHost + ":" + mysqlPort + "/" + mysqlDBName + "?" + "user=" + mysqlUsername
                    + "&password=" + mysqlPassword);

            // Statements allow to issue SQL queries to the database
            preparedStatement = connect.prepareStatement("select * from " + mysqlDBName + ".ilove_posts where post_date > ? order by post_date desc limit ? ");
            java.sql.Date mysqlDate = new java.sql.Date(date.getTime());
            preparedStatement.setDate(1, mysqlDate);
            preparedStatement.setInt(2, limit);
            // Result set get the result of the SQL query
            resultSet = preparedStatement.executeQuery();
            return convertResultSet(resultSet);
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            close(resultSet, preparedStatement, connect);
        }
        return new ArrayList<BOBlogPost>();
    }

    private List<BOBlogPost> convertResultSet(ResultSet resultSet) throws SQLException {
        List<BOBlogPost> blogPosts = new ArrayList<BOBlogPost>();
        while (resultSet.next()) {
            Integer postId = resultSet.getInt("ID");
            Date postDate = resultSet.getDate("post_date");
            String title = resultSet.getString("post_title");
            String postName = resultSet.getString("post_name");
            String guid = resultSet.getString("guid");
            String postContent = resultSet.getString("post_content");
            postContent = StringEscapeUtils.escapeHtml4(postContent);
            BOBlogPost blogPost = new BOBlogPost();
            blogPost.setGuid(guid);
            blogPost.setPostName(postName);
            blogPost.setId(postId);
            if (postContent.length() > 100) {
                postContent = postContent.substring(0, 100);
                postContent = postContent + "...";
            }
            blogPost.setContent(postContent);
            blogPost.setTitle(title);
            blogPost.setDate(postDate);
            blogPosts.add(blogPost);
        }
        return blogPosts;
    }

    @Override
    public List<BOBlogPost> getPosts(Date startDate, int count) {
        return readDataBase(startDate, count);
    }

    // You need to close the resultSet
    private void close(ResultSet resultSet, Statement statement, Connection connect) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }

            if (statement != null) {
                statement.close();
            }

            if (connect != null) {
                connect.close();
            }
        } catch (Exception e) {

        }
    }
}
