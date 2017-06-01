                                                                                                                                                                                                                                                                                                                                                                                                                                                                            package com.gleb.dao.cassandra;

import static com.datastax.driver.core.querybuilder.QueryBuilder.eq;
import static com.datastax.driver.core.querybuilder.QueryBuilder.select;
import static com.datastax.driver.core.querybuilder.QueryBuilder.set;
import static com.datastax.driver.core.querybuilder.QueryBuilder.update;

import java.util.UUID;

import javax.annotation.PostConstruct;

import com.gleb.dao.UsersDAO;
import com.gleb.dao.objects.DBAdminUser;
import com.gleb.dao.objects.DBUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.querybuilder.Select;
import com.datastax.driver.core.querybuilder.Update;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;

                                                                                                                                                                                                                                                                                                                                                                                                                                                                            /**
 * DAO object that handles Courses objects with new CQL cassandra approach.
 * 
 * @author viacheslav.vasianovych
 *
 */
@Repository
public class CQLUserDAO implements UsersDAO {

	public static final String USERS_COLUMN_FAMILY = "users";
	public static final String ADMIN_USERS_COLUMN_FAMILY = "admin_users";

	@Autowired
	private BasicCQLCassandraDAO basicCassandraDAO;

	private Mapper<DBUser> userMapper;

	private Mapper<DBAdminUser> adminUserMapper;

	@PostConstruct
	public void init() {
		MappingManager manager = new MappingManager(basicCassandraDAO.getSession());
		userMapper = manager.mapper(DBUser.class);
		adminUserMapper = manager.mapper(DBAdminUser.class);
	}

	@Override
	public DBUser getUserById(UUID id) {
		return userMapper.get(id);
	}

	public void setDeleted(String id, boolean deleted) {
		Update.Where query = update(USERS_COLUMN_FAMILY).with(set("deleted", deleted)).where(eq("user_id", id));
		basicCassandraDAO.getSession().execute(query);
	}

	@Override
	public UUID saveUser(DBUser dbUser) {
		userMapper.save(dbUser);
		return dbUser.getId();
	}

	@Override
	public DBUser getUserByLogin(String login) {
		Select.Where query = select().all().from(USERS_COLUMN_FAMILY).where(eq("login", login));
		ResultSet resultSet = basicCassandraDAO.getSession().execute(query);
		if (resultSet.getAvailableWithoutFetching() < 1) {
			return null;
		}
		return userMapper.map(resultSet).one();
	}

	@Override
	public DBUser getUserByEmail(String email) {
		Select.Where query = select().all().from(USERS_COLUMN_FAMILY).where(eq("email", email));
		ResultSet resultSet = basicCassandraDAO.getSession().execute(query);
		if (resultSet.getAvailableWithoutFetching() < 1) {
			return null;
		}
		return userMapper.map(resultSet).one();
	}

	@Override
	public DBUser getUserBySocialId(String socialNetwork, String id) {
		ResultSet resultSet = basicCassandraDAO.getSession().execute(
				"SELECT * FROM " + USERS_COLUMN_FAMILY + " WHERE social_ids['" + socialNetwork + "'] = '" + id + "'");
		return userMapper.map(resultSet).one();
	}

	@Override
	public DBAdminUser getAdminUserByLogin(String login) {
		Select.Where query = select().all().from(ADMIN_USERS_COLUMN_FAMILY).where(eq("login", login));
		ResultSet resultSet = basicCassandraDAO.getSession().execute(query);
		if (resultSet.getAvailableWithoutFetching() < 1) {
			return null;
		}
		return adminUserMapper.map(resultSet).one();
	}
}
