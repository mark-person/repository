package com.ppx.cloud.repository.todo.pojo;

import java.util.Date;

public class Todo {
	private Integer todoId;
	private String todoTitle;
	private Integer todoStatus;
	private Integer todoImportant;
	private Integer todoEmergent;
	private Integer todoPrio;
	private Date modified;
	private Integer modifiedBy;

	public Integer getTodoId() {
		return todoId;
	}

	public void setTodoId(Integer todoId) {
		this.todoId = todoId;
	}

	public String getTodoTitle() {
		return todoTitle;
	}

	public void setTodoTitle(String todoTitle) {
		this.todoTitle = todoTitle;
	}

	public Integer getTodoStatus() {
		return todoStatus;
	}

	public void setTodoStatus(Integer todoStatus) {
		this.todoStatus = todoStatus;
	}

	public Integer getTodoImportant() {
		return todoImportant;
	}

	public void setTodoImportant(Integer todoImportant) {
		this.todoImportant = todoImportant;
	}

	public Integer getTodoEmergent() {
		return todoEmergent;
	}

	public void setTodoEmergent(Integer todoEmergent) {
		this.todoEmergent = todoEmergent;
	}

	public Integer getTodoPrio() {
		return todoPrio;
	}

	public void setTodoPrio(Integer todoPrio) {
		this.todoPrio = todoPrio;
	}

	public Date getModified() {
		return modified;
	}

	public void setModified(Date modified) {
		this.modified = modified;
	}

	public Integer getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(Integer modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

}
