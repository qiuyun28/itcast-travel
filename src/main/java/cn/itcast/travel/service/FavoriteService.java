package cn.itcast.travel.service;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;

public interface FavoriteService {
    boolean isFavorite(int parseInt, int uid);

    void add(int rid, int uid);

    void remove(int rid, int uid);

    PageBean<Route> pageQuery(int uid, int currentPage, int pageSize);

    PageBean<Route> pageQuery(int currentPage, String rname, int smallDollar, int bidDollar, int pageSize);
}
