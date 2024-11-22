package com.bookstore.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bookstore.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    // 自定义用户查询方法可以在这里定义
}
