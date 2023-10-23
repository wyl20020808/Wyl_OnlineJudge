package com.wyl.backend.classes.user.Controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wyl.backend.classes.user.sql.UserExtraOperator;
import com.wyl.backend.classes.user.sql.UserFansSQL;
import com.wyl.backend.classes.user.userinfo.UserExtra;
import com.wyl.backend.classes.user.userinfo.UserFans;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@CrossOrigin
@RequestMapping("/userfans")
@RestController
public class UserFansController {
    @Autowired
    private UserFansSQL userFansSQL;
    @Autowired
    private UserExtraOperator  userExtraOperator;
    @PostMapping("/add")
    public void setUserShip(@RequestBody UserFans userFans){
        userFansSQL.insert(userFans);//增加粉丝关系

        int userid = userFans.getUserid();
        int fansuserid = userFans.getFansuserid();
        UpdateWrapper<UserExtra> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("userid", userid).setSql("fans = fans + 1");//增加粉丝数量
        userExtraOperator.update(null,updateWrapper);

        UpdateWrapper<UserExtra> updateWrapper2 = new UpdateWrapper<>();
        updateWrapper2.eq("userid", fansuserid).setSql("concern = concern + 1");//增加关注数量
        userExtraOperator.update(null, updateWrapper2);
    }
    @GetMapping("/query/fans")
    public Boolean queryUserFans(@RequestParam("userid") int userId,@RequestParam("fansid") int fansId){
        QueryWrapper<UserFans> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userid",userId).eq("fansuserid",fansId);
        if(userFansSQL.selectOne(queryWrapper) != null)return true;
        return false;
    }

    @GetMapping("/delete/fans")
    public void deleteUserFans(@RequestParam("userid") int userId,@RequestParam("fansid") int fansId){
        QueryWrapper<UserFans> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userid",userId).eq("fansuserid",fansId);

        UpdateWrapper<UserExtra> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("userid", userId).setSql("fans = fans - 1");//减少粉丝数量
        userExtraOperator.update(null,updateWrapper);

        UpdateWrapper<UserExtra> updateWrapper2 = new UpdateWrapper<>();
        updateWrapper2.eq("userid", fansId).setSql("concern = concern - 1");//减少关注数量
        userExtraOperator.update(null, updateWrapper2);
        userFansSQL.delete(queryWrapper);
    }

    @GetMapping("/query/myfans")
    public List<UserFans> queryMyFans(@RequestParam("userid") int userId){
        QueryWrapper<UserFans> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userid",userId);
        return userFansSQL.selectList(queryWrapper);
    }

    @GetMapping("/query/myconcern")
    public List<UserFans> queryMyConcern(@RequestParam("userid") int userId){
        QueryWrapper<UserFans> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("fansuserid",userId);
        return userFansSQL.selectList(queryWrapper);
    }
}
