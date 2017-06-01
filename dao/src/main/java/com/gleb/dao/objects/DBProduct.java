package com.gleb.dao.objects;

import java.util.List;

/**
 * Represents Elasticsearch DB product entity.
 * 
 * @author Viacheslav Vasianovych
 *
 */
public class DBProduct {
	public static final String IDENTIFIER = "ELDBProduct";
	private String id;
	private String importedId;
	private String name;
	private String desc;
	private String url;
	private String brand;
	private String sku;
	private List<String> images;
	private double price;
	private List<String> size;
	private List<String> color;
	private String store;
	private String style;
	private String category;
	private String topCategory;
	public boolean isAvatar2Dready() {
        return avatar2Dready;
    }

    public void setAvatar2Dready(boolean avatar2Dready) {
        this.avatar2Dready = avatar2Dready;
    }

    private int gender;
	private List<String> resolvedImages;
	private Long cluster;
	private boolean avatar2Dready;
	
	public String getStyle() {
		return style;
	}

	public String getTopCategory() {
        return topCategory;
    }

    public void setTopCategory(String topCategory) {
        this.topCategory = topCategory;
    }

    public void setStyle(String style) {
		this.style = style;
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

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public List<String> getSize() {
		return size;
	}

	public void setSize(List<String> size) {
		this.size = size;
	}

	public List<String> getColor() {
		return color;
	}

	public void setColor(List<String> color) {
		this.color = color;
	}

	public String getStore() {
		return store;
	}

	public void setStore(String store) {
		this.store = store;
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

	public String getImportedId() {
		return importedId;
	}

	public void setImportedId(String importedId) {
		this.importedId = importedId;
	}
	
	public List<String> getResolvedImages() {
	  return resolvedImages;
	}

	public void setResolvedImages(List<String> resolvedImages) {
	  this.resolvedImages = resolvedImages;
	}

	public Long getCluster() {
	  return cluster;
	}

	public void setCluster(Long cluster) {
	  this.cluster = cluster;
	}
	
	@Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((sku == null) ? 0 : sku.hashCode());
        result = prime * result + ((store == null) ? 0 : store.hashCode());
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
        DBProduct other = (DBProduct) obj;
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
        if (store == null) {
            if (other.store != null)
                return false;
        } else if (!store.equals(other.store))
            return false;
        return true;
    }
}