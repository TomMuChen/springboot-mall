package com.tom.springbootmall.dao.impl;

import com.tom.springbootmall.dao.UserDao;
import com.tom.springbootmall.dto.UserRegisterRequest;
import com.tom.springbootmall.model.User;
import com.tom.springbootmall.rowMapper.UserRowMapper;
import com.tom.springbootmall.service.impl.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserDaoImpl implements UserDao {
    private final static Logger log= LoggerFactory.getLogger(UserDaoImpl.class);
    @Autowired
    private NamedParameterJdbcTemplate npjt;

    @Override
    public User getUserByEmail(String email) {
        String sql="SELECT user_id,email,password,created_date,last_modified_date" +
                " FROM user WHERE email=:email";
        Map<String,Object>map=new HashMap<>();
        map.put("email",email);
        List<User> list=npjt.query(sql,map,new UserRowMapper());

        if (list.size()>0){
            log.info("所抓出的物件:{}",list.get(0).toString());
            return list.get(0);
        }else {
            return null;
        }



    }


    @Override
    public User getUserById(Integer userId) {

        String sql="SELECT user_id,email,password,created_date,last_modified_date " +
                "FROM user " +
                "WHERE user_id=:userId";
        Map<String,Object> map=new HashMap<>();
        map.put("userId",userId);
        UserRowMapper userRowMapper=new UserRowMapper();

        List<User> list=npjt.query(sql,map,userRowMapper);
        if (list.size()>0){
            return list.get(0);
        }else {
            return null;
        }
    }




    @Override
    //建立新帳號
    public Integer createUser(UserRegisterRequest userRegisterRequest){
        String sql="INSERT INTO user (email,password,created_date,last_modified_date) " +
                "VALUES (:email,:password,:created_date,:last_modified_date)";
        Map<String,Object> map=new HashMap<>();
        map.put("email",userRegisterRequest.getEmail());
        map.put("password",userRegisterRequest.getPassword());

        Date now=new Date();
        map.put("created_date",now);
        map.put("last_modified_date",now);

        KeyHolder keyHolder=new GeneratedKeyHolder();
        npjt.update(sql,new MapSqlParameterSource(map),keyHolder);
        int userId=keyHolder.getKey().intValue();
        System.out.println("createUser success");
        return userId ;
    }


}
