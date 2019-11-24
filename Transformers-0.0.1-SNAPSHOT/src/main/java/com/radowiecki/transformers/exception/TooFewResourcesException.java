package com.radowiecki.transformers.exception;

import com.radowiecki.transformers.constant.BuildingStatus;
import com.radowiecki.transformers.model.Resource;
import java.util.Iterator;
import java.util.Map;
import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class TooFewResourcesException extends Exception {
	private static final long serialVersionUID = -117171103424225093L;
	private Map<Resource, Boolean> enoughResources;
	private Logger logger = Logger.getLogger(TooFewResourcesException.class);

	public TooFewResourcesException() {
		super("Too few resources");
	}

	public TooFewResourcesException(String message) {
		super(message);
	}

	public Map<Resource, Boolean> getEnoughResources() {
		return this.enoughResources;
	}

	public void setEnoughResources(Map<Resource, Boolean> enoughResources) {
		this.enoughResources = enoughResources;
	}

	public String prepareJSONResponse() {
		JSONObject jsonObject = new JSONObject();

		try {
			jsonObject.put("STATUS", BuildingStatus.TO_FEW_RESOURCES.toString());
			Iterator var2 = this.getEnoughResources().keySet().iterator();

			while (var2.hasNext()) {
				Resource r = (Resource) var2.next();
				String key = String.valueOf(r.getResourceTypeId());
				String value = String.valueOf(this.getEnoughResources().get(r));
				jsonObject.put(key, value);
			}
		} catch (JSONException var6) {
			this.logger.error("Error with creating json response object. Method returns empty object.");
		}

		return jsonObject.toString();
	}
}