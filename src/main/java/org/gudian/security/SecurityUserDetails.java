package org.gudian.security;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author GJW
 * 自定义userDetails接口，为了能够获取到userId，该接口应被实现
 *  2020/12/28 18:01
 */
public interface SecurityUserDetails extends UserDetails {

    /**
     * 获取当前登陆人Id
     * */
    public Long getUserId();
}
