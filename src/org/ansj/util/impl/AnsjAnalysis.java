package org.ansj.util.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.ansj.dic.DicReader;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.ansj.util.Analysis;
import org.ansj.util.recognition.NatureRecognition;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

public class AnsjAnalysis implements Analysis {

	public static final Analysis DEFAUlT = new AnsjAnalysis(true);

	@SuppressWarnings(value = { "all" })
	private HashSet<String> filter = null;

	/**
	 * 停用词词典
	 * 
	 * @param filter
	 */
	public AnsjAnalysis(HashSet<String> filter) {
		this.filter = filter;
	}

	/**
	 * 是否需要过滤停用词
	 * 
	 * @param needFilter
	 */
	public AnsjAnalysis(boolean needFilter) {
		if (needFilter) {
			filter = initSystemFilter();
		}
	}

	private HashSet<String> initSystemFilter() {
		// TODO Auto-generated method stub
		HashSet<String> hs = new HashSet<String>();
		BufferedReader reader = null;
        try {
            reader = Files.newReader(new File("library/stopwords.dic"), Charsets.UTF_8);
        } catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
//		BufferedReader reader = DicReader.getReader("library/newWordFilter.dic");
		String temp = null;

		try {
			while ((temp = reader.readLine()) != null) {
				hs.add(temp);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return hs;
	}

	@Override
	public List<String> getWords(Reader reader) throws IOException {
		// TODO Auto-generated method stub
		BufferedReader br = null ;
		try {
			br = new BufferedReader(reader);
			String temp = null;
			List<String> all = new ArrayList<String>();
			while ((temp = br.readLine()) != null) {
			    System.out.println("原句："+temp);
//				List<Term> paser = ToAnalysis.paser(temp);
				List<Term> paser = NlpAnalysis.paser(temp);
				new NatureRecognition(paser).recognition();
				System.out.println("分词及词性标注："+paser);
				for (Term term : paser) {
					if (!filter(term)) {
						all.add(term.getName());
					}
				}
				System.out.println("过滤分词："+all);
			}
			return all;
		} finally {
			if(br!=null)
				br.close() ;
		}
	}

	/**
	 * 第一层词性过滤
	 * 
	 * @param term
	 * @return
	 */
	public boolean filter(Term term) {
		if (filter == null) {
			return true;
		}
//		if (term.getName().length() == 1) {
//            return true;
//        }
		String natureStr = term.getNatrue().natureStr;
		if (natureStr == null || "w".equals(natureStr) || "m".equals(natureStr) ||  "p".equals(natureStr)  ||  "c".equals(natureStr)  ||  "en".equals(natureStr)  ||  "d".equals(natureStr)  ||  "r".equals(natureStr)) {
			return true;
		}
		return filter(term.getName());
	}

	@Override
	public boolean filter(String word) {
		return filter.contains(word);
	}
}
