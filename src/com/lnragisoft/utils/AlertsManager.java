package com.lnragisoft.utils;

import com.alee.extended.panel.GroupPanel;
import com.alee.extended.time.WebClock;
import com.alee.managers.notification.NotificationIcon;
import com.alee.managers.notification.NotificationManager;
import com.alee.managers.notification.WebNotification;
import com.alee.managers.style.StyleId;

public class AlertsManager {
	final static WebNotification<?> alert = new WebNotification<>(StyleId.notification);
	final static WebClock clock = new WebClock();
	
	public static void info(String message) {
		buildAlert(message);
		alert.setIcon(NotificationIcon.information);
	}

	public static void error(String message) {
		buildAlert(message);
		alert.setIcon(NotificationIcon.error);
	}

	public static void warning(String message) {
		buildAlert(message);
		alert.setIcon(NotificationIcon.warning);
	}

	public static void success(String message) {
		buildAlert(message);
		alert.setIcon(Icons.success);
	}

	private static void buildAlert(String message) {
		alert.setDisplayTime(3000);
		alert.setPreferredSize(300,140);
		clock.setTimeLeft(3000);
		clock.setTimePattern("'" + message + "'");
		alert.setContent(new GroupPanel(clock));
		NotificationManager.showNotification(alert);
		clock.start();
	}
}
