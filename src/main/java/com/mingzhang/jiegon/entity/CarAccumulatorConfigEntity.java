package com.mingzhang.jiegon.entity;

import lombok.Data;

@Data
public class CarAccumulatorConfigEntity {
    private Integer tbId;
    private Integer carDetailsId;
    private String type;
    private String name;
    private String capacity;
    private String specification;
    private String pillarType;
    private String fixedPolarity;
}
