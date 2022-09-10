package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.FavoriteDao;
import cn.itcast.travel.domain.Favorite;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FavoriteDaoImpl implements FavoriteDao {
    JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());
    @Override
    public Favorite findByRidAndUid(int rid, int uid) {
        String sql = "select * from tab_favorite where rid = ? and uid = ?";
        Favorite favorite = null;
        try{
            favorite = template.queryForObject(sql, new BeanPropertyRowMapper<Favorite>(Favorite.class), rid, uid);
        } catch (EmptyResultDataAccessException e) {
            System.out.println("该用户没有收藏当前的旅游路线");
        }

        return favorite;
    }

    @Override
    public int findCountByRid(int rid) {
        String sql = "select count(*) from tab_favorite where rid = ?";
        return template.queryForObject(sql, Integer.class, rid);
    }

    @Override
    public void add(int rid, int uid) {
        String sql = "insert into tab_favorite values (?,?,?)";
        String sql2 = "UPDATE tab_route set `count` = `count` + 1 where rid = ?";
        template.update(sql, rid, new Date(), uid);
        template.update(sql2, rid);
    }

    @Override
    public void remove(int rid, int uid) {
        String sql = "DELETE FROM tab_favorite where rid = ? and uid = ?";
        String sql2 = "UPDATE tab_route set `count` = `count` - 1 where rid = ?";
        template.update(sql, rid, uid);
        template.update(sql2, rid);
    }

    public List<Favorite> findAllByUid(int uid, int start, int pageSize) {
        String sql = "select * from tab_favorite where uid = ? limit ?, ?";
        return template.query(sql, new BeanPropertyRowMapper<Favorite>(Favorite.class), uid, start, pageSize);
    }

    @Override
    public int findCountByUid(int uid) {
        String sql = "select count(*) from tab_favorite where uid = ?";
        return template.queryForObject(sql, Integer.class, uid);
    }

}
