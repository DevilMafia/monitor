package com.dennis.api.interceptor;

import com.dennis.api.exception.SignAuthException;
import com.dennis.common.constants.ApiConstant;
import com.dennis.common.tools.SignAuthUtil;
import com.dennis.common.tools.StringUtil;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by Mr.Dxs on 2018/10/29.
 * 签名认证
 */
public class SignAuthInterceptor extends HandlerInterceptorAdapter {

    private Logger logger = LoggerFactory.getLogger(SignAuthInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 获取 所有请求参数
        Map map = request.getParameterMap();
        JSONObject params = JSONObject.fromObject(map);

        // 获取 请求参数 中 sign
        String sign = request.getParameter("sign");

        if (StringUtil.isEmpty(sign)){
            logger.warn("sign error: " + params);
            throw new SignAuthException();
        }

        // 对请求参数进行排序 生成服务端 自己 sign
        SortedMap<Object,Object> sort = new TreeMap<>(params);
        String checkSign = SignAuthUtil.createSign(ApiConstant.SIGN_KEY, sort);

        // 校验 签名
        if (sign.equals(checkSign)){
            return true;
        }
        else {
            logger.warn("sign check error: " + params);
            throw new SignAuthException();
        }

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
    }


}
