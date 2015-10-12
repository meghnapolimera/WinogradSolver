package onlineNarrativeChains.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;


public class Googler {
	
	public final static String WEB_URL    = "http://ajax.googleapis.com/ajax/services/search/web";
	public final static String VIDEO_URL  = "http://ajax.googleapis.com/ajax/services/search/video";
	public final static String NEWS_URL   = "http://ajax.googleapis.com/ajax/services/search/news";
	public final static String LOCAL_URL  = "http://ajax.googleapis.com/ajax/services/search/local";
	public final static String IMAGES_URL = "http://ajax.googleapis.com/ajax/services/search/images";
	public final static String BOOKS_URL  = "http://ajax.googleapis.com/ajax/services/search/books";
	public final static String BLOGS_URL  = "http://ajax.googleapis.com/ajax/services/search/blogs";
	
	public final static String VERSION_KEY  = "v";
	public final static String QUERY_KEY  = "q";
	public final static String START_KEY  = "start";
	
	public static int ONE_REQUEST_RESULT_COUNT = 4;
	
	
	public enum SearchArea {WEB , VIDEO, NEWS, LOCAL, IMAGES, BOOKS, BLOGS};
	
	static String getUrlForSearchArea(SearchArea area){
		switch (area) {
		case WEB:
			return WEB_URL;
		case VIDEO:
			return VIDEO_URL;
		case NEWS:
			return NEWS_URL;
		case LOCAL:
			return LOCAL_URL;
		case IMAGES:
			return IMAGES_URL;
		case BOOKS:
			return BOOKS_URL;
		case BLOGS:
			return BLOGS_URL;
		default:
			return null;
		}
	}
	
	private static String formParam(String key, String value){return "&" + key + "=" + value;}
	private static String formFirstParam(String key, String value){return "?" + key + "=" + value;}
	
	public static String search(String query, SearchArea area, int start) throws SearchError{
		HttpURLConnection connection = null;
		try {
			
			//form url
			StringBuffer url = new StringBuffer();
			url.append(getUrlForSearchArea(area));
			url.append(formFirstParam(VERSION_KEY, "1.0"));
			url.append(formParam(QUERY_KEY, query));
			url.append(formParam(START_KEY, new Integer(start).toString()));
			
			URLConnection uc = new URL(url.toString()).openConnection();
        	connection = (HttpURLConnection) uc;
        	connection.setDoOutput(true);
			connection.setRequestMethod("GET");
			connection.connect();
		} catch (Exception e1) {
			e1.printStackTrace();
			throw new SearchError();
		}

 
		String result = null;
        InputStream inputStream = null;
        try {
            inputStream = connection.getInputStream();
        } catch (IOException e) {
            inputStream = connection.getErrorStream();
        }
        BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream));
        try {
			result = rd.readLine();
		} catch (IOException e) {
			e.printStackTrace();
			throw new SearchError();
		}
		if (result == null || result.length() == 0)
			throw new SearchError();
        return result;
	}
	
	public static String searchWeb(String query, int start) throws SearchError{
		return search(query, SearchArea.WEB, start);
		}
	
	@SuppressWarnings("serial")
	public static class SearchError extends Exception {
		private final static String DEFAULT_MSG = "Some error occured while searching in Google";
		
		public SearchError(){
			super(DEFAULT_MSG);
		}
		
		public SearchError(String msg){
			super(msg);
		}
	}

}
