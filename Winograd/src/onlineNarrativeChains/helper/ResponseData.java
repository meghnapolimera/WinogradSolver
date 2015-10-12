package onlineNarrativeChains.helper;


public class ResponseData{
	GsearchResult results[];
	Cursor cursor;
	String responseDetails;
	int responseStatus;
	
	public ResponseData(){}
	
	public ResponseData(GsearchResult[] results, Cursor cursor,
			String responseDetails, int responseStatus) {
		super();
		this.results = results;
		this.cursor = cursor;
		this.responseDetails = responseDetails;
		this.responseStatus = responseStatus;
	}

	public GsearchResult[] getResults() {
		return results;
	}

	public void setResults(GsearchResult[] results) {
		this.results = results;
	}

	public Cursor getCursor() {
		return cursor;
	}

	public void setCursor(Cursor cursor) {
		this.cursor = cursor;
	}

	public String getResponseDetails() {
		return responseDetails;
	}

	public void setResponseDetails(String responseDetails) {
		this.responseDetails = responseDetails;
	}

	public int getResponseStatus() {
		return responseStatus;
	}

	public void setResponseStatus(int responseStatus) {
		this.responseStatus = responseStatus;
	}
}

