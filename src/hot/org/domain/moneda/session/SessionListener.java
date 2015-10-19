package org.domain.moneda.session;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.jboss.logging.Logger;

public class SessionListener implements HttpSessionListener, java.io.Serializable{
	
    private static final Logger log = Logger.getLogger(SessionListener.class);
    public List< HttpSession> listsession = new ArrayList<HttpSession>();
    
    
    
    public void sessionCreated(HttpSessionEvent event) {
        listsession.add(event.getSession());
    }

    public void sessionDestroyed(HttpSessionEvent event) {
        listsession.remove(event.getSession());
    }
}