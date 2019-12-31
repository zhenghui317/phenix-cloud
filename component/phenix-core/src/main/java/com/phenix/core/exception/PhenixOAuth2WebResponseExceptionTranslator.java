package com.phenix.core.exception;

import com.phenix.defines.response.ResponseMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 自定义oauth2异常提示
 * @author zhenghui
 */
@Slf4j
public class PhenixOAuth2WebResponseExceptionTranslator implements WebResponseExceptionTranslator {

    @Override
    public ResponseEntity translate(Exception e) throws Exception {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        ResponseMessage responseData = PhenixGlobalExceptionHandler.resolveException(e,request.getRequestURI());
        return ResponseEntity.status(responseData.getHttpStatus()).body(responseData);
    }
}
