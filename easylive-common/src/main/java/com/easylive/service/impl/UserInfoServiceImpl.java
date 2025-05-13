package com.easylive.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.easylive.component.RedisComponent;
import com.easylive.entity.dto.TokenUserInfoDto;
import com.easylive.entity.enums.UserSexEnum;
import com.easylive.entity.enums.UserStatusEnum;
import com.easylive.exception.BusinessException;
import com.easylive.utils.CopyTools;
import org.springframework.stereotype.Service;

import com.easylive.entity.enums.PageSize;
import com.easylive.entity.query.UserInfoQuery;
import com.easylive.entity.po.UserInfo;
import com.easylive.entity.vo.PaginationResultVO;
import com.easylive.entity.query.SimplePage;
import com.easylive.mappers.UserInfoMapper;
import com.easylive.service.UserInfoService;
import com.easylive.utils.StringTools;
import com.easylive.entity.constants.Constants;


/**
 *  业务接口实现
 */
@Service("userInfoService")
public class UserInfoServiceImpl implements UserInfoService {

	@Resource
	private UserInfoMapper<UserInfo, UserInfoQuery> userInfoMapper;

	@Resource
	private RedisComponent redisComponent;
	/**
	 * 根据条件查询列表
	 */
	@Override
	public List<UserInfo> findListByParam(UserInfoQuery param) {
		return this.userInfoMapper.selectList(param);
	}

	/**
	 * 根据条件查询列表
	 */
	@Override
	public Integer findCountByParam(UserInfoQuery param) {
		return this.userInfoMapper.selectCount(param);
	}

