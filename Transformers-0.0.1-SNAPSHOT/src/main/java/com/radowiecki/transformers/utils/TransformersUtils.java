package com.radowiecki.transformers.utils;

import com.radowiecki.transformers.model.ScheduledTask;
import java.util.GregorianCalendar;
import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class TransformersUtils {
	private Logger logger = Logger.getLogger(TransformersUtils.class);

	public String prepareSimpleJSONResponse(String key, String value) {
		JSONObject jsonObject = new JSONObject();

		try {
			jsonObject.put(key, value);
		} catch (JSONException var5) {
			this.logger.error("Error with creating json response object. Method returns empty object.");
		}

		return jsonObject.toString();
	}

	public static long computeRemainingSecondsToWait(ScheduledTask scheduledTask) {
		GregorianCalendar gregorianCalendar = new GregorianCalendar();
		gregorianCalendar.setTime(scheduledTask.getExecutionDate());
		long secondsToWait = gregorianCalendar.getTimeInMillis() / 1000L
				- (new GregorianCalendar()).getTimeInMillis() / 1000L;
		return secondsToWait > 0L ? secondsToWait : 0L;
	}
}