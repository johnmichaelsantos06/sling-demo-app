package com.demo.models;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;

@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class DemoModel {
	
	@Self
    private SlingHttpServletRequest request;
	
	public boolean getRefreshButtonDisplayed() {
		boolean isDisplayed = false;
		
		Resource resource = request.getResource();
		String parentName = resource.getParent().getName();
		
		String dateNow = getFormattedDate(new Date());
		if (parentName.equals(dateNow)) {
			isDisplayed = true;
		}
		
		return isDisplayed;
	}
	
	private String getFormattedDate(Date date) {
		return new SimpleDateFormat("yyyyMMdd").format(date);
	}
}