	/**
	 * 分页查询方法
	 */
	@Override
	public PaginationResultVO<UserInfo> findListByPage(UserInfoQuery param) {
		int count = this.findCountByParam(param);
		int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

		SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
		param.setSimplePage(page);
		List<UserInfo> list = this.findListByParam(param);
		PaginationResultVO<UserInfo> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
	 * 新增
	 */
	@Override
	public Integer add(UserInfo bean) {
		return this.userInfoMapper.insert(bean);
	}

	/**
	 * 批量新增
	 */
	@Override
	public Integer addBatch(List<UserInfo> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.userInfoMapper.insertBatch(listBean);
	}

	/**
	 * 批量新增或者修改
	 */
	@Override
	public Integer addOrUpdateBatch(List<UserInfo> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.userInfoMapper.insertOrUpdateBatch(listBean);
	}

	/**
	 * 多条件更新
	 */
	@Override
	public Integer updateByParam(UserInfo bean, UserInfoQuery param) {
		StringTools.checkParam(param);
		return this.userInfoMapper.updateByParam(bean, param);
	}

	/**
	 * 多条件删除
	 */
	@Override
	public Integer deleteByParam(UserInfoQuery param) {
		StringTools.checkParam(param);
		return this.userInfoMapper.deleteByParam(param);
	}

	/**
	 * 根据UserInfo获取对象
	 */
	@Override
	public UserInfo getUserInfoByUserInfo(String userInfo) {
		return this.userInfoMapper.selectByUserInfo(userInfo);
	}

	/**
	 * 根据UserInfo修改
	 */
	@Override
	public Integer updateUserInfoByUserInfo(UserInfo bean, String userInfo) {
		return this.userInfoMapper.updateByUserInfo(bean, userInfo);
	}

	/**
	 * 根据UserInfo删除
	 */
	@Override
	public Integer deleteUserInfoByUserInfo(String userInfo) {
		return this.userInfoMapper.deleteByUserInfo(userInfo);
	}

	/**
	 * 根据Email获取对象
	 */
	@Override
	public UserInfo getUserInfoByEmail(String email) {
		return this.userInfoMapper.selectByEmail(email);
	}

	/**
	 * 根据Email修改
	 */
	@Override
	public Integer updateUserInfoByEmail(UserInfo bean, String email) {
		return this.userInfoMapper.updateByEmail(bean, email);
	}

	/**
	 * 根据Email删除
	 */
	@Override
	public Integer deleteUserInfoByEmail(String email) {
		return this.userInfoMapper.deleteByEmail(email);
	}

	/**
	 * 根据Nickname获取对象
	 */
	@Override
	public UserInfo getUserInfoByNickname(String nickname) {
		return this.userInfoMapper.selectByNickname(nickname);
	}

	/**
	 * 根据Nickname修改
	 */
	@Override
	public Integer updateUserInfoByNickname(UserInfo bean, String nickname) {
		return this.userInfoMapper.updateByNickname(bean, nickname);
	}

	/**
	 * 根据Nickname删除
	 */
	@Override
	public Integer deleteUserInfoByNickname(String nickname) {
		return this.userInfoMapper.deleteByNickname(nickname);
	}

	@Override
	public void register(String email, String nickName, String registerPassword) {
//		UserInfo userInfo = this.userInfoMapper.selectByEmail(email);
//		if (userInfo != null) {
//			UserInfo nickNameUser = this.userInfoMapper.selectByNickname(nickName);
//			if (nickNameUser != null) {
//
//				/**数据库里的id是UserInfo，这里写成UserId，特此声明怕后面找不到bug
//				 * Feggg 2025.4.20 17：35
//				 */
//
//				userInfo = new UserInfo();
//				String UserId = StringTools.getRandomNumber(constants.LENTH_10);
//				userInfo.setUserInfo(UserId);
//				userInfo.setNickname(nickName);
//				userInfo.setPassword(StringTools.encodeByMD5(registerPassword));//420 17.32
//				userInfo.setJoinTime(new Date());
//				userInfo.setStatus(UserStatusEnum.ENABLE.getStatus());
//				userInfo.setSex(UserSexEnum.SECRECY.getType());
//				userInfo.setTheme(constants.ONE);
//
//				this.userInfoMapper.insert(userInfo);
//
//
//			} else {
//
//			}
//		} else {
//
//		}
		UserInfo userInfo = this.userInfoMapper.selectByEmail(email);
		if (null != userInfo) {
			throw new BusinessException("邮箱账号已经存在");
		}
		UserInfo nickNameUser = this.userInfoMapper.selectByNickname(nickName);
		if (null != nickNameUser) {
			throw new BusinessException("昵称已经存在");
		}
		userInfo = new UserInfo();
		String userId = StringTools.getRandomNumber(Constants.LENTH_10);
		userInfo.setUserInfo(userId);
		userInfo.setNickname(nickName);
		userInfo.setEmail(email);
		userInfo.setPassword(StringTools.encodeByMD5(registerPassword));
		userInfo.setJoinTime(new Date());
		userInfo.setStatus(UserStatusEnum.ENABLE.getStatus());
		userInfo.setSex(UserSexEnum.SECRECY.getType());
		userInfo.setTheme(Constants.ONE);
		userInfo.setCurrentCoinCount(Constants.ONE);
		userInfo.setTotalCoinCount(Constants.ONE);
		userInfo.setNoticeInfo("欢迎使用easylive");

// TODO 初始化 用户的硬币
		this.userInfoMapper.insert(userInfo);


	}



	@Override
	public TokenUserInfoDto login( String email, String password, String ip) {
		UserInfo userInfo = this.userInfoMapper.selectByEmail(email);
		if (null == userInfo || userInfo.getPassword().equals(password)) {
			throw new BusinessException("账号或密码错误");
		}
		if (UserStatusEnum.DISABLE.getStatus().equals(userInfo.getStatus())){
			throw new BusinessException("账号已被禁用");
		}
		UserInfo updateUserInfo = new UserInfo();
		updateUserInfo.setLastLoginTime(new Date());
		updateUserInfo.setLastLoginIp(ip);
		this.userInfoMapper.updateByUserInfo(updateUserInfo, userInfo.getUserInfo());

		TokenUserInfoDto tokenUserInfoDto = CopyTools.copy(userInfo , TokenUserInfoDto.class);

		redisComponent.saveTokenInfo(tokenUserInfoDto);

		return tokenUserInfoDto;
	}
}