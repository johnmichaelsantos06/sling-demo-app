package com.lastfm.http.client.response;

public class Attr {
    private String page;
    private String perPage;
    private String totalPages;
    private String total;
    public String getPage() {
        return page;
    }
    public void setPage(String page) {
        this.page = page;
	}

	public String getPerPage() {
        return perPage;
    }
    public void setPerPage(String perPage) {
        this.perPage = perPage;
    }
    public String getTotalPages() {
        return totalPages;
    }
    public void setTotalPages(String totalPages) {
        this.totalPages = totalPages;
    }
    public String getTotal() {
        return total;
    }
    public void setTotal(String total) {
        this.total = total;
    }
}
