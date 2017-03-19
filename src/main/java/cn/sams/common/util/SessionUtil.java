package cn.sams.common.util;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class SessionUtil implements HttpSessionListener {

	@Override
	public void sessionCreated(HttpSessionEvent event) {
		System.out.println("session created --- " + DateUtil.getDateString() );
		
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
		System.out.println("session destroyed --- " + DateUtil.getDateString());
		
	}

}
