package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import lpsolve.LpSolve;
import lpsolve.LpSolveException;


import org.apache.commons.io.FileUtils;
public class Readfile {
	String currword ;
	String currpos;
	String headword;
	String headpos;
	String edgetag;
	String sentnum;
	double depth = 0;
	double count;
	double doccount;
	double inf = Double.POSITIVE_INFINITY;

	HashMap<String,ArrayList<HashMap<String,Headvalue>>> doc_sent_map = new HashMap<String,ArrayList<HashMap<String,Headvalue>>>();
	HashMap<String,Headvalue> sent_map = new HashMap<String,Headvalue>();
	ArrayList<HashMap<String,Headvalue>> sent_list ; //= new ArrayList<HashMap<String,Headvalue>>();
	ArrayList<String> words_list = new ArrayList<String>();
	//HashMap<String,HashMap<String,Double>> doc_word_freq_map = new HashMap<String,HashMap<String,Double>>();
	//HashMap<String,Double> word_freq_map = new HashMap<String,Double>();
	HashMap<String,HashMap<String,Headvalue>> doc_word_freq_map = new HashMap<String,HashMap<String,Headvalue>>();
	HashMap<String,Headvalue> word_freq_map ;
	HashMap<String,Double> doc_freq = new HashMap<String,Double>();
	
	ArrayList<Sentence> final_sentlist ; //= new ArrayList<Sentence>();
	
	int iter;
	
	
	
	public void readFile() throws IOException{
		String key="";
		File folder = new File("C:\\Users\\Vijayalakshmi\\Desktop\\nlp\\Assign4\\data1");
		File[] listOfFiles = folder.listFiles();
		
		
		for (int i = 0; i < listOfFiles.length; i++) {
 		  String docnum = "doc" + (i+1);
		  File file = listOfFiles[i];
		  if (file.isFile() && file.getName().endsWith(".orig")) {
			  
			//  System.out.println("DOc name   " + file.getName());
			  doccount = doccount +1;
			  docnum = file.getName().substring(0, file.getName().lastIndexOf("."));
			  int sent_trac=0;
	   		  word_freq_map = new HashMap<String,Headvalue>();
	   		  	   		
			  BufferedReader br = new BufferedReader(new FileReader(file));
			  
			  sent_list = new ArrayList<HashMap<String,Headvalue>>();
			  
			  			  
			//  System.out.println("doccount " + doccount);
			  
			  String line;
			  while ((line = br.readLine()) != null) {
				  sentnum = "line # " + (iter = iter+1);
				 
				 // System.out.println("sentnum " + sentnum);
				  
				  if (!line.trim().isEmpty()) {
					 sent_trac=1;
					 String word = line.substring(line.indexOf("(") + 1, line.lastIndexOf(")"));
					  
					// System.out.println("              word " + word);	
					
					 String previous_wordpos = word.replaceAll("\\s+", " ").split(", ")[0];
					 String cur_wordpos = word.replaceAll("\\s+", " ").split(", ")[1];
					 

					 headword = previous_wordpos.substring(0, previous_wordpos.lastIndexOf("-"));
					 headpos = previous_wordpos.substring(previous_wordpos.lastIndexOf("-")+1);

					 currword = cur_wordpos.substring(0, cur_wordpos.lastIndexOf("-"));
					 currpos = cur_wordpos.substring(cur_wordpos.lastIndexOf("-")+1);
					 
					 
					// System.out.println(doccount + "-" + sentnum + " : " + headword + "/" + headpos + 
					//		 					" " + currword + "/" + currpos);

					  Headvalue hv = new Headvalue();
					  
					  hv.headword = headword;
					  hv.headpos = headpos;
					  hv.currword = currword;
					  hv.currpos = currpos;

					  Pattern p4 = Pattern.compile("-(\\d+)\\)");
					  Matcher m4 = p4.matcher(line);
					  if(m4.find()){
						  edgetag = m4.group(1);
						  hv.edgetag = edgetag;
					  }

					  
			    	    if(!word_freq_map.containsKey(currword)){
			    	    	Headvalue h2 = new Headvalue();
			    	    	h2.currword = currword;
			    	    	h2.sentcount=1;
			    	    	word_freq_map.put(currword, h2);
			    	    }
			    	    else{
			    	    	//count = word_freq_map.get(currword)+1;
			    	    	Headvalue h2 = word_freq_map.get(currword);
			    	    	h2.sentcount = h2.sentcount + 1;
			    	    	word_freq_map.put(currword, h2);
			    	    }
			    	    key = currword + "-" + currpos;  
			    	    
						if (!sent_map.containsKey(key)){
							sent_map.put(key, hv);
						}
					} 
				  else {
					  iter=0;
					 // iter = iter+1;
					  sent_trac=0;
					 
					//  System.out.println(" --------------    Adding sent map to list ------------------- ");
					 sent_list.add(sent_map);
					 sent_map = new HashMap<String,Headvalue>();
					  
				  }
				  
			     
			  }
			  
			  br.close();
			  
			  if(sent_trac == 1){
				 //System.out.println("----------------------------    Last sent map not added to the list");
				  sent_list.add(sent_map);
			  }
		  
			  if(!doc_sent_map.containsKey(docnum)){
				  doc_sent_map.put(docnum, sent_list);
			  }
			  if(!doc_word_freq_map.containsKey(docnum)){
				  doc_word_freq_map.put(docnum, word_freq_map);
			  }
			  
		  } 
		  
		 // System.out.println("DOC COUNT   " + doccount  );
		}

	}
	
