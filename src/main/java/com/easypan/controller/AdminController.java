package com.easypan.controller;

import com.easypan.annotation.GlobalInterceptor;
import com.easypan.annotation.VerifyParam;
import com.easypan.component.RedisComponent;
import com.easypan.entity.dto.SessionWebUserDto;
import com.easypan.entity.dto.SysSettingsDto;
import com.easypan.entity.po.FileInfo;
import com.easypan.entity.po.UserInfo;
import com.easypan.entity.query.FileInfoQuery;
import com.easypan.entity.query.UserInfoQuery;
import com.easypan.entity.vo.FileInfoVO;
import com.easypan.entity.vo.PaginationResultVO;
import com.easypan.entity.vo.ResponseVO;
import com.easypan.service.FileInfoService;
import com.easypan.service.UserInfoService;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController("adminController")
@RequestMapping("/admin")
public class AdminController extends  CommonFileController {
    @Resource
    private FileInfoService fileInfoService;
    @Resource
    private UserInfoService userInfoService;

    @Resource
    private RedisComponent redisComponent;

    @RequestMapping("/getSysSettings")
    @GlobalInterceptor(checkParams = true,checkAdmin = true)
    public ResponseVO getSysSettings(){
        return getSuccessResponseVO(null);
    }

    @RequestMapping("/saveSysSettings")
    @GlobalInterceptor(checkParams = true,checkAdmin = true)
    public ResponseVO saveSysSettings(@VerifyParam(required = true) String registerEmailTitle,
                                      @VerifyParam(required = true) String registerEmailContent,
                                      @VerifyParam(required = true) Integer userInitUseSpace){
        SysSettingsDto sysSettingsDto = new SysSettingsDto();
        sysSettingsDto.setRegisterEMailTitle(registerEmailTitle);
        sysSettingsDto.setRegisterEmailConent(registerEmailContent);
        sysSettingsDto.setUserInitUseSpace(userInitUseSpace);
        redisComponent.saveSysSettingDto(sysSettingsDto);
        return getSuccessResponseVO(null);
    }
    @RequestMapping("/loadUserList")
    @GlobalInterceptor(checkParams = true,checkAdmin = true)
    public ResponseVO loadUserList(UserInfoQuery userInfoQuery){
        userInfoQuery.setOrderBy("join_time desc");
        PaginationResultVO resultVO = userInfoService.findListByPage(userInfoQuery);
        return getSuccessResponseVO(convert2PaginationVO(resultVO,UserInfo.class));
    }

    @RequestMapping("/updateUserStatus")
    @GlobalInterceptor(checkParams = true,checkAdmin = true)
    public ResponseVO updateUserStatus(@VerifyParam(required = true) String userId,
                                       @VerifyParam(required = true) Integer status){
        userInfoService.updateUserStatus(userId,status);
        return getSuccessResponseVO(null);
    }
    @RequestMapping("/loadFileList")
    @GlobalInterceptor(checkParams = true,checkAdmin = true)
    public ResponseVO loadFileList(FileInfoQuery query){
        query.setOrderBy("last_update_time desc");
        PaginationResultVO resultVO = fileInfoService.findListByPage(query);
        return getSuccessResponseVO(convert2PaginationVO(resultVO, FileInfoVO.class));
    }

    @RequestMapping("/getFolderInfo")
    @GlobalInterceptor(checkParams = true,checkAdmin = true)
    public ResponseVO getFolderInfo(@VerifyParam(required = true) String path){
        return super.getFolderInfo(path,null);
    }

    @RequestMapping("/getFile/{userId}/{fileId}")
    @GlobalInterceptor(checkParams = true,checkAdmin = true)
    public void getFile(HttpServletResponse response,
                              @PathVariable("userId") String userId,
                              @PathVariable("fileId") String fileId){
       super.getFile(response,fileId,userId );
    }

    @RequestMapping ("/createDownloadUrl/{userId}/{fileId}")
    @GlobalInterceptor(checkParams = true,checkAdmin = true)
    public ResponseVO createDownloadUrl(@PathVariable("userId") String userId,@PathVariable("fileId") String fileId){

        return super.createDownloadUrl(fileId,userId);
    }

    @RequestMapping ("/download/{code}")
    @GlobalInterceptor(checkParams = true,checkLogin = false)
    public void download(HttpServletRequest request, HttpServletResponse response, @VerifyParam(required = true)@PathVariable("code") String code) throws Exception {
        super.download(request,response,code);
    }

    @RequestMapping ("/delFile")
    @GlobalInterceptor(checkParams = true,checkLogin = false)
    public ResponseVO delFile(HttpSession session,@VerifyParam(required = true) String fileIdAndUserIds)  {
        String[] fileIdAndUserIdArray = fileIdAndUserIds.split(",");
        for (String fileIdAndUserId :fileIdAndUserIdArray){
            String[] itemArray = fileIdAndUserId.split("_");
            fileInfoService.delFileBatch(itemArray[0],itemArray[1],true);
        }
        return getSuccessResponseVO(null);
    }


}
