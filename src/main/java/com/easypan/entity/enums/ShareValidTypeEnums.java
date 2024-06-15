package com.easypan.entity.enums;

public enum ShareValidTypeEnums {
    DAY_1(0,1,"1天"),
    DAY_7(1,7,"7天"),
    DAY_30(2,30,"30天"),
    FOREVER(3,-1,"永久有效");

    private Integer type;
    private Integer days;
    private String desc;

    ShareValidTypeEnums(Integer type, Integer days, String desc) {
        this.type = type;
        this.days = days;
        this.desc = desc;
    }
    public static ShareValidTypeEnums getByType(Integer type){
        for(ShareValidTypeEnums item :ShareValidTypeEnums.values()){
            if(item.getType().equals(type)){
                return item;
            }
        }
        return null;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
