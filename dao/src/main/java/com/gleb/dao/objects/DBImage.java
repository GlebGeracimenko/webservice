package com.gleb.dao.objects;

import java.util.UUID;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import com.gleb.dao.cassandra.CQLImagesDAO;

/**
 * Created by gleb on 25.10.15.
 */
@Table(name = CQLImagesDAO.IMAGES_TABLE)
public class DBImage {
    public static final String IDENTIFIER = "DBImage";
    @PartitionKey
    private UUID id;
    @Column(name = "internal_name")
    private String internalName;
    @Column(name = "internal_link")
    private String internalLink;
    @Column(name = "external_link")
    private String externalLink;
    private String hoster;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
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

}
