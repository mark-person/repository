package com.ppx.cloud.repository.user;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

import com.ppx.cloud.common.jdbc.MyDaoSupport;

@Service
public class RepoUserServiceImpl extends MyDaoSupport implements RepoUserService {
 
	public RepoUser get(Integer id) {
		RepoUser pojo = getJdbcTemplate().queryForObject("select * from repo_user where repo_user_id = ?",
                BeanPropertyRowMapper.newInstance(RepoUser.class), id);
        return pojo;
    }
	
	

}
