package org.example.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 授权中心鉴权之后给客户端的Token
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtToken {
    /**  JWT */
    private String token;
}
