package com.zeal.family.repository;

import com.zeal.family.entiy.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 成员查询类.
 *
 * @author  zhanglei
 * @date 2020/2/8  2:49 下午
 */
@Repository
public interface UserRepository extends MongoRepository<User, String> {

  List<User> findByNameAndPassword(String name, String password);

  List<User> findByName(String name);

  Page<User> findByGroupIdAndNameIsLike(String groupId, String name, Pageable pageable);

  Page<User> findByGroupId(String groupId, Pageable pageable);

  Page<User> findByNameIsLike(String name, Pageable pageable);
}
