package com.gleb.dao.cassandra;

import static com.datastax.driver.core.querybuilder.QueryBuilder.eq;
import static com.datastax.driver.core.querybuilder.QueryBuilder.select;

import java.util.Collection;

import javax.annotation.PostConstruct;

import com.gleb.dao.TopCategoriesDAO;
import com.gleb.dao.objects.DBTopCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.querybuilder.Select;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;

@Repository
public class CQLTopCategoriesDAO implements TopCategoriesDAO {

  @Autowired
  private BasicCQLCassandraDAO basicCassandraDAO;

  private Mapper<DBTopCategory> categoryMapper;

  @PostConstruct
  public void init() {
	MappingManager manager = new MappingManager(basicCassandraDAO.getSession());
	categoryMapper = manager.mapper(DBTopCategory.class);
  }

  @Override
  public String saveCategory(DBTopCategory dbTopCategory) {
	categoryMapper.save(dbTopCategory);
	return dbTopCategory.getId();
  }

  @Override
  public DBTopCategory getCategoryById(String id) {
	return categoryMapper.get(id);
  }

  @Override
  public DBTopCategory getCategoryByName(String name) {
	Select.Where query = select().from(TOP_CATEGORIES_COLUMN_FAMILY).where(eq("name", name));
	ResultSet resultSet = basicCassandraDAO.getSession().execute(query);
	if (resultSet.getAvailableWithoutFetching() < 1) {
	  return null;
	}
	return categoryMapper.map(resultSet).one();
  }

  @Override
  public Collection<DBTopCategory> getAllCategories() {
	Select query = select().all().from(TOP_CATEGORIES_COLUMN_FAMILY);
	ResultSet resultSet = basicCassandraDAO.getSession().execute(query);
	if (resultSet.getAvailableWithoutFetching() < 1) {
	  return null;
	}
	return categoryMapper.map(resultSet).all();
  }
}