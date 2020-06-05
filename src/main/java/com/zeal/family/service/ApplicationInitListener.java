package com.zeal.family.service;

import com.zeal.family.entiy.User;
import com.zeal.family.enums.Gender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 初始化后，设置默认账号和密码.
 *
 * @author zhanglei
 * @date 2020/6/3  11:20 上午
 */
@Slf4j
@Component
public class ApplicationInitListener implements ApplicationRunner {

  @Value("${app.username}")
  private String username;
  @Value("${app.password}")
  private String password;

  @Autowired
  UserService userService;

  @Override
  public void run(ApplicationArguments args) throws Exception {
    log.info("开始执行初始化操作...");
    log.info("查询默认用户名:{}", username);
    List<User> users = userService.findByUsername(username);
    log.info("结果为{}", users);
    if (!CollectionUtils.isEmpty(users)) {
      return;
    }
    userService.save(User.builder()
        .name(username)
        .password(new BCryptPasswordEncoder().encode(password))
        .gender(Gender.MALE)
        .build());
  }
}
