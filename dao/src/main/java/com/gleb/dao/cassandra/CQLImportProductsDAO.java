package com.gleb.dao.cassandra;

import static com.datastax.driver.core.querybuilder.QueryBuilder.eq;
import static com.datastax.driver.core.querybuilder.QueryBuilder.select;
import static com.datastax.driver.core.querybuilder.QueryBuilder.set;
import static com.datastax.driver.core.querybuilder.QueryBuilder.update;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.querybuilder.Select;
import com.datastax.driver.core.querybuilder.Update;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.gleb.dao.ImportProductsDAO;
import com.gleb.dao.objects.DBImportedProduct;

/**
 * DAO object that handles Courses objects with new CQL cassandra approach.
 * 
 * @author viacheslav.vasianovych
 *
 */
@Repository
public class CQLImportProductsDAO implements ImportProductsDAO {

  public static final String IMPORTED_PRODUCTS_TABLE = "imported_products";

  @Autowired
  private BasicCQLCassandraDAO basicCassandraDAO;

  private Mapper<DBImportedProduct> productMapper;

  @PostConstruct
  public void init() {
	MappingManager manager = new MappingManager(basicCassandraDAO.getSession());
	productMapper = manager.mapper(DBImportedProduct.class);
  }

  @Override
  public String save(DBImportedProduct dbImportedProduct) {
	productMapper.save(dbImportedProduct);
	return dbImportedProduct.getId();
  }

  @Override
  public void resolveImportedProduct(String productId) {
	Update.Where update = update(IMPORTED_PRODUCTS_TABLE).with(set("resolved", true)).where(eq("id", productId));
	basicCassandraDAO.getSession().execute(update);
  }

  @Override
  public List<DBImportedProduct> getUnresolvedProducts(String startId, int count) {
	Select select = null;
	ResultSet resultSet = null;
	if (startId == null) {
	  select = select().all().from(IMPORTED_PRODUCTS_TABLE).where(eq("imported", false)).limit(count);
	  resultSet = basicCassandraDAO.getSession().execute(select);
	} else {
	  String query = "select * from "+IMPORTED_PRODUCTS_TABLE+" where imported=false and token(id) > token("+startId+") limit "+count;
	  resultSet = basicCassandraDAO.getSession().execute(query);
	}
	if (resultSet.isExhausted()) {
	  return new ArrayList<DBImportedProduct>();
	}
	return productMapper.map(resultSet).all();
  }

  @Override
  public DBImportedProduct getProductByImportedId(String importedId) {
	Select.Where select = select().all().from(IMPORTED_PRODUCTS_TABLE).allowFiltering()
		.where(eq("imported_id", importedId)).and(eq("imported", false));
	ResultSet resultSet = basicCassandraDAO.getSession().execute(select);
	if (resultSet.isExhausted()) {
	  return null;
	}
	return productMapper.map(resultSet).one();
  }

  @Override
  public DBImportedProduct getById(String id) {
	return productMapper.get(id);
  }

  @Override
  public void deleteById(String id) {
	productMapper.delete(id);
  }
}