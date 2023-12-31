package com.wyl.backend.classes.discuss.Controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.wyl.backend.classes.discuss.Discuss;
import com.wyl.backend.classes.discuss.DiscussState;
import com.wyl.backend.classes.discuss.SQL.DiscussSQL;
import com.wyl.backend.classes.discuss.SQL.DiscussStateSQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@CrossOrigin
@RequestMapping(value = "/discuss")
@RestController
public class DiscussController {
    @Autowired
    private DiscussSQL discussSQL;
    @Autowired
    private DiscussStateSQL stateSQL;

    @PostMapping(value = "/update")
    public Discuss insertDiscuss(@RequestBody Discuss discuss){
        if(discuss.getId() != 0){//是更新
            discussSQL.updateById(discuss);
            return discuss;
        }
        discussSQL.insert(discuss);
        //返回有discussid的值，方便前端更新
        return discussSQL.selectOne(new QueryWrapper<Discuss>().eq("userid", discuss.getUserid()).eq("createtime", discuss.getCreatetime()));
    }
    @PostMapping(value = "/query/bylist")
    public List<Discuss> queryDiscussByList(@RequestBody List<Integer> list){
        QueryWrapper<Discuss> queryWrapper = new QueryWrapper<>();
        HashSet<Integer> set = new HashSet<>();
        for(int i = 0; i < list.size(); i++) {
            set.add(list.get(i));
        }
        List<Discuss>  discussList = new ArrayList<>();
        List<Discuss> queryList = discussSQL.selectList(queryWrapper);

        for(int i = 0; i < queryList.size(); i++) {
            if(set.contains(queryList.get(i).getId())){
                discussList.add(queryList.get(i));
            }
        }
        return discussList;
    }


    @GetMapping(value = "/query")
    public List<Discuss> queryDiscuss(@RequestParam int id, @RequestParam(required = false) String type){
        QueryWrapper<Discuss> queryWrapper = new QueryWrapper<>();
        if(id != 0){
            queryWrapper.eq("id", id);
        }
        if(type != null){
            queryWrapper.eq("type", type);
        }
        return discussSQL.selectList(queryWrapper);
    }


    @GetMapping(value = "/query/all")
    public List<Discuss> queryAllDiscussByUserid(@RequestParam int userid){
        if(userid != 0){
            return discussSQL.selectList(new QueryWrapper<Discuss>().eq("userid", userid));
        }
        return discussSQL.selectList(null);
    }

    @GetMapping(value = "/reply/query")//返回某个评论的所有回复
    public List<Discuss> queryReply(@RequestParam int id){
      return discussSQL.selectList(new QueryWrapper<Discuss>().eq("target", id).eq("type", "reply"));
    }

    @GetMapping(value = "/comment/query") //根据文章id查询评论
    public List<Discuss> queryCommentById(@RequestParam int target){
        return discussSQL.selectList(new QueryWrapper<Discuss>().eq("target", target).eq("type", "comment"));
    }

    @PostMapping(value = "/operator")
    public void operatorDiscuss(@RequestBody Discuss discuss) {//文章的多个操作
        if (discuss.getSpecial().equals("delete")) {
            discussSQL.deleteById(discuss.getId());
        } else  {//这里是让某个字段值变化
            UpdateWrapper<Discuss> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id", discuss.getId())
                    .setSql(discuss.getSpecial() + " = " + discuss.getSpecial() + (discuss.getDetal() > 0 ? " + 1" : " - 1")); // 设置要更新的字段及操作
            discussSQL.update(null, updateWrapper);
        }
    }

    @Transactional
    @PostMapping(value = "/state/update")//这里为什么要返回，因为要返回更新的数据，方便前端互动
    public void updateState(@RequestBody DiscussState discussState){
        // 使用悲观锁查询数据
        DiscussState result = stateSQL.selectOne(new QueryWrapper<DiscussState>().eq("userid", discussState.getUserid()).eq("discussid", discussState.getDiscussid()).last("FOR UPDATE"));
        if (result != null) {
            // 存在符合条件的数据
            // 执行相应的操作
            stateSQL.update(discussState, new UpdateWrapper<DiscussState>().eq("userid", discussState.getUserid()).eq("discussid", discussState.getDiscussid()));
        } else {
            // 不存在符合条件的数据
            // 执行其他操作
            stateSQL.insert(discussState);
        }

    }
    @GetMapping(value = "/state/query")
    public DiscussState  queryState(@RequestParam("userid") int userid, @RequestParam("discussid") int discussid) {
        return stateSQL.selectOne(new QueryWrapper<DiscussState>().eq("userid", userid).eq("discussid", discussid));
    }

}
