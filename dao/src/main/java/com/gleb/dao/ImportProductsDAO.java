package com.gleb.dao;

import java.util.List;

import com.gleb.dao.objects.DBImportedProduct;

/**
 * @author Viacheslav Vasianovych
 */
public interface ImportProductsDAO {

	public DBImportedProduct getById(String id);

	public void deleteById(String id);

	public String save(DBImportedProduct dbImportedProduct);

	public void resolveImportedProduct(String productId);

	public DBImportedProduct getProductByImportedId(String importedId);

	public List<DBImportedProduct> getUnresolvedProducts(String startId, int count);

}