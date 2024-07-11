package com.wyl.backend.keda.classes.Shop.Controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.wyl.backend.keda.classes.Shop.RedeemInfo;
import com.wyl.backend.keda.classes.Shop.SQL.RedeemInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RequestMapping(value = "/redeem")
@RestController
public class RedeemController {

    @Autowired
    private RedeemInfoMapper redeemInfoMapper;

    @PostMapping(value = "/submit")
    public RedeemInfo submitRedeem(@RequestBody RedeemInfo redeemInfo) {
        redeemInfoMapper.insert(redeemInfo);
        return redeemInfoMapper.selectById(redeemInfo.getId());
    }

    @GetMapping(value = "/query")
    public List<RedeemInfo> queryRedeem(@RequestParam(required = false) String recipientName,
                                        @RequestParam(required = false) String phone,
                                        @RequestParam(required = false) Integer productId) {
        QueryWrapper<RedeemInfo> queryWrapper = new QueryWrapper<>();
        if (recipientName != null) {
            queryWrapper.like("recipient_name", recipientName);
        }
        if (phone != null) {
            queryWrapper.eq("phone", phone);
        }
        if (productId != null) {
            queryWrapper.eq("product_id", productId);
        }
        return redeemInfoMapper.selectList(queryWrapper);
    }
}
