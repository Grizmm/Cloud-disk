package com.easypan.service;

import java.util.List;

import com.easypan.entity.dto.SessionWebUserDto;
import com.easypan.entity.query.UserInfoQuery;
import com.easypan.entity.po.UserInfo;
import com.easypan.entity.vo.PaginationResultVO;


/**
 * 用户信息 业务接口
 */
public interface UserInfoService {

	/**
	 * 根据条件查询列表
	 */
	List<UserInfo> findListByParam(UserInfoQuery param);

	/**
	 * 根据条件查询列表
	 */
	Integer findCountByParam(UserInfoQuery param);

	/**
	 * 分页查询
	 */
	PaginationResultVO<UserInfo> findListByPage(UserInfoQuery param);

	/**
	 * 新增
	 */
	Integer add(UserInfo bean);

	/**
	 * 批量新增
	 */
	Integer addBatch(List<UserInfo> listBean);

	/**
	 * 批量新增/修改
	 */
	Integer addOrUpdateBatch(List<UserInfo> listBean);

	/**
	 * 多条件更新
	 */
	Integer updateByParam(UserInfo bean,UserInfoQuery param);

	/**
	 * 多条件删除
	 */
	Integer deleteByParam(UserInfoQuery param);

	/**
	 * 根据UserId查询对象
	 */
	UserInfo getUserInfoByUserId(String userId);


	/**
	 * 根据UserId修改
	 */
	Integer updateUserInfoByUserId(UserInfo bean,String userId);


	/**
	 * 根据UserId删除
	 */
	Integer deleteUserInfoByUserId(String userId);


	/**
	 * 根据UserIdAndNickName查询对象
	 */
	UserInfo getUserInfoByUserIdAndNickName(String userId,String nickName);


	/**
	 * 根据UserIdAndNickName修改
	 */
	Integer updateUserInfoByUserIdAndNickName(UserInfo bean,String userId,String nickName);


	/**
	 * 根据UserIdAndNickName删除
	 */
	Integer deleteUserInfoByUserIdAndNickName(String userId,String nickName);


	/**
	 * 根据Email查询对象
	 */
	UserInfo getUserInfoByEmail(String email);


	/**
	 * 根据Email修改
	 */
	Integer updateUserInfoByEmail(UserInfo bean,String email);


	/**
	 * 根据Email删除
	 */
	Integer deleteUserInfoByEmail(String email);


	/**
	 * 根据QqOpenId查询对象
	 */
	UserInfo getUserInfoByQqOpenId(String qqOpenId);


	/**
	 * 根据QqOpenId修改
	 */
	Integer updateUserInfoByQqOpenId(UserInfo bean,String qqOpenId);


	/**
	 * 根据QqOpenId删除
	 */
	Integer deleteUserInfoByQqOpenId(String qqOpenId);

	/**
	 * 注册
	 */
	Void register(String email,String nickName, String password, String emailCode);
	/**
	 * 登录
	 */
	SessionWebUserDto login(String email,String password);
	/**
	 * 重置密码
	 */
	Void resetPwd(String email,String password,String emailCode);

	SessionWebUserDto qqLogin(String code);

	void updateUserStatus(String userId,Integer status);
	void changeUserSpace(String userId,Integer changeSpace);
}