package com.easylive.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.easylive.entity.enums.ResponseCodeEnum;
import com.easylive.entity.enums.VideoStatusEnum;
import com.easylive.entity.po.VideoInfoFilePost;
import com.easylive.entity.query.VideoInfoFilePostQuery;
import com.easylive.entity.query.VideoInfoFileQuery;
import com.easylive.exception.BusinessException;
import com.easylive.mappers.VideoInfoFilePostMapper;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Service;

import com.easylive.entity.enums.PageSize;
import com.easylive.entity.query.VideoInfoPostQuery;
import com.easylive.entity.po.VideoInfoPost;
import com.easylive.entity.vo.PaginationResultVO;
import com.easylive.entity.query.SimplePage;
import com.easylive.mappers.VideoInfoPostMapper;
import com.easylive.service.VideoInfoPostService;
import com.easylive.utils.StringTools;
import com.easylive.entity.constants.Constants;
import com.easylive.component.RedisComponent;
import org.springframework.transaction.annotation.Transactional;

/**
 * 视频信息 业务接口实现
 */
@Service("videoInfoPostService")
@Transactional(rollbackFor = Exception.class)
public class VideoInfoPostServiceImpl implements VideoInfoPostService {

	@Resource
	private RedisComponent redisComponent;

	@Resource
	private VideoInfoPostMapper<VideoInfoPost, VideoInfoPostQuery> videoInfoPostMapper;

	@Resource
	private VideoInfoFilePostMapper<VideoInfoFilePost, VideoInfoFilePostQuery> videoInfoFilePostMapper;

	/**
	 * 根据条件查询列表
	 */
	@Override
	public List<VideoInfoPost> findListByParam(VideoInfoPostQuery param) {
		return this.videoInfoPostMapper.selectList(param);
	}

	/**
	 * 根据条件查询列表
	 */
	@Override
	public Integer findCountByParam(VideoInfoPostQuery param) {
		return this.videoInfoPostMapper.selectCount(param);
	}

