drop table if exists car_list;
CREATE TABLE `car_list` (
  `id` BIGINT(32) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `layer_model` varchar(32) DEFAULT NULL COMMENT '类型',
  `name` varchar(100) DEFAULT NULL COMMENT '名称',
  `img` varchar(320) DEFAULT NULL COMMENT '图片',
  `is_valid` int(1) DEFAULT 1 COMMENT '是否有效 1：有效 0：无效',
  `created_time` datetime NOT NULL,
  `modified_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


drop table if exists car_class;
CREATE TABLE `car_class` (
  `id` BIGINT(32) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `car_id` BIGINT(32) NOT NULL COMMENT '车辆品牌主键ID',
  `car_type` varchar(100) DEFAULT NULL COMMENT '名称',
  `car_style` varchar(320) DEFAULT NULL COMMENT '图片',
  `is_valid` int(1) DEFAULT 1 COMMENT '是否有效 1：有效 0：无效',
  `created_time` datetime NOT NULL,
  `modified_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
