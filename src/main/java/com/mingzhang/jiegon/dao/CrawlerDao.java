package com.mingzhang.jiegon.dao;

import com.mingzhang.jiegon.entity.CarCcEntity;
import com.mingzhang.jiegon.entity.CarClassEntity;
import com.mingzhang.jiegon.entity.CarListEntity;
import com.mingzhang.jiegon.entity.CarYearEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author merz
 * @Description:
 */
@Mapper
public interface CrawlerDao {
    @Insert("insert into car_list (id,layer_model,name,img,created_time) " +
            "values (#{carListEntity.id},#{carListEntity.layerModel},#{carListEntity.name},#{carListEntity.img},now())")
    int saveCarList(@Param("carListEntity") CarListEntity carListEntity);

    @Insert("insert into car_class (id,car_list_id,car_type,car_style,created_time) " +
            "values (#{carClassEntity.id},#{carClassEntity.carListId},#{carClassEntity.carType},#{carClassEntity.carStyle},now())")
    int saveCarClass(@Param("carClassEntity") CarClassEntity carClassEntity);

    @Insert("insert into car_year (id,car_class_id,name,created_time) " +
            "values (#{carYearEntity.id},#{carYearEntity.carClassId},#{carYearEntity.name},now())")
    int saveCarYear(@Param("carYearEntity") CarYearEntity carYearEntity);

    @Insert("insert into car_cc (id,car_class_id,name,created_time) " +
            "values (#{carCcEntity.id},#{carCcEntity.carClassId},#{carCcEntity.name},now())")
    int saveCarCc(@Param("carCcEntity") CarCcEntity carCcEntity);
}
