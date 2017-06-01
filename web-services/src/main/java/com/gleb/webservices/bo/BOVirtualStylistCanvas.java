package com.gleb.webservices.bo;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by gleb on 03.11.15.
 */
@XmlRootElement
public class BOVirtualStylistCanvas {
	private Long id;
	private Long userId;
	private List<BOVirtualStylistCanvasItem> items;
	private Date creationTime;
	private Date updateTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public List<BOVirtualStylistCanvasItem> getItems() {
		return items;
	}

	public void setItems(List<BOVirtualStylistCanvasItem> items) {
		this.items = items;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

}
