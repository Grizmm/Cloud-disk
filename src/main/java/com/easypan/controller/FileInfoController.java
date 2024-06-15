package com.easypan.controller;

import java.awt.event.WindowFocusListener;
import java.io.File;
import java.util.List;

import com.easypan.annotation.GlobalInterceptor;
import com.easypan.annotation.VerifyParam;
import com.easypan.entity.dto.SessionWebUserDto;
import com.easypan.entity.dto.UploadResultDto;
import com.easypan.entity.enums.FIleFolderTypeEnums;
import com.easypan.entity.enums.FileCateGoryEnums;
import com.easypan.entity.enums.FileDelFlagEnums;
import com.easypan.entity.query.FileInfoQuery;
import com.easypan.entity.po.FileInfo;
import com.easypan.entity.vo.FileInfoVO;
import com.easypan.entity.vo.PaginationResultVO;
import com.easypan.entity.vo.ResponseVO;
import com.easypan.service.FileInfoService;
import com.easypan.utils.CopyTools;
import com.easypan.utils.StringTools;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.mail.Session;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *  Controller
 */
@RestController("fileInfoController")
@RequestMapping("/file")
public class FileInfoController extends CommonFileController{

	@Resource
	private FileInfoService fileInfoService;
	/**
	 * 根据条件分页查询
	 */
	@RequestMapping ("/loadDataList")
	@GlobalInterceptor
	public ResponseVO loadDataList(HttpSession session ,FileInfoQuery query,String category){
		FileCateGoryEnums cateGoryEnum = FileCateGoryEnums.getBycode(category);
		if(null != cateGoryEnum){
			query.setFileCategory(cateGoryEnum.getCategory());
		}
		query.setUserId(getUserInfoFromSession(session).getUserId());
		query.setOrderBy("last_update_time desc");
		query.setDelFlag(FileDelFlagEnums.USING.getFlag());
		PaginationResultVO result = fileInfoService.findListByPage(query);
		return getSuccessResponseVO(convert2PaginationVO(result, FileInfoVO.class));
	}

	@RequestMapping ("uploadFile")
	@GlobalInterceptor(checkParams = true)
	public ResponseVO uploadFile(HttpSession session ,
								 String fileId,
								 MultipartFile file,
								 @VerifyParam(required = true)String fileName,
								 @VerifyParam(required = true)String filePid,
								 @VerifyParam(required = true)String fileMd5,
								 @VerifyParam(required = true)Integer chunkIndex,
								 @VerifyParam(required = true)Integer chunks
								 ){
		SessionWebUserDto webUserDto = getUserInfoFromSession(session);
		UploadResultDto resultDto = fileInfoService.uploadFile(webUserDto,fileId,file,fileName,filePid,fileMd5,chunkIndex,chunks);
		return getSuccessResponseVO(resultDto);
	}
	@RequestMapping ("getImage/{imageFolder}/{imageName}")
	@GlobalInterceptor(checkParams = true)
	public void getImage(HttpServletResponse response,@PathVariable("imageFolder") String imageFolder,@PathVariable("imageName")String imageName){
		super.getImage(response,imageFolder,imageName);
	}

	@RequestMapping ("ts/getVideoInfo/{fileId}")
	@GlobalInterceptor(checkParams = true)
	public void getVideoInfo(HttpServletResponse response,HttpSession session,@PathVariable("fileId")String fileId){
		SessionWebUserDto webUserDto = getUserInfoFromSession(session);
		super.getFile(response,fileId,webUserDto.getUserId());
	}
	@RequestMapping ("/newFoloder")
	@GlobalInterceptor(checkParams = true)
	public ResponseVO newFolder(HttpSession session,@VerifyParam(required = true) String filePid,@VerifyParam(required = true) String fileName){
		SessionWebUserDto webUserDto = getUserInfoFromSession(session);
		FileInfo fileInfo = fileInfoService.newFolder(filePid,webUserDto.getUserId(),fileName);
		return getSuccessResponseVO(fileInfo);
	}

	@RequestMapping ("/getFolderInfo")
	@GlobalInterceptor(checkParams = true)
	public ResponseVO getFolderInfo(HttpSession session,@VerifyParam(required = true) String path){
		SessionWebUserDto webUserDto = getUserInfoFromSession(session);
		return super.getFolderInfo(path,webUserDto.getUserId());
	}
	@RequestMapping ("/rename")
	@GlobalInterceptor(checkParams = true)
	public ResponseVO rename(HttpSession session,@VerifyParam(required = true) String fileId,@VerifyParam(required = true) String fileName){
		SessionWebUserDto webUserDto = getUserInfoFromSession(session);
		FileInfo fileInfo = fileInfoService.rename(fileId, webUserDto.getUserId(), fileName);
		return getSuccessResponseVO(fileInfo);
	}

	@RequestMapping ("/loadAllFolder")
	@GlobalInterceptor(checkParams = true)
	public ResponseVO loadAllFolder(HttpSession session,@VerifyParam(required = true) String filePid, String currentFileIds){
		SessionWebUserDto webUserDto = getUserInfoFromSession(session);
		FileInfoQuery infoQuery = new FileInfoQuery();
		infoQuery.setUserId(webUserDto.getUserId());
		infoQuery.setFilePid(filePid);
		if(!StringTools.isEmpty(currentFileIds)){
			infoQuery.setExcludeFileIdArray(currentFileIds.split(","));
		}
		infoQuery.setDelFlag(FileDelFlagEnums.USING.getFlag());
		infoQuery.setOrderBy("create_time desc");
		infoQuery.setFolderType(FIleFolderTypeEnums.FOLDER.getType());
		List<FileInfo> fileInfoList = fileInfoService.findListByParam(infoQuery);
		return getSuccessResponseVO(CopyTools.copyList(fileInfoList,FileInfoVO.class));
	}

	@RequestMapping ("/changeFileFolder")
	@GlobalInterceptor(checkParams = true)
	public ResponseVO changeFileFolder(HttpSession session,@VerifyParam(required = true) String fileIds,@VerifyParam(required = true) String filePid){
		SessionWebUserDto webUserDto = getUserInfoFromSession(session);
		fileInfoService.changeFileFolder(fileIds,filePid, webUserDto.getUserId());
		return getSuccessResponseVO(null);
	}

	@RequestMapping ("/createDownloadUrl/{fileId}")
	@GlobalInterceptor(checkParams = true)
	public ResponseVO createDownloadUrl(HttpSession session,@VerifyParam(required = true)@PathVariable("fileId") String fileId){
		SessionWebUserDto webUserDto = getUserInfoFromSession(session);
		return super.createDownloadUrl(fileId,webUserDto.getUserId());
	}


	@RequestMapping ("/download/{code}")
	@GlobalInterceptor(checkParams = true,checkLogin = false)
	public void changeFileFolder(HttpServletRequest request,HttpServletResponse response,  @VerifyParam(required = true)@PathVariable("code") String code) throws Exception {
		super.download(request,response,code);
	}

	@RequestMapping ("/delFile")
	@GlobalInterceptor(checkParams = true)
	public ResponseVO delFile(HttpSession session,@VerifyParam(required = true) String fileIds)  {
		SessionWebUserDto webUserDto =getUserInfoFromSession(session);
		fileInfoService.removeFile2RecycleBatch(webUserDto.getUserId(),fileIds);
		return getSuccessResponseVO(null);
	}


}