package com.gleb.webservices.service;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

import com.gleb.dao.objects.DBUser;
import com.gleb.webservices.bo.BOBrand;
import com.gleb.webservices.bo.BOBrandWithFollowing;

public interface BrandsService {
    String saveBrand(BOBrand boBrand);

    BOBrandWithFollowing getBrand(String id, UUID userId);

    Set<BOBrandWithFollowing> getBrands(Collection<String> ids, UUID userId);

    BOBrand getBrandByName(String name);

    BOBrand getBrandByDescription(String descPhrase);

    Collection<BOBrandWithFollowing> getAllBrands(DBUser dbUser, int from);

    Collection<BOBrand> getAllBrandsFollowedByUser(UUID userId);

    Collection<String> getAllBrandIdsFollowedByUser(UUID userId);

    boolean followBrand(UUID userId, String brandId);

    boolean unfollowBrand(UUID userId, String brandId);

    Collection<BOBrandWithFollowing> convertToBrandsWithFollowing(Collection<BOBrand> boBrands, UUID userId);
}
