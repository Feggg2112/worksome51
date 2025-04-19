package com.easylive.web.controller;

import java.util.List;

import com.easylive.entity.query.UserInfoQuery;
import com.easylive.entity.po.UserInfo;
import com.easylive.entity.vo.ResponseVO;
import com.easylive.redis.RedisUtils;
import com.easylive.service.UserInfoService;
import com.wf.captcha.ArithmeticCaptcha;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 *  Controller
 */
@RestController
@RequestMapping("/account")
public class AccountController extends ABaseController{

	@Resource
	private UserInfoService userInfoService;

	@Resource
	private RedisUtils redisUtils;

	@RequestMapping("/checkCode")
	public ResponseVO checkCode(){
		ArithmeticCaptcha captcha = new ArithmeticCaptcha(100, 42);
		String code = captcha.text();
		redisUtils.setex("checkCode",code,1000 * 60 * 5);
		String checkCodeBase64 = captcha.toBase64();
		return getSuccessResponseVO(checkCodeBase64);
	}

	@RequestMapping("/register")
	public ResponseVO register(String checkCode) {
		String myCheckCode = (String) redisUtils.get("checkCode");

		return getSuccessResponseVO(myCheckCode.equalsIgnoreCase(checkCode));
	}

}