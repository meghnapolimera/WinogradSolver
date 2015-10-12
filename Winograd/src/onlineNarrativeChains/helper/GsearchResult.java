package onlineNarrativeChains.helper;

public class GsearchResult{
	String GsearchResultClass;
	String unescapedUrl;
	String url;
	String visibleUrl;
	String title;
	String titleNoFormatting;
	String content;
	String cacheUrl;
	
	public GsearchResult(){}
	
	public GsearchResult(String gsearchResultClass, String unescapedUrl,
			String url, String visibleUrl, String title,
			String titleNoFormatting, String content, String cacheUrl) {
		super();
		GsearchResultClass = gsearchResultClass;
		this.unescapedUrl = unescapedUrl;
		this.url = url;
		this.visibleUrl = visibleUrl;
		this.title = title;
		this.titleNoFormatting = titleNoFormatting;
		this.content = content;
		this.cacheUrl = cacheUrl;
	}

	public String toString(){
		return unescapedUrl + url;
	}

	public String getGsearchResultClass() {
		return GsearchResultClass;
	}

	public void setGsearchResultClass(String gsearchResultClass) {
		GsearchResultClass = gsearchResultClass;
	}

	public String getUnescapedUrl() {
		return unescapedUrl;
	}

	public void setUnescapedUrl(String unescapedUrl) {
		this.unescapedUrl = unescapedUrl;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getVisibleUrl() {
		return visibleUrl;
	}

	public void setVisibleUrl(String visibleUrl) {
		this.visibleUrl = visibleUrl;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitleNoFormatting() {
		return titleNoFormatting;
	}

	public void setTitleNoFormatting(String titleNoFormatting) {
		this.titleNoFormatting = titleNoFormatting;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCacheUrl() {
		return cacheUrl;
	}

	public void setCacheUrl(String cacheUrl) {
		this.cacheUrl = cacheUrl;
	}
}
