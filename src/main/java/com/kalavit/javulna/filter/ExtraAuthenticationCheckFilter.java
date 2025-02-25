/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kalavit.javulna.filter;

import com.kalavit.javulna.model.User;
import com.kalavit.javulna.springconfig.CustomAuthenticationSuccessHandler;
import com.kalavit.javulna.utils.SerializationUtil;
import java.io.IOException;
import java.util.Base64;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 *
 * @author peti
 */
@Component
public class ExtraAuthenticationCheckFilter implements Filter {

    private static final Logger LOG = LoggerFactory.getLogger(ExtraAuthenticationCheckFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

@Override
public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    if (request instanceof HttpServletRequest) {
        HttpServletRequest req = (HttpServletRequest) request;
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cooky : cookies) {
                if (cooky.getName().equals(CustomAuthenticationSuccessHandler.USER_AUTHENTICATION_EXTRA_SECURITY)) {
                    String value = cooky.getValue();
                    Object principalFromCookie = null;
                    try {
                        principalFromCookie = SerializationUtil.readUserFromFile(Base64.getDecoder().decode(value));
                    } catch (IOException | ClassNotFoundException ex) {
                        LOG.error("Error deserializing user from cookie: ", ex);
                    }
                    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                    if (principal instanceof User && principalFromCookie != null && !principal.equals(principalFromCookie)) {
                        LOG.error("Something is wrong. Principal in cookie is not good. Possible security failure!");
                    } else {
                        LOG.debug("The two principals are the same. Good.");
                    }
                }
            }
        }
    }
    chain.doFilter(request, response);
}

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }

}
