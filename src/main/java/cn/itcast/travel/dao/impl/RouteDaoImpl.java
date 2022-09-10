package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

public class RouteDaoImpl implements RouteDao {
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());
    @Override
    public int findTotalCount(int cid, String rname) {
        StringBuilder sb = new StringBuilder("select count(*) from tab_route where 1 = 1");
        List params = new ArrayList();
        if(cid != 0) {
            sb.append(" and cid = ?");
            params.add(cid);
        }
        if(rname != null && rname.length() > 0) {
            sb.append(" and rname like ?");
            params.add("%" + rname + "%");
        }
        return template.queryForObject(sb.toString(), Integer.class, params.toArray() );
    }

    @Override
    public int findTotalCount(String rname, int smallDollar, int bigDollar) {
        StringBuilder sb = new StringBuilder("select count(*) from tab_route where 1 = 1");
        List params = new ArrayList();
        if(rname != null && rname.length() > 0) {
            sb.append(" and rname like ?");
            params.add("%" + rname + "%");
        }
        sb.append(" and price >= ? and price <= ? ");
        params.add(smallDollar);
        params.add(bigDollar);
        return template.queryForObject(sb.toString(), Integer.class, params.toArray());
    }

    @Override
    public List<Route> findByPage(int cid, int start, int pageSize, String rname ) {
        StringBuilder sb = new StringBuilder("select * from tab_route where 1 = 1 ");
        List params = new ArrayList();
        if(cid != 0) {
            sb.append(" and cid = ? ");
            params.add(cid);
        }
        if(rname != null && rname.length() > 0) {
            sb.append(" and rname like ? ");
            params.add("%" + rname + "%");
        }
        sb.append(" limit ?, ? ");
        params.add(start);
        params.add(pageSize);
        return template.query(sb.toString(), new BeanPropertyRowMapper<Route>(Route.class), params.toArray() );
    }

    @Override
    public Route findOne(int rid) {
        String sql = "select * from tab_route where rid = ?";
        return template.queryForObject(sql, new BeanPropertyRowMapper<Route>(Route.class), rid);
    }

    @Override
    public List<Route> findAllOrderByCount(int start, String rname, int smallDollar, int bigDollar, int pageSize) {
        StringBuilder sb = new StringBuilder("select * from tab_route where price >= ? and price <= ? ");
        List params = new ArrayList();
        params.add(smallDollar);
        params.add(bigDollar);
        if(rname != null && !rname.equals("null") && rname.length() > 0) {
            sb.append(" and rname like ? ");
            params.add("%" + rname + "%");
        }
        sb.append(" order by `count` desc limit ?,?");
        params.add(start);
        params.add(pageSize);
        return template.query(sb.toString(), new BeanPropertyRowMapper<Route>(Route.class), params.toArray());
    }
}
