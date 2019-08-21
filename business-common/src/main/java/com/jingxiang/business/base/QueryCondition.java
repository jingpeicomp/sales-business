package com.jingxiang.business.base;


import org.springframework.data.domain.Example;

import java.io.Serializable;

/**
 * JPA查询条件
 * Created by liuzhaoming on 2019/8/21.
 */
public interface QueryCondition<T> extends Serializable {
    Example<T> generate();
}
