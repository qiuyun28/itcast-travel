package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.dao.impl.UserDaoImpl;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.util.MailUtils;
import cn.itcast.travel.util.UuidUtil;

public class UserServiceImpl implements UserService {
    private UserDao userDao = new UserDaoImpl();
    @Override
    public boolean active(String code) {
        User user = userDao.findUserByCode(code);
        if(user == null) {
            return false;
        }
        userDao.updateStatus(user);
        return true;
    }

    @Override
    public User login(User user) {
        return userDao.findUserByUserNameAndPassWord(user.getUsername(), user.getPassword());
    }


    /**
     * 注册用户
     * @param user
     * @return
     */
    @Override
    public boolean regist(User user) {
        User u = userDao.findUserByName(user.getUsername());
        if(u != null) {
            return false;
        }
        user.setStatus("N");
        user.setCode(UuidUtil.getUuid());
        userDao.save(user);
        String context = "<a href='http://192.168.244.46:9090/travel/user/active?code=" +
                            user.getCode() + "'>点击此处激活 </a>";
        MailUtils.sendMail(user.getEmail(), context,"激活邮件");

        return true;
    }
}
