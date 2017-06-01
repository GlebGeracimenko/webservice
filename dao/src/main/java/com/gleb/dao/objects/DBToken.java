package com.gleb.dao.objects;

import java.util.Date;
import java.util.UUID;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import com.gleb.dao.cassandra.CQLUserTokenDAO;

@Table(name = CQLUserTokenDAO.TOKEN_COLUMN_FAMILY)
public class DBToken {
	@PartitionKey(0)
	@Column(name = "user_id")
	private UUID userId;
	@PartitionKey(1)
	@Column(name = "user_token")
	private String token;

	@Column(name = "created_at")
	private Date createdAt;

	@Column(name = "expired_at")
	private Date expiredAt;
	
	@Column(name = "state")
	private String state;

	public UUID getUserId() {
		return userId;
	}

	public void setUserId(UUID userId) {
		this.userId = userId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getExpiredAt() {
		return expiredAt;
	}

	public void setExpiredAt(Date expiredAt) {
		this.expiredAt = expiredAt;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return "DBToken [userId=" + userId + ", token=" + token + ", createdAt=" + createdAt + ", expiredAt="
				+ expiredAt + ", state=" + state + "]";
	}
}
