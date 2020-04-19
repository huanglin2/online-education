package com.ithl.eduservice.entity.chapter;

import lombok.Data;

import java.io.Serializable;

/**
 * @author hl
 * <p>
 * 小节
 */
@Data
public class VideoVo implements Serializable {
    private static final float serialVersionUID = 1L;

    private String id;
    private String title;
    private Integer sort;
}
