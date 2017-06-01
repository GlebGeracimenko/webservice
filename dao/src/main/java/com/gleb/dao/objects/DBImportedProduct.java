package com.gleb.dao.objects;

import java.util.Date;
import java.util.List;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import com.gleb.dao.cassandra.CQLImportProductsDAO;

/**
 * Represents Elasticsearch DB product entity.
 * 
 * @author Viacheslav Vasianovych
 *
 */
@Table(name= CQLImportProductsDAO.IMPORTED_PRODUCTS_TABLE)
public class DBImportedProduct {
    public static final String IDENTIFIER = "ELDBImportedProduct";
    @PartitionKey
    private String id;
    private String name;
    @Column(name="description")
    private String desc;
    private String url;
    private String brand;
    private String sku;
    @Column(name="imported_id")
    private String importedId;
    private List<String> sizes;
    private List<String> colours;
    @Column(name="category_resolved")
    private boolean categoryResolved;
    @Column(name="style_resolved")
    private boolean styleResolved;
    @Column(name="colour_resolved")
    private boolean colourResolved;
    @Column(name="top_category_resolved")
    private boolean topCategoryResolved;
    private List<String> images;
    private String price;
    private List<String> stores;
    private List<String> styles;
    private String category;
    @Column(name="top_category")
    private String topCategory;
    private int gender;
    @Column(name="resolved_date")
    private Date resolvedDate;
    @Column(name="imported_date")
    private Date importedDate;
    private boolean imported;

    public String getImportedId() {
        return importedId;
    }

    public Date getResolvedDate() {
        return resolvedDate;
    }

    public void setResolvedDate(Date resolvedDate) {
        this.resolvedDate = resolvedDate;
    }

    public Date getImportedDate() {
        return importedDate;
    }

    public void setImportedDate(Date importedDate) {
        this.importedDate = importedDate;
    }

    public boolean isImported() {
        return imported;
    }

    public void setImported(boolean imported) {
        this.imported = imported;
    }

    public void setImportedId(String importedId) {
        this.importedId = importedId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((sku == null) ? 0 : sku.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DBImportedProduct other = (DBImportedProduct) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (sku == null) {
            if (other.sku != null)
                return false;
        } else if (!sku.equals(other.sku))
            return false;
        return true;
    }

    public boolean isCategoryResolved() {
        return categoryResolved;
    }

    public List<String> getSizes() {
        return sizes;
    }

    public void setSizes(List<String> sizes) {
        this.sizes = sizes;
    }

    public List<String> getColours() {
        return colours;
    }

    public void setColours(List<String> colours) {
        this.colours = colours;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public List<String> getStores() {
        return stores;
    }

    public void setStores(List<String> stores) {
        this.stores = stores;
    }

    public List<String> getStyles() {
        return styles;
    }

    public void setStyles(List<String> styles) {
        this.styles = styles;
    }

    public String getTopCategory() {
        return topCategory;
    }

    public void setTopCategory(String topCategory) {
        this.topCategory = topCategory;
    }

    public void setCategoryResolved(boolean categoryResolved) {
        this.categoryResolved = categoryResolved;
    }

    public boolean isStyleResolved() {
        return styleResolved;
    }

    public void setStyleResolved(boolean styleResolved) {
        this.styleResolved = styleResolved;
    }

    public boolean isColourResolved() {
        return colourResolved;
    }

    public void setColourResolved(boolean colourResolved) {
        this.colourResolved = colourResolved;
    }

    public boolean isTopCategoryResolved() {
        return topCategoryResolved;
    }

    public void setTopCategoryResolved(boolean topCategoryResolved) {
        this.topCategoryResolved = topCategoryResolved;
    }
}
