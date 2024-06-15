package com.easypan.entity.enums;

public enum UploadStatusEnums {
    UPLOAD_SECONDS("upload_seconds","妙传"),
    UPLOADING("uploading","上传中"),
    UPLOAD_FINISH("upload_finish","上传完成");

    private String code;
    private String desc;

    UploadStatusEnums(String code, String desc) {
        this.code = code;
        this.desc = desc;
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
