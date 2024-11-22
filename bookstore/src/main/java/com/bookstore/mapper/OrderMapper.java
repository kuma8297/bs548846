package com.bookstore.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bookstore.entity.Order;
import com.bookstore.entity.OrderItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {
    // 自定义订单查询方法可以在这里定义

}
