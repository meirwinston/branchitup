package com.branchitup.persistence.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
public class ImageFileId extends BaseEntity{
	private static final long serialVersionUID = 1L;
	
	@Id
	public Long id;
	
	@Transient
	public String url;
	
	@Transient
	public String ThumbnailUrl;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getThumbnailUrl() {
		return ThumbnailUrl;
	}

	public void setThumbnailUrl(String thumbnailUrl) {
		ThumbnailUrl = thumbnailUrl;
	}
}