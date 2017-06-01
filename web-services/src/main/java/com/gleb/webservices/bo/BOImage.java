package com.gleb.webservices.bo;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by gleb on 26.10.15.
 */
@XmlRootElement
public class BOImage {

    private Long id;
    private String internalName;
    private String internalLink;
    private String externalLink;
    private String hoster;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInternalName() {
        return internalName;
    }

    public void setInternalName(String internalName) {
        this.internalName = internalName;
    }

    public String getInternalLink() {
        return internalLink;
    }

    public void setInternalLink(String internalLink) {
        this.internalLink = internalLink;
    }

    public String getExternalLink() {
        return externalLink;
    }

    public void setExternalLink(String externalLink) {
        this.externalLink = externalLink;
    }

    public String getHoster() {
        return hoster;
    }

    public void setHoster(String hoster) {
        this.hoster = hoster;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BOImage boImage = (BOImage) o;

        if (externalLink != null ? !externalLink.equals(boImage.externalLink) : boImage.externalLink != null)
            return false;
        if (hoster != null ? !hoster.equals(boImage.hoster) : boImage.hoster != null) return false;
        if (id != null ? !id.equals(boImage.id) : boImage.id != null) return false;
        if (internalLink != null ? !internalLink.equals(boImage.internalLink) : boImage.internalLink != null)
            return false;
        if (internalName != null ? !internalName.equals(boImage.internalName) : boImage.internalName != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (internalName != null ? internalName.hashCode() : 0);
        result = 31 * result + (internalLink != null ? internalLink.hashCode() : 0);
        result = 31 * result + (externalLink != null ? externalLink.hashCode() : 0);
        result = 31 * result + (hoster != null ? hoster.hashCode() : 0);
        return result;
    }

}
