package com.tom.springbootmall.service.impl;

import com.tom.springbootmall.dao.UserDao;
import com.tom.springbootmall.dao.impl.UserDaoImpl;
import com.tom.springbootmall.dto.UserLoginRequest;
import com.tom.springbootmall.dto.UserRegisterRequest;
import com.tom.springbootmall.model.User;
import com.tom.springbootmall.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.web.server.ResponseStatusException;

@Component
public class UserServiceImpl implements UserService {
    private final static Logger log= LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private UserDao userDao;

    public User getUserById(Integer userId){
        return userDao.getUserById(userId);
    }


    /**
     * 以register命名，係因該方法中，尚要執行其他動作，如檢查email，故以概括性的方式命名
     * 而dao以createuser，即因單純的創見user
     * @param userRegisterRequest
     * @return
     */
    public Integer register(UserRegisterRequest userRegisterRequest){
        //在註冊前就要先檢查是否email已被註冊過
        User user=userDao.getUserByEmail(userRegisterRequest.getEmail());

        //把前端資訊轉換成hash value
        String hashedPassword= DigestUtils.md5DigestAsHex(userRegisterRequest.getPassword().getBytes());
        userRegisterRequest.setPassword(hashedPassword);

        if (user!=null){
            //當DB中找到已被註冊的帳號時，則噴出一個exception
            log.warn("該email:{}已被註冊",userRegisterRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }else {
            return userDao.createUser(userRegisterRequest) ;
        }

    }


    /**
     * 驗證使用者登入
     * @param userLoginRequest
     * @return
     */
    @Override
    public User login(UserLoginRequest userLoginRequest) {
        log.info("----開始登入----");
        User user=userDao.getUserByEmail(userLoginRequest.getEmail());

        //檢查User是否存在
        if (user==null){
            log.warn("此email:{}未被註冊",userLoginRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        //把前端傳來的密碼轉成雜湊直，供下列比較密碼
        String hashedPassword=DigestUtils.md5DigestAsHex(userLoginRequest.getPassword().getBytes());

        //檢查密碼是否正確
        if (user.getPassword().equals(hashedPassword)){
            log.info("成功取得user資料");
            return user;
        }else {
            log.warn("此信箱{} 密碼輸入錯誤",userLoginRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}
