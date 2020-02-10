package com.zeal.family.enums;

/**
 * 性别
 */
public enum Gender {
  MALE("男"),
  FEMALE("女"),
  UNKNOW("未知"),
  ;

  private String description;

  Gender(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
