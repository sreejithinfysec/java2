/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kalavit.javulna.services;

import com.kalavit.javulna.model.User;
import com.kalavit.javulna.services.autodao.UserAutoDao;
import java.io.StringReader;
import javax.transaction.Transactional;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

/**
 *
 * @author peti
 */
@Service
public class RemotePasswordChangeService {
    
    private static final Logger LOG = LoggerFactory.getLogger(RemotePasswordChangeService.class);
    
    @Autowired
    private UserAutoDao uDao;
    
@Transactional
public boolean changePassword(String psChangeXml) {
    try {
        SAXBuilder sb = new SAXBuilder();
        Document doc = sb.build(new StringReader(psChangeXml));
        String userName = doc.getRootElement().getChildText("userName");
        String pwd = doc.getRootElement().getChildText("pwd");
        LOG.debug("Will change the password of user: {} to {}", userName, pwd);
        User u = uDao.findUserByName(userName);
        if (u != null) {
            u.setPassword(pwd);
            return true;
        }
        return false;
    } catch (Exception ex) {
        throw new RuntimeException(ex);
    } 
}

        
    }
    
}
