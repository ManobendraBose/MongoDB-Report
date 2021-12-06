package com.data.report.datareport.modal;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection="CEmail")
public class Email {
	
	@Id
	private String contactEmailId;
	@Field
	private String contactId;
	@Field
	private String email;
	@Field
	private int emailTypeId;
	@Field
	private String emailTypeDesc;
	@Field
	private String status;
  @Field
  private LocalDateTime created;

}
