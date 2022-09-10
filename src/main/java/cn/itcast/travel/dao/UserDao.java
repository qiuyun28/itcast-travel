package cn.itcast.travel.dao;

import cn.itcast.travel.domain.User;
public interface UserDao {
    public User findUserByName(String name);
    public User findUserByCode(String code);
    public void save(User user);

    public void updateStatus(User user);

    public User findUserByUserNameAndPassWord(String username, String password);
}
