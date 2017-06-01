package com.gleb.dao;

import java.util.UUID;

import com.gleb.dao.objects.DBCheckSum;

/**
 * Created by gleb on 28.10.15.
 */
public interface CheckSumDAO {

    UUID saveSum(DBCheckSum dbCheckSum);

}
