/**
 * @author Arpit Sharma
 * @version	Fall 2013
 */
package onlineNarrativeChains;

import com.aliasi.sentences.MedlineSentenceModel;
import com.aliasi.sentences.SentenceModel;
import com.aliasi.tokenizer.IndoEuropeanTokenizerFactory;
import com.aliasi.tokenizer.TokenizerFactory;
import com.aliasi.tokenizer.Tokenizer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/** Use SentenceModel to find sentence boundaries in text */
public class SentenceBoundaryDemo {

	static final TokenizerFactory TOKENIZER_FACTORY = IndoEuropeanTokenizerFactory.INSTANCE;
	static final SentenceModel SENTENCE_MODEL  = new MedlineSentenceModel();

	public static void main(String[] args) throws IOException {
		String text = "";
		SentenceBoundaryDemo sbd = new SentenceBoundaryDemo();
		ArrayList<String> test = sbd.createSentences(text);
		System.out.println(test.size());
		System.exit(0);
	}

	public ArrayList<String> createSentences(String text){

		ArrayList<String> sents = new ArrayList<String>();
		List<String> tokenList = new ArrayList<String>();
		List<String> whiteList = new ArrayList<String>();
		Tokenizer tokenizer = TOKENIZER_FACTORY.tokenizer(text.toCharArray(),0,text.length());
		tokenizer.tokenize(tokenList,whiteList);

		String[] tokens = new String[tokenList.size()];
		String[] whites = new String[whiteList.size()];
		tokenList.toArray(tokens);
		whiteList.toArray(whites);
		int[] sentenceBoundaries = SENTENCE_MODEL.boundaryIndices(tokens,whites);
		if (sentenceBoundaries.length > 0) {
			int sentStartTok = 0;
			int sentEndTok = 0;
			for (int i = 0; i < sentenceBoundaries.length; ++i) {
				sentEndTok = sentenceBoundaries[i];
				StringBuffer sb = new StringBuffer();
				int counter = 0;
				for (int j=sentStartTok; j<=sentEndTok; j++) {
					String temp = tokens[j]+whites[j+1];
					counter++;
					sb.append(temp);
					if(counter>25){
						break;
					}
				}
				String sentNew = sb.toString();
				if(sentNew.split(" ").length > 4){
					sents.add(sentNew);
				}
				sentStartTok = sentEndTok+1;
			}
		}
		return sents;
	}
}