		public void display(){
			
		System.out.println("*********** display ****************");
		
		
		System.out.println("doc_sent_map size " + doc_sent_map.size());
		
		
		for(Entry<String, ArrayList<HashMap<String, Headvalue>>> entry : doc_sent_map.entrySet()) {
			String docno = entry.getKey();
			ArrayList<HashMap<String, Headvalue>> sentlist = (ArrayList<HashMap<String, Headvalue>>) entry.getValue();
			
			System.out.println("Sent size  " + docno + "/" + sentlist.size());
		}
		
		System.out.println("doc_word_freq_map size " + doc_word_freq_map.size());
		
		for(Entry<String, HashMap<String, Headvalue>> entry : doc_word_freq_map.entrySet()) {
			String docno = entry.getKey();
			System.out.println("word has size  " + docno + "/" + entry.getValue().size());
		}
		
		
		for(Entry<String, HashMap<String, Headvalue>> entry : doc_word_freq_map.entrySet()) {
			String docno = entry.getKey();
		    
		    for(Entry<String, Headvalue>  entry1 : entry.getValue().entrySet()) {
		    	String curword = entry1.getKey();
		    	double weight;
		    	Headvalue hv = entry1.getValue();
		    	hv.doccount = getdoccount(curword);
		    	 
		    	//System.out.println(" DOC #/" + docno + "  CUR_WORD/" + curword + "  SENT_FREQ/" + hv.sentcount + "  DOC_FREQ/" + hv.doccount);
		    }
		}
	}


	
	public void updateDocfrequency(){
		
		HashMap<String, HashMap<String, Headvalue>> temp_map = doc_word_freq_map;
		
		for(Entry<String, HashMap<String, Headvalue>> entry : temp_map.entrySet()) {
			word_freq_map = new HashMap<String, Headvalue>();
			String docno = entry.getKey();
		    HashMap<String, Headvalue> word_freq_map_temp = entry.getValue();
		    
		    for(Entry<String, Headvalue>  entry1 : word_freq_map_temp.entrySet()) {
		    	String curword = entry1.getKey();
		    	Double docfreq = getdoccount(curword);
		    	Headvalue hv = entry1.getValue();
		    	hv.doccount = docfreq;
		    	
		  //  	System.out.println(" DOC #/" + docno + "  CUR_WORD/" + curword + "  SENT_FREQ/" + hv.sentcount + "  DOC_FREQ/" + hv.doccount);
		    	if(!word_freq_map.containsKey(curword)){
		    		word_freq_map.put(curword, hv);
		    	}
		    }
		    
		    doc_word_freq_map.put(docno, word_freq_map);
		}
	}

	
	public double getdoccount(String curwrord){
		double count=0;
		
		for(Entry<String, HashMap<String, Headvalue>> entry : doc_word_freq_map.entrySet()) {
		    
		    if(entry.getValue().containsKey(curwrord)){
		    	count +=1;
		    	}
		}
		return count;
		
	}
	
