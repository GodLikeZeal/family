package com.zeal.family.enums;

/**
 * 角色.
 *
 * @author  zhanglei
 * @date 2019-05-07  15:34
 */
public enum Role {
    ADMIN("系统管理员"),
    USER("用户")
    ;

    private String description;

    Role(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
