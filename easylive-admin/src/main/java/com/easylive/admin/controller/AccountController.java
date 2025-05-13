package com.easylive.admin.controller;

import com.easylive.component.RedisComponent;
import com.easylive.entity.config.AppConfig;
import com.easylive.entity.constants.Constants;
import com.easylive.entity.vo.ResponseVO;
import com.easylive.exception.BusinessException;
import com.easylive.redis.RedisUtils;
import com.easylive.service.UserInfoService;
import com.easylive.utils.StringTools;
import com.wf.captcha.ArithmeticCaptcha;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller
 */
@RestController
@RequestMapping("/account")
public class AccountController extends ABaseController {

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private RedisComponent redisComponent;
    @Autowired
    private AppConfig appConfig;

    @RequestMapping("/checkCode")
    public ResponseVO checkCode() {
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(100, 42);
        String code = captcha.text();
        String checkCodeKey = redisComponent.saveCheckCode(code);
        String checkCodeBase64 = captcha.toBase64();

        Map<String, String> result = new HashMap<>();

        result.put("checkCodeBase64", checkCodeBase64);
        result.put("checkCodeKey", checkCodeKey);

        return getSuccessResponseVO(result);//修改到这个
    }

//    @RequestMapping("/register")
//    public ResponseVO register(@NotEmpty @Email @Size(max = 150) String email,
//                               @NotEmpty @Size(max = 20) String nickName,
//                               @NotEmpty @Pattern(regexp = constants.REGEX_PASSWORD) String registerPassword,
//                               @NotEmpty String checkCodeKey,
//                               @NotEmpty String checkCode
//    ) {
//
//        try {
//            if (!checkCode.equalsIgnoreCase(redisComponent.getCheckCode(checkCodeKey))) {
//                throw new BusinessException("图片验证码不正确");
//            }
//            userInfoService.register(email, nickName, registerPassword);
//            return getSuccessResponseVO(null);
//        } finally {
//            redisComponent.cleanCheckCode(checkCodeKey);
//        }
//
//    }

    @RequestMapping("/login")
    public ResponseVO login(
                            HttpServletRequest request,
                            HttpServletResponse response,
                            @NotEmpty String account,
                            @NotEmpty String password,
                            @NotEmpty String checkCodeKey,
                            @NotEmpty String checkCode
    ) {
/**
 *
 * 登录，try下面一行我搞成全部都进了，我不懂为什么会抛出验证码异常，而且我验证码redis看里都是对的
 * 解决了，apipost里参数设置成params了，应该是放在body里，一个是查询一个是请求体
 */
        try {
            if (!checkCode.equalsIgnoreCase(redisComponent.getCheckCode(checkCodeKey))) {
                throw new BusinessException("图片验证码不正确");

            }
            if(!account.equals(appConfig.getAdminAccount())||!password.equals(StringTools.encodeByMD5(appConfig.getAdminPassword()))){
                throw new BusinessException("账号或密码错误");
            }

            String token = redisComponent.saveTokenInfo4Admin(account);
            saveToken2Cookie(response, token);
            return getSuccessResponseVO(account);
        } finally {
            redisComponent.cleanCheckCode(checkCodeKey);
            Cookie[] cookies = request.getCookies();
            if(cookies != null){
                String token = null;
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals(Constants.TOKEN_ADMIN)) {
                        token = cookie.getValue();
                    }
                }
                if (!StringUtils.isEmpty(token)) {
                    redisComponent.cleanToken4Admin(token);
                }
            }
        }
    }

//    @RequestMapping("/autoLogin")
//    public ResponseVO autoLogin(HttpServletResponse response) {
//        //从redis拿到token,并且续费时间
//        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfoDto();
//        if(tokenUserInfoDto == null){
//            return getSuccessResponseVO(null);
//        }
//        if(tokenUserInfoDto.getExpireAt() - System.currentTimeMillis() < constants.REDIS_KEY_EXPIRES_ONE_DAY ){
//            redisComponent.saveTokenInfo(tokenUserInfoDto);
//            saveToken2Cookie(response, tokenUserInfoDto.getToken());
//            return getSuccessResponseVO(tokenUserInfoDto);
//        }
//        saveToken2Cookie(response, tokenUserInfoDto.getToken());
//        return getSuccessResponseVO(tokenUserInfoDto);
//    }

    @RequestMapping("/logout")
    public ResponseVO logout(HttpServletResponse response) {
        cleanCookie(response);
        return getSuccessResponseVO(null);

    }

}