	public void depth_find(){
		
		System.out.println("DEPTH FIND");
		
		for(Entry<String, ArrayList<HashMap<String, Headvalue>>> entry : doc_sent_map.entrySet()) {
			String docno = entry.getKey();
			ArrayList<HashMap<String, Headvalue>> sentlist = (ArrayList<HashMap<String, Headvalue>>) entry.getValue();

			//System.out.println("DEPTH FIND - sentense size " + sentlist.size() );
			
			for(int i=0; i<sentlist.size();i++){
				HashMap<String,Headvalue> temp_sent_map = sentlist.get(i);
				    for(Entry<String, Headvalue>  entry1 : temp_sent_map.entrySet()) {
				    	String curwordkey = entry1.getKey();
//				    	Headvalue hv = entry1.getValue();
				    	//String wordkey = hv.currword + "-" + hv.currpos;
				    	depth=0;
				    	double res_depth =  actual_calc(curwordkey,temp_sent_map);
				    	
				    	//updatesent_map(docno,wordkey,"depth",res_depth);
				    	
				    	doc_sent_map.get(docno).get(i).get(curwordkey).depth = res_depth;
				    	
				    //	System.out.println(" DOC #/" + docno + "  CUR_WORD/" + curwordkey + "  res_depth/" + res_depth + "  HEAD_WORD/" + hv.headword);
				    }
			}

		}
	}
	public double actual_calc(String key, HashMap<String,Headvalue> fn_sent_map){
    	
    	//System.out.println("*********** CURRWORD      " + key);
		
		if(fn_sent_map.containsKey(key)){
			Headvalue  h1 =  fn_sent_map.get(key);
			String temp_headword = h1.headword;
			
			//System.out.println("HEADWORD/key     " +h1.headword + "/" + key +"  CURRENT WORD " + h1.currword + " depth " + depth);
			
	        if (temp_headword.trim().equalsIgnoreCase("root")){
	        	//System.out.println("ROOT WORD      " +h1.headword + " Previous word " + temp_headword + " depth " + depth);
	        	return depth;
	        }
	        else{
	        	//String temp_curword = h1.headword;
	        	depth = depth +1;
	        	String temp_headword_key = h1.headword + "_"+h1.headpos;
	        	 return actual_calc(temp_headword_key,fn_sent_map);
	        }
		}
			       // System.out.println(pairs1.getKey() + " = " + h.currpos + "  "+ h.currword);
	      //  it1.remove(); 
		return depth;
	}
	
