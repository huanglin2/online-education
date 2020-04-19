package com.ithl.eduservice.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author hl
 */
@Data
public class CourseQuery implements Serializable {
    private static final Long serialVersionUID = 1L;

    @ApiModelProperty(value = "创建开始时间")
    private String title;
    @ApiModelProperty(value = "状态 Normal 为已发布 Draft为未发布")
    private String status;
    @ApiModelProperty(value = "课时数")
    private Integer lessonNum;
    @ApiModelProperty(value = "最小价格")
    private BigDecimal minPrice;
    @ApiModelProperty(value = "最大价格")
    private BigDecimal maxPrice;

}
