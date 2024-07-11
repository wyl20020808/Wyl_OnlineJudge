package com.wyl.backend.keda.classes.Shop;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@TableName("redeem_info")
@JsonIgnoreProperties(ignoreUnknown = true)
public class RedeemInfo {
    @TableId(value = "id")
    private int id;

    @TableField(value = "recipient_name")
    private String recipientName;

    @TableField(value = "address")
    private String address;

    @TableField(value = "phone")
    private String phone;

    @TableField(value = "product_id")
    private int productId;

    @TableField(value = "quantity")
    private int quantity;

    @TableField(value = "redeem_date")
    private String redeemDate;

    @TableLogic
    private Boolean deleted = false;
}
