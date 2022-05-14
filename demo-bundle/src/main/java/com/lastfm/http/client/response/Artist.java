package com.lastfm.http.client.response;

import java.util.ArrayList;
import java.util.List;

public class Artist {
	private String name;
	private String playcount;
	private String listeners;
	private String mbid;
	private String url;
	private String streamable;
	private List<Image> image = new ArrayList<Image>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPlaycount() {
		return playcount;
	}

	public void setPlaycount(String playcount) {
		this.playcount = playcount;
	}

	public String getListeners() {
		return listeners;
	}

	public void setListeners(String listeners) {
		this.listeners = listeners;
	}

	public String getMbid() {
		return mbid;
	}

	public void setMbid(String mbid) {
		this.mbid = mbid;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getStreamable() {
		return streamable;
	}

	public void setStreamable(String streamable) {
		this.streamable = streamable;
	}

	public List<Image> getImage() {
		return image;
	}

	public void setImage(List<Image> image) {
		this.image = image;
	}
}
