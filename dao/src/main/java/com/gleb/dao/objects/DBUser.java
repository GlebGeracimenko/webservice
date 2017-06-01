package com.gleb.dao.objects;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import com.gleb.dao.cassandra.CQLUserDAO;

@Table(name = CQLUserDAO.USERS_COLUMN_FAMILY)
public class DBUser implements Serializable {
	public static final String IDENTIFIER = "DBUser";
	private static final long serialVersionUID = 1L;

	public static final int GENDER_MALE = 1;
	public static final int GENDER_FEMALE = 0;

	@PartitionKey
    @Column(name = "user_id")
    private UUID id;
    private String login;
    private String password;
    private String email;
    private String name;
    @Column(name = "created_at")
    private Date createdAt;
    private List<String> groups;
    private Boolean deleted;
    private int gender;
    @Column(name="social_ids")
    private Map<String, String> socialIds;
    @Column(name="social_tokens")
    private Map<String, String> socialTokens;
    @Column(name="wizard_state")
    private Map<String, Boolean> wizardState;

    public Map<String, String> getSocialIds() {
		return socialIds;
	}

	public void setSocialIds(Map<String, String> socialIds) {
		this.socialIds = socialIds;
	}

	public Map<String, String> getSocialTokens() {
		return socialTokens;
	}

	public void setSocialTokens(Map<String, String> socialTokens) {
		this.socialTokens = socialTokens;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
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

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Map<String, Boolean> getWizardState() {
        return wizardState;
    }

    public void setWizardState(Map<String, Boolean> wizardState) {
        this.wizardState = wizardState;
    }
}
