package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.CategoryDao;
import cn.itcast.travel.dao.impl.CategoryDaoImpl;
import cn.itcast.travel.domain.Category;
import cn.itcast.travel.service.CategoryService;
import cn.itcast.travel.util.JedisUtil;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CategoryServiceImpl implements CategoryService {
    CategoryDao categoryDao = new CategoryDaoImpl();

    @Override
    public List<Category> findAll() {
        Jedis jedis = JedisUtil.getJedis();
        Set<String> categorys = jedis.zrange("category", 0, -1);
        if(categorys == null || categorys.size() != 8) {
            List<Category> all = categoryDao.findAll();
            for(Category c : all) {
                jedis.zadd("category", c.getCid(), c.getCname());
            }
            categorys = jedis.zrange("category",0, -1);
        }
        List<Category> res = new ArrayList<Category>();
        int id = 1;
        for(String c : categorys) {
            res.add(new Category(id++, c));
        }
        return res;
    }
}
