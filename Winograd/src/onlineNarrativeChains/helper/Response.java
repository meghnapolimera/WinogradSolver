package onlineNarrativeChains.helper;

public class Response{
	ResponseData responseData;

	public Response(){}
	
	public Response(ResponseData responseData) {
		super();
		this.responseData = responseData;
	}

	public ResponseData getResponseData() {
		return responseData;
	}

	public void setResponseData(ResponseData responseData) {
		this.responseData = responseData;
	}
}