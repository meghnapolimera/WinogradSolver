package onlineNarrativeChains;
/**
 * Functionality to resolve pronouns using online narrative chain extraction
 * @author 	Arpit Sharma 
 * @version	Fall 2013
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.IllegalCharsetNameException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


import module.actualMethod.ActualMethodInterface;
import module.actualMethod.InputNode;
import module.commonUtils.Configuration;
import module.parsing.ParserInterface;
import module.parsing.Tuple;
import module.parsing.boxer.BoxerParser;
import module.parsing.stanford.StanfordParser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class OnlineNCModule{
	private String candidate1 = "";
	private String candidate2 = "";
	private ArrayList<String> canEvent = new ArrayList<String>();
	private ArrayList<String> proEvent = new ArrayList<String>();
	
	private ArrayList<String> proEventRole = new ArrayList<String>();
	
	private HashMap<String,String> posMap = new HashMap<String,String>();
	
	private String sentence = "";
	private String pronoun = "";
	
//	private boolean invalidLinkFlag = false;
	private ParserInterface NC_parser;
	

//	Parser outputs
	private static HashMap<String,String[]> parserMap = new HashMap<String,String[]>();
	
	
	public OnlineNCModule(){
		initParser();
	}
	
	//@Override
	public double[] getConfidence(InputNode input) {
		parserMap.clear();
		posMap.clear();
		candidate1 = "";
		candidate2 = "";
		pronoun = "";
		sentence = "";
		
		pronoun = input.getPronoun();
		parserMap = new HashMap<String,String[]>(input.getAgentPatientMap());
		ArrayList<String> POSTagdWords = input.getWordPOSList();
		
		for(String wordPos : POSTagdWords){
			String[] temp = wordPos.split("/");
			posMap.put(temp[0], temp[1]);
			sentence = sentence + " " + temp[0];
		}
		
		sentence = sentence.trim();
		
		candidate1 = input.getCandidateAnswers().get(0);
		candidate2 = input.getCandidateAnswers().get(1);
		
//		01/04/2014
//		///////
		ArrayList<String> finListOfSents = onlineModule();
//		ArrayList<String> finListOfSents = getListOfSentences();
//		///////
		double[] answer = getAnswer(finListOfSents);
		return answer;
	}

	//@Override
	public HashMap<String, Double> getFinalResult(double[] confidence) {
		HashMap<String,Double> finalResult = new HashMap<String,Double>();
		if(confidence[0] > confidence[1]){
			finalResult.put(candidate1,confidence[0]);
		}else if(confidence[0] < confidence[1]){
			finalResult.put(candidate2,confidence[1]);
		}else{
			finalResult.put("No Decision",0.0);
		}
		return finalResult;
	}
	
	
//	@SuppressWarnings("unused")
	private ArrayList<String> onlineModule() {
		canEvent.clear();
		proEvent.clear();
		proEventRole.clear();
		
		for (String event : parserMap.keySet()){
			if(pronoun.equalsIgnoreCase(getFromMap(parserMap,event)[0])){
				proEventRole.add(event+"-o");
				proEvent.add(event);
			}else if(pronoun.equalsIgnoreCase(getFromMap(parserMap,event)[1])){
				proEventRole.add(event+"-s");
				proEvent.add(event);
			}
			if(candidate1.equalsIgnoreCase(getFromMap(parserMap,event)[0]) || candidate1.equalsIgnoreCase(getFromMap(parserMap,event)[1])){
				canEvent.add(event);
			}
			if(candidate2.equalsIgnoreCase(getFromMap(parserMap,event)[0]) || candidate2.equalsIgnoreCase(getFromMap(parserMap,event)[1])){
				canEvent.add(event);
			}
		}
		

//		Remove duplicate events from canEvents list
		HashSet<String> set2 = new HashSet<String>(canEvent);
		canEvent.clear();
		canEvent.addAll(set2);
		
//		Remove duplicate events from proEvents list
		set2 = new HashSet<String>(proEvent);
		proEvent.clear();
		proEvent.addAll(set2);
		
//		Get search links from google in a list (links)
		ArrayList<String> links = new ArrayList<String>();
		links = GoogleAJAXSearchAPI.getSearchUrls(canEvent,proEvent);

//		Crawl the links and find all the sentences which have candidate events and pronoun events
//		in a list named "sentencesList"
		ArrayList<String> sentencesList = new ArrayList<String>();
		for(int i=0;i<links.size();++i){
			sentencesList.addAll(getTextFrmHTML(links.get(i)));
        }
		
		return sentencesList;
	}
	
	public static void main(String[] args){
		
		OnlineNCModule o = new OnlineNCModule();
		System.out.println(o.getTextFrmHTML("http://thehill.com/business-a-lobbying/lobbyist-profiles/306029-a-seat-at-the-table-"));
		
	}
	
	
	private ArrayList<String> getTextFrmHTML(String link){
		ArrayList<String> finalListOfSents = new ArrayList<String>();
		Document doc = null;
		try {
			ArrayList<String> listOfSentences = new ArrayList<String>();
			doc = Jsoup.connect(link).get();
			Elements p1 = doc.getAllElements();
			SentenceBoundaryDemo sbd = new SentenceBoundaryDemo();
			for (Element x: p1) {
				String temp = x.text();
				if(!temp.equalsIgnoreCase("")){
					if(temp.split(" ").length>10){
						temp = temp.replaceAll("[‘’\'\"+^:,|©»✖–!?()�]","");
						temp = temp.replaceAll("&", "and");
						temp = temp.replaceAll("-", " ");
						temp = temp.toLowerCase();
						if((stringContainsList(temp,canEvent)||stringContainsList(temp,simpleFormList(canEvent)))
								&& (stringContainsList(temp,proEvent)||stringContainsList(temp,simpleFormList(proEvent)))){
							ArrayList<String> tempListOfSentences = sbd.createSentences(temp);
							listOfSentences.addAll(tempListOfSentences);
						}
					}
				}
			}
			HashSet<String> set = new HashSet<String>(listOfSentences);
			listOfSentences.clear();
			listOfSentences.addAll(set);
			
			
			for(String s : listOfSentences){
				if((stringContainsList(s,canEvent)||stringContainsList(s,simpleFormList(canEvent)))
						&& (stringContainsList(s,proEvent)||stringContainsList(s,simpleFormList(proEvent)))){
					finalListOfSents.add(s);
				}
			}
			
		} catch (IOException | IllegalCharsetNameException e) {
//			invalidLinkFlag = true;
//			e.printStackTrace();
		}
		return finalListOfSents;
	}
	
	private double[] getAnswer(ArrayList<String> finListOfSentences){
		int candidate1Count = 0;
		int candidate2Count = 0;
		for(String s : finListOfSentences){
			
			if((stringContainsList(s,canEvent)||stringContainsList(s,simpleFormList(canEvent)))
					&& (stringContainsList(s,proEvent)||stringContainsList(s,simpleFormList(proEvent)))){
//				System.out.println("Sentence is:- "+s);
				HashMap<String,String[]> parsedOutput = new HashMap<String,String[]>();
//				System.out.println(s);
				Tuple tuple = NC_parser.callParser(s);
//				HashMap<HashMap<String,String[]>,ArrayList<String>> temp = tuple.getAgntPatMap();
//				for(HashMap<String,String[]> key : temp.keySet()){
				parsedOutput = tuple.getAgntPatMap();
//				}

				HashMap<String,String[]> parsedOutput1 = new HashMap<String,String[]>();
				parsedOutput1.putAll(parsedOutput);
				parsedOutput.clear();
				for(String key : parsedOutput1.keySet()){
					String[] tempStr = parsedOutput1.get(key);
					parsedOutput.put(simpleForm(key,"v"), tempStr);
				}
				
				if((mapContainsList(parsedOutput,canEvent)||mapContainsList(parsedOutput,simpleFormList(canEvent)))
						&& (mapContainsList(parsedOutput,proEvent)||mapContainsList(parsedOutput,simpleFormList(proEvent)))){
//					System.out.println("Sentence is:- "+s);
//					System.out.println("The sentence with both candidate and pronoun event is found");
					for(String proNounEvent : proEventRole){
						String flag = proNounEvent.split("-")[1];
						String event = proNounEvent.split("-")[0];
						String[] tempStr = parsedOutput.get(simpleForm(event,posMap.get(event)));
						String toSearch = "";
						if(flag.equalsIgnoreCase("o")){
							toSearch = tempStr[0];
						}else if(flag.equalsIgnoreCase("s")){
							toSearch = tempStr[1];
						}
//						System.out.println("String replacing pronoun :- "+toSearch);
						for(String candidateEvent : canEvent){
							String[] tempMap =  parsedOutput.get(simpleForm(candidateEvent,posMap.get(candidateEvent)));
//							System.out.println(tempMap[0]);
//							System.out.println(tempMap[1]);
							
							if(tempMap[0]!=null && tempMap[1]!=null){
								if(tempMap[0].equalsIgnoreCase(toSearch)){
									candidate1Count++;
								}else if(tempMap[1].equalsIgnoreCase(toSearch)){
									candidate2Count++;
								}
							}else if(tempMap[0]!=null && tempMap[1]==null){
								if(tempMap[0].equalsIgnoreCase(toSearch)){
									candidate1Count++;
								}
							}else if(tempMap[1]!=null && tempMap[0]==null){
								if(tempMap[1].equalsIgnoreCase(toSearch)){
									candidate2Count++;
								}
							}
						}
					}
				}
			}
		}
		double[] canCounts = new double[2];

		canCounts[0] = candidate1Count;
		canCounts[1] = candidate2Count;

		return canCounts;
	}
	
	
//	Method to check if a list of strings "list" is contained in a string s 
	private boolean stringContainsList(String s, ArrayList<String> list){
		boolean flag = true;
		for(String temp : list){
			if(!s.contains(temp)){
				flag = false;
				return flag;
			}
		}
		return flag;
	}
	
//	Method to check if a list of strings "list" is contained in a string s 
	private boolean mapContainsList(HashMap<String,String[]> map, List<String> list){
		boolean flag = true;
		Set<String> mapKeys = map.keySet();
		for(String temp : list){
			if(!mapKeys.contains(temp)){
				flag = false;
				return flag;
			}
		}
		return flag;
	}
	
//	Method to convert a string s1 to its simple form in case it is a verb
	private String simpleForm(String s1, String pos){
		pos = pos.trim();
		pos = pos.substring(0,1);
		String newString = "";
		String[] command = new String[3];
		command[0] = "/bin/bash";
		command[1] = "-c";
		command[2] = "echo '" + s1 + "_" + pos.toUpperCase() + "' | ./rasp3os/morph/morpha -f ./rasp3os/morph/verbstem.list";
		try{
			Process process = Runtime.getRuntime().exec(command);
			BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
			newString = input.readLine();
			newString = newString.split("_")[0].trim();
		}
		catch(Exception e){e.printStackTrace();}
		return newString;
	}
	
//	Method to convert a list of string to it simple verb forms 
	private ArrayList<String> simpleFormList(ArrayList<String> list){
		ArrayList<String> simpleFormList = new ArrayList<String>();
		for(String s : list){
			String pos = posMap.get(s);
			simpleFormList.add(simpleForm(s,pos));
		}
		return simpleFormList;
	}
	
	
	private void initParser() {
		String methodType = Configuration.defaultConfig.loadProperty(
				"NC_PARSER", "Stanford");
		if (methodType.equals("Stanford")) {
			this.NC_parser = new StanfordParser();
		} else if (methodType.equals("Boxer")) {
			this.NC_parser = new BoxerParser();
		}
	}
	
	private String[] getFromMap(HashMap<String,String[]> map, String key){
		String[] result = null;
		for(String s : map.keySet()){
			if(simpleForm(s,"v").equalsIgnoreCase(simpleForm(key,"v"))){
				result = map.get(key);
			}
		}
		return result;
	}
	
	@SuppressWarnings("unused")
	private ArrayList<String> getListOfSentences() {
		ArrayList<String> sentenceList = new ArrayList<String>();
		canEvent.clear();
		proEvent.clear();
		proEventRole.clear();
		
		for (String event : parserMap.keySet()){
			if(pronoun.equalsIgnoreCase(getFromMap(parserMap,event)[0])){
				proEventRole.add(event+"-o");
				proEvent.add(event);
			}else if(pronoun.equalsIgnoreCase(getFromMap(parserMap,event)[1])){
				proEventRole.add(event+"-s");
				proEvent.add(event);
			}
			if(candidate1.equalsIgnoreCase(getFromMap(parserMap,event)[0]) || candidate1.equalsIgnoreCase(getFromMap(parserMap,event)[1])){
				canEvent.add(event);
			}
			if(candidate2.equalsIgnoreCase(getFromMap(parserMap,event)[0]) || candidate2.equalsIgnoreCase(getFromMap(parserMap,event)[1])){
				canEvent.add(event);
			}
		}
		

//		Remove duplicate events from canEvents list
		HashSet<String> set2 = new HashSet<String>(canEvent);
		canEvent.clear();
		canEvent.addAll(set2);
		
//		Remove duplicate events from proEvents list
		set2 = new HashSet<String>(proEvent);
		proEvent.clear();
		proEvent.addAll(set2);
		
		try {
			BufferedReader sentFile = new BufferedReader(new FileReader("/host/stuff/WINOGRAD/MyWinograd/testCorpus/ForOnlineNC/sentences"));
			String line="";
			while((line=sentFile.readLine())!=null){
				if((stringContainsList(line,canEvent)||stringContainsList(line,simpleFormList(canEvent)))
						&& (stringContainsList(line,proEvent)||stringContainsList(line,simpleFormList(proEvent)))){
					sentenceList.add(line);
				}
			}
			sentFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sentenceList;
	}
}

