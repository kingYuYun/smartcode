package org.smart4j.plugin.security;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloShiro {
    private static final Logger logger = LoggerFactory.getLogger(HelloShiro.class);

    public static void main(String[] args) {
        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
        SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager((org.apache.shiro.mgt.SecurityManager) securityManager);

        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken("shiro", "20241201");
        try {
            subject.login(token);
            logger.info("登录成功, username: " + subject.getPrincipals());
        } catch (Exception e) {
            logger.info("登录失败");
        }
        subject.logout();
    }
}
