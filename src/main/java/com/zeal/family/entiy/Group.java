package com.zeal.family.entiy;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

/**
 *分组
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Group {

  /**
   * id.
   */
  @Id
  private String id;

  /**
   * 名称.
   */
  private String name;

  /**
   * 介绍.
   */
  private String introduction;

  /**
   * 创建时间.
   */
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
  private LocalDate createDate;
}
