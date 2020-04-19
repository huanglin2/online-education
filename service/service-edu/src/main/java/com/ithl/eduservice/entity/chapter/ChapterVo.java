package com.ithl.eduservice.entity.chapter;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hl
 * 章节
 */
@Data
public class ChapterVo implements Serializable {

    private static final float serialVersionUID = 1L;
    private String id;
    private String title;
    private Integer sort;
    private List<VideoVo> children = new ArrayList<>();
}
