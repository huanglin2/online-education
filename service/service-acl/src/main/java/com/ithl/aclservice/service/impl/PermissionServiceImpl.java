package com.ithl.aclservice.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ithl.aclservice.entity.Permission;
import com.ithl.aclservice.entity.RolePermission;
import com.ithl.aclservice.entity.User;
import com.ithl.aclservice.helper.MemuHelper;
import com.ithl.aclservice.helper.PermissionHelper;
import com.ithl.aclservice.mapper.PermissionMapper;
import com.ithl.aclservice.service.PermissionService;
import com.ithl.aclservice.service.RolePermissionService;
import com.ithl.aclservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 权限 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-01-12
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

    @Autowired
    private RolePermissionService rolePermissionService;

    @Autowired
    private UserService userService;


    @Override
    public List<JSONObject> selectPermissionByUserId(String userId) {
        List<Permission> selectPermissionList = null;
        if (this.isSysAdmin(userId)) {
            //如果是超级管理员，获取所有菜单
            selectPermissionList = baseMapper.selectList(null);
        } else {
            selectPermissionList = baseMapper.selectPermissionByUserId(userId);
        }

        List<Permission> permissionList = PermissionHelper.bulid(selectPermissionList);
        List<JSONObject> result = MemuHelper.bulid(permissionList);
        return result;
    }


    /**
     * 判断用户是否系统管理员
     *
     * @param userId
     * @return
     */
    private boolean isSysAdmin(String userId) {
        User user = userService.getById(userId);

        if (null != user && "admin".equals(user.getUsername())) {
            return true;
        }
        return false;
    }

    //========================递归查询所有菜单================================================

    /*用递归套娃*/
    @Override
    public List<Permission> queryAllMenuGuii() {
        List<Permission> permissions = baseMapper.selectList(null);
        List<Permission> result = bulidPermission(permissions);
        return result;
    }

    private List<Permission> bulidPermission(List<Permission> permissions) {
        ArrayList<Permission> permission = new ArrayList<>();
        for (Permission permissionNode : permissions) {
            if ("0".equals(permissionNode.getPid())) {
                permissionNode.setLevel(1);
                permission.add(selectChildren(permissionNode, permissions));
            }
        }
        return permission;
    }

    private Permission selectChildren(Permission permissionNode, List<Permission> permissions) {
        permissionNode.setChildren(new ArrayList<>());
        for (Permission permission : permissions) {
            if (permissionNode.getId().equals(permission.getPid())) {
                Integer level = permissionNode.getLevel() + 1;
                permission.setLevel(level);
                permissionNode.getChildren().add(selectChildren(permission, permissions));
            }
        }
        return permissionNode;
    }

    /*递归的删除所有子节点*/
    @Override
    public void removeChildByIdGuli(String id) {
        List<String> idList = new ArrayList<>();
        idList = removeIds(id, idList);
        baseMapper.deleteBatchIds(idList);
    }

    private List<String> removeIds(String id, List<String> idList) {
        QueryWrapper<Permission> wrapper = new QueryWrapper<>();
        wrapper.eq("pid", id);
        wrapper.select("id");
        List<Permission> permissions = baseMapper.selectList(wrapper);
        permissions.stream().forEach(item -> {
            idList.add(item.getId());
            this.removeIds(item.getId(), idList);
        });
        return idList;
    }

    /*给角色分配菜单*/
    @Override
    public void saveRolePermissionRealtionShipGuli(String roleId, String[] permissionIds) {
        List<RolePermission> rolePermissionList = new ArrayList<>();
        for (String permissionId : permissionIds) {
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(roleId);
            rolePermission.setPermissionId(permissionId);
            rolePermissionList.add(rolePermission);
        }
        rolePermissionService.saveBatch(rolePermissionList);
    }

    @Override
    public List<String> selectPermissionValueByUserId(String id) {
        return null;
    }
}
