drop table if exists car_list;
CREATE TABLE `car_list` (
  `tb_id` BIGINT(32) NOT NULL AUTO_INCREMENT COMMENT '系统主键',
  `id` BIGINT(32) NOT NULL COMMENT '主键',
  `layer_model` varchar(32) NOT NULL COMMENT '类型',
  `name` varchar(100) NOT NULL COMMENT '名称',
  `img` varchar(320) NOT NULL COMMENT '图片',
  `is_valid` int(1) DEFAULT 1 COMMENT '是否有效 1：有效 0：无效',
  `created_time` datetime NOT NULL,
  `modified_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`tb_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='车辆品牌列表';


drop table if exists car_class;
CREATE TABLE `car_class` (
  `tb_id` BIGINT(32) NOT NULL AUTO_INCREMENT COMMENT '系统主键',
  `id` BIGINT(32) NOT NULL COMMENT '主键',
  `car_list_id` BIGINT(32) NOT NULL COMMENT '车辆品牌主键ID',
  `car_type` varchar(100) NOT NULL COMMENT '品牌',
  `car_style` varchar(320) NOT NULL COMMENT '车型',
  `is_valid` int(1) DEFAULT 1 COMMENT '是否有效 1：有效 0：无效',
  `created_time` datetime NOT NULL,
  `modified_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`tb_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='车辆型号列表';


drop table if exists car_year;
CREATE TABLE `car_year` (
  `tb_id` BIGINT(32) NOT NULL AUTO_INCREMENT COMMENT '系统主键',
  `id` BIGINT(32) NOT NULL COMMENT '主键',
  `car_class_id` BIGINT(32) NOT NULL COMMENT '车辆型号主键ID',
  `name` varchar(100) NOT NULL COMMENT '名称',
  `is_valid` int(1) DEFAULT 1 COMMENT '是否有效 1：有效 0：无效',
  `created_time` datetime NOT NULL,
  `modified_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`tb_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='年限';

drop table if exists car_cc;
CREATE TABLE `car_cc` (
  `tb_id` BIGINT(32) NOT NULL AUTO_INCREMENT COMMENT '系统主键',
  `id` BIGINT(32) NOT NULL COMMENT '主键',
  `car_class_id` BIGINT(32) NOT NULL COMMENT '车辆型号主键ID',
  `name` varchar(100) NOT NULL COMMENT '名称',
  `is_valid` int(1) DEFAULT 1 COMMENT '是否有效 1：有效 0：无效',
  `created_time` datetime NOT NULL,
  `modified_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`tb_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='排量';

drop table if exists car_details;
CREATE TABLE `car_details` (
  `tb_id` BIGINT(32) NOT NULL AUTO_INCREMENT COMMENT '系统主键',
  `id` BIGINT(32) NOT NULL COMMENT '主键',
  `car_class_id` BIGINT(32) NOT NULL COMMENT '车辆型号主键ID',
  `car_year_id` BIGINT(32) NOT NULL COMMENT '年款',
  `car_cc_id` BIGINT(32) NOT NULL COMMENT '排量',
  `name` varchar(100) NOT NULL COMMENT '名称',
  `is_valid` int(1) DEFAULT 1 COMMENT '是否有效 1：有效 0：无效',
  `created_time` datetime NOT NULL,
  `modified_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`tb_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='车辆型号详情列表';


drop table if exists car_accumulator_config;
CREATE TABLE `car_accumulator_config` (
  `tb_id` BIGINT(32) NOT NULL AUTO_INCREMENT COMMENT '系统主键',
  `id` BIGINT(32) NOT NULL COMMENT '主键',
  `car_details_id` BIGINT(32) NOT NULL COMMENT '车辆型号主键ID',
  `type` varchar(100) DEFAULT NULL COMMENT '电池种类',
  `name` varchar(100) DEFAULT NULL COMMENT '参考名称',
  `capacity` varchar(100) DEFAULT NULL COMMENT '电池容量',
  `specification` varchar(100) DEFAULT NULL COMMENT '规格尺寸',
  `pillar_type` varchar(100) DEFAULT NULL COMMENT '端柱类型',
  `fixed_polarity` varchar(100) DEFAULT NULL COMMENT '极性固定',
  `is_valid` int(1) DEFAULT 1 COMMENT '是否有效 1：有效 0：无效',
  `created_time` datetime NOT NULL,
  `modified_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`tb_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='车辆蓄电池配置详情';

drop table if exists car_accumulator_list;
CREATE TABLE `car_accumulator_list` (
  `tb_id` BIGINT(32) NOT NULL AUTO_INCREMENT COMMENT '系统主键',
  `id` BIGINT(32) NOT NULL COMMENT '主键',
  `car_details_id` BIGINT(32) NOT NULL COMMENT '车辆型号主键ID',
  `name` varchar(100) NOT NULL COMMENT '名称',
  `is_valid` int(1) DEFAULT 1 COMMENT '是否有效 1：有效 0：无效',
  `created_time` datetime NOT NULL,
  `modified_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`tb_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='车辆蓄电池配置列表';