	public void calculate_final() throws IOException
	{
		File file1 = new File("C:\\Users\\Vijayalakshmi\\Desktop\\nlp\\Assign4\\Wordweight.txt");
		FileWriter fw = new FileWriter(file1.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		
		final_sentlist = new ArrayList<Sentence>();
		
		ArrayList<String> temp_list;
		String docno;
		int sentnum=1;
		
		if (!file1.exists()) {
			file1.createNewFile();
		}
		System.out.println("***************** Display in depth ********************");
		for(Entry<String, ArrayList<HashMap<String, Headvalue>>> entry : doc_sent_map.entrySet()) {
			docno = entry.getKey();
			ArrayList<HashMap<String, Headvalue>> sentlist = (ArrayList<HashMap<String, Headvalue>>) entry.getValue();
              
			//System.out.println("Doc#/seent size - " + docno + "/" + sentlist.size());
			
			sentnum =1;
			
			for(int i=0; i<sentlist.size();i++){
			//	System.out.println("starting new sentence   " + i);
				String sentpos = " ";
				String headpos = " ";
				String edgepos = " ";

				
				temp_list = new ArrayList<String> ();
				
		    //	System.out.println("seent pos before   " + sentpos);

				ArrayList<String> sentence = new ArrayList<String>();
				ArrayList<Double> weight = new ArrayList<Double>();
				ArrayList<String> head = new ArrayList<String>();
				ArrayList<String> edge= new ArrayList<String>();

	              
				HashMap<String,Headvalue> temp_sent_map = sentlist.get(i);
				
				    for(Entry<String, Headvalue>  entry1 : temp_sent_map.entrySet()) {
				    	String key = entry1.getKey();
				    	//System.out.println("Key   " + key);
				       	//System.out.println("doc num  " + docno);
				    	Headvalue hv = entry1.getValue();
				    	String curword = hv.currword;
				    	
				    	temp_list.add(key);
				    	
				    	//System.out.println("word  " + hv.currword + hv.currpos+"   prev  "  +hv.headword + hv.headpos);

				    	double tfidfval = 0;
				    	bw.write(hv.currword +  "\t");
				    	
				    	if(doc_word_freq_map.containsKey(docno)){
				    		if (doc_word_freq_map.get(docno).containsKey(curword)){
				    			hv.tf = doc_word_freq_map.get(docno).get(curword).sentcount;
				    			hv.doccount = doc_word_freq_map.get(docno).get(curword).doccount;
				    		}else{
				    			//System.out.println(" current word not found - " + curword);
				    			hv.tf =0;
				    			hv.doccount =0;
				    		}
				    		
				    	}else{
				    		System.out.println(" docno not found - " + docno);
				    	}
 	
				    	tfidfval = hv.tf * Math.log(doccount/(hv.doccount + 1));
				    	hv.wordweight = tfidfval - 0.4 * hv.depth - 0.5;
				    	
				    	sentpos = hv.currword + "_" + hv.currpos;
				    	headpos = hv.headword + "_" + hv.headpos;
				    	edgepos = hv.edgetag;

				    	//System.out.println("seent pos   " + sentpos);
				    	//sentpos = hv.currword;
				    	sentence.add(sentpos);
				    	weight.add(hv.wordweight);
				    	head.add(headpos);
				    	edge.add(edgepos);
				    	
				    /*	System.out.println(" DOC # - " + docno + "  WORD_POS " + hv.currword + "-" + hv.currpos
				    			//+ "  HEAD_WORD - " + hv.headword 
				    			+ "  Depth - " + hv.depth + " tf - " + hv.tf
				    			+ " DOC_FREQ - " + hv.doccount  + "tfidf val   " + tfidfval + "word weight   " +hv.wordweight);*/
				    	
				    	bw.write(hv.currword +  "\t" + hv.wordweight + "\t");
				    }
				  //  System.out.println("Sentence   " + sentence);
				   // System.out.println("weight   " +weight);
				    
					 Sentence sent = new Sentence();
					 sent.docnum = docno;
					 sent.sentnum=sentnum;
					 sent.origsent=parseSentence(temp_list);
					 sentnum +=1;
				    
				    try {
				    	ArrayList<String> sentence_comp;
				    	//sentence_comp = ilp_part1(sentence,weight);
				    	//sentence_comp = ilp_part2(sentence,weight,head);
				    	sentence_comp = ilp_part3(sentence,weight,head,edge);

						//ilp_part3(sentence,weight,head,edge);
				    	
				    	sent.compsent = parseSentence(sentence_comp);
				    	
				    	final_sentlist.add(sent);
					

						
					} catch (LpSolveException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				    
			}bw.write("\r\n");

		}
	
		
		System.out.println("Printing Sentences");
		HashMap<String,HashMap<Integer,String>> comp_map = readFile_compressed();
		/* for(Entry<String,HashMap<Integer,String>>  entry2 : comp_map.entrySet()) {
		    	String key = entry2.getKey();
		    	//System.out.println("Key   " + key);
		       	//System.out.println("doc num  " + docno);
		    	HashMap<Integer,String> hm = entry2.getValue();
		    	 
		    	for(Entry<Integer,String>  entry3 : hm.entrySet()) {
			    	Integer key1 = entry3.getKey();
			    	//System.out.println("Key   " + key);
			       	//System.out.println("doc num  " + docno);
			    	String val2 = entry3.getValue();
			    	System.out.println("COMP_   " + key + " / " + key1 + "   " + val2);
		    	
		 }
		 }*/    	
		
		for(int i=0;i<final_sentlist.size();i++){
			//System.out.println("Orig: " + final_sentlist.get(i).docnum + "/" + final_sentlist.get(i).sentnum +  " : " + final_sentlist.get(i).origsent);
			System.out.println("Orig: " + final_sentlist.get(i).origsent);

			System.out.print("Human: ");
			
			String tempdocnum="";
			int tempsentnum = 0;
			String tempsent ="";
			if(comp_map.containsKey(final_sentlist.get(i).docnum)){
				HashMap<Integer,String> tempmap = comp_map.get(final_sentlist.get(i).docnum);
				tempdocnum = final_sentlist.get(i).docnum;
				if(tempmap.containsKey(final_sentlist.get(i).sentnum)){
					tempsentnum = final_sentlist.get(i).sentnum;
					tempsent = tempmap.get(final_sentlist.get(i).sentnum);
				}
			}
			
			//System.out.println(tempdocnum + "/" + tempsentnum + " : " + tempsent);
			System.out.println(tempsent);

			//System.out.println("Ours : " + final_sentlist.get(i).docnum + "/" + final_sentlist.get(i).sentnum + " : " + final_sentlist.get(i).compsent);
			System.out.println("Ours: " + final_sentlist.get(i).compsent);

			System.out.println("");
		}
	}
	
	public HashMap<String,HashMap<Integer,String>> readFile_compressed() throws IOException{
		String key="";
		File folder = new File("C:\\Users\\Vijayalakshmi\\Desktop\\nlp\\Assign4\\data1");
		File[] listOfFiles = folder.listFiles();
		String sentence = "";
		int sentnum=1;
		HashMap<Integer,String> sent_map_comp ;
		HashMap<String,HashMap<Integer,String>> doc_map_comp = new HashMap<String,HashMap<Integer,String>>();

		
		for (int i = 0; i < listOfFiles.length; i++) {
			sentnum = 1;
 		  String docnum = "";
 		  
		  File file = listOfFiles[i];
		  if (file.isFile() && file.getName().endsWith(".comp")) {
			  docnum = file.getName().substring(0, file.getName().lastIndexOf("."));
			  BufferedReader br = new BufferedReader(new FileReader(file));
			  String line;
			  sentence="";
			  sent_map_comp = new HashMap<Integer,String>();
			  while ((line = br.readLine()) != null) {
				  if (!line.trim().isEmpty()) {
					 String word = line.substring(line.indexOf("(") + 1, line.lastIndexOf(")"));
					 String cur_wordpos = word.replaceAll("\\s+", " ").split(", ")[1];
					 currword = cur_wordpos.substring(0, cur_wordpos.lastIndexOf("-"));
					 currpos = cur_wordpos.substring(cur_wordpos.lastIndexOf("-")+1);
					 sentence = sentence + currword + " ";
				  }
				  else {
					  
					  if (!(sent_map_comp.containsKey(sentnum))){
						  sent_map_comp.put(sentnum, sentence);
						  sentnum +=1;
					  }

					  sentence="";
				  }
			  }
			  br.close();
			  //new doc starts
			  if (!(doc_map_comp.containsKey(docnum))){
				  doc_map_comp.put(docnum, sent_map_comp);
			  }
		  } 
		  
		}
		return doc_map_comp;
		

	}


	
	public String parseSentence(ArrayList<String> sentlist){
		String parsedsent="";
		//List<Integer> wordpos = new ArrayList<Integer>();
		ArrayList<Integer> wordpos = new ArrayList<Integer>();
		HashMap<Integer,String> sentmap= new HashMap<Integer,String>();
		
		for(int i=0;i<sentlist.size();i++){
			int pos=0 ;
			String word="";
			String tempword="";
			
			tempword = sentlist.get(i);
			
			if(tempword.lastIndexOf("_")>0){
				//pos = Integer.parseInt(tempword.split("_")[1]);
				pos = Integer.parseInt(tempword.substring(tempword.lastIndexOf("_")+1));
				//word = sentlist.get(i).split("_")[0];
				word = tempword.substring(0, tempword.lastIndexOf("_")) ;
				
			}else if(tempword.lastIndexOf("-")>0){
				//pos = Integer.parseInt(tempword.split("_")[1]);
				pos = Integer.parseInt(tempword.substring(tempword.lastIndexOf("-")+1));
				//word = sentlist.get(i).split("_")[0];
				word = tempword.substring(0, tempword.lastIndexOf("-")) ;
			}else{
				System.out.println("Invalid Seperator for word/pos");
				return "";
			}
			
			//System.out.println("parseSentence   " + i + " " + tempword + " " + pos + " " + word);
			
			wordpos.add(pos);
			if(!sentmap.containsKey(pos)){
				sentmap.put(pos, word);
			}
		}

		Collections.sort(wordpos);
		
		for(int i=0;i<wordpos.size();i++){
			parsedsent = parsedsent + sentmap.get(wordpos.get(i)) + " ";
		}
		
		//System.out.println("parseSentence   " + parsedsent);
		
		return parsedsent;
	}
	@SuppressWarnings("unused")
	private ArrayList<String> ilp_part3(ArrayList<String> sentence,
			ArrayList<Double> weight, ArrayList<String> head, ArrayList<String> edge) throws LpSolveException {
		
		String[] dep = {"neg", "conj", "aux", "possessive", "auxpas", "poss", "det", "preconj", "predet", "prep", "prepc", "vmod", "nn", "cc", "pcomp", "ccomp", "num", "discourse", "dobj", "agent"};

		LpSolve lp;
		int Ncol, j, ret = 0;
		Ncol = sentence.size();
		ArrayList<String> sentence_comp3 = new ArrayList<String>(); 
        
        int[] colno = new int[Ncol];
        double[] row = new double[Ncol];

        lp = LpSolve.makeLp(0, Ncol);
        if(lp.getLp() == 0)
          ret = 1; 
        
        if(ret == 0) {
         
        //	for (int i= 1; i<=sentence.size();i++){
        		
        	//	System.out.println("WORD    " + sentence.get(i));
        	int m=1;
        	for(String str : sentence){

        		lp.setColName(m, str);
        	     lp.setBinary(m, true);
        	    m++;

        	}
        	lp.setAddRowmode(true);  

          j = 0;
         int n = 1;
          
      	for(Double wt : weight){

        	  colno[j] = n;
        	  row[j++] =  1;
        	  n++;

          }
          lp.addConstraintex(j, row, colno, LpSolve.GE, 1);

          
         }
        
        if(ret == 0) {
            /* construct second row (110 x + 30 y <= 4000) */
        	//j = 0;
            for( int i = 1; i<sentence.size();i++){
            	j = 0;
            colno[j] = i; /* first column */
            row[j++] = -1;

            int posi = find_pos_head(sentence,head.get(i));
           // System.out.println(" head position   " +posi);
            colno[j] = posi; /* second column */

            row[j++] = 1;
            lp.addConstraintex(j, row, colno, LpSolve.GE, 0);

            }
            //System.out.println("row, colno,col2   " + row.toString()+ colno.toString() + col2);
          }
        if(ret == 0) {
            /* construct second row (110 x + 30 y <= 4000) */
        	//j = 0;
        	
            for( int i = 1; i<sentence.size();i++){
            	int edge_pos = 0;
            	j = 0;
            	for (int k = 0; k< dep.length;k++){
            		if(edge.get(i).equalsIgnoreCase(dep[k])){
            	edge_pos = find_edge_pos(edge,edge.get(i));
            	}
            		}
            colno[j] = edge_pos; /* first column */
            row[j++] = 1;

            
            lp.addConstraintex(j, row, colno, LpSolve.GE, 1);

            }
          }
        
    
        if(ret == 0) {
          lp.setAddRowmode(false); 
          
          j = 0;
          int t = 1;
         // for(int i = 0; i<weight.size();i++){
        	for(Double wt1 : weight){

        	  colno[j] = t;
        	  row[j++] = wt1;
        	  t++;
        	  }
          lp.setObjFnex(j, row, colno);
         
        }

        if(ret == 0) {
          /* set the object direction to maximize */
          lp.setMaxim();
         

          /* just out of curioucity, now generate the model in lp format in file model.lp */
          lp.writeLp("model.lp");

          /* I only want to see important messages on screen while solving */
          lp.setVerbose(LpSolve.IMPORTANT);

          /* Now let lpsolve calculate a solution */
          ret = lp.solve();
          if(ret == LpSolve.OPTIMAL)
            ret = 0;
          else
            ret = 5;
        }

        if(ret == 0) {

          /* variable values */
          lp.getVariables(row);
 
        }
        
        for(j = 0; j < Ncol; j++){
        	if (row[j] == 1){
        		//System.out.println("Ours : " + lp.getColName(j + 1) );
            	sentence_comp3.add(lp.getColName(j + 1));
        	}
       } 

        /* clean up such that all used memory by lpsolve is freed */
        if(lp.getLp() != 0)
          lp.deleteLp();

        return(sentence_comp3);
		
	}

	
	private int find_edge_pos(ArrayList<String> edge, String string) {
		int pos_ed = 1;
		for (int i=1; i<edge.size();i++){
			if (string.equalsIgnoreCase(edge.get(i)));
			pos_ed = i;
		}
		return pos_ed;
		
	}

	

	@SuppressWarnings("unused")
	private ArrayList<String> ilp_part2(ArrayList<String> sentence,
			ArrayList<Double> weight, ArrayList<String> head) throws LpSolveException {
		LpSolve lp;
		int Ncol, j, ret = 0;
		Ncol = sentence.size();
		ArrayList<String> sentence_comp2 = new ArrayList<String>(); 
        
        int[] colno = new int[Ncol];
        double[] row = new double[Ncol];

        lp = LpSolve.makeLp(0, Ncol);
        if(lp.getLp() == 0)
          ret = 1; 
        
        if(ret == 0) {
         
        //	for (int i= 1; i<=sentence.size();i++){
        		
        	//	System.out.println("WORD    " + sentence.get(i));
        	int m=1;
        	for(String str : sentence){

        		lp.setColName(m, str);
        	     lp.setBinary(m, true);
        	    m++;

        	}
        	lp.setAddRowmode(true);  

          j = 0;
         int n = 1;
          
      	for(Double wt : weight){

        	  colno[j] = n;
        	  row[j++] =  1;
        	  n++;

          }
          lp.addConstraintex(j, row, colno, LpSolve.GE, 1);

          
         }
        
        if(ret == 0) {
            /* construct second row (110 x + 30 y <= 4000) */
        	//j = 0;
            for( int i = 1; i<sentence.size();i++){
            	j = 0;
            colno[j] = i; /* first column */
            row[j++] = -1;

            int posi = find_pos_head(sentence,head.get(i));
           // System.out.println(" head position   " +posi);
            colno[j] = posi; /* second column */

            row[j++] = 1;
            lp.addConstraintex(j, row, colno, LpSolve.GE, 0);

            }
            //System.out.println("row, colno,col2   " + row.toString()+ colno.toString() + col2);
          }
    
        if(ret == 0) {
          lp.setAddRowmode(false); 
          
          j = 0;
          int t = 1;
         // for(int i = 0; i<weight.size();i++){
        	for(Double wt1 : weight){

        	  colno[j] = t;
        	  row[j++] = wt1;
        	  t++;
        	  }
          lp.setObjFnex(j, row, colno);
         
        }

        if(ret == 0) {
          /* set the object direction to maximize */
          lp.setMaxim();
         

          /* just out of curioucity, now generate the model in lp format in file model.lp */
          lp.writeLp("model.lp");

          /* I only want to see important messages on screen while solving */
          lp.setVerbose(LpSolve.IMPORTANT);

          /* Now let lpsolve calculate a solution */
          ret = lp.solve();
          if(ret == LpSolve.OPTIMAL)
            ret = 0;
          else
            ret = 5;
        }

        if(ret == 0) {

          /* variable values */
          lp.getVariables(row);
 
        }
        
        for(j = 0; j < Ncol; j++){
        	if (row[j] == 1){
        		//System.out.println("Ours : " + lp.getColName(j + 1) );
            	sentence_comp2.add(lp.getColName(j + 1));
        	}
       } 

        /* clean up such that all used memory by lpsolve is freed */
        if(lp.getLp() != 0)
          lp.deleteLp();

        return(sentence_comp2);
		
	}
		
	private int find_pos_head(ArrayList<String> sentence, String string) {
		int position = 0;
		for(int i=1;i<sentence.size();i++){
			if (string.equalsIgnoreCase(sentence.get(i))){
				position = i;
				//return position;
			}
		}
		
		return position;
		
	}

	
	private ArrayList<String> ilp_part1 (ArrayList<String> sentence, ArrayList<Double> weight) throws LpSolveException {
		
		LpSolve lp;
		int Ncol, j, ret = 0;
		Ncol = sentence.size();
		ArrayList<String> sentence_comp = new ArrayList<String>(); 
        
        int[] colno = new int[Ncol];
        double[] row = new double[Ncol];

        lp = LpSolve.makeLp(0, Ncol);
        if(lp.getLp() == 0)
          ret = 1; 
        
        if(ret == 0) {
         
        //	for (int i= 1; i<=sentence.size();i++){
        		
        	//	System.out.println("WORD    " + sentence.get(i));
        	int m=1;
        	for(String str : sentence){

        		lp.setColName(m, str);
        	     lp.setBinary(m, true);
        	    m++;

        	}



          lp.setAddRowmode(true);  

          j = 0;
         int n = 1;
          
      	for(Double wt : weight){

        	  colno[j] = n;
        	  row[j++] =  1;
        	  n++;

          }
          lp.addConstraintex(j, row, colno, LpSolve.GE, 1);

          
         }
    
        if(ret == 0) {
          lp.setAddRowmode(false); 
          
          j = 0;
          int t = 1;
         // for(int i = 0; i<weight.size();i++){
        	for(Double wt1 : weight){

        	  colno[j] = t;
        	  row[j++] = wt1;
        	  t++;
        	  }
          lp.setObjFnex(j, row, colno);
         
        }

        if(ret == 0) {
          /* set the object direction to maximize */
          lp.setMaxim();
         

          /* just out of curioucity, now generate the model in lp format in file model.lp */
          lp.writeLp("model.lp");

          /* I only want to see important messages on screen while solving */
          lp.setVerbose(LpSolve.IMPORTANT);

          /* Now let lpsolve calculate a solution */
          ret = lp.solve();
          if(ret == LpSolve.OPTIMAL)
            ret = 0;
          else
            ret = 5;
        }

        if(ret == 0) {

          /* objective value */
       // System.out.println("Objective value: " + lp.getObjective());

          /* variable values */
          lp.getVariables(row);
      /*   for(j = 0; j < Ncol; j++)
      System.out.println(lp.getColName(j + 1) + ": " + row[j]);

       */   
        }
        
        for(j = 0; j < Ncol; j++){
        	if (row[j] == 1){
        		//System.out.println("Ours : " + lp.getColName(j + 1) );
            	sentence_comp.add(lp.getColName(j + 1));
        	}
       } 

        /* clean up such that all used memory by lpsolve is freed */
        if(lp.getLp() != 0)
          lp.deleteLp();

        return(sentence_comp);
		
	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Readfile r = new Readfile();
		
		System.out.println("Reading Documetns");
		r.readFile();
		System.out.println("Updating Frequency");
		r.updateDocfrequency();
		
		//r.display();
		
		System.out.println("Calculating Depth");
		r.depth_find();
		System.out.println("Final Calculation");
		r.calculate_final();
	}

}
