package com.wyl.backend.classes.discuss.Controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.wyl.backend.classes.discuss.Discuss;
import com.wyl.backend.classes.discuss.SQL.DiscussSQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RequestMapping(value = "/discuss")
@RestController
public class DiscussController {
    @Autowired
    private DiscussSQL discussSQL;

    @PostMapping(value = "/update")
    public void insertDiscuss(@RequestBody Discuss discuss){
        if(discuss.getId() != 0){//是更新
            discussSQL.updateById(discuss);
            return;
        }
        discussSQL.insert(discuss);
    }

    @GetMapping(value = "/query")
    public List<Discuss> queryAllDiscuss(@RequestParam int id,@RequestParam String type){
        if(id != 0){
            return discussSQL.selectList(new QueryWrapper<Discuss>().eq("id", id).eq("type", type));
        }
        return discussSQL.selectList(new QueryWrapper<Discuss>().eq("type", type));
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
}
