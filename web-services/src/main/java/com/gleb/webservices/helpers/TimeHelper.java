package com.gleb.webservices.helpers;

import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class TimeHelper {

	public Date getCurrentTime() {
		return new Date();
	}

	public Date getTimeFromNow(int daysAdd, int hoursAdd, int minutesAdd) {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, daysAdd);
		c.add(Calendar.HOUR, hoursAdd);
		c.add(Calendar.MINUTE, minutesAdd);
		return c.getTime();
	}
	
	public Date increaseDAte(Date date, int daysAdd, int hoursAdd, int minutesAdd) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, daysAdd);
		c.add(Calendar.HOUR, hoursAdd);
		c.add(Calendar.MINUTE, minutesAdd);
		return c.getTime();
	}
}
