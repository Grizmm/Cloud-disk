package com.easypan.entity.enums;

public enum FileCateGoryEnums {
    VIDEO(1,"video","视频"),
    MUSIC(2,"music","音频"),
    IMAGE(3,"image","图片"),
    DOC(4,"doc","文档"),
    OTHERS(5,"others","其他");

    private Integer category;
    private String code;
    private String desc;

     FileCateGoryEnums(Integer category, String code, String desc) {
        this.category = category;
        this.code = code;
        this.desc = desc;
    }
    public static FileCateGoryEnums getBycode(String code){
        for(FileCateGoryEnums item : FileCateGoryEnums.values()){
            if(item.getCode().equals(code)){
                return item;
            }
        }
        return  null;
    }
    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
