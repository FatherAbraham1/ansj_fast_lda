import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

import org.ansj.lda.LDA;
import org.ansj.lda.impl.LDAGibbsModel;
import org.ansj.util.impl.AnsjAnalysis;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

public class Test3 {

	public static void main(String[] args) throws IOException {
	    int topicNum = 100;
	    double alpha = 50/(double)topicNum;
	    double beta = 0.01;
	    int iteration = 100;
		LDA lda = new LDA(AnsjAnalysis.DEFAUlT,new LDAGibbsModel(topicNum, alpha, beta, iteration, Integer.MAX_VALUE, Integer.MAX_VALUE));
		BufferedReader newReader = Files.newReader(new File("test_data/data3.txt"), Charsets.UTF_8);
		String temp =null ;
		int i = 0 ;
		while((temp=newReader.readLine())!=null){
			lda.addDoc(String.valueOf(++i),temp) ;
//			if(i>1000){
//				break ;
//			}
		}

		lda.trainAndSave("result3/topicNum_"+topicNum+"_iteration_"+iteration, "utf-8") ;
	}
}
