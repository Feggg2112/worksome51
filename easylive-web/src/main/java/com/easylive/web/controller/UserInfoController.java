package com.easylive.web.controller;

import java.util.List;

import com.easylive.entity.query.UserInfoQuery;
import com.easylive.entity.po.UserInfo;
import com.easylive.entity.vo.ResponseVO;
import com.easylive.service.UserInfoService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 *  Controller
 */
@RestController("userInfoController")
@RequestMapping("/userInfo")
public class UserInfoController extends ABaseController{

	@Resource
	private UserInfoService userInfoService;
	/**
	 * 根据条件分页查询
	 */
	@RequestMapping("/loadDataList")
	public ResponseVO loadDataList(UserInfoQuery query){
		return getSuccessResponseVO(userInfoService.findListByPage(query));
	}

	/**
	 * 新增
	 */
	@RequestMapping("/add")
	public ResponseVO add(UserInfo bean) {
		userInfoService.add(bean);
		return getSuccessResponseVO(null);
	}

	/**
	 * 批量新增
	 */
	@RequestMapping("/addBatch")
	public ResponseVO addBatch(@RequestBody List<UserInfo> listBean) {
		userInfoService.addBatch(listBean);
		return getSuccessResponseVO(null);
	}

	/**
	 * 批量新增/修改
	 */
	@RequestMapping("/addOrUpdateBatch")
	public ResponseVO addOrUpdateBatch(@RequestBody List<UserInfo> listBean) {
		userInfoService.addBatch(listBean);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据UserInfo查询对象
	 */
	@RequestMapping("/getUserInfoByUserInfo")
	public ResponseVO getUserInfoByUserInfo(String userInfo) {
		return getSuccessResponseVO(userInfoService.getUserInfoByUserInfo(userInfo));
	}

	/**
	 * 根据UserInfo修改对象
	 */
	@RequestMapping("/updateUserInfoByUserInfo")
	public ResponseVO updateUserInfoByUserInfo(UserInfo bean,String userInfo) {
		userInfoService.updateUserInfoByUserInfo(bean,userInfo);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据UserInfo删除
	 */
	@RequestMapping("/deleteUserInfoByUserInfo")
	public ResponseVO deleteUserInfoByUserInfo(String userInfo) {
		userInfoService.deleteUserInfoByUserInfo(userInfo);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据Email查询对象
	 */
	@RequestMapping("/getUserInfoByEmail")
	public ResponseVO getUserInfoByEmail(String email) {
		return getSuccessResponseVO(userInfoService.getUserInfoByEmail(email));
	}

	/**
	 * 根据Email修改对象
	 */
	@RequestMapping("/updateUserInfoByEmail")
	public ResponseVO updateUserInfoByEmail(UserInfo bean,String email) {
		userInfoService.updateUserInfoByEmail(bean,email);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据Email删除
	 */
	@RequestMapping("/deleteUserInfoByEmail")
	public ResponseVO deleteUserInfoByEmail(String email) {
		userInfoService.deleteUserInfoByEmail(email);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据Nickname查询对象
	 */
	@RequestMapping("/getUserInfoByNickname")
	public ResponseVO getUserInfoByNickname(String nickname) {
		return getSuccessResponseVO(userInfoService.getUserInfoByNickname(nickname));
	}

	/**
	 * 根据Nickname修改对象
	 */
	@RequestMapping("/updateUserInfoByNickname")
	public ResponseVO updateUserInfoByNickname(UserInfo bean,String nickname) {
		userInfoService.updateUserInfoByNickname(bean,nickname);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据Nickname删除
	 */
	@RequestMapping("/deleteUserInfoByNickname")
	public ResponseVO deleteUserInfoByNickname(String nickname) {
		userInfoService.deleteUserInfoByNickname(nickname);
		return getSuccessResponseVO(null);
	}
}