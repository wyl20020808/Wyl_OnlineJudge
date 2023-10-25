package com.wyl.backend.classes.user.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

public interface UserProtocol {
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class UserLoginInfoInput implements Serializable {
        private String  username;
        private String password;
    }

}
