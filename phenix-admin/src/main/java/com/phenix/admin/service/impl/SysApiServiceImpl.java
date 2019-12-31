package com.phenix.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phenix.admin.entity.SysApi;
import com.phenix.admin.enums.ResourceTypeEnum;
import com.phenix.admin.mapper.SysApiMapper;
import com.phenix.admin.service.ISysApiService;
import com.phenix.admin.service.ISysAuthorityService;
import com.phenix.core.exception.PhenixAlertException;
import com.phenix.defines.constants.BaseConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author zhenghui
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class SysApiServiceImpl extends ServiceImpl<SysApiMapper, SysApi> implements ISysApiService {
    @Autowired
    private ISysAuthorityService authorityService;

    /**
     * 查询列表
     *
     * @return
     */
    @Override
    public List<SysApi> findAllList(String serviceId) {
        QueryWrapper<SysApi> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(ObjectUtils.isNotEmpty(serviceId), SysApi::getServiceId, serviceId);
        List<SysApi> list = this.list(queryWrapper);
        return list;
    }

    /**
     * 根据主键获取接口
     *
     * @param apiId
     * @return
     */
    @Override
    public SysApi getApi(Long apiId) {
        return this.getById(apiId);
    }


    /**
     * 检查接口编码是否存在
     *
     * @param apiCode
     * @return
     */
    @Override
    public Boolean isExist(String apiCode) {
        QueryWrapper<SysApi> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(SysApi::getApiCode, apiCode);
        int count = getCount(queryWrapper);
        return count > 0 ? true : false;
    }

    /**
     * 添加接口
     *
     * @param sysApi
     * @return
     */
    @Override
    public Boolean addApi(SysApi sysApi) {
        try {
            if (isExist(sysApi.getApiCode())) {
                throw new PhenixAlertException(String.format("%s编码已存在!", sysApi.getApiCode()));
            }
            if (sysApi.getPriority() == null) {
                sysApi.setPriority(0);
            }
            if (sysApi.getStatus() == null) {
                sysApi.setStatus(BaseConstants.ENABLED);
            }
            if (sysApi.getApiCategory() == null) {
                sysApi.setApiCategory(BaseConstants.DEFAULT_API_CATEGORY);
            }
            if (sysApi.getIsPersist() == null) {
                sysApi.setIsPersist(BaseConstants.FALSE);
            }
            if (sysApi.getIsAuth() == null) {
                sysApi.setIsAuth(BaseConstants.FALSE);
            }
            sysApi.setCreateTime(LocalDateTime.now());
            sysApi.setUpdateTime(sysApi.getCreateTime());
            this.save(sysApi);
            // 同步权限表里的信息
            authorityService.saveOrUpdateAuthority(sysApi.getApiId(), ResourceTypeEnum.api);
            return true;
        } catch (Exception e) {
            log.error("【sysApi addApi】", e);
            return false;
        }
    }

    /**
     * 修改接口
     *
     * @param sysApi
     * @return
     */
    @Override
    public Boolean updateApi(SysApi sysApi) {
        try {
            SysApi saved = getApi(sysApi.getApiId());
            if (saved == null) {
                throw new PhenixAlertException("信息不存在!");
            }
            if (!saved.getApiCode().equals(sysApi.getApiCode())) {
                // 和原来不一致重新检查唯一性
                if (isExist(sysApi.getApiCode())) {
                    throw new PhenixAlertException(String.format("%s编码已存在!", sysApi.getApiCode()));
                }
            }
            if (sysApi.getPriority() == null) {
                sysApi.setPriority(0);
            }
            if (sysApi.getApiCategory() == null) {
                sysApi.setApiCategory(BaseConstants.DEFAULT_API_CATEGORY);
            }
            sysApi.setUpdateTime(LocalDateTime.now());
            this.updateById(sysApi);
            // 同步权限表里的信息
            authorityService.saveOrUpdateAuthority(sysApi.getApiId(), ResourceTypeEnum.api);
            return true;
        } catch (Exception e) {
            log.error("【sysApi updateApi】", e);
            return false;
        }
    }

    /**
     * 查询接口
     *
     * @param apiCode
     * @return
     */
    @Override
    public SysApi getApi(String apiCode) {
        QueryWrapper<SysApi> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(SysApi::getApiCode, apiCode);
        return this.getOne(queryWrapper);
    }


    /**
     * 移除接口
     *
     * @param apiId
     * @return
     */
    @Override
    public Boolean removeApi(Long apiId) {
        SysApi sysApi = getApi(apiId);
        if (sysApi != null && BaseConstants.ENABLED.equals(sysApi.getIsPersist())) {
            throw new PhenixAlertException(String.format("保留数据,不允许删除"));
        }
        authorityService.removeAuthority(apiId, ResourceTypeEnum.api);
        return this.removeById(apiId);
    }


    /**
     * 获取数量
     *
     * @param queryWrapper
     * @return
     */
    @Override
    public int getCount(QueryWrapper<SysApi> queryWrapper) {
        return this.count(queryWrapper);
    }


}
