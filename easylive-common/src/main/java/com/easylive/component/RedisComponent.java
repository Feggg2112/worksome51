package com.easylive.component;

import com.easylive.entity.constants.constants;
import com.easylive.entity.dto.TokenUserInfoDto;
import com.easylive.entity.po.CategoryInfo;
import com.easylive.redis.RedisUtils;
import com.easylive.utils.StringTools;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
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
        return (String) redisUtils.get(constants.REDIS_KEY_CHECK_CODE + checkCodeKey);
    }

    public void cleanCheckCode(String checkCodeKey) {
        redisUtils.delete(constants.REDIS_KEY_CHECK_CODE + checkCodeKey);
    }

    public void saveTokenInfo(TokenUserInfoDto tokenUserInfoDto) {
        String token = UUID.randomUUID().toString();
        tokenUserInfoDto.setExpireAt(System.currentTimeMillis() + constants.REDIS_KEY_EXPIRES_ONE_DAY * 7);
        tokenUserInfoDto.setToken(token);
        redisUtils.set(constants.REDIS_KEY_TOKEN_WEB + token + tokenUserInfoDto, constants.REDIS_KEY_EXPIRES_ONE_DAY * 7);
    }

    public TokenUserInfoDto getTokenInfo(String token) {
        return (TokenUserInfoDto) redisUtils.get(constants.REDIS_KEY_TOKEN_WEB + token);
    }

    public String getToken4Admin(String token) {
        return (String) redisUtils.get(constants.REDIS_KEY_TOKEN_ADMIN + token);
    }

    public String saveTokenInfo4Admin(String account) {

        String token = UUID.randomUUID().toString();
        redisUtils.setex(constants.REDIS_KEY_TOKEN_ADMIN + token, account, constants.REDIS_KEY_EXPIRES_ONE_DAY * 1);
        return token;


    }

    public void cleanToken(String token) {
        redisUtils.delete(constants.REDIS_KEY_TOKEN_WEB + token);
    }

    public void cleanToken4Admin(String token) {
        redisUtils.delete(constants.REDIS_KEY_TOKEN_ADMIN + token);
    }

    public void saveCategoryList(List<CategoryInfo> categoryInfoList){
        redisUtils.set(constants.REDIS_KEY_CATEGORY_LIST,categoryInfoList);//永久保存的
    }

    public List<CategoryInfo> getCategoryList(){
        return (List<CategoryInfo>) redisUtils.get(constants.REDIS_KEY_CATEGORY_LIST);
    }

    public void svePreVideoFileInfo(String userId, String fileName, Integer chunks) {
        String uploadId = StringTools.getRandomString(constants.LENTH_15);
    }

}
