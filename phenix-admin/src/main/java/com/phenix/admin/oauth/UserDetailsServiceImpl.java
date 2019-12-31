package com.phenix.admin.oauth;

import com.phenix.admin.pojo.dto.UserSysAccountDTO;
import com.phenix.admin.service.ISysUserService;
import com.phenix.core.security.PhenixUserDetails;
import com.phenix.core.security.configuration.PhenixOAuthAdminProperties;
import com.phenix.defines.constants.BaseConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Security用户信息获取实现类
 *
 * @author liuyadu
 */
@Slf4j
@Service("userDetailService")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private ISysUserService userService;
    @Autowired
    private PhenixOAuthAdminProperties adminProperties;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        UserSysAccountDTO account = userService.getUserAccountInfo(username);
        if (account == null || account.getAccountId() == null) {
            throw new UsernameNotFoundException("系统用户 " + username + " 不存在!");
        }
        String domain = account.getDomain();
        Long accountId = account.getAccountId();
        Long userId = account.getUserId();
        String password = account.getPassword();
        String nickName = account.getNickName();
        String avatar = account.getAvatar();
        String accountType = account.getAccountType();
        boolean accountNonLocked = account.getStatus().intValue() != BaseConstants.ACCOUNT_STATUS_LOCKED;
        boolean credentialsNonExpired = true;
        boolean enabled = account.getStatus().intValue() == BaseConstants.ACCOUNT_STATUS_NORMAL ? true : false;
        boolean accountNonExpired = true;
        if (account.getExpireTime() != null) {
            //帐号是否过期
            accountNonExpired = LocalDateTime.now().isBefore(account.getExpireTime());
        }
        PhenixUserDetails userDetails = new PhenixUserDetails();
        userDetails.setDomain(domain);
        userDetails.setAccountId(accountId);
        userDetails.setTenantId(account.getTenantId());
        userDetails.setUserId(userId);
        userDetails.setUserName(username);
        userDetails.setPassword(password);
        userDetails.setNickName(nickName);
        userDetails.setAuthorities(account.getAuthorities());
        userDetails.setRoles(account.getRoles());
        userDetails.setAvatar(avatar);
        userDetails.setAccountId(accountId);
        userDetails.setAccountNonLocked(accountNonLocked);
        userDetails.setAccountNonExpired(accountNonExpired);
        userDetails.setAccountType(accountType);
        userDetails.setCredentialsNonExpired(credentialsNonExpired);
        userDetails.setEnabled(enabled);
        userDetails.setClientId(adminProperties.getClientId());
        return userDetails;
    }
}
