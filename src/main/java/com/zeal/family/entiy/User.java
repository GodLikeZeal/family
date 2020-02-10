package com.zeal.family.entiy;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zeal.family.enums.Gender;
import com.zeal.family.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

/**
 * 成员.
 *
 * @author  zhanglei
 * @date 2020/2/8  2:43 下午
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document
public class User {

  /**
   * id.
   */
  @Id
  private String id;

  /**
   * 姓名.
   */
  private String name;

  /**
   * 配偶名称.
   */
  private String spouse;

  /**
   * 密码.
   */
  private String password;

  /**
   * 性别.
   */
  private Gender gender;

  /**
   * 头像.
   */
  private String avatar;

  /**
   * 角色.
   */
  private Role role;

  /**
   * 介绍.
   */
  private String introduction;

  /**
   * 父节点.
   */
  private String parentId;

  /**
   * 父亲节点名称.
   */
  private String parentName;

  /**
   * 分支id.
   */
  private String groupId;

  /**
   * 创建时间.
   */
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
  private LocalDate createDate;
}
