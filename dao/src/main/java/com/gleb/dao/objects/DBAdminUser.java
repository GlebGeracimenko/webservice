package com.gleb.dao.objects;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import com.gleb.dao.cassandra.CQLUserDAO;

@Table(name = CQLUserDAO.ADMIN_USERS_COLUMN_FAMILY)
public class DBAdminUser implements Serializable {
	public static final String IDENTIFIER = "DBAdminUser";
	private static final long serialVersionUID = 1L;


	@PartitionKey
    @Column(name = "user_id")
    private String id;
    private String login;
    private String password;
    private String email;
    private String name;
    @Column(name = "created_at")
    private Date createdAt;
    private List<String> groups;

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getGroups() {
        return groups;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
