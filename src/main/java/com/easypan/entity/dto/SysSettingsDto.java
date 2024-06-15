package com.easypan.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SysSettingsDto implements Serializable {
    private  String registerEMailTitle="邮箱验证码";

    private String registerEmailConent="您好，邮箱验证码是 ：%s，15min有效";

    private Integer userInitUseSpace=5;

    public String getRegisterEMailTitle() {
        return registerEMailTitle;
    }

    public void setRegisterEMailTitle(String registerEMailTitle) {
        this.registerEMailTitle = registerEMailTitle;
    }


    public String getRegisterEmailConent() {
        return registerEmailConent;
    }

    public void setRegisterEmailConent(String registerEmailConent) {
        this.registerEmailConent = registerEmailConent;
    }

    public Integer getUserInitUseSpace() {
        return userInitUseSpace;
    }

    public void setUserInitUseSpace(Integer userInitUseSpace) {
        this.userInitUseSpace = userInitUseSpace;
    }


}
