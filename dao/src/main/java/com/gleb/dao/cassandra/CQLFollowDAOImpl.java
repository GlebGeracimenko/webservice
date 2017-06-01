package com.gleb.dao.cassandra;

import static com.datastax.driver.core.querybuilder.QueryBuilder.batch;
import static com.datastax.driver.core.querybuilder.QueryBuilder.delete;
import static com.datastax.driver.core.querybuilder.QueryBuilder.eq;
import static com.datastax.driver.core.querybuilder.QueryBuilder.insertInto;
import static com.datastax.driver.core.querybuilder.QueryBuilder.select;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import com.gleb.dao.FollowsDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.querybuilder.Batch;
import com.datastax.driver.core.querybuilder.Delete;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.Select;

@Repository
public class CQLFollowDAOImpl implements FollowsDAO {

	public static final String USER_ID_COLUMN = "user_id";
	public static final String OBJECT_ID_COLUMN = "object_id";
	public static final String OBJECT_TYPE_COLUMN = "type";

	public static final String FOLLOWS_USERS_COLUMN_FAMILY = "follows_users";
	public static final String USERS_FOLLOWS_COLUMN_FAMILY = "users_follows";

	@Autowired
	private BasicCQLCassandraDAO basicCQLDAO;

	@Override
	public void follow(UUID userId, String brandId, String type) {
		Insert insertFollows = insertInto(FOLLOWS_USERS_COLUMN_FAMILY).value(OBJECT_ID_COLUMN, brandId)
				.value(USER_ID_COLUMN, userId).value(OBJECT_TYPE_COLUMN, type);
		Insert insertUsers = insertInto(USERS_FOLLOWS_COLUMN_FAMILY).value(USER_ID_COLUMN, userId)
				.value(OBJECT_ID_COLUMN, brandId).value(OBJECT_TYPE_COLUMN, type);
		Batch batch = batch(insertFollows, insertUsers);
		basicCQLDAO.getSession().execute(batch);
	}

	@Override
	public void unfollow(UUID userId, String brandId, String type) {
		Delete.Where deleteFollows = delete().all().from(FOLLOWS_USERS_COLUMN_FAMILY)
				.where(eq(OBJECT_ID_COLUMN, brandId)).and(eq(USER_ID_COLUMN, userId)).and(eq(OBJECT_TYPE_COLUMN, type));
		Delete.Where deleteUsers = delete().all().from(USERS_FOLLOWS_COLUMN_FAMILY).where(eq(USER_ID_COLUMN, userId))
				.and(eq(OBJECT_ID_COLUMN, brandId)).and(eq(OBJECT_TYPE_COLUMN, type));
		Batch batch = batch(deleteFollows, deleteUsers);
		basicCQLDAO.getSession().execute(batch);
	}

	@Override
	public Collection<String> getAllFollowedObjectsByUser(UUID userId, String type) {
		Select.Where select = select(OBJECT_ID_COLUMN).from(USERS_FOLLOWS_COLUMN_FAMILY)
				.where(eq(USER_ID_COLUMN, userId)).and(eq(OBJECT_TYPE_COLUMN, type));
		ResultSet resultSet = basicCQLDAO.getSession().execute(select);
		List<String> result = new ArrayList<String>(100);
		for (Row row : resultSet.all()) {
			String brandId = row.getString(OBJECT_ID_COLUMN);
			result.add(brandId);
		}
		return result;
	}

	@Override
	public Collection<String> getAllUserFollowingObject(String brandId, String type) {
		Select.Where select = select(USER_ID_COLUMN).from(FOLLOWS_USERS_COLUMN_FAMILY)
				.where(eq(OBJECT_ID_COLUMN, brandId)).and(eq(OBJECT_TYPE_COLUMN, type));
		ResultSet resultSet = basicCQLDAO.getSession().execute(select);
		List<String> result = new ArrayList<String>(100);
		for (Row row : resultSet.all()) {
			String userId = row.getUUID(USER_ID_COLUMN).toString();
			result.add(userId);
		}
		return result;
	}

	@Override
	public boolean checkFollow(UUID userId, String objectId, String type) {
		Select.Where select = select(USER_ID_COLUMN).from(FOLLOWS_USERS_COLUMN_FAMILY).where(eq(OBJECT_ID_COLUMN, objectId)).and(eq(OBJECT_TYPE_COLUMN, type)).and(eq(USER_ID_COLUMN, userId));
		ResultSet resultSet = basicCQLDAO.getSession().execute(select);
		Row row = resultSet.one();
		return row != null;
		
	}
}
