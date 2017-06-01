package com.gleb.dao;

import com.gleb.dao.objects.SizeGuide;

/**
 * Created by User on 17.08.2015.
 */
public interface SizeGuideDao {

    SizeGuide findSizeGuideByStore(String store);
}
