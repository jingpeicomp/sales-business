package com.jingxiang.business.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * 分页查询响应结果
 * Created by liuzhaoming on 2019/8/2.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse<T> implements Serializable {
    /**
     * 记录总条数
     */
    private long total;

    /**
     * 返回数据
     */
    private List<T> content;

    private static final PageResponse EMPTY = new PageResponse<>(0L, Collections.emptyList());

    @SuppressWarnings("unchecked")
    public static <T> PageResponse<T> empty() {
        return EMPTY;
    }
}
