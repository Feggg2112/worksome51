package com.easylive.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.easylive.component.RedisComponent;
import com.easylive.entity.constants.constants;
import com.easylive.entity.dto.TokenUserInfoDto;
import com.easylive.entity.query.UserInfoQuery;
import com.easylive.entity.po.UserInfo;
import com.easylive.entity.vo.ResponseVO;
import com.easylive.exception.BusinessException;
import com.easylive.redis.RedisUtils;
import com.easylive.service.UserInfoService;
import com.wf.captcha.ArithmeticCaptcha;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

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

    @RequestMapping("/register")
    public ResponseVO register(@NotEmpty @Email @Size(max = 150) String email,
                               @NotEmpty @Size(max = 20) String nickName,
                               @NotEmpty @Pattern(regexp = constants.REGEX_PASSWORD) String registerPassword,
                               @NotEmpty String checkCodeKey,
                               @NotEmpty String checkCode
    ) {

        try {
            if (!checkCode.equalsIgnoreCase(redisComponent.getCheckCode(checkCodeKey))) {
                throw new BusinessException("图片验证码不正确");
            }
            userInfoService.register(email, nickName, registerPassword);
            return getSuccessResponseVO(null);
        } finally {
            redisComponent.cleanCheckCode(checkCodeKey);
        }

    }

    @RequestMapping("/login")
    public ResponseVO login(HttpServletResponse response,
                            @NotEmpty @Email String email,
                            @NotEmpty String password,
                            @NotEmpty String checkCodeKey,
                            @NotEmpty String checkCode
    ) {

        try {
            if (!checkCode.equalsIgnoreCase(redisComponent.getCheckCode(checkCodeKey))) {
                throw new BusinessException("图片验证码不正确");
            }

            String ip = getIpAddr();
            TokenUserInfoDto tokenUserInfoDto = userInfoService.login( email, password, ip );


            saveToken2Cookie(response, tokenUserInfoDto.getToken());
            //TODO 设置粉丝数 关注数 硬币数
            //做到04——33：33，现在正在测试接口中，需要做一下测试，test@123456，test123456

			return getSuccessResponseVO(tokenUserInfoDto);
        } finally {
            redisComponent.cleanCheckCode(checkCodeKey);
        }

    }


}