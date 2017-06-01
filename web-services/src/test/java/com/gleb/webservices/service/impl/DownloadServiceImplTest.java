package com.gleb.webservices.service.impl;

import com.gleb.webservices.service.DownloadService;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by gleb on 27.10.15.
 */
public class DownloadServiceImplTest {

    @Test
    public void test() {
        DownloadService downloadService = new DownloadServiceImpl();
        try {
            downloadService.dowload("http://datafeed.api.productserve.com/datafeed/download/apikey/be04a790443cf070c8c67e30a82f4bb5/fid/2872/columns/aw_product_id,merchant_product_id,merchant_category,aw_deep_link,merchant_image_url,search_price,description,product_name,merchant_deep_link,aw_image_url,merchant_name,merchant_id,category_name,category_id,delivery_cost,currency,store_price,display_price,data_feed_id,rrp_price,specifications,condition,promotional_text,warranty,merchant_thumb_url,aw_thumb_url,brand_name,brand_id,delivery_time,valid_from,valid_to,web_offer,pre_order,in_stock,stock_quantity,is_for_sale,product_type,commission_group,upc,ean,mpn,isbn,model_number,parent_product_id,language,last_updated,dimensions,colour,keywords,custom_1,custom_2,custom_3,custom_4,custom_5,saving,delivery_weight,delivery_restrictions,reviews,average_rating,number_stars,number_available,rating,alternate_image,large_image,basket_link/format/csv/delimiter/,/compression/gzip/adultcontent/1/");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
