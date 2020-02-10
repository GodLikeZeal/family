package com.zeal.family.repository;

import com.zeal.family.entiy.Group;
import com.zeal.family.entiy.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * 分组查询类.
 *
 * @author  zhanglei
 * @date 2020/2/8  2:49 下午
 */
@Repository
public interface GroupRepository extends MongoRepository<Group, String> {
}
