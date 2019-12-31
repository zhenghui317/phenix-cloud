package com.phenix.core.exception;

import com.phenix.core.utils.WebUtils;
import com.phenix.defines.response.ResponseMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义未认证处理
 *
 * @author zhenghui
 */
@Slf4j
public class PhenixAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException exception) throws IOException, ServletException {
        ResponseMessage resultBody = PhenixGlobalExceptionHandler.resolveException(exception,request.getRequestURI());
        response.setStatus(resultBody.getHttpStatus());
        WebUtils.writeJson(response, resultBody);
    }
}