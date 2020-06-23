package com.ithl.aclservice.service;

import com.alibaba.fastjson.JSONObject;
import com.ithl.aclservice.entity.Permission;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 权限 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-01-12
 */
public interface PermissionService extends IService<Permission> {


    List<JSONObject> selectPermissionByUserId(String id);

    //递归删除菜单
    void removeChildByIdGuli(String id);

    //给角色分配权限
    void saveRolePermissionRealtionShipGuli(String roleId, String[] permissionIds);

    List<Permission> queryAllMenuGuii();

    List<String> selectPermissionValueByUserId(String id);
}
