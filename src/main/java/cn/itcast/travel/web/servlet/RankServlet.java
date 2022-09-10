package cn.itcast.travel.web.servlet;


import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.service.FavoriteService;
import cn.itcast.travel.service.impl.FavoriteServiceImpl;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.List;

@WebServlet("/rank/*")
public class RankServlet extends BaseServlet {

    FavoriteService favoriteService = new FavoriteServiceImpl();

    public void pageQuery(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String currentPageStr = request.getParameter("currentPage");
        String smallDollarStr = request.getParameter("smallDollar");
        String bigDollarStr = request.getParameter("bigDollar");
        String rname = request.getParameter("rname");
        if(rname != null && rname.length() > 0 && !rname.equals("null")) {
            rname = URLDecoder.decode(rname, "utf-8");
        }
        int currentPage = 1;
        int smallDollar = Integer.MIN_VALUE;
        int bigDollar = Integer.MAX_VALUE;
        if(currentPageStr != null && !currentPageStr.equals("null") && currentPageStr.length() > 0) {
            currentPage = Integer.parseInt(currentPageStr);
        }
        if(smallDollarStr != null && !smallDollarStr.equals("null") && smallDollarStr.length() > 0) {
            smallDollar = Integer.parseInt(smallDollarStr);
        }
        if(bigDollarStr != null && !bigDollarStr.equals("null") && bigDollarStr.length() > 0) {
            bigDollar = Integer.parseInt(bigDollarStr);
        }

        PageBean<Route> pb = favoriteService.pageQuery(currentPage, rname, smallDollar, bigDollar, 8);
        writeValue(pb, response);
    }


    public void hotQuery(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PageBean<Route> pb = favoriteService.pageQuery(1, null, Integer.MIN_VALUE, Integer.MAX_VALUE, 5);
        writeValue(pb, response);
    }

}
