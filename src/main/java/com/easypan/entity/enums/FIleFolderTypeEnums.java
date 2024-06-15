package com.easypan.entity.enums;

public enum FIleFolderTypeEnums {
    FILE(0,"文件"),
    FOLDER(1,"目录");

    private Integer type;
    private String desc;


    FIleFolderTypeEnums(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public Integer getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }
}
