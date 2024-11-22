package com.bookstore.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bookstore.entity.OrderItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderItemMapper extends BaseMapper<OrderItem> {
    /**
     * 批量插入订单项
     *
     * @param items 订单项列表
     */
//    @Insert({
//            "<script>",
//            "INSERT INTO order_items (order_id, book_id, quantity, price) VALUES ",
//            "<foreach collection='list' item='item' separator=','>",
//            "(#{item.orderId}, #{item.bookId}, #{item.quantity}, #{item.price})",
//            "</foreach>",
//            "</script>"
//    })
//    void batchInsert(@Param("list") List<OrderItem> items);


    /**
     * 根据订单ID查询订单项
     *
     * @param orderId 订单ID
     * @return 订单项列表
     */
    @Select("SELECT * FROM order_items WHERE order_id = #{orderId}")
    List<OrderItem> selectByOrderId(@Param("orderId") Long orderId);
}
