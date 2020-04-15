package com.ithl.eduservice.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ithl.commonutils.R;
import com.ithl.eduservice.entity.EduTeacher;
import com.ithl.eduservice.entity.vo.TeacherQuery;
import com.ithl.eduservice.service.EduTeacherService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 * 三个可以加的中文描述
 *
 * @author hl
 * @ApiOperation
 * @Api
 * @ApiParam
 * @since 2020-04-13
 */
@RestController
@RequestMapping("/eduservice/teacher")
@CrossOrigin
public class EduTeacherController {
    @Autowired
    private EduTeacherService eduTeacherService;

    /**
     * 查询讲师表所有数据
     */
    @ApiOperation("查询所有讲师")
    @GetMapping("/findAll")
    public R getAllTeacherInfo() {
        List<EduTeacher> teachers = eduTeacherService.list(null);
        return R.ok().data("item", teachers);
    }

    /**
     * 根据id逻辑删除讲师
     *
     * @param id
     * @return
     */
    @ApiOperation("根据id逻辑删除讲师")
    @DeleteMapping("/remove/{id}")
    public R removeTeacher(@PathVariable("id") String id) {
        boolean isSuccess = eduTeacherService.removeById(id);
        if (isSuccess) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    /**
     * 分页查询
     *
     * @return
     */
    @GetMapping("/findByPage/{current}/{limit}")
    public R findByPage(@PathVariable("current") Long current,
                        @PathVariable("limit") Long limit) {
        Page<EduTeacher> teacherPage = new Page<>(current, limit);
        // 调用方法分页
        eduTeacherService.page(teacherPage, null);
        // 总记录数
        long total = teacherPage.getTotal();
        // 数据list集合
        List<EduTeacher> records = teacherPage.getRecords();
        return R.ok().data("total", total).data("rows", records);
    }

    /**
     * @param current
     * @param limit
     * @param teacherQuery
     * @return
     */
    @PostMapping("/pageTeacherCondition/{current}/{limit}")
    public R pageTeacherCondition(@PathVariable("current") Long current,
                                  @PathVariable("limit") Long limit,
                                  @RequestBody(required = false)/*使用json传递数据，把json数据封装到对应对像里面 required = false 表示对象值可以为空*/ TeacherQuery teacherQuery) {
        // 创建page
        Page<EduTeacher> teacherPage = new Page<>(current, limit);
        // 构建条件
        QueryWrapper<EduTeacher> queryWrapper = new QueryWrapper<>();
        // 多条件组合查询
        // 判断是否为空，不是就拼接
        String name = teacherQuery.getName();
        Integer level = teacherQuery.getLevel();
        String begin = teacherQuery.getBegin();
        String end = teacherQuery.getEnd();
        if (!StringUtils.isEmpty(name)) {
            queryWrapper.like("name", name);
        }
        if (!StringUtils.isEmpty(level)) {
            queryWrapper.eq("level", level);
        }
        if (!StringUtils.isEmpty(begin)) {
            queryWrapper.ge("gmt_create", begin);
        }
        if (!StringUtils.isEmpty(end)) {
            queryWrapper.le("gmt_modified", end);
        }
        queryWrapper.orderByDesc("gmt_create");
        eduTeacherService.page(teacherPage, queryWrapper);

        // 总记录数
        long total = teacherPage.getTotal();
        // 数据list集合
        List<EduTeacher> records = teacherPage.getRecords();
        return R.ok().data("total", total).data("rows", records);
    }

    /**
     * 添加讲师
     *
     * @param eduTeacher
     * @return
     */
    @PostMapping("/addTeacher")
    public R addTeacher(@RequestBody EduTeacher eduTeacher) {
        boolean flag = eduTeacherService.save(eduTeacher);
        if (flag) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    @GetMapping("/getTeacher/{id}")
    public R getTeacher(@PathVariable("id") Long id) {
        EduTeacher teacher = eduTeacherService.getById(id);
        if (!StringUtils.isEmpty(teacher)) {
            return R.ok().data("teacher", teacher);
        } else {
            return R.error();
        }
    }

    /**
     * 使用json数据修改
     *
     * @param eduTeacher
     * @return
     */
    @PostMapping("/updateTeacher")
    public R updateTeacher(@RequestBody EduTeacher eduTeacher) {
        boolean flag = eduTeacherService.updateById(eduTeacher);
        if (flag) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    /**
     * 使用传入id修改
     *
     * @param eduTeacher
     * @param id
     * @return
     */
    @PutMapping("updateTeacherById/{id}")
    public R updateTeacherById(EduTeacher eduTeacher, @PathVariable("id") String id) {
        eduTeacher.setId(id);
        boolean flag = eduTeacherService.updateById(eduTeacher);
        if (flag) {
            return R.ok();
        } else {
            return R.error();
        }
    }
}

