package com.mingzhang.jiegon.dao;

import com.mingzhang.jiegon.entity.CarClassEntity;
import com.mingzhang.jiegon.entity.CarListEntity;
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
}
