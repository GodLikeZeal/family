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
import java.util.stream.Collectors;

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
  @Value("${app.username}")
  private String username;
  @Value("${app.default-group}")
  private String groupName;
  @Autowired
  UserRepository userRepository;
  @Autowired
  GroupRepository groupRepository;
  @Autowired
  GroupService groupService;


  public List<User> findList() {
    return userRepository.findAll();
  }

  public List<User> findListByGroupId(String id) {
    return userRepository.findByGroupId(id);
  }

  public User findById(String id) {
    return userRepository.findById(id).orElse(User.builder().build());
  }

  public List<User> findByUsername(String username) {
    return userRepository.findByName(username);
  }

  /**
   * 保存.
   *
   * @param user
   * @return
   */
  public User save(@NonNull User user) {

    List<User> users = userRepository
        .findByGroupIdAndName(user.getGroupId().trim(), user.getName().trim());
    if (!CollectionUtils.isEmpty(users)) {
      throw new RuntimeException("不可重复添加");
    }

    if (StringUtils.isEmpty(user.getParentId())) {
      if (userRepository.existsUserByGroupIdAndParentIdIsNull(user.getGroupId().trim())) {
        throw new RuntimeException("已存在根节点，请选择长辈信息！");
      }
      user.setParentId(null);
    }

    if (StringUtils.isEmpty(user.getPassword())) {
      user.setPassword(new BCryptPasswordEncoder().encode(defaultPassword));
    }

    Optional<Group> group = groupRepository.findById(user.getGroupId().trim());

    if (!StringUtils.isEmpty(user.getParentId())) {
      Optional<User> parent = userRepository.findById(user.getParentId());
      String id = parent.map(User::getId).orElse(null);
      String groupId = group.isPresent() ? group.get().getId() : "";
      if (!groupId.equals(id)) {
        throw new RuntimeException("长辈信息不在该群组范围中...");
      }
    }

    user.setGroupName(group.isPresent() ? group.get().getName() : "");

    if (user.getRole() == null) {
      user.setRole(Role.USER);
    }
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
    User user = findById(id);
    if (Objects.isNull(user)) {
      throw new RuntimeException("找不到该用户");
    }
    if (username.equals(user.getName())) {
      throw new RuntimeException("无法删除默认管理员");
    }
    List<User> users = userRepository.findAll(Example.of(User.builder().groupId(id).build()));
    if (!CollectionUtils.isEmpty(users)) {
      throw new RuntimeException("该节点下有子节点，无法删除");
    }
    userRepository.deleteById(id);
  }

  /**
   * 构造树
   *
   * @return
   */
  public UserBo getTree() {
    List<Group> groups = groupService.findListByName(groupName);
    if (CollectionUtils.isEmpty(groups)) {
      throw new RuntimeException("默认群组不存在！");
    }
    String id = groups.get(0).getId();
    return getTree(id);
  }

  /**
   * 构造树
   *
   * @return
   */
  public UserBo getTree(String id) {
    List<User> users = this.findListByGroupId(id);
    List<UserBo> userBos = new ArrayList<>();
    UserBo r = new UserBo();
    for (User u : users) {
      UserBo user = new UserBo();
      BeanUtils.copyProperties(u, user);
      userBos.add(user);
      if (StringUtils.isEmpty(u.getParentId())) {
        r = user;
      }
    }
    r.setChildren(treeCreate(r.getId(), userBos));
    return r;
  }

  /**
   * 递归生成树
   *
   * @param id      id
   * @param userBos 子集合
   * @return
   */
  public List<UserBo> treeCreate(String id, List<UserBo> userBos) {
    List<UserBo> bos = new ArrayList<>();
    Iterator<UserBo> iterator = userBos.iterator();
    while (iterator.hasNext()) {
      UserBo userBo = iterator.next();
      if (Objects.equals(id, userBo.getParentId())) {
//        iterator.remove();
        userBo.setChildren(treeCreate(userBo.getId(), userBos));
        bos.add(userBo);
      }
    }
    return bos;
  }
}
