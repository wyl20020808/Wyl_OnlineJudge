package com.wyl.backend.classes.problem;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@TableName("problemcode")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProblemCode {
    public int userid;
    public int problemid;
    public String code;
    public String lasteditortime;
}
