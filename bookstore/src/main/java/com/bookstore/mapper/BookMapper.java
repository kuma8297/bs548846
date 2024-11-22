package com.bookstore.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bookstore.entity.Book;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface BookMapper extends BaseMapper<Book> {
    /**
     * 使用乐观锁更新库存
     *
     * @param id       书籍ID
     * @param quantity 减少的库存数量
     * @param version  乐观锁版本号
     * @return 更新的行数
     */
    @Update("UPDATE books SET stock = stock - #{quantity}, version = version + 1 " +
            "WHERE id = #{id} AND stock >= #{quantity} AND version = #{version}")
    int updateStockWithVersion(@Param("id") Long id, @Param("quantity") Integer quantity, @Param("version") Integer version);
}
