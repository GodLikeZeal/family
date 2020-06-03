package com.zeal.family.service;

import com.zeal.family.bo.Mypage;
import com.zeal.family.bo.UserBo;
import com.zeal.family.entiy.Group;
import com.zeal.family.entiy.User;
import com.zeal.family.enums.Role;
import com.zeal.family.repository.GroupRepository;
import com.zeal.family.repository.UserRepository;
import lombok.NonNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.*;

/**
 * 用户服务.
 *
 * @author zhanglei
 * @date 2020/2/9  3:37 下午
 */
@Service
public class UserService {

  @Value("${app.default-password}")
  private String defaultPassword;
  @Autowired
  UserRepository userRepository;
  @Autowired
  GroupRepository groupRepository;


  public List<User> findList() {
    return userRepository.findAll();
  }

  public User findById(String id) {
    return userRepository.findById(id).orElse(User.builder().build());
  }

  public User findByUsernameAndPassword(String username, String password) {
    return userRepository.findByNameAndPassword(username, password);
  }
  /**
   * 保存.
   *
   * @param user
   * @return
   */
  public User save(@NonNull User user) {
    if (StringUtils.isEmpty(user.getPassword())) {
      user.setPassword(new BCryptPasswordEncoder().encode(defaultPassword));
    }
    user.setRole(Role.USER);
    user.setCreateDate(LocalDate.now());
    return userRepository.save(user);
  }

  /**
   * 更新.
   *
   * @param user
   * @return
   */
  public User update(@NonNull User user) {
    User user1 = findById(user.getId());
    user.setCreateDate(user1.getCreateDate());
    return userRepository.save(user);
  }

  /**
   * 分页查询.
   *
   * @param pageNumber 页码
   * @param pageSize   每页条数
   * @param groupId    分组id
   * @param name       名称
   * @return
   */
  public Mypage<User> findPage(int pageNumber, int pageSize, String groupId, String name) {
    Sort sort = Sort.by(Sort.Direction.DESC, "createDate");
    Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
    Page<User> users;
    if (StringUtils.isEmpty(groupId)) {
      if (StringUtils.isEmpty(name)) {
        users = userRepository.findAll(pageable);
      } else {
        users = userRepository.findByNameIsLike(name.trim(), pageable);
      }
    } else {
      if (StringUtils.isEmpty(name)) {
        users = userRepository.findByGroupId(groupId, pageable);
      } else {
        users = userRepository.findByGroupIdAndNameIsLike(groupId, name.trim(), pageable);
      }
    }
    Mypage<User> mypage = Mypage.of(users);

    for (User user : mypage.getData()) {
      if (!StringUtils.isEmpty(user.getGroupId())) {
        Group group = groupRepository.findById(user.getGroupId()).orElse(null);
        if (Objects.nonNull(group)) {
          user.setGroupName(group.getName());
        }
      }
      if (!StringUtils.isEmpty(user.getParentId())) {
        User u = userRepository.findById(user.getParentId()).orElse(null);
        if (Objects.nonNull(u)) {
          user.setParentName(u.getName());
        }
      }
    }
    return mypage;
  }

  /**
   * 根据id删除.
   *
   * @param id
   */
  public void remove(String id) {
    List<User> users = userRepository.findAll(Example.of(User.builder().groupId(id).build()));
    if (!CollectionUtils.isEmpty(users)) {
      throw new RuntimeException("该节点下有子节点，无法删除");
    }
    userRepository.deleteById(id);
  }

  public UserBo getTree() {
    List<User> users = userRepository.findAll();
    List<UserBo> userBos = new ArrayList<>();
    UserBo r = new UserBo();
    for (User u : users) {
      UserBo user = new UserBo();
      BeanUtils.copyProperties(u,user);
      userBos.add(user);
      if (StringUtils.isEmpty(u.getParentId()) && !u.getName().equals("张磊")) {
        r= user;
      }
    }
    r.setChildren(treeCreate(r.getId(), userBos));
    return r;
  }

  public List<UserBo> treeCreate(String id, List<UserBo> userBos) {
    List<UserBo> bos = new ArrayList<>();
    Iterator<UserBo> iterator = userBos.iterator();
    while (iterator.hasNext()) {
      UserBo userBo = iterator.next();
      if (id.equals(userBo.getParentId())) {
//        iterator.remove();
        userBo.setChildren(treeCreate(userBo.getId(), userBos));
        bos.add(userBo);
      }
    }
    return bos;
  }
}
