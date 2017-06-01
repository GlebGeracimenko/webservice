package com.gleb.webservices.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.gleb.webservices.bo.BOBrand;
import com.gleb.webservices.bo.BOBrandWithFollowing;
import com.gleb.webservices.mapping.BrandsMapper;
import com.gleb.webservices.service.BrandsService;
import com.gleb.webservices.service.IdsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.gleb.dao.BrandsDAO;
import com.gleb.dao.FollowsDAO;
import com.gleb.dao.objects.DBBrand;
import com.gleb.dao.objects.DBUser;

@Service
public class BrandsServiceImpl implements BrandsService {

    @Value("${services.brand.pagesize}")
    private Integer pagesize;

    @Autowired
    private IdsService idsService;

    @Autowired
    private BrandsDAO brandsDAO;

    @Autowired
    private BrandsMapper brandsMapper;

    @Autowired
    private FollowsDAO followsDAO;

    @Override
    public String saveBrand(BOBrand boBrand) {
        DBBrand dbBrand = brandsMapper.map(boBrand);
        if (dbBrand.getId() == null) {
            dbBrand.setId(idsService.getNewIdAsString());
        }
        return brandsDAO.saveBrand(dbBrand);
    }

    @Override
    public BOBrandWithFollowing getBrand(String id, UUID userId) {
        DBBrand dbBrand = brandsDAO.getBrand(id);
        if(dbBrand == null) {
            return null;
        }
        BOBrand boBrand = brandsMapper.map(dbBrand);
        boolean follow = followsDAO.checkFollow(userId, id, FollowsDAO.TYPE_BRAND);
        BOBrandWithFollowing brandWithFollowing = brandsMapper.mapWithFollowing(boBrand);
        brandWithFollowing.setFollowing(follow);
        return brandWithFollowing;
    }

    @Override
    public BOBrand getBrandByName(String name) {
        DBBrand dbBrand = brandsDAO.getBrandByName(name);
        return brandsMapper.map(dbBrand);
    }

    @Override
    public BOBrand getBrandByDescription(String descPhrase) {
        DBBrand dbBrand = brandsDAO.getBrandByDescription(descPhrase);
        return brandsMapper.map(dbBrand);
    }

    @Override
    public Collection<BOBrandWithFollowing> getAllBrands(DBUser dbUser, int from) {
        List<DBBrand> brands = brandsDAO.getAllBrands(new HashMap<String, Object>(), dbUser, from, pagesize);
        Set<BOBrand> boBrands = brandsMapper.map(brands);
        Collection<String> followingBrands = getAllBrandIdsFollowedByUser(dbUser.getId());
        List<BOBrandWithFollowing> boBrandsWithFollowing = new ArrayList<BOBrandWithFollowing>(boBrands.size());
        for (BOBrand boBrand : boBrands) {
            BOBrandWithFollowing boBrandWithFollowing = brandsMapper.mapWithFollowing(boBrand);
            boBrandWithFollowing.setFollowing(followingBrands.contains(boBrand.getId()));
            boBrandsWithFollowing.add(boBrandWithFollowing);
        }
        return boBrandsWithFollowing;
    }

    @Override
    public Collection<BOBrand> getAllBrandsFollowedByUser(UUID userId) {
        Collection<String> brandIds = followsDAO.getAllFollowedObjectsByUser(userId, FollowsDAO.TYPE_BRAND);
        List<String> brandIdsString = new ArrayList<String>(brandIds.size());
        for (String id : brandIds) {
            brandIdsString.add(id);
        }
        Collection<DBBrand> dbBrands = brandsDAO.getBrands(brandIdsString);
        return brandsMapper.map(dbBrands);
    }

    @Override
    public boolean followBrand(UUID userId, String brandId) {
        followsDAO.follow(userId, brandId, FollowsDAO.TYPE_BRAND);
        return true;
    }

    @Override
    public boolean unfollowBrand(UUID userId, String brandId) {
        followsDAO.unfollow(userId, brandId, FollowsDAO.TYPE_BRAND);
        return true;
    }

    @Override
    public Collection<String> getAllBrandIdsFollowedByUser(UUID userId) {
        return followsDAO.getAllFollowedObjectsByUser(userId, FollowsDAO.TYPE_BRAND);
    }

    @Override
    public Set<BOBrandWithFollowing> getBrands(Collection<String> ids, UUID userId) {
        Set<String> stringIds = new HashSet<String>();
        for (String id : ids) {
            stringIds.add(id);
        }
        Collection<DBBrand> dbBrands = brandsDAO.getBrands(stringIds);
        Collection<BOBrand> boBrands = brandsMapper.map(dbBrands);
        Collection<String> followingBrands = getAllBrandIdsFollowedByUser(userId);
        Set<BOBrandWithFollowing> brandsWithFollowing = new HashSet<BOBrandWithFollowing>();
        for (BOBrand boBrand : boBrands) {
            BOBrandWithFollowing brandWithFollowing = brandsMapper.mapWithFollowing(boBrand);
            brandWithFollowing.setFollowing(followingBrands.contains(boBrand.getId()));
            brandsWithFollowing.add(brandWithFollowing);
        }
        return brandsWithFollowing;
    }

    @Override
    public Collection<BOBrandWithFollowing> convertToBrandsWithFollowing(Collection<BOBrand> boBrands, UUID userId) {
        Collection<String> followingBrands = getAllBrandIdsFollowedByUser(userId);
        List<BOBrandWithFollowing> brandsWithFollowing = new ArrayList<BOBrandWithFollowing>(boBrands.size());
        for (BOBrand boBrand : boBrands) {
            boolean following = followingBrands.contains(boBrand.getId());
            BOBrandWithFollowing brandWithFollowing = brandsMapper.mapWithFollowing(boBrand);
            brandWithFollowing.setFollowing(following);
            brandsWithFollowing.add(brandWithFollowing);
        }
        return brandsWithFollowing;
    }
}
