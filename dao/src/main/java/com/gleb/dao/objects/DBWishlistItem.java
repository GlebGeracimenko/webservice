package com.gleb.dao.objects;

import java.util.Date;
import java.util.UUID;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import com.gleb.dao.cassandra.CQLWishlistsDAOImpl;

@Table(name = CQLWishlistsDAOImpl.WISHLISTS_COLUMN_FAMILY)
public class DBWishlistItem {
    @PartitionKey(1)
    @Column(name = "item_id")
    private String itemId;
    @PartitionKey(0)
    @Column(name = "user_id")
    private UUID userId;
    @Column(name = "item_type")
    private String type;
    @Column(name = "date_added")
    private Date dateAdded;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }
}
