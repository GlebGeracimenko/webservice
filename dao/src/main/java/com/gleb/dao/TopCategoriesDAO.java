package com.gleb.dao;

import java.util.Collection;

import com.gleb.dao.objects.DBTopCategory;

public interface TopCategoriesDAO {
  public static final String TOP_CATEGORIES_COLUMN_FAMILY = "top_categories";

  String saveCategory (DBTopCategory dbTopCategory);

  DBTopCategory getCategoryById(String id);

  DBTopCategory getCategoryByName(String name);

  Collection<DBTopCategory> getAllCategories();

}