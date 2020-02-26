package main.java.practise.review.review_dao.impl;

import main.java.practise.review.review_dao.UserDao;
import main.java.practise.review.review_pojo.User;

import java.util.ArrayList;

public class UserDaoImpl implements UserDao {
    private static ArrayList<User> array = new ArrayList<>();

    @Override
    public boolean login(String username, String password) {
        boolean flag = false;
        for (User u : array) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    @Override
    public void regist(User user) {
        array.add(user);
    }
}
