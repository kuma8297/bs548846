-- ==============================================
-- 创建数据库：在线书店系统
-- ==============================================
CREATE DATABASE IF NOT EXISTS `online_bookstore`
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;
USE `online_bookstore`;

-- ==============================================
-- 用户表：存储用户的基本信息
-- ==============================================
CREATE TABLE `users` (
                         `id` BIGINT AUTO_INCREMENT COMMENT '用户ID，主键',
                         `username` VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名，唯一',
                         `password` VARCHAR(255) NOT NULL COMMENT '用户密码，使用加密存储',
                         `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱地址，可选',
                         `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '用户创建时间',
                         PRIMARY KEY (`id`)
) COMMENT='用户表，存储用户的基本信息';

-- ==============================================
-- 书籍表：存储书籍信息
-- ==============================================
CREATE TABLE `books` (
                         `id` BIGINT AUTO_INCREMENT COMMENT '书籍ID，主键',
                         `title` VARCHAR(255) NOT NULL COMMENT '书名',
                         `author` VARCHAR(100) NOT NULL COMMENT '作者',
                         `price` DECIMAL(10, 2) NOT NULL COMMENT '书籍价格，精确到两位小数',
                         `stock` INT NOT NULL COMMENT '库存数量',
                         `version` INT DEFAULT 0 COMMENT '乐观锁版本号',
                         `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
                         `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录更新时间',
                         PRIMARY KEY (`id`)
) COMMENT='书籍表，存储书籍的基本信息';

-- ==============================================
-- 订单表：存储订单的基本信息
-- ==============================================
CREATE TABLE `orders` (
                          `id` BIGINT AUTO_INCREMENT COMMENT '订单ID，主键',
                          `user_id` BIGINT NOT NULL COMMENT '下单用户ID，关联users表',
                          `total_price` DECIMAL(10, 2) NOT NULL COMMENT '订单总金额，精确到两位小数',
                          `status` VARCHAR(20) DEFAULT 'PENDING' COMMENT '订单状态，例如：PENDING, COMPLETED, CANCELED',
                          `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '订单创建时间',
                          PRIMARY KEY (`id`),
                          FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) COMMENT='订单表，存储订单的基本信息';

-- ==============================================
-- 订单项表：存储订单中的每项书籍
-- ==============================================
CREATE TABLE `order_items` (
                               `id` BIGINT AUTO_INCREMENT COMMENT '订单项ID，主键',
                               `order_id` BIGINT NOT NULL COMMENT '所属订单ID，关联orders表',
                               `book_id` BIGINT NOT NULL COMMENT '书籍ID，关联books表',
                               `quantity` INT NOT NULL COMMENT '购买数量',
                               `price` DECIMAL(10, 2) NOT NULL COMMENT '书籍单价，精确到两位小数',
                               PRIMARY KEY (`id`),
                               FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
                               FOREIGN KEY (`book_id`) REFERENCES `books` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) COMMENT='订单项表，存储订单中的每项书籍';

-- ==============================================
-- 创建索引：提高查询效率
-- ==============================================
-- 用户名索引
CREATE UNIQUE INDEX `idx_username` ON `users` (`username`);

-- 书籍表作者字段索引
CREATE INDEX `idx_author` ON `books` (`author`);

-- 订单表用户字段索引
CREATE INDEX `idx_order_user` ON `orders` (`user_id`);

-- 订单项表订单字段索引
CREATE INDEX `idx_order_item_order` ON `order_items` (`order_id`);

-- ==============================================
-- 数据库表结构完成
-- ==============================================
