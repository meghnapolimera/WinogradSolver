package onlineNarrativeChains.helper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class GetSentencesFrmWSJ {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int count= 0;
		try {
//		BufferedWriter outputFile = new BufferedWriter(new FileWriter("/host/stuff/WINOGRAD/MyWinograd/testCorpus/ForOnlineNC/sentences"));
		for(int i=1;i<200;++i){
			
				
				BufferedReader inputFile = new BufferedReader(new FileReader("/home/arpit/nltk_data/corpora/treebank/raw/wsj_"+convertNumber(i)));
				String line = "";
				inputFile.readLine();
				while((line=inputFile.readLine())!=null){
					if(!line.equalsIgnoreCase("\n") && !line.equalsIgnoreCase("")){
						count++;
//						System.out.println(line);
//						outputFile.write(line);
//						outputFile.newLine();
					}
				}
				inputFile.close();
			
		}
//		outputFile.close();
		System.out.println(count);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static String convertNumber(int i){
		String result="0000";
		if(i/10 == 0){
			result = "000"+i;
		}else if(i/10>0 && i/10<10){
			result = "00"+i;
		}else{
			result = "0"+i;
		}
		return result;
	}

}
