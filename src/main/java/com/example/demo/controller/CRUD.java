package com.example.demo.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
 
 
@RestController
@RequestMapping(value = "/CRUD", method = { RequestMethod.GET, RequestMethod.POST })
public class CRUD {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private UserService userservice;
	
	@Autowired
	private JedisPool jedisPool;
	
	@RequestMapping(value = "set")
	public String setInfo() {
		User user = new User();
		user.setId(2);
		user.setName("name");
		user.setPassword("password");
		JSONObject json = new JSONObject();
		json.put("users", user);
		Jedis jedis = jedisPool.getResource();
		jedis.set(String.valueOf(user.getId()), json.toString());
		return json.toString();
	}
	
	
 
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public String delete(int id) {
		int result = userservice.delete(id);
		if (result >= 1) {
			return "删除成功";
		} else {
			return "删除失败";
		}
	}
 
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(User user) {
		int result = userservice.Update(user);
		if (result >= 1) {
			return "修改成功";
		} else {
			return "修改失败";
		}
 
	}
	
	@RequestMapping(value = "/insert", method = RequestMethod.POST)
	public User insert(User user) {
		return userservice.insertUser(user);
	}
	
	@RequestMapping("/ListUser")
	@ResponseBody
	public List<User> ListUser(){
		return userservice.ListUser();
	}
 
	@RequestMapping("/ListUserByname")
	@ResponseBody
	public User ListUserByname(String name){
		Jedis jedis = null;
		User user = null;
		try {
			jedis = jedisPool.getResource();
			
			String info =jedis.get(name);
			if(info!=null&&!"".equals(info)) {
				user = JSONObject.parseObject(info, User.class);
				return user;
			}
			
			user = userservice.findByName(name);
			
			String infos = JSONObject.toJSONString(user);
			jedis.set(name,infos);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			if(jedis!=null) {
				jedis.close();
			}
		}
		return user;
	}
	
	@RequestMapping("/FindUserById")
	@ResponseBody
	public User findUserById(String id){
		int ids = Integer.parseInt(id);
        User user = null;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();

            // 1. 查询Redis是否有数据 -- hash的方式
            Map<String, String> result = jedis.hgetAll(id); // hgetall
            if (result != null && result.size() > 0) {
                // TODO map对象转换为User
                user = new User();
                user.setId(Integer.parseInt(result.get("id")));
                user.setName(result.get("name").toString());
                user.setPassword(result.get("password").toString());
                user.setNumber(result.get("number").toString());
                return user; // 命中 缓存
            }

            user = userservice.findUserById(ids);
            // 3. 数据塞到redis中 // json格式，或者 hmset hset
            // String userJsonStr = JSONObject.toJSONString(user);
            HashMap<String,String> userInfo = new HashMap<>();
            userInfo.put("id", String.valueOf(user.getId()));
            userInfo.put("name", String.valueOf(user.getName()));
            userInfo.put("password", String.valueOf(user.getPassword()));
            userInfo.put("number", String.valueOf(user.getNumber()));
            jedis.hmset(id,userInfo); // jedis --- hmset key filed1 value1 ....
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
		return user;
	}
	
	
}
