package com.gleb.dao.objects;

import java.util.Date;
import java.util.UUID;

import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import com.gleb.dao.cassandra.CQLCheckSumDAOImpl;

/**
 * Created by gleb on 28.10.15.
 */
@Table(name = CQLCheckSumDAOImpl.CHECK_SUM_TABLE)
public class DBCheckSum {
    public static final String IDENTIFIER = "DBCheckSum";
    @PartitionKey
    private UUID id;
    private String name;
    private String brand;
    private Date time;
    private String md5;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

}
