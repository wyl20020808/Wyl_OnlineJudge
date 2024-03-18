package com.wyl.backend.classes.limit;

import com.wyl.backend.classes.contest.ContestContent;
import com.wyl.backend.classes.contest.SQL.ContestContentSQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RequestMapping(value = "/discuss")
@RestController
public class checkGrade {
    @Autowired
    private ContestContentSQL contestContentSql;

    public String checkGradeLegal(){

        return "yes";
    }
}
