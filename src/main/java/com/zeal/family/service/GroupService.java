package com.zeal.family.service;

import com.zeal.family.bo.Mypage;
import com.zeal.family.entiy.Group;
import com.zeal.family.entiy.User;
import com.zeal.family.enums.Role;
import com.zeal.family.repository.GroupRepository;
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
public class GroupService {

  @Autowired
  GroupRepository groupRepository;
  @Autowired
  UserRepository userRepository;

  /**
   * 查询分组
   * @return
   */
  public List<Group> findList() {
    return groupRepository.findAll();
  }
  /**
   * 保存
   * @param group
   * @return
   */
  public Group save(@NonNull Group group) {
    group.setCreateDate(LocalDate.now());
    return groupRepository.save(group);
  }

  public Group update(@NonNull Group group) {
    return groupRepository.save(group);
  }

  /**
   * 分页查询
   * @param pageNumber 页码
   * @param pageSize 每页条数
   * @return
   */
  public Mypage<Group> findPage(int pageNumber, int pageSize) {
    Sort sort = Sort.by(Sort.Direction.DESC,"createDate");
    Pageable pageable = PageRequest.of(pageNumber - 1,pageSize, sort);
    Page<Group> groups = groupRepository.findAll(pageable);
    return Mypage.of(groups);
  }

  /**
   * 根据id删除.
   * @param id
   */
  public void remove (String id) {
    List<User> users = userRepository.findAll(Example.of(User.builder().groupId(id).build()));
    if (!CollectionUtils.isEmpty(users)) {
      throw new RuntimeException("该分组下有节点，无法删除");
    }
    userRepository.deleteById(id);
  }
}
