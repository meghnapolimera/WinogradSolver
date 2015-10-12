package onlineNarrativeChains.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.IllegalCharsetNameException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class GetThesaurus {
	
	public static void main(String[] args){
		ArrayList<String> list = getThesaurus("lift","verb");
		for(String s : list){
			System.out.println(s);
		}
//		surfWeb();
		System.exit(0);
	}
	
	public static ArrayList<String> getThesaurus(String s, String pos){
		String location = "http://words.bighugelabs.com/api/2/fabf79cbc178d96cf56d8a3dbfe1111d/" + s +"/";
		ArrayList<String> synonyms = new ArrayList<String>();
		URL url;
		try {
			int counter = 0;
			url = new URL(location);
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			String line = (in.readLine());
			while(line != null){
				Pattern pattern = Pattern.compile("([a-z]+)(.)([a-z]+)(.)(.*)");
				Matcher m = pattern.matcher(line);
				while (m.find()) {
					String newStr1 = m.group(1);
					String newStr2 = m.group(3);
					String newStr3 = m.group(5);
					if(newStr1.startsWith(pos) && newStr2.equalsIgnoreCase("syn")){
//						System.out.println(line);
						line = newStr3;
						line = line.replace(" ", "_");
						line = line.replace("-", "_");
						line = line.replace("'", "");
						line = line.replace(".", "");
//						line = line.replace("24", "twentyfour");
						line = line.toLowerCase();
						synonyms.add(line);
						line = in.readLine();
						if(counter>9){
							break;
						}
						counter++;
					}else{
						line = in.readLine();
					}
				}
				if(counter>9){
					break;
				}
			}
		} catch (MalformedURLException e) {
			System.out.println("not found");
		} catch (IOException e) {
			System.out.println("not found");
		}
		return synonyms;
	}

	public static void surfWeb(){
		String location = "http://wikipedia.org";
//		URL url;
		Document doc;
		try{
		doc = Jsoup.connect(location).get();
		System.out.println(doc.toString());
//		System.out.println(doc.text());
		
//		Elements p1 = doc.getAllElements();
//		for (Element x: p1) {
//			String temp = x.text();
//			if(!temp.equalsIgnoreCase("")){
//				if(temp.split(" ").length>10){
//					temp = temp.replaceAll("[‘’\'\"+^:,|©»✖–!?()]","");
//					temp = temp.replaceAll("&", "and");
//					temp = temp.replaceAll("-", " ");
//					temp = temp.toLowerCase();
//					//					System.out.println(temp);
//				}
//			}
//		}
		} catch (IOException | IllegalCharsetNameException e) {
			System.out.println("Hello");
			e.printStackTrace();
		}
	}
}
	





