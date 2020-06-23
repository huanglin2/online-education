package com.ithl.educms.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ithl.commonutils.R;
import com.ithl.educms.entity.CrmBanner;
import com.ithl.educms.service.CrmBannerService;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 首页banner表 前端控制器
 * </p>
 * <p>
 * 这个是后台管理员玩的
 *
 * @author hl
 * @since 2020-04-21
 */
@RestController
@RequestMapping("/educms/banneradmin")
//@CrossOrigin
public class BannerAdminController {

    @Autowired
    private CrmBannerService crmBannerService;

    //    分页查询
    @GetMapping("/pageBanner/{page}/{limit}")
    @ApiModelProperty(value = "分页查询")
    public R getPageBanner(@PathVariable("page") long page, @PathVariable("limit") long limit) {
        Page<CrmBanner> pageBanner = new Page<>(page, limit);
        crmBannerService.page(pageBanner, null);
        return R.ok().data("items", pageBanner.getRecords()).data("total", pageBanner.getTotal());
    }

    // 添加
    @PostMapping("/addBanner")
    @ApiModelProperty(value = "添加bannner")
    public R addBanner(@RequestBody CrmBanner crmBanner) {
        boolean flag = crmBannerService.save(crmBanner);
        if (!flag) {
            throw new RuntimeException("添加失败");
        }
        return R.ok();
    }

    // 删除
    @DeleteMapping("/deleteBanner/{bannerId}")
    @ApiModelProperty(value = "删除bannner")
    public R deleteBanner(@PathVariable("bannerId") String bannerId) {
        boolean flag = crmBannerService.removeById(bannerId);
        if (!flag) {
            throw new RuntimeException("删除失败");
        }
        return R.ok();
    }

    // 查找
    @GetMapping("/getBanner/{bannerId}")
    public R getBannerById(@PathVariable("bannnerId") String bannerId) {
        CrmBanner crmBanner = crmBannerService.getById(bannerId);
        return R.ok().data("banner", crmBanner);
    }

    // 修改
    @DeleteMapping("/updateBanner")
    @ApiModelProperty(value = "修改bannner")
    public R deleteBanner(@RequestBody CrmBanner crmBanner) {
        boolean flag = crmBannerService.updateById(crmBanner);
        if (!flag) {
            throw new RuntimeException("修改失败");
        }
        return R.ok();
    }
}

