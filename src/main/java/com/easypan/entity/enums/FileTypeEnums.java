package com.easypan.entity.enums;

import org.apache.commons.lang3.ArrayUtils;

public enum FileTypeEnums {
    VIDEO(FileCateGoryEnums.VIDEO,1,new String[]{".mp4",".avi",".rmvb",".mkv",".mov"},"视频"),
    MUSIC(FileCateGoryEnums.MUSIC,2,new String[]{".mp3",".wav",".wma",".mp2",".flac",".midi",".ra",".ape",".aac",".cda"},"音频"),
    IMAGE(FileCateGoryEnums.IMAGE,3,new String[]{".jpeg",".jpg",".png",".gif",".bmp",".dds",".psd",".pdt",".webp",".xmp",".svg",".tiff"},"图片"),
    PDF(FileCateGoryEnums.DOC,4,new String[]{".pdf"},"pdf"),
    WORD(FileCateGoryEnums.DOC,5,new String[]{".docx"},"word"),
    EXCEL(FileCateGoryEnums.DOC,6,new String[]{".xlsx"},"excel"),
    TXT(FileCateGoryEnums.DOC,7,new String[]{".txt"},"txt文本"),
    PROGRAME(FileCateGoryEnums.OTHERS,8,new String[]{".h",".c",".hpp",".cpp",".cc",".c++",".cxx",".m",".o",".s",".dll",".cs"
            ,".java",".class",".js",".ts",".css",".scss",".vue",".jsx",".sql",".md",".json",".html",".xml"},"CODE"),
    ZIP(FileCateGoryEnums.OTHERS,9,new String[]{"rar",".zip",".7z",".cab",".arj",".lzh",".tar",".gz",".ace",".uue",".bz",".jar",".iso",".,pq"},"压缩包"),
    OTHERS(FileCateGoryEnums.OTHERS,10,new String[]{},"其他");

    private FileCateGoryEnums category;
    private Integer type;
    private String[] suffixs;
    private String desc;

    FileTypeEnums(FileCateGoryEnums category, Integer type, String[] suffixs, String desc) {
        this.category = category;
        this.type = type;
        this.suffixs = suffixs;
        this.desc = desc;
    }
    public static FileTypeEnums getFileTypeBySuffix(String suffix){
        for(FileTypeEnums item : FileTypeEnums.values()){
            if(ArrayUtils.contains(item.getSuffixs(),suffix)){
                return item;
            }
        }
        return  null;
    }
    public FileCateGoryEnums getCategory() {
        return category;
    }

    public void setCategory(FileCateGoryEnums category) {
        this.category = category;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String[] getSuffixs() {
        return suffixs;
    }

    public void setSuffixs(String[] suffixs) {
        this.suffixs = suffixs;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
