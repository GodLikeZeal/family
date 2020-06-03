package com.zeal.family.service;

import com.zeal.family.entiy.User;
import com.zeal.family.enums.Gender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 初始化后，设置默认账号和密码.
 *
 * @author  zhanglei
 * @date 2020/6/3  11:20 上午
 */
@Component
public class ApplicationInitListener implements ApplicationListener<ContextRefreshedEvent> {

  @Value("${app.username}")
  private String username;
  @Value("${app.password}")
  private String password;

  @Autowired
  UserService userService;

  @Override
  public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
    User user = userService.findByUsernameAndPassword(username,password);
    if (Objects.nonNull(user)) {
      return;
    }
    userService.save(User.builder()
        .name(username)
        .password(new BCryptPasswordEncoder().encode(password))
        .gender(Gender.MALE)
        .build());
  }
}
