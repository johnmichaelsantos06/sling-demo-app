package com.demo.servlet;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ResourceUtil;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lastfm.http.client.LastFMHTTPClient;
import com.lastfm.http.client.response.Artist;
import com.lastfm.http.client.response.LastFMAPIArtistResponse;

@SlingServlet(metatype = false, resourceTypes = { "components/demo/servlet" }, methods = "GET")
public class DemoServlet extends SlingSafeMethodsServlet {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private static final long serialVersionUID = 1L;

	@Reference
    private ResourceResolverFactory resourceResolverFactory;
	
	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
		Map<String, Object> param = new HashMap<String, Object>();
        param.put(ResourceResolverFactory.SUBSERVICE, "writeservice");
        ResourceResolver resourceResolver = null;
    	try {
    		resourceResolver = resourceResolverFactory.getServiceResourceResolver(param);
			
    		String formattedDate = getFormattedDate(new Date());
			String rootPath = "/content/demo/";
			
			Resource dateResource = resourceResolver.getResource(rootPath + formattedDate);

			if (dateResource != null) {
				resourceResolver.delete(dateResource);
			}
			
    		Map<String, Object> dateResourceParams = new HashMap<String, Object>();
    		dateResourceParams.put("jcr:primaryType", "nt:unstructured");
    		ResourceUtil.getOrCreateResource(resourceResolver, rootPath + formattedDate, dateResourceParams, null, true);
    		
    		Map<String, Object> topArtistsResourceParams = new HashMap<String, Object>();
    		topArtistsResourceParams.put("jcr:primaryType", "nt:unstructured");
    		topArtistsResourceParams.put("sling:resourceType", "components/demo");
    		ResourceUtil.getOrCreateResource(resourceResolver, rootPath + formattedDate + "/topartists", topArtistsResourceParams, null, true);
    		
    		LastFMHTTPClient client = new LastFMHTTPClient();
			LastFMAPIArtistResponse lastFMApiResponse = client.getArtists(100);
			if (lastFMApiResponse != null && lastFMApiResponse.getArtists() != null && lastFMApiResponse.getArtists().getArtist() != null) {
				List<Artist> list = lastFMApiResponse.getArtists().getArtist();
				
				Integer i = 1;
				for (Artist artistObj : list) {
					if (artistObj != null) {
						Map<String, Object> artistResourceParams = new HashMap<String, Object>();
						artistResourceParams.put("jcr:primaryType", "nt:unstructured");
						artistResourceParams.put("artistName", artistObj.getName());
						artistResourceParams.put("playCount", artistObj.getPlaycount());
						artistResourceParams.put("listeners", artistObj.getListeners());
						artistResourceParams.put("url", artistObj.getUrl());
						artistResourceParams.put("mbid", artistObj.getMbid());
						
						ResourceUtil.getOrCreateResource(resourceResolver, rootPath + formattedDate + "/topartists/" + i, artistResourceParams, null, true);
						
						i++;
					}
				}
    		}
			
			response.sendRedirect(rootPath + formattedDate + "/topartists.html");
		} catch (LoginException e) {
			logger.error(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			resourceResolver.close();
		}
	}
	
    private String getFormattedDate(Date date) {
    	return new SimpleDateFormat("yyyyMMdd").format(date);
    }
}