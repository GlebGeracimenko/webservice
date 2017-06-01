package com.gleb.dao.objects;

import java.util.UUID;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import com.gleb.dao.cassandra.CQLVirtualStylistItemDAOImpl;

/**
 * Created by gleb on 31.10.15.
 */
@Table(name = CQLVirtualStylistItemDAOImpl.MATCH_CANVAS_ITEMS)
public class DBVirtualStylistCanvasItem {
    public static final String IDENTIFIER = "DBVirtualStylistItem";

    @PartitionKey
    private UUID id;
    @Column(name="canvas_id")
    private UUID canvasId;
    @Column(name="sku_code")
    private String skuCode;
    private long positionX;
    private long positionY;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getCanvasId() {
        return canvasId;
    }

    public void setCanvasId(UUID canvasId) {
        this.canvasId = canvasId;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public long getPositionX() {
        return positionX;
    }

    public void setPositionX(long positionX) {
        this.positionX = positionX;
    }

    public long getPositionY() {
        return positionY;
    }

    public void setPositionY(long positionY) {
        this.positionY = positionY;
    }
}
