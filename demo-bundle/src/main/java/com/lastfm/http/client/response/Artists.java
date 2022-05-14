package com.lastfm.http.client.response;

import java.util.ArrayList;
import java.util.List;

public class Artists {
	private List<Artist> artist = new ArrayList<Artist>();
	private Attr attr;

	public List<Artist> getArtist() {
		return artist;
	}

	public void setArtist(List<Artist> artist) {
		this.artist = artist;
	}

	public Attr getAttr() {
		return attr;
	}

	public void setAttr(Attr attr) {
		this.attr = attr;
	}
}
