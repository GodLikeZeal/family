package com.zeal.family.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.List;

/**
 * 自定义分页.
 *
 * @author zhanglei
 * @date 2020/2/9  5:15 下午
 */

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Mypage<T> {

  private Integer pageNumber;
  // 当前页面条数
  private Integer pageSize;

  private Integer total;
  // 排序条件
  private Sort sort;

  private List<T> data;

  public static Mypage of(Page page) {
    return Mypage.builder()
        .pageNumber(page.getNumber() + 1)
        .pageSize(page.getSize())
        .total(page.getTotalPages())
        .data(page.getContent())
        .sort(page.getSort())
        .build();
  }
}
