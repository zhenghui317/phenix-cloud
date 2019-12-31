package com.phenix.core.exception;

import com.phenix.core.utils.WebUtils;
import com.phenix.defines.response.ResponseMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义访问拒绝
 * @author zhenghui
 */
@Slf4j
public class PhenixAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException exception) throws IOException, ServletException {
        ResponseMessage responseMessage = PhenixGlobalExceptionHandler.resolveException(exception,request.getRequestURI());
        response.setStatus(responseMessage.getHttpStatus());
        WebUtils.writeJson(response, responseMessage);
    }
}
