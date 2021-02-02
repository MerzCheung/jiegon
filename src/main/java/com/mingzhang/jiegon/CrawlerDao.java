package com.mingzhang.jiegon;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author merz
 * @Description:
 */
@Mapper
public interface CrawlerDao {
    @Insert("insert into car_list (layer_model,name,img,created_time) " +
            "values (#{carListEntity.layerModel},#{carListEntity.name},#{carListEntity.img},now())")
    int save(@Param("carListEntity") CarListEntity carListEntity);
}
