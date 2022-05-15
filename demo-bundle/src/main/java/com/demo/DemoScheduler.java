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
    		Resource topArtistsResource = resourceResolver.getResource("/content/topartists");
    		Resource content = resourceResolver.getResource("/content");
    		
    		if (topArtistsResource == null) {
    			Map<String, Object> topArtistsResourceParams = new HashMap<String, Object>();
        		topArtistsResourceParams.put("jcr:primaryType", "nt:unstructured");
        		topArtistsResourceParams.put("sling:resourceType", "components/demo");
    			resourceResolver.create(content, "topartists", topArtistsResourceParams);
    			
    			resourceResolver.commit();
    			
    			topArtistsResource = resourceResolver.getResource("/content/topartists");
    			
    			LastFMHTTPClient client = new LastFMHTTPClient();
    			LastFMAPIArtistResponse response = client.getArtists(100);
    			if (response != null && response.getArtists() != null && response.getArtists().getArtist() != null) {
    				List<Artist> list = response.getArtists().getArtist();
    				
    				Integer i = 1;
    				for (Artist artistObj : list) {
    					if (artistObj != null) {
    						Resource artistResource = resourceResolver.getResource("/content/topartists/" + i);
    						if (artistResource == null) {
    							Map<String, Object> artistResourceParams = new HashMap<String, Object>();
    			        		artistResourceParams.put("jcr:primaryType", "nt:unstructured");
    			        		artistResourceParams.put("artistName", artistObj.getName());
    			        		artistResourceParams.put("playCount", artistObj.getPlaycount());
    			        		artistResourceParams.put("listeners", artistObj.getListeners());
    			        		artistResourceParams.put("url", artistObj.getUrl());
    			    			resourceResolver.create(topArtistsResource, i.toString(), artistResourceParams);
    			    			
    			    			resourceResolver.commit();
    						}
    						
    						i++;
    					}
					}
    			}
    		} else {
    			return;
    		}
		} catch (LoginException e) {
			logger.error(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
    }

}

