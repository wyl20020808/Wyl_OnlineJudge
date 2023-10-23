package com.wyl.backend.classes.Collect.Controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.wyl.backend.classes.Collect.Collect;
import com.wyl.backend.classes.Collect.CollectSet;
import com.wyl.backend.classes.Collect.SQL.CollectSQL;
import com.wyl.backend.classes.Collect.SQL.CollectSetSQL;
import com.wyl.backend.classes.discuss.Discuss;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RequestMapping(value = "/collect")
@RestController
public class CollectController {
    @Autowired
    private CollectSQL collectSQL;
    @Autowired
    private CollectSetSQL collectSetSQL;

    @PostMapping(value = "/update")//包含插入、更新、删除
    public Collect updateCollect(@RequestBody Collect collect,@RequestParam(required = false) String delete){
        if(delete != null){
            collectSQL.delete(new QueryWrapper<Collect>().eq("userid",collect.getUserid()).eq("collectid",collect.getCollectid()));
            return null;
        }
        if(collect.getId() > 0){
            collectSQL.updateById(collect);
        }else{
            collectSQL.insert(collect);
        }
        return collectSQL.selectOne(new QueryWrapper<Collect>().eq("userid",collect.getUserid()).eq("createtime",collect.getCreatetime()));
    }

    @GetMapping(value = "/query")//包含插入和更新
    public List<Collect> queryCollect(@RequestParam int userid,@RequestParam(required = false) Integer belong,

                                      @RequestParam(required = false) String type,
                                      @RequestParam(required = false) Integer collectid){
        QueryWrapper<Collect> query = new QueryWrapper<>();
        query.eq("userid",userid);
        if(collectid != null) {
            query.eq("collectid", collectid);
        }
        if(belong != null){
            query.eq("belong",belong);
        }
        if(type != null){

            query.eq("type",type);
        }

        return collectSQL.selectList(query);
    }

    @PostMapping(value = "/update/set")//包含插入、更新、删除
    public CollectSet updateCollectSet(@RequestBody CollectSet collectSet,@RequestParam(required = false) String delete){
        System.out.println(collectSet.toString() +"ahjsdhas");
        if(delete != null){
            collectSetSQL.delete(new QueryWrapper<CollectSet>().eq("id",collectSet.getId()));
            return null;
        }
        if(collectSet.getId() > 0){
            collectSetSQL.updateById(collectSet);
        }else{
            collectSetSQL.insert(collectSet);
        }
        return collectSetSQL.selectOne(new QueryWrapper<CollectSet>().eq("userid",collectSet.getUserid()).eq("createtime",collectSet.getCreatetime()));
    }

    @GetMapping(value = "/set/operator")
    public void operatorDiscuss(@RequestParam Integer id,@RequestParam Integer detal) {//统计次数
        UpdateWrapper<CollectSet> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", id)
                .setSql("count = count" + (detal > 0 ? " + 1" : " - 1")); // 设置要更新的字段及操作
        collectSetSQL.update(null, updateWrapper);
    }

    @GetMapping(value = "/query/set")//包含所有查询
    public List<CollectSet> queryCollectSet(@RequestParam(required = false) Integer userid,@RequestParam(required = false) String type,@RequestParam(required = false) Integer id){
        QueryWrapper<CollectSet> query = new QueryWrapper<>();
        if(userid != null){
            query.eq("userid",userid);
        }
        if(id != null){
            query.eq("id",id);
        }
        if(type != null){
            query.eq("type",type);
        }
        return collectSetSQL.selectList(query);
    }
}
