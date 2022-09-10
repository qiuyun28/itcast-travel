package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.FavoriteDao;
import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.dao.RouteImgDao;
import cn.itcast.travel.dao.SellerDao;
import cn.itcast.travel.dao.impl.FavoriteDaoImpl;
import cn.itcast.travel.dao.impl.RouteDaoImpl;
import cn.itcast.travel.dao.impl.RouteImgDaoImpl;
import cn.itcast.travel.dao.impl.SellerDaoImpl;
import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.service.RouteService;

public class RouteServiceImpl implements RouteService {
    private RouteDao routeDao = new RouteDaoImpl();
    private RouteImgDao routeImgDao = new RouteImgDaoImpl();
    private SellerDao sellerDao = new SellerDaoImpl();
    private FavoriteDao favoriteDao = new FavoriteDaoImpl();
    @Override
    public PageBean<Route> pageQuery(int cid, int currentPage, int pageSize, String rname) {
        PageBean<Route> pb = new PageBean<Route>();
        pb.setCurrentPage(currentPage);
        pb.setPageSize(pageSize);
        pb.setTotalCount(routeDao.findTotalCount(cid, rname));
        pb.setList(routeDao.findByPage(cid, (currentPage - 1) * pageSize, pageSize, rname));
        pb.setTotalPage(pb.getTotalCount() % pageSize == 0 ? pb.getTotalCount() / pageSize : pb.getTotalCount() / pageSize + 1 );
        return pb;
    }

    @Override
    public Route findOne(int rid) {
        Route route = routeDao.findOne(rid);
        route.setRouteImgList(routeImgDao.findByRid(rid));
        route.setSeller(sellerDao.findBySid(route.getSid()));
        route.setCount(favoriteDao.findCountByRid(rid));
        return route;
    }

}
