package com.zeal.family.service;

import com.zeal.family.bo.Mypage;
import com.zeal.family.entiy.User;
import com.zeal.family.enums.Role;
import com.zeal.family.repository.UserRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * 用户服务.
 *
 * @author  zhanglei
 * @date 2020/2/9  3:37 下午
 */
@Service
public class UserService {

  @Autowired
  UserRepository userRepository;

  public List<User> findList() {
    return userRepository.findAll();
  }
  /**
   * 保存.
   * @param user
   * @return
   */
  public User save(@NonNull User user) {
    user.setPassword("123");
    user.setRole(Role.USER);
    user.setCreateDate(LocalDate.now());
    return userRepository.save(user);
  }

  /**
   * 更新.
   * @param user
   * @return
   */
  public User update(@NonNull User user) {
    return userRepository.save(user);
  }

  /**
   * 分页查询.
   * @param pageNumber 页码
   * @param pageSize 每页条数
   * @param groupId 分组id
   * @param name 名称
   * @return
   */
  public Mypage<User> findPage(int pageNumber, int pageSize, String groupId, String name) {
    Sort sort = Sort.by(Sort.Direction.DESC,"createDate");
    Pageable pageable = PageRequest.of(pageNumber - 1,pageSize, sort);
    Page<User> users;
    if (Objects.isNull(groupId)) {
      if (Objects.isNull(name)) {
        users = userRepository.findAll(pageable);
      }else {
        users = userRepository.findByNameIsLike(name.trim(),pageable);
      }
    }else {
      if (Objects.isNull(name)) {
        users = userRepository.findByGroupId(groupId,pageable);
      }else {
        users = userRepository.findByGroupIdAndNameIsLike(groupId,name.trim(),pageable);
      }
    }
    return Mypage.of(users);
  }

  /**
   * 根据id删除.
   * @param id
   */
  public void remove (String id) {
    List<User> users = userRepository.findAll(Example.of(User.builder().groupId(id).build()));
    if (!CollectionUtils.isEmpty(users)) {
      throw new RuntimeException("该节点下有子节点，无法删除");
    }
    userRepository.deleteById(id);
  }
}
