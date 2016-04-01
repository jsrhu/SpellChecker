package spellcheck;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.LinkedList;

import algos.Heap;
import algos.RBBST;
import algos.TST;

public class SpellCheck {
	String [] Temp;
	RBBST CommonRB = new RBBST();
	TST<String> WordsRB = new TST<String>();
	boolean Return [];
	String check;
	String []  listOfWords;

	public SpellCheck (){
		refreshInput();
	}

	private void refreshInput (){
		String Temp;
		Scanner Common, Words;

		try {
			// try \n ?
			Common = new Scanner (new File("data/1000Common.txt")).useDelimiter("/n");
			Words = new Scanner (new File("data/Words.txt")).useDelimiter("/n");
			while (Common.hasNext()){
				Temp = Common.nextLine();
				LinkedList<Integer> Key = new LinkedList<Integer>();
				for (int i = 0; i < Temp.length(); i ++){
					Key.add(new Integer((int) Temp.charAt(i)));
					
				}
				CommonRB.put(Key, Temp);
			}
				
			while (Words.hasNext()){
				Temp = Words.nextLine();
				WordsRB.put(Temp, Temp);	
			}
			Words.close();Common.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("FNFE");
			e.printStackTrace();
		}
	}
	
	private void updateOutput(String [] Word) throws FileNotFoundException{
		PrintStream output = new PrintStream ( new File("data/1000Common.txt"));
		for (int i = 0; i < Word.length; i ++){
			output.println(Word[i]);
		}output.close();
	}
	public boolean[] check(String [] input){
		// get each word by itself
		Return = new boolean[input.length];

		// check each word separately
		for (int i = 0; i < input.length; i++){
			if(input[i].length() == 0) continue;
			check = CommonRB.get(input[i]);
			// check = null if the get couldn't find it
			if (check == null){
				check = (String) WordsRB.get(input[i]);
				if(check == null){
					String tryNoGrammar = input[i].replaceAll("[^a-zA-Z ]", "");
					if(tryNoGrammar.length() >= 1) check = WordsRB.get(tryNoGrammar);
					if (check == null) {
						tryNoGrammar = tryNoGrammar.toLowerCase();
						if(tryNoGrammar.length() >= 1) check = WordsRB.get(tryNoGrammar);
						if(check == null) Return[i] = false;
						else Return[i] = true;
					}else Return[i] = true;
				}else Return [i] = true;
			} else Return [i] = true;
			if(input[i].matches("[0-9]+")) Return[i] = true;
		} return Return;
	}

	public void addWord(String word) throws FileNotFoundException{
		Scanner Words = new Scanner (new File("data/1000Common.txt"));
		String Temp;
		int count = 0;

		// get size of database
		while (Words.hasNextLine()){
			Words.nextLine();
			count ++;
		}
		listOfWords = new String[count + 1];
		
		Words.close();
		
		Scanner WordsTwo = new Scanner (new File("data/1000Common.txt"));
		count = 0;
		while (WordsTwo.hasNextLine()){
			Temp = WordsTwo.nextLine();
			listOfWords[count] = Temp;
			count ++;
		}
		listOfWords[count] = word; 
		WordsTwo.close();
		Heap.sortHeap(listOfWords);
		updateOutput(listOfWords);
	}
	
	
}
