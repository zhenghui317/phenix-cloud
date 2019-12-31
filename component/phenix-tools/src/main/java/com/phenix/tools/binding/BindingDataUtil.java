package com.phenix.tools.binding;

import com.phenix.defines.result.ExecuteResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.List;

/**
 * 绑定数据工具类
 */
public final class BindingDataUtil {

    /**
     * 校验参数绑定是否满足要求
     *
     * @param bindingResult 绑定验证参数的对象
     * @return 是否满足校验要求
     */
    public static ExecuteResult check(BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {

            List<ObjectError> errorList = bindingResult.getAllErrors();
            if (errorList.size() != 0){
                return ExecuteResult.fail(errorList.get(0).getDefaultMessage());
            }
        }
        return ExecuteResult.ok();
    }
}
