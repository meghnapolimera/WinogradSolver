package onlineNarrativeChains.helper;

public class Cursor{
	Page[] pages;
	String estimatedResultCount;
	int currentPageIndex;
	String moreResultsUrl;
	
	public Cursor(){}
			
	public Cursor(Page[] pages, String estimatedResultCount,
			int currentPageIndex, String moreResultsUrl) {
		super();
		this.pages = pages;
		this.estimatedResultCount = estimatedResultCount;
		this.currentPageIndex = currentPageIndex;
		this.moreResultsUrl = moreResultsUrl;
	}

	public Page[] getPages() {
		return pages;
	}

	public void setPages(Page[] pages) {
		this.pages = pages;
	}

	public String getEstimatedResultCount() {
		return estimatedResultCount;
	}

	public void setEstimatedResultCount(String estimatedResultCount) {
		this.estimatedResultCount = estimatedResultCount;
	}

	public int getCurrentPageIndex() {
		return currentPageIndex;
	}

	public void setCurrentPageIndex(int currentPageIndex) {
		this.currentPageIndex = currentPageIndex;
	}

	public String getMoreResultsUrl() {
		return moreResultsUrl;
	}

	public void setMoreResultsUrl(String moreResultsUrl) {
		this.moreResultsUrl = moreResultsUrl;
	}
}
