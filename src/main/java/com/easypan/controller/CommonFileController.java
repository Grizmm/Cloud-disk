package com.easypan.controller;

import com.easypan.component.RedisComponent;
import com.easypan.entity.config.AppConfig;
import com.easypan.entity.constants.Constants;
import com.easypan.entity.dto.DownloadFileDto;
import com.easypan.entity.enums.FIleFolderTypeEnums;
import com.easypan.entity.enums.FileCateGoryEnums;
import com.easypan.entity.enums.FileStatusEnums;
import com.easypan.entity.enums.ResponseCodeEnum;
import com.easypan.entity.po.FileInfo;
import com.easypan.entity.query.FileInfoQuery;
import com.easypan.entity.vo.ResponseVO;
import com.easypan.exception.BusinessException;
import com.easypan.service.FileInfoService;
import com.easypan.utils.CopyTools;
import com.easypan.utils.StringTools;

import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.File;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.ProviderNotFoundException;
import java.util.List;
import java.util.Locale;

public class CommonFileController extends ABaseController{
    @Resource
    private AppConfig appConfig;
    @Resource
    private FileInfoService fileInfoService;
    @Resource
    private RedisComponent redisComponent;

    public void getImage(HttpServletResponse response ,String imageFolder,String imageName){
        if(StringTools.isEmpty(imageFolder)||StringTools.isEmpty(imageName)||!StringTools.pathIsOk(imageFolder)||!StringTools.pathIsOk(imageName)){
            return;
        }
        String imageSuffix = StringTools.getFileSuffix(imageName);
        String filePath = appConfig.getProjectFolder()+ Constants.FILE_FOLDER_FILE+imageFolder+"/"+imageName;
        imageSuffix = imageSuffix.replace(".","");
        String contentType = "image/" +imageSuffix;
        response.setContentType(contentType);
        response.setHeader("Cache-Control","max-age=2592000");
        readFile(response,filePath);
    }
    protected void getFile(HttpServletResponse response,String fileId,String userId){
        String filePath = null;
        if(fileId.endsWith(".ts")){
            String[] tsArray = fileId.split("_");
            String readFileId = tsArray[0];
            FileInfo fileInfo = fileInfoService.getFileInfoByFileIdAndUserId(readFileId,userId);
            if(null==fileInfo){
                return;
            }
            String fileName = fileInfo.getFilePath();
            fileName = StringTools.getFileNameNoSuffix(fileName)+"/"+fileId;
            filePath = appConfig.getProjectFolder()+Constants.FILE_FOLDER_FILE+fileName;
        }else {
            FileInfo fileInfo = fileInfoService.getFileInfoByFileIdAndUserId(fileId,userId);
            if(null==fileInfo){
                return;
            }
            if(FileCateGoryEnums.VIDEO.getCategory().equals(fileInfo.getFileCategory())){
                String fileNameNoSuffix = StringTools.getFileNameNoSuffix(fileInfo.getFilePath());
                filePath = appConfig.getProjectFolder()+Constants.FILE_FOLDER_FILE+fileNameNoSuffix+"/"+Constants.M3U8_NAME;
            }
            File file = new File(filePath);
            if(!file.exists()){
                return;
            }
        }
        readFile(response,filePath);
    }
    protected ResponseVO getFolderInfo (String path,String userId){
        String[] pathArray = path.split("/");
        FileInfoQuery infoQuery = new FileInfoQuery();
        infoQuery.setUserId(userId);
        infoQuery.setFolderType(FIleFolderTypeEnums.FOLDER.getType());
        infoQuery.setFileIdArray(pathArray);
        String orderBy = "fileId(file_id,\"" +StringUtils.join(pathArray,"\",\"") + "\")";
        infoQuery.setOrderBy(orderBy);
        List<FileInfo> fileInfoList = fileInfoService.findListByParam(infoQuery);
        return getSuccessResponseVO(CopyTools.copyList(fileInfoList,FileInfo.class));
    }

    protected ResponseVO createDownloadUrl(String fileId,String userId){
        FileInfo fileInfo = fileInfoService.getFileInfoByFileIdAndUserId(fileId,userId);
        if(fileInfo == null){
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        if(FIleFolderTypeEnums.FOLDER.getType().equals(fileInfo.getFolderType())){
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        String code = StringTools.getRandomString(Constants.LENGTH_50);
        DownloadFileDto fileDto = new DownloadFileDto();
        fileDto.setDownloadCode(code);
        fileDto.setFilePath(fileInfo.getFilePath());
        fileDto.setFileName(fileInfo.getFileName());
        redisComponent.saveDownloadCode(code,fileDto);
        return getSuccessResponseVO(code);
    }

    protected void download(HttpServletRequest request,HttpServletResponse response,String code) throws Exception{
        DownloadFileDto downloadFileDto = redisComponent.getDownloadCode(code);
        if(null ==downloadFileDto){
            return;
        }
        String filePath = appConfig.getProjectFolder()+Constants.FILE_FOLDER_FILE+downloadFileDto.getFilePath();
        String fileName = downloadFileDto.getFileName();
        response.setContentType("application/x-msdownload; charset=UTF-8");
        if(request.getHeader("User-Agent").toLowerCase().indexOf("msie")>0){
            //ie浏览器
            fileName = URLEncoder.encode(fileName,"UTF-8");
        }else {
            fileName = new String(fileName.getBytes("UTF-8"),"ISO8859-1");
        }
        response.setHeader("Content-Disposition","attachment;filename=\""+fileName+"\"");
        readFile(response,filePath);
    }


}
