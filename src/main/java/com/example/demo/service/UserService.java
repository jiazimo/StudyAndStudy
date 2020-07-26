package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
 
 
 
@Service
public class UserService {
	@Autowired
	private UserMapper userMapper;
 
	public User findByName(String name) {
		return userMapper.findUserByName(name);
	}
 
	public User insertUser(User user) {
		userMapper.insertUser(user);
		return user;
	}
	public List<User> ListUser(){
		return	userMapper.ListUser();
	}
	
	
	public int Update(User user){
		return userMapper.Update(user);
	}
	
	public int delete(int id){
		return userMapper.delete(id);
	}
	
	public User findUserById(int id) {
		return userMapper.findUserById(id);
	}
	
	public static void main(String[] args) {
		StringUtils.isEmpty(new Object());
	}
	
}
