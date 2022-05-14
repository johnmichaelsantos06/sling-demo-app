package com.lastfm.http.client;

import java.io.InputStream;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lastfm.http.client.response.LastFMAPIArtistResponse;

public class LastFMHTTPClient {
	
    private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public LastFMAPIArtistResponse getArtists(Integer limit) {
		LastFMAPIArtistResponse response = new LastFMAPIArtistResponse();
		
		try {
			HttpClient httpclient = HttpClients.createDefault();
			URIBuilder builder = new URIBuilder("https://ws.audioscrobbler.com/2.0/");
			builder
				.setParameter("method", "chart.gettopartists")
				.setParameter("api_key", "3d9b6353b41e19f768282cc30c06a2e0")
				.setParameter("format", "json")
				.setParameter("limit", limit.toString());
			HttpGet httpGet = new HttpGet(builder.build());
			HttpResponse httpResponse = httpclient.execute(httpGet);
			
			ObjectMapper objMapper = new ObjectMapper();
			objMapper.setSerializationInclusion(Include.NON_NULL);
			objMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

			InputStream is = httpResponse.getEntity().getContent();
			response = objMapper.readValue(is, LastFMAPIArtistResponse.class);
		} catch (URISyntaxException e) {
			logger.error(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		
		return response;
	}
}
