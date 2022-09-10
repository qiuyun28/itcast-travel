package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.FavoriteDao;
import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.dao.impl.FavoriteDaoImpl;
import cn.itcast.travel.dao.impl.RouteDaoImpl;
import cn.itcast.travel.domain.Favorite;
import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.service.FavoriteService;

import java.util.ArrayList;
import java.util.List;

public class FavoriteServiceImpl implements FavoriteService {
    private FavoriteDao favoriteDao = new FavoriteDaoImpl();
    private RouteDao routeDao = new RouteDaoImpl();
    @Override
    public boolean isFavorite(int rid, int uid) {return favoriteDao.findByRidAndUid(rid, uid) != null;}

    @Override
    public void add(int rid, int uid) {favoriteDao.add(rid, uid);}

    @Override
    public void remove(int rid, int uid) {favoriteDao.remove(rid, uid);}

    @Override
    public PageBean<Route> pageQuery(int uid, int currentPage, int pageSize) {
        PageBean<Route> pb = new PageBean<Route>();
        pb.setCurrentPage(currentPage);
        pb.setPageSize(pageSize);
        List<Favorite> favorites = favoriteDao.findAllByUid(uid, (currentPage - 1) * pageSize, pageSize);
        pb.setTotalCount(favoriteDao.findCountByUid(uid));
        pb.setTotalPage(pb.getTotalCount() % pageSize == 0 ? pb.getTotalCount() / pageSize : pb.getTotalCount() / pageSize + 1);
        List<Route> routes = new ArrayList<Route>();
        for(Favorite favorite : favorites) {
            routes.add(routeDao.findOne(favorite.getRid()));
        }
        pb.setList(routes);
        pb.setTotalPage(pb.getTotalCount() % pageSize == 0 ? pb.getTotalCount() / pageSize : pb.getTotalCount() / pageSize + 1 );
        return pb;
    }

    @Override
    public PageBean<Route> pageQuery(int currentPage, String rname, int smallDollar, int bigDollar, int pageSize) {
        PageBean<Route> pb = new PageBean<Route>();
        pb.setCurrentPage(currentPage);
        pb.setPageSize(pageSize);
        pb.setList(routeDao.findAllOrderByCount((currentPage - 1) * pageSize, rname, smallDollar, bigDollar, pageSize));
        pb.setTotalCount(routeDao.findTotalCount(rname, smallDollar, bigDollar));
        pb.setTotalPage(pb.getTotalCount() % pageSize == 0 ? pb.getTotalCount() / pageSize : pb.getTotalCount() / pageSize + 1 );
        return pb;
    }

}
