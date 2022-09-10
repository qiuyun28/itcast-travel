package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.FavoriteService;
import cn.itcast.travel.service.RouteService;
import cn.itcast.travel.service.impl.FavoriteServiceImpl;
import cn.itcast.travel.service.impl.RouteServiceImpl;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@WebServlet("/route/*")
public class RouteServlet extends BaseServlet {
    private RouteService routeService = new RouteServiceImpl();
    private FavoriteService favoriteService = new FavoriteServiceImpl();

    public void favoritePageQuery(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String currentPageStr = request.getParameter("currentPage");
        int uid = ((User) request.getSession().getAttribute("User")).getUid();
        int pageSize = 12;
        int currentPage = 1;
        if(currentPageStr != null && !currentPageStr.equals("null") && currentPageStr.length() > 0) {
            currentPage = Integer.parseInt(currentPageStr);
        }
        PageBean<Route> pageBean = favoriteService.pageQuery(uid, currentPage, pageSize);
        writeValue(pageBean, response);
    }

    public void pageQuery(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String currentPageStr = request.getParameter("currentPage");
        String pageSizeStr = request.getParameter("pageSize");
        String cidStr = request.getParameter("cid");
        String rname = request.getParameter("rname");
        if(rname != null && rname.length() > 0 && !rname.equals("null")) {
            rname = new String(rname.getBytes("iso-8859-1"), "utf-8");
        }
        if(rname.equals("null")) rname = null;
        int cid = 0;
        int currentPage = 0;
        int pageSize = 0;
        if(cidStr != null && cidStr.length() > 0 && !cidStr.equals("null")) {
            cid = Integer.parseInt(cidStr);
        }
        if(currentPageStr != null && currentPageStr.length() > 0 && !currentPageStr.equals("null")) {
            currentPage = Integer.parseInt(currentPageStr);
        } else {
            currentPage = 1;
        }
        if(pageSizeStr != null && pageSizeStr.length() > 0 && !pageSizeStr.equals("null")) {
            pageSize = Integer.parseInt(pageSizeStr);
        } else {
            pageSize = 5;
        }

        PageBean<Route> pageBean = routeService.pageQuery(cid, currentPage, pageSize, rname);
        writeValue(pageBean, response);
    }


    public void findOne(HttpServletRequest request, HttpServletResponse response) throws IOException {
        writeValue(routeService.findOne(Integer.parseInt(request.getParameter("rid"))), response);
    }

    public void isFavorite(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String rid = request.getParameter("rid");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("User");
        int uid = user == null ? 0 : user.getUid();
        if(uid == 0) {
            writeValue(false, response);
            return;
        }
        writeValue(favoriteService.isFavorite(Integer.parseInt(rid), uid), response);
    }

    public void addFavorite(HttpServletRequest request, HttpServletResponse response) {
        User user = (User) request.getSession().getAttribute("User");
        if(user == null) return;
        favoriteService.add(Integer.parseInt(request.getParameter("rid")), user.getUid());
    }

    public void removeFavorite(HttpServletRequest request, HttpServletResponse response) {
        User user = (User) request.getSession().getAttribute("User");
        if(user == null) return;
        favoriteService.remove(Integer.parseInt(request.getParameter("rid")), user.getUid());
    }

}
