package com.gleb.dao.cassandra;

import static com.datastax.driver.core.querybuilder.QueryBuilder.eq;
import static com.datastax.driver.core.querybuilder.QueryBuilder.select;

import java.util.UUID;

import javax.annotation.PostConstruct;

import com.gleb.dao.UserTokenDAO;
import com.gleb.dao.objects.DBToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.querybuilder.Select;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.gleb.dao.objects.TokenState;

@Repository
public class CQLUserTokenDAO implements UserTokenDAO {
	public static final String TOKEN_COLUMN_FAMILY = "user_tokens";

	private Mapper<DBToken> tokenMapper;

	@Autowired
	private BasicCQLCassandraDAO basicCassandraDAO;

	@PostConstruct
	private void init() {
		tokenMapper = new MappingManager(basicCassandraDAO.getSession()).mapper(DBToken.class);
	}

	@Override  
	public void saveToken(DBToken dbToken) {
		tokenMapper.save(dbToken);
	}

	@Override
	public DBToken getTokenInfoForUser(UUID userId, String token) {
		return tokenMapper.get(userId, token);
	}

	@Override
	public DBToken getValidToken(UUID userId) {
		Select.Where selectTokens = select().all().from(TOKEN_COLUMN_FAMILY).where(eq("user_id", userId)).and(eq("state", TokenState.VALID.name()));
		ResultSet resultSet = basicCassandraDAO.getSession().execute(selectTokens);
		return tokenMapper.map(resultSet).one();
	}
}
