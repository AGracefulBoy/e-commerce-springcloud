package org.example.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户名和密码
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAndPassword {
    /** 用户名 */
    private String username;
    /** 密码*/
    private String password;
}
