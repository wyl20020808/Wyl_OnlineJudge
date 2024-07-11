package com.wyl.backend.keda.classes.Shop;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@TableName("product_info")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductInfo {
    @TableId(value = "id")
    private int id;

    @TableField(value = "name")
    private String name;

    @TableField(value = "price")
    private double price;

    @TableField(value = "stock")
    private int stock;

    @TableField(value = "tags")
    private String tags;

    @TableField(value = "description")
    private String description;

    @TableField(value = "launch_date")
    private String launchDate;

    @TableField(value = "images")
    private String images; // 存储图片路径集合的JSON字符串

    @TableField(value = "freeShipping")
    private Boolean freeShipping; // 新增字段

    @TableLogic
    private Boolean deleted = false;
}
