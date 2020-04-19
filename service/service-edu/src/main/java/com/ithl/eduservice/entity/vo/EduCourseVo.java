package com.ithl.eduservice.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author hl
 */
@Data
public class EduCourseVo implements Serializable {

    private static final Long serialVersionUID = 1L;
    @ApiModelProperty("课程id")
    private String id;
    @ApiModelProperty("讲师id")
    private String teacherId;
    @ApiModelProperty("一级课程专业id")
    private String subjectId;
    @ApiModelProperty("二级课程的父亲id")
    private String subjectParentId;
    @ApiModelProperty("课程标题")
    private String title;
    @ApiModelProperty("课程价格")
    private BigDecimal price;
    @ApiModelProperty("总课时")
    private Integer lessonNum;
    @ApiModelProperty("封面")
    private String cover;
    @ApiModelProperty("描述")
    private String description;

}
