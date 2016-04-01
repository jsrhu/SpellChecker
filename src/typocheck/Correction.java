package typocheck;

/*
 * Record to hold each line from typos.txt
 * Holds suggested corrections and the incorrect spelling that corresponds
 */
public class Correction {
	private String typo;
	private String[] corrections;
	
	public Correction(String t, String[] c){
		this.typo = t;
		this.corrections = c;
	}
	
	public String getTypo(){
		return this.typo;
	}
	
	public String[] getCorrection(){
		return this.corrections;
	}
	
	//Returns this correction in the form typo->correction1, correction2...., correctionN
	public String toString(){
		StringBuilder st = new StringBuilder();
		
		st.append(this.typo);
		st.append("->");
		for(int i = 0; i < this.corrections.length; i++){
			st.append(this.corrections[i]);
			if(i != this.corrections.length - 1) st.append(", ");
		}return st.toString();
	}
}
