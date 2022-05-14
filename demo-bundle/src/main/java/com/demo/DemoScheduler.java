package com.demo;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lastfm.http.client.LastFMHTTPClient;
import com.lastfm.http.client.response.Artist;
import com.lastfm.http.client.response.LastFMAPIArtistResponse;

@Component
@Service
@Property( name="scheduler.period", longValue = 10)
public class DemoScheduler implements Runnable {
    
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Reference
    private ResourceResolverFactory resourceResolverFactory;
    
    public void run() {
    	Map<String, Object> param = new HashMap<String, Object>();
        param.put(ResourceResolverFactory.SUBSERVICE, "writeservice");
        ResourceResolver resourceResolver = null;
    	try {
    		resourceResolver = resourceResolverFactory.getServiceResourceResolver(param);
    		Resource content = resourceResolver.getResource("/content");
    		
    		Resource topArtistsResource = resourceResolver.getResource("/content/topartists");
    		if (topArtistsResource == null) {
    			Map<String, Object> parameters = new HashMap<String, Object>();
        		parameters.put("jcr:primaryType", "nt:unstructured");
        		parameters.put("sling:resourceType", "components/demo");
    			resourceResolver.create(content, "topartists", parameters);
    		} else {
    			LastFMHTTPClient client = new LastFMHTTPClient();
    			LastFMAPIArtistResponse response = client.getArtists(100);
    			if (response != null && response.getArtists() != null && response.getArtists().getArtist() != null) {
    				List<Artist> list = response.getArtists().getArtist();
    				
    				Integer i = 1;
    				for (Artist artistObj : list) {
    					if (artistObj != null) {
    						Resource artistResource = resourceResolver.getResource("/content/topartists/" + i);
    						if (artistResource == null) {
    							Map<String, Object> parameters = new HashMap<String, Object>();
    			        		parameters.put("jcr:primaryType", "nt:unstructured");
    			        		parameters.put("artistName", artistObj.getName());
    			        		parameters.put("playCount", artistObj.getPlaycount());
    			        		parameters.put("listeners", artistObj.getListeners());
    			        		parameters.put("url", artistObj.getUrl());
    			    			resourceResolver.create(topArtistsResource, i.toString(), parameters);
    						}
    						
    						i++;
    					}
					}
    			}
    		}
    		
    		resourceResolver.commit();
		} catch (LoginException e) {
			logger.error(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
    }

}

