package com.gleb.dao.objects;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import com.gleb.dao.cassandra.CQLVirtualStylistCanvasDAOImpl;

/**
 * Created by gleb on 31.10.15.
 */
@Table(name = CQLVirtualStylistCanvasDAOImpl.MATCH_CANVAS_TABLE)
public class DBVirtualStylistCanvas {
    public static final String IDENTIFIER = "DBVirtualStylistCanvas";

    @PartitionKey
    private UUID id;
    @Column(name = "user_id")
    private String userId;
    @Column(name = "date_added")
    private Date dateAdded;
    @Column(name = "main_object")
    private UUID mainObject;
    private List<UUID> items;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public List<UUID> getItems() {
        return items;
    }

    public void setItems(List<UUID> items) {
        this.items = items;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }

    public UUID getMainObject() {
        return mainObject;
    }

    public void setMainObject(UUID mainObject) {
        this.mainObject = mainObject;
    }
}
