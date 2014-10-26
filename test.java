package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

public class test {
	String currword ;
	String currpos;
	String headword;
	String headpos;
	String edgetag;
	String sentnum;
	double depth = 0;
	double count;
	double doccount;
	
	HashMap<String,ArrayList<HashMap<String,Headvalue>>> doc_sent_map = new HashMap<String,ArrayList<HashMap<String,Headvalue>>>();
	HashMap<String,Headvalue> sent_map = new HashMap<String,Headvalue>();
	ArrayList<HashMap<String,Headvalue>> sent_list = new ArrayList<HashMap<String,Headvalue>>();
	ArrayList<String> words_list = new ArrayList<String>();
	//HashMap<String,HashMap<String,Double>> doc_word_freq_map = new HashMap<String,HashMap<String,Double>>();
	//HashMap<String,Double> word_freq_map = new HashMap<String,Double>();
	HashMap<String,HashMap<String,Headvalue>> doc_word_freq_map = new HashMap<String,HashMap<String,Headvalue>>();
	HashMap<String,Headvalue> word_freq_map ;
	HashMap<String,Double> doc_freq = new HashMap<String,Double>();
	
	int iter;
	
	public void readFile() throws IOException{
		String key="";
		File folder = new File("C:\\Users\\Vijayalakshmi\\Desktop\\nlp\\Assign4\\data1");
		File[] listOfFiles = folder.listFiles();
		
		
		for (int i = 0; i < listOfFiles.length; i++) {
			
			String docnum = "doc" + (i+1);
			
			
			//System.out.println("INSIDE FOR");
		  File file = listOfFiles[i];
		  
		 // System.out.println(" ************************** " + file.getName());
		  
		  if (file.isFile() && file.getName().endsWith(".orig")) {
			  
			  
				//System.out.println("INSIDE if");
			  
			  
			  doccount = doccount +1;
			  int sent_trac=0;
	//			ArrayList<String> words_list = new ArrayList<String>();
				
				word_freq_map = new HashMap<String,Headvalue>();
				
			  BufferedReader br = new BufferedReader(new FileReader(file));
			  String line;
			  while ((line = br.readLine()) != null) {
				  
				  sentnum = "sent" + (iter+1);
				 
				 //  System.out.println(sentnum);
				  
				  if (!line.trim().isEmpty()) {
					  sent_trac=1;
/*					  
					  Headvalue hv = new Headvalue();
					  String line1 = line.replaceAll("'", "");
					  Pattern p = Pattern.compile("\\((\\w+|-{2,5}|[A-Za-z0-9,:,.]*)(.)?-");
					  Matcher m = p.matcher(line1);
					  if (m.find()){
						  headword = m.group(1);
						  String headword1 = headword.replaceAll("[-+.^:,]","");
						  hv.headword = headword1;
					 }
					  Pattern p1 = Pattern.compile("-(\\d+),");
					 // Matcher m1 = p1.matcher("conj(weakness-49, activities-55)");
					  Matcher m1 = p1.matcher(line1);
					  if (m1.find()){
					  headpos = m1.group(1);
					  hv.headpos = headpos;
					  }
					  
					  Pattern p2 = Pattern.compile(",\\s*(\\w+|-{2,5}|[A-Za-z0-9,:,.]*)(.)?-");
					  Matcher m2 = p2.matcher(line1);
					  if(m2.find()){
					  currword = m2.group(1);
					  String currword1 = currword.replaceAll("[-+.^:,]","");
					  hv.currword = currword1;
					  }
					  
                      
					  Pattern p3 = Pattern.compile("-(\\d+)\\)");
					  Matcher m3 = p3.matcher(line1);
					  if(m3.find()){
					  currpos = m3.group(1);
					  hv.currpos = currpos;
					  }
				  
					  Pattern p4 = Pattern.compile("-(\\d+)\\)");
					  Matcher m4 = p4.matcher(line);
					  if(m4.find()){
					  edgetag = m4.group(1);
					  hv.edgetag = edgetag;
					  }
					  
					 // words_list.add(currword);
	*/		
					  
					 String word = line.substring(line.indexOf("(") + 1, line.lastIndexOf(")"));
					  
				//	 System.out.println(word);	
					
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
							
							//System.out.println("Adding to sent map : " + currword + "/" + key);
						}
					} 
				  else {
					  iter = iter+1;
					  sent_trac=0;
					 // System.out.println("Adding sent map to list ");
					  
					 sent_list.add(sent_map); 
					  
				  }
				  
			     
			  }
			  
			  br.close();
			  
			  if(sent_trac==1){
				 // System.out.println("Last sent map not added to the list");
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
		    	 
		    	System.out.println(" DOC #/" + docno + "  CUR_WORD/" + curword + "  SENT_FREQ/" + hv.sentcount + "  DOC_FREQ/" + hv.doccount);
		    }
		}
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
	        	String temp_headword_key = h1.headword + "-"+h1.headpos;
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
		

		if (!file1.exists()) {
			file1.createNewFile();
		}
		System.out.println("***************** Display in depth ********************");
		for(Entry<String, ArrayList<HashMap<String, Headvalue>>> entry : doc_sent_map.entrySet()) {
			String docno = entry.getKey();
			ArrayList<HashMap<String, Headvalue>> sentlist = (ArrayList<HashMap<String, Headvalue>>) entry.getValue();

			for(int i=0; i<sentlist.size();i++){
				HashMap<String,Headvalue> temp_sent_map = sentlist.get(i);
				    for(Entry<String, Headvalue>  entry1 : temp_sent_map.entrySet()) {
				    	//String key = entry1.getKey();
				    	//System.out.println("Key   " + key);
				    	
				    	//System.out.println("doc num  " + docno);
				    	Headvalue hv = entry1.getValue();
				    	String curword = hv.currword;
				    	
				    	System.out.println("word  " + hv.currword + hv.currpos+"   prev  "  +hv.headword + hv.headpos);

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
				    	
				    /*	System.out.println(" DOC # - " + docno + "  WORD_POS " + hv.currword + "-" + hv.currpos
				    			//+ "  HEAD_WORD - " + hv.headword 
				    			+ "  Depth - " + hv.depth + " tf - " + hv.tf
				    			+ " DOC_FREQ - " + hv.doccount  + "tfidf val   " + tfidfval + "word weight   " +hv.wordweight);*/
				    	
				    	bw.write(hv.currword +  "\t" + hv.wordweight + "\t");
				    }
			}bw.write("\r\n");

		}
	}
	

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		test r = new test();
		
		System.out.println("Reading Documetns");
		r.readFile();
		System.out.println("Updating Frequency");
		r.updateDocfrequency();
		
		r.display();
		
		System.out.println("Calculating Depth");
		r.depth_find();
		System.out.println("Final Calculation");
		r.calculate_final();
	}

}
