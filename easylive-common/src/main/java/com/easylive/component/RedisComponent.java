package com.easylive.component;

import com.easylive.entity.constants.constants;
import com.easylive.entity.dto.TokenUserInfoDto;
import com.easylive.redis.RedisUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.UUID;

@Component
public class RedisComponent {
    @Resource
    private RedisUtils redisUtils;

    public String saveCheckCode(String code) {
        String checkCodeKey = UUID.randomUUID().toString();
        redisUtils.setex(constants.REDIS_KEY_CHECK_CODE + checkCodeKey, code, constants.REDIS_KEY_EXPIRES_ONE_MIN * 10);
        return checkCodeKey;
    }

    public String getCheckCode(String checkCodeKey) {
        return (String)redisUtils.get(constants.REDIS_KEY_CHECK_CODE + checkCodeKey);
    }

    public void cleanCheckCode(String checkCodeKey) {
        redisUtils.delete(constants.REDIS_KEY_CHECK_CODE + checkCodeKey);
    }

    public void saveTokenInfo(TokenUserInfoDto tokenUserInfoDto){
        String token = UUID.randomUUID().toString();
        tokenUserInfoDto.setExpireAt(System.currentTimeMillis() + constants.REDIS_KEY_EXPIRES_ONE_DAY * 7);
        tokenUserInfoDto.setToken(token);
        redisUtils.set(constants.REDIS_KEY_TOKEN_WEB + token + tokenUserInfoDto , constants.REDIS_KEY_EXPIRES_ONE_DAY * 7);
    }
}
