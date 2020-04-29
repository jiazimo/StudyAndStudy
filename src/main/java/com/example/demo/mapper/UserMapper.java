package com.example.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.User;

@Repository
public interface UserMapper {
	
	public User findUserByName(String name);
	
	public List<User> ListUser();
	
	public int insertUser(User user);
	
	public int delete(int id);
	
	public int Update(User user);
	
	public User findUserById(int id);
}
