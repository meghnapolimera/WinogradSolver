package onlineNarrativeChains;

/**
 * @author Arpit Sharma
 * @version	Fall 2013
 */
import java.util.ArrayList;

import onlineNarrativeChains.helper.Googler;
import onlineNarrativeChains.helper.GsearchResult;
import onlineNarrativeChains.helper.Response;
import onlineNarrativeChains.helper.Googler.SearchError;



import com.google.gson.Gson;

public class GoogleAJAXSearchAPI {

	private static String searchString = "\"%20*%20yell%20*%20because%20*%20convince%20*.\"";

	public static void main_old(String[] args) {
		ArrayList<String> can = new ArrayList<String>();
		ArrayList<String> pro = new ArrayList<String>();
		can.add("shot");
		pro.add("died");
		ArrayList<String> links = new ArrayList<String>();
		links = getSearchUrls(can, pro);

		System.out.println(links.size());

		System.exit(0);
	}

	public static void main(String[] args) throws InterruptedException{

		String result = null;
		int current = 0;
		int max = 30;
		Gson gson = new Gson();

		try {
			do
			{
				//call main search method: specify what to search and start result
				result = Googler.searchWeb(searchString, current);
				//    			Thread.sleep(2000);
				Response response = gson.fromJson(result, Response.class);

				if(response.getResponseData()!=null){
					for (GsearchResult gsearchResult : response.getResponseData().getResults()) {
						System.out.println("Result url #" + current + ":" +gsearchResult.getUrl());
						current++;
					}
				}else{
					System.out.println("HERE");
					current++;
				}
			}
			while(current < max);
		} catch (SearchError e) {
			e.printStackTrace();
		}
		System.exit(0);
	}


	public static ArrayList<String> getSearchUrls(ArrayList<String> canEvent, ArrayList<String> proEvent){
		ArrayList<String> urlArray = new ArrayList<String>();
		String tempCanEvent = "";
		for(int i=0;i<canEvent.size();++i){
			if(i==1){
				tempCanEvent+=canEvent.get(i);
			}else{
				tempCanEvent+="%20"+canEvent.get(i);
			}
		}

		String tempProEvent = "";
		for(int i=0;i<proEvent.size();++i){
			if(i==1){
				tempProEvent+=proEvent.get(i);
			}else{
				tempProEvent+="%20"+proEvent.get(i);
			}
		}

		searchString = tempCanEvent+"%20"+tempProEvent;
		String result = null;
		int current = 0;
		int max = 30;
		Gson gson = new Gson();

		try {
			do
			{
				//call main search method: specify what to search and start result
				result = Googler.searchWeb(searchString, current);
				//    			Thread.sleep(2000);
				Response response = gson.fromJson(result, Response.class);

				if(response.getResponseData()!=null){
					for (GsearchResult gsearchResult : response.getResponseData().getResults()) {
						//    	          	System.out.println("Result url #" + current + ":" +gsearchResult.getUrl());
						urlArray.add(gsearchResult.getUrl());
						current++;
					}
				}else{
					current++;
				}
			}
			while(current < max);
		} catch (SearchError e) {
			e.printStackTrace();
		}
		return urlArray;
	}

}
