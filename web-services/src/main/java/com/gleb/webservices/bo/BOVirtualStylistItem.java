package com.gleb.webservices.bo;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by gleb on 03.11.15.
 */
@XmlRootElement
public class BOVirtualStylistItem {

    private Long id;
    private Long canvasId;
    private String skuCode;
    private int positionX;
    private int positionY;
    private boolean mainObject;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCanvasId() {
        return canvasId;
    }

    public void setCanvasId(Long canvasId) {
        this.canvasId = canvasId;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public int getPositionX() {
        return positionX;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    public boolean isMainObject() {
        return mainObject;
    }

    public void setMainObject(boolean mainObject) {
        this.mainObject = mainObject;
    }

}
