package com.ithl.educms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ithl.educms.entity.CrmBanner;
import com.ithl.educms.mapper.CrmBannerMapper;
import com.ithl.educms.service.CrmBannerService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 首页banner表 服务实现类
 * </p>
 *
 * @author hl
 * @since 2020-04-21
 */
@Service
public class CrmBannerServiceImpl extends ServiceImpl<CrmBannerMapper, CrmBanner> implements CrmBannerService {

    @Override
    @Cacheable(key = "'selectList'", value = "banner")
    public List<CrmBanner> selectAllBanner() {
        QueryWrapper<CrmBanner> queryWrapper = new QueryWrapper<>();
        // 降序查询前两条记录
        queryWrapper.orderByDesc("id");
        queryWrapper.last("limit 2");
        List<CrmBanner> crmBanners = baseMapper.selectList(null);
        return crmBanners;
    }
}
