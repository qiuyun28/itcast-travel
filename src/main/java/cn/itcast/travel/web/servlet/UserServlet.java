package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.Serializers;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet("/user/*")
public class UserServlet extends BaseServlet {

    private UserService service = new UserServiceImpl();

    /**
     * 激活功能
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void active(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String code = request.getParameter("code");
        boolean flag = service.active(code);
        String msg = null;
        if(flag) {
            msg = "<a href='login.html'>激活成功, 请点击此处登录</a>";
        } else {
            msg = "激活失败，请联系管理员";
        }
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().write(msg);

    }

    /**
     * 自动登录功能
     * @param request
     * @param response
     * @throws IOException
     */
    public void autoLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Cookie[] cookies = request.getCookies();
        ResultInfo info = new ResultInfo();
        User user = new User();
        if(cookies != null) {
            for(Cookie c : cookies) {
                if(c.getName().equals("autoLogin") && c.getValue().equals("true")) {
                    info.setFlag(true);
                }
            }

        }
        if(info.getFlag() == true) {
            for(Cookie c : cookies) {
                if(c.getName().equals("username")) {
                    user.setUsername(c.getValue());
                }
                if(c.getName().equals("password")) {
                    user.setPassword(c.getValue());
                }
            }
        }
        info.setData(user);
        writeValue(info, response);
    }

    /**
     * 退出功能
     * @param request
     * @param response
     * @throws IOException
     */
    public void exit(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.getSession().removeAttribute("User");
        response.sendRedirect(request.getContextPath() + "/login.html");
    }


    /**
     * 查找单个用户
     * @param request
     * @param response
     * @throws IOException
     */
    public void findOne(HttpServletRequest request, HttpServletResponse response) throws IOException {
        writeValue((User)request.getSession().getAttribute("User"), response);
    }

    /**
     * 登录功能
     * @param request
     * @param response
     * @throws IOException
     */
    public void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String check = request.getParameter("check");
        ResultInfo info = new ResultInfo();
        HttpSession session = request.getSession();
        String checkcode_server = (String) session.getAttribute("CHECKCODE_SERVER");
        session.removeAttribute("CHECKCODE_SERVER");
        if(checkcode_server == null || !checkcode_server.equalsIgnoreCase(check)) {
            info.setFlag(false);
            info.setErrorMsg("验证码错误");
            writeValue(info, response);
            return;
        }

        Map<String, String[]> map = request.getParameterMap();
        User u = new User();
        try {
            BeanUtils.populate(u, map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        User user = service.login(u);
        if(user == null) {
            info.setFlag(false);
            info.setErrorMsg("用户名或密码错误");
        } else {
            if(user.getStatus().equals("N")) {
                info.setFlag(false);
                info.setErrorMsg("您还没有激活邮箱");
            } else {
                info.setFlag(true);
                session.setAttribute("User", user);
                String autoLogin = request.getParameter("autoLogin");
                if(autoLogin != null && autoLogin.equals("on")) {
                    Cookie cookie1 = new Cookie("username", user.getUsername());
                    Cookie cookie2 = new Cookie("password", user.getPassword());
                    Cookie cookie3 = new Cookie("autoLogin", "true");
                    cookie1.setMaxAge(3600 * 24 * 3);
                    cookie2.setMaxAge(3600 * 24 * 3);
                    cookie3.setMaxAge(3600 * 24 * 3);
                    response.addCookie(cookie1);
                    response.addCookie(cookie2);
                    response.addCookie(cookie3);
                } else {
                    Cookie[] cookies = request.getCookies();
                    for(Cookie c : cookies) {
                        c.setMaxAge(0);
                    }
                }
            }
        }
        writeValue(info, response);
    }

    /**
     * 注册功能
     * @param request
     * @param response
     * @throws IOException
     */
    public void regist(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        String checkcode_server = (String) session.getAttribute("CHECKCODE_SERVER");
        session.removeAttribute("CHECKCODE_SERVER");
        String check = request.getParameter("check");
        if(checkcode_server == null || !checkcode_server.equalsIgnoreCase(check)) {
            ResultInfo info = new ResultInfo();
            info.setFlag(false);
            info.setErrorMsg("验证码错误");
            writeValue(info, response);
            return;
        }

        Map<String, String[]> map = request.getParameterMap();

        User user = new User();
        try {
            BeanUtils.populate(user, map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        boolean flag = service.regist(user);
        ResultInfo info = new ResultInfo();
        if(flag) {
            info.setFlag(true);

        } else {
            info.setFlag(false);
            info.setErrorMsg("注册失败");

        }
        writeValue(info, response);
    }


}
