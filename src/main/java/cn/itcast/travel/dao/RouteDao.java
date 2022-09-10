package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Route;

import java.util.List;

public interface RouteDao {
    public int findTotalCount(int cid, String rname);
    public int findTotalCount(String rname, int smallDollar, int bigDollar);
    public List<Route> findByPage(int cid, int start, int pageSize, String rname);
    public Route findOne(int rid);
    public List<Route> findAllOrderByCount(int start, String rname, int smallDollar, int bigDollar, int pageSize);
}