	/**
	 * 分页查询方法
	 */
	@Override
	public PaginationResultVO<VideoInfoPost> findListByPage(VideoInfoPostQuery param) {
		int count = this.findCountByParam(param);
		int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

		SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
		param.setSimplePage(page);
		List<VideoInfoPost> list = this.findListByParam(param);
		PaginationResultVO<VideoInfoPost> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
	 * 新增
	 */
	@Override
	public Integer add(VideoInfoPost bean) {
		return this.videoInfoPostMapper.insert(bean);
	}

	/**
	 * 批量新增
	 */
	@Override
	public Integer addBatch(List<VideoInfoPost> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.videoInfoPostMapper.insertBatch(listBean);
	}

	/**
	 * 批量新增或者修改
	 */
	@Override
	public Integer addOrUpdateBatch(List<VideoInfoPost> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.videoInfoPostMapper.insertOrUpdateBatch(listBean);
	}

	/**
	 * 多条件更新
	 */
	@Override
	public Integer updateByParam(VideoInfoPost bean, VideoInfoPostQuery param) {
		StringTools.checkParam(param);
		return this.videoInfoPostMapper.updateByParam(bean, param);
	}

	/**
	 * 多条件删除
	 */
	@Override
	public Integer deleteByParam(VideoInfoPostQuery param) {
		StringTools.checkParam(param);
		return this.videoInfoPostMapper.deleteByParam(param);
	}

	/**
	 * 根据VideoId获取对象
	 */
	@Override
	public VideoInfoPost getVideoInfoPostByVideoId(String videoId) {
		return this.videoInfoPostMapper.selectByVideoId(videoId);
	}

	/**
	 * 根据VideoId修改
	 */
	@Override
	public Integer updateVideoInfoPostByVideoId(VideoInfoPost bean, String videoId) {
		return this.videoInfoPostMapper.updateByVideoId(bean, videoId);
	}

	/**
	 * 根据VideoId删除
	 */
	@Override
	public Integer deleteVideoInfoPostByVideoId(String videoId) {
		return this.videoInfoPostMapper.deleteByVideoId(videoId);
	}

	@Override
	/**
	 * 保存视频信息，包括新增和编辑两种情况
	 *
	 * @param videoInfoPost 视频基本信息
	 * @param uploadFileList 上传的视频文件列表
	 */
	public void saveVideoInfo(VideoInfoPost videoInfoPost, List<VideoInfoFilePost> uploadFileList) {
		// 校验上传文件数量是否超过系统限制
		if (uploadFileList.size() > RedisComponent.getSysSettingDto().getVideoPCount()) {
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		}

		// 如果传入了 videoId，说明是编辑操作
		if (!StringTools.isEmpty(videoInfoPost.getVideoId())) {
			// 查询当前视频信息
			VideoInfoPost videoInfoPostDb = this.videoInfoPostMapper.selectByVideoId(videoInfoPost.getVideoId());

			// 视频不存在，抛异常
			if (videoInfoPostDb == null) {
				throw new BusinessException(ResponseCodeEnum.CODE_600);
			}

			// 当前视频状态不允许修改（如正在转码或审核中），抛异常
			if (ArrayUtils.contains(new Integer[]{
					VideoStatusEnum.STATUS0.getStatus(),
					VideoStatusEnum.STATUS2.getStatus()
			}, videoInfoPostDb.getStatus())) {
				throw new BusinessException(ResponseCodeEnum.CODE_600);
			}
		}

		// 当前时间戳用于记录创建时间和更新时间
		Date curDate = new Date();
		String videoId = videoInfoPost.getVideoId();

		// 初始化删除文件列表和新增文件列表
		List<VideoInfoFilePost> deleteFileList = new ArrayList<>();
		List<VideoInfoFilePost> addFileList = new ArrayList<>();

		// 如果是新增视频
		if (StringTools.isEmpty(videoId)) {
			// 生成随机 videoId
			videoId = StringTools.getRandomString(Constants.LENGTH_10);
			videoInfoPost.setVideoId(videoId);

			// 设置创建时间和更新时间
			videoInfoPost.setCreateTime(curDate);
			videoInfoPost.setLastUpdateTime(curDate);

			// 初始状态设为“转码中”
			videoInfoPost.setStatus(VideoStatusEnum.STATUS0.getStatus());

			// 插入视频信息到数据库
			this.videoInfoPostMapper.insert(videoInfoPost);

		} else { // 如果是编辑视频
			// 查询该视频关联的已有文件
			VideoInfoFilePostQuery fileQuery = new VideoInfoFilePostQuery();
			fileQuery.setVideoId(videoId);
			fileQuery.setUserId(videoInfoPost.getUserId());
			List<VideoInfoFilePost> dbInfoFileList = this.videoInfoFilePostMapper.selectList(fileQuery);

			// 构建上传文件的 Map：uploadId -> 文件对象，用于后续比对
			Map<String, VideoInfoFilePost> uploadFileMap = uploadFileList.stream()
					.filter(item -> item.getFileId() != null) // 只处理已有 fileId 的文件
					.collect(Collectors.toMap(
							item -> item.getUploadId(),
							Function.identity(),
							(data1, data2) -> data2)); // 冲突时保留新的

			Boolean updateFileName = false;

			// 遍历数据库中已有的文件，判断是否需要删除或更新
			for (VideoInfoFilePost fileInfo : dbInfoFileList) {
				VideoInfoFilePost updateFile = uploadFileMap.get(fileInfo.getUploadId());

				// 如果上传文件中没有这个 uploadId，说明要删除
				if (updateFile == null) {
					deleteFileList.add(fileInfo);
				}
				// 如果文件名不一致，说明需要更新文件名
				else if (!updateFile.getFileName().equals(fileInfo.getFileName())) {
					updateFileName = true;
				}
			}

			// 收集新增的文件（即 fileId 为 null 的文件）
			addFileList = uploadFileList.stream()
					.filter(item -> item.getFileId() == null)
					.collect(Collectors.toList());

			// 更新视频最后修改时间
			videoInfoPost.setLastUpdateTime(curDate);

			// 判断视频基本信息是否发生了变化
			Boolean changeVideoInfo = this.changeVideoInfo(videoInfoPost);

			// 根据是否有新增文件或信息变更，决定视频状态
			if (!addFileList.isEmpty()) {
				// 有新增文件，状态改为“转码中”
				videoInfoPost.setStatus(VideoStatusEnum.STATUS0.getStatus());
			} else if (changeVideoInfo || updateFileName) {
				// 信息发生变更，状态改为“待审核”
				videoInfoPost.setStatus(VideoStatusEnum.STATUS2.getStatus());
			}

			// 更新视频信息
			this.videoInfoPostMapper.updateByVideoId(videoInfoPost, videoInfoPost.getVideoId());
		}

		if(!deleteFileList.isEmpty()){
			//取出这个对象的id，map()的作用是转换
			List<String> delFileList = deleteFileList.stream().map(item -> item.getFileId()).collect(Collectors.toList());
			this.videoInfoFilePostMapper.deleteBatchByFileId(delFileList,  videoInfoPost.getUserId());
			List<String> delFilePathList = deleteFileList.stream().map(item -> item.getFilePath()).collect(Collectors.toList());

			// 删除文件，加入消息队列中慢慢删留在系统中的文件
			redisComponent.addFile2DelList(videoId,delFilePathList);
		}

		Integer index = 1;

	}

	private Boolean changeVideoInfo(VideoInfoPost videoInfoPost) {
		VideoInfoPost dbInfo = this.videoInfoPostMapper.selectByVideoId(videoInfoPost.getVideoId());
		// 标题，封面，标签，简介
		if (!videoInfoPost.getVideoName().equals(dbInfo.getVideoName())
				|| !videoInfoPost.getVideoCover().equals(dbInfo.getVideoCover())
				|| !videoInfoPost.getTags().equals(dbInfo.getTags())
				|| !videoInfoPost.getIntroduction().equals(dbInfo.getIntroduction())) {
			return true;
		} else {
			return false;
		}
	}


}