package typocheck;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Iterator;

import sun.misc.Queue;

import java.util.Scanner;

import algos.TST;

public class TypoChecker2 {
	/*
	 * this.typos holds the Correction TST
	 * loadFile holds the file the TypoCheck was loaded in from,
	 * so the same file can be used to save again.
	 */
	private TST<Correction> typos;
	private File loadFile;
	
	/*
	 * default constructor uses default file.
	 * Allows alternate files to be used if needed
	 */
	public TypoChecker2(){
		this(new File("data/typos.txt"));
	}
	
	public TypoChecker2(File input){
		this.loadFile = input;
		
		this.typos = new TST<Correction>();
		
		Scanner in;
		String[] corrections;
		String typo;
		
		
		try{
			in = new Scanner(input);
			
			while(in.hasNextLine()){
				corrections = in.nextLine().split("->");
				typo = corrections[0].toString();
				corrections = corrections[1].split(",");
				for(int i = 0; i < corrections.length; i++) corrections[i] = corrections[i].trim();
				this.typos.put(typo, new Correction(typo, corrections));
			}
			
			in.close();
		}catch(FileNotFoundException fnfe){
			System.out.println("File not found");
		}
	}
	
	/*
	 * Searches each word in the line and returns all possible corrections at
	 * the word index.
	 */
	public String[][] searchLine(String[] words){
		String[][] ret = new String[words.length][];
		for(int i = 0; i < words.length; i++){
			ret[i] = this.getCorrection(words[i]);
		}return ret;
	}
	
	/*
	 * Adds a new correction to the TST. We do not necessarily want to replace the old correction,
	 * so we make a combination of the two objects
	 */
	public void addCorrection(Correction c){
		String typo = c.getTypo();
		Correction possibility = this.typos.get(typo);
		if(possibility == null) this.typos.put(typo, c);
		else{
			HashSet<String> h = new HashSet<String>();
			String[] d = possibility.getCorrection(), finalCorr;
			Iterator<String> it;
			for(String p : d) h.add(p);
			d = c.getCorrection();
			for(String p : d) h.add(p);
			it = h.iterator();
			finalCorr = new String[h.size()];
			for(int i = 0; it.hasNext(); i++) finalCorr[i] = it.next();
			this.typos.put(typo, new Correction(typo, finalCorr));
		}
	}
	
	/*
	 * Saves data back to the loadFile
	 */
	public void saveData() throws InterruptedException{
		try{
			PrintWriter saver = new PrintWriter(this.loadFile);
			Queue<String> q = this.typos.keys();
			Correction c;
			while(!q.isEmpty()){
				c = this.typos.get(q.dequeue());
				saver.println(c.toString());
			}saver.close();
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}
	}
	
	/*
	 * Retrieve corrections from TST
	 */
	private String[] getCorrection(String word){
		//First we check the word in the common list of corrections
		//Then we look for the lowercase, just to be certain
		if(word.length() == 0) return null;
		Correction c = this.typos.get(word);
		if(c != null) return c.getCorrection();
		c = this.typos.get(word.toLowerCase());
		if(c != null) return c.getCorrection();
		
		return null;
	}
	
	public String getCorrectionString(String k){
		return this.typos.get(k).toString();
	}
	
	
}
