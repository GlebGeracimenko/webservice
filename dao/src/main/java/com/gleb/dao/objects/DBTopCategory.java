package com.gleb.dao.objects;

import java.io.Serializable;
import java.util.List;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import com.gleb.dao.cassandra.CQLTopCategoriesDAO;

@Table(name = CQLTopCategoriesDAO.TOP_CATEGORIES_COLUMN_FAMILY)
public class DBTopCategory implements Serializable {
  public static final String IDENTIFIER = "DBTopCategory";
  private static final long serialVersionUID = 1L;

  @PartitionKey
  @Column(name = "top_category_id")
  String id;
  String name;
  String description;
  @Column(name = "sub_categories")
  List<String> subCategories;
  
  public String getId() {
	return id;
  }
  public void setId(String id) {
	this.id = id;
  }
  public String getName() {
	return name;
  }
  public void setName(String name) {
	this.name = name;
  }
  public String getDescription() {
	return description;
  }
  public void setDescription(String description) {
	this.description = description;
  }
  public List<String> getSubCategories() {
	return subCategories;
  }
  public void setSubCategories(List<String> subCategories) {
	this.subCategories = subCategories;
  }
}