package com.phenix.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phenix.admin.entity.SysAction;
import com.phenix.admin.enums.ResourceTypeEnum;
import com.phenix.admin.mapper.SysActionMapper;
import com.phenix.admin.service.ISysActionService;
import com.phenix.admin.service.ISysAuthorityService;
import com.phenix.core.exception.PhenixAlertException;
import com.phenix.defines.constants.BaseConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
public class SysActionServiceImpl extends ServiceImpl<SysActionMapper, SysAction> implements ISysActionService {

    @Autowired
    private ISysAuthorityService authorityService;

    @Value("${spring.application.name}")
    private String DEFAULT_SERVICE_ID;


    /**
     * 根据主键获取操作
     *
     * @param actionId
     * @return
     */
    @Override
    public SysAction getAction(Long actionId) {
        return this.getById(actionId);
    }

    /**
     * 查询菜单下所有操作
     *
     * @param menuId
     * @return
     */
    @Override
    public List<SysAction> findListByMenuId(Long menuId) {
        QueryWrapper<SysAction> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .eq(SysAction::getMenuId, menuId);
        List<SysAction> list = this.list(queryWrapper);
        //根据优先级从小到大排序
        list.sort((SysAction h1, SysAction h2) -> h1.getPriority().compareTo(h2.getPriority()));
        return list;
    }

    /**
     * 检查Action编码是否存在
     *
     * @param acitonCode
     * @return
     */
    @Override
    public Boolean isExist(String acitonCode) {
        QueryWrapper<SysAction> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .eq(SysAction::getActionCode, acitonCode);
        int count = this.count(queryWrapper);
        return count > 0 ? true : false;
    }

    /**
     * 添加Action操作
     *
     * @param aciton
     * @return
     */
    @Override
    public Boolean addAction(SysAction aciton) {
        try {
            if (isExist(aciton.getActionCode())) {
                throw new PhenixAlertException(String.format("%s编码已存在!", aciton.getActionCode()));
            }
            if (aciton.getMenuId() == null) {
                aciton.setMenuId(0L);
            }
            if (aciton.getPriority() == null) {
                aciton.setPriority(0);
            }
            if (aciton.getStatus() == null) {
                aciton.setStatus(BaseConstants.ENABLED);
            }
            if (aciton.getIsPersist() == null) {
                aciton.setIsPersist(BaseConstants.FALSE);
            }
            aciton.setCreateTime(LocalDateTime.now());
            aciton.setServiceId(DEFAULT_SERVICE_ID);
            aciton.setUpdateTime(aciton.getCreateTime());
            Boolean success = this.save(aciton);
            if (success) {
                // 同步权限表里的信息
                authorityService.saveOrUpdateAuthority(aciton.getActionId(), ResourceTypeEnum.action);
            }
            return success;
        } catch (Exception e) {
            log.error("【SysAction addAction】", e);
            return false;
        }
    }

    /**
     * 修改Action操作
     *
     * @param aciton
     * @return
     */
    @Override
    public Boolean updateAction(SysAction aciton) {
        try {
            SysAction saved = this.getById(aciton.getActionId());
            if (saved == null) {
                throw new PhenixAlertException(String.format("%s信息不存在", aciton.getActionId()));
            }
            if (!saved.getActionCode().equals(aciton.getActionCode())) {
                // 和原来不一致重新检查唯一性
                if (isExist(aciton.getActionCode())) {
                    throw new PhenixAlertException(String.format("%s编码已存在!", aciton.getActionCode()));
                }
            }
            if (aciton.getMenuId() == null) {
                aciton.setMenuId(0L);
            }
            if (aciton.getPriority() == null) {
                aciton.setPriority(0);
            }
            aciton.setUpdateTime(LocalDateTime.now());
            Boolean success = this.updateById(aciton);
            if (success) {
                // 同步权限表里的信息
                authorityService.saveOrUpdateAuthority(aciton.getActionId(), ResourceTypeEnum.action);
            }
            return success;
        } catch (Exception e) {
            log.error("【SysAction addAction】", e);
            return false;
        }
    }

    /**
     * 移除Action
     *
     * @param actionId
     * @return
     */
    @Override
    public Boolean removeAction(Long actionId) {
        try {
            SysAction aciton = this.getById(actionId);
            if (aciton != null && BaseConstants.TRUE.equals(aciton.getIsPersist())) {
                throw new PhenixAlertException(String.format("保留数据,不允许删除"));
            }
            authorityService.removeAuthorityAction(actionId);
            authorityService.removeAuthority(actionId, ResourceTypeEnum.action);
            this.removeById(actionId);
            return true;
        } catch (Exception e) {
            log.error("【SysAction removeAction】", e);
            return false;
        }
    }

    /**
     * 移除菜单相关资源
     *
     * @param menuId
     */
    @Override
    public Boolean removeByMenuId(Long menuId) {
        try {
            List<SysAction> sysActionList = findListByMenuId(menuId);
            if (sysActionList != null && sysActionList.size() > 0) {
                for (SysAction sysAction : sysActionList) {
                    removeAction(sysAction.getActionId());
                }
            }
            return true;
        } catch (Exception e) {
            log.error("【SysAction removeByMenuId】", e);
            return false;
        }
    }
}
