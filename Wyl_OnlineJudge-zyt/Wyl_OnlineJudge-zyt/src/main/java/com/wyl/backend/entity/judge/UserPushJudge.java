package com.wyl.backend.entity.judge;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPushJudge {
    private long problemId;
    private String source_code;
    private int language_id;
}
