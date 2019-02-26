/**
 * 
 */
package com.ppx.cloud.demo.test;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ppx.cloud.common.jdbc.annotation.Id;
import com.ppx.cloud.common.jdbc.annotation.Table;
import com.ppx.cloud.common.util.DateUtils;

/**
 * @author mark
 *
 * @date 2018年11月5日
 */
@Table("test")
public class Test {
	
	@Id
	private Integer testId;
	
	private String testName;
	
	@JsonFormat(pattern=DateUtils.DATE_PATTERN)
	@DateTimeFormat(pattern=DateUtils.DATE_PATTERN)
	private Date testDate;
	
	private Float testValue;
	
	private Integer testType;
	
	private Date created;

	public Integer getTestId() {
		return testId;
	}

	public void setTestId(Integer testId) {
		this.testId = testId;
	}

	public String getTestName() {
		return testName;
	}

	public void setTestName(String testName) {
		this.testName = testName;
	}

	public Date getTestDate() {
		return testDate;
	}

	public void setTestDate(Date testDate) {
		this.testDate = testDate;
	}

	public Float getTestValue() {
		return testValue;
	}

	public void setTestValue(Float testValue) {
		this.testValue = testValue;
	}

	public Integer getTestType() {
		return testType;
	}

	public void setTestType(Integer testType) {
		this.testType = testType;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}
	
	
	
	

}
