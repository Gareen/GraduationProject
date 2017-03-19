package cn.sams.interceptor;

import cn.sams.entity.Teacher;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Fanpeng on 2017/1/23.
 *
 * 配置拦截器, 对页面的跳转请求进行拦截
 */
public class UserInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        //System.out.println("Interceptor work !");
        Teacher teacher = (Teacher)httpServletRequest.getSession().getAttribute("user");
        if (teacher == null) {
            httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/system/login/index.do");
            return false;
        }
       return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
