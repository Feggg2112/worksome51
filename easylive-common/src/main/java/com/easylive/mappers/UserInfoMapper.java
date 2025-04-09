package com.easylive.mappers;

import org.apache.ibatis.annotations.Param;

/**
 *  数据库操作接口
 */
public interface UserInfoMapper<T,P> extends BaseMapper<T,P> {

	/**
	 * 根据UserInfo更新
	 */
	 Integer updateByUserInfo(@Param("bean") T t,@Param("userInfo") String userInfo);


	/**
	 * 根据UserInfo删除
	 */
	 Integer deleteByUserInfo(@Param("userInfo") String userInfo);


	/**
	 * 根据UserInfo获取对象
	 */
	 T selectByUserInfo(@Param("userInfo") String userInfo);


	/**
	 * 根据Email更新
	 */
	 Integer updateByEmail(@Param("bean") T t,@Param("email") String email);


	/**
	 * 根据Email删除
	 */
	 Integer deleteByEmail(@Param("email") String email);


	/**
	 * 根据Email获取对象
	 */
	 T selectByEmail(@Param("email") String email);


	/**
	 * 根据Nickname更新
	 */
	 Integer updateByNickname(@Param("bean") T t,@Param("nickname") String nickname);


	/**
	 * 根据Nickname删除
	 */
	 Integer deleteByNickname(@Param("nickname") String nickname);


	/**
	 * 根据Nickname获取对象
	 */
	 T selectByNickname(@Param("nickname") String nickname);


}
