package com.zeal.family.bo;

import com.zeal.family.entiy.User;
import lombok.*;

import java.util.List;

/**
 * 节点封装.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class UserBo extends User {
  /**
   * 自节点.
   */
  private List<UserBo> children;
}
