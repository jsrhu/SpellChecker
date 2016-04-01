package algos;

public class Heap{
	public static void sortHeap ( String [] x) {
		int N = x.length;

		// uses sink and exchange
		for (int i = N/2; i >= 1; i--){
			sink (x, i , N);
		}
		while (N>1){
			exch(x,1,N--);
			sink(x,1,N);
		}
	}
	private static void exch(String [] x, int a, int b ){
		b --; a --;
		String temp = x[a];
		x[a] = x[b];
		x[b] = temp;
	}
	private static void sink(String []  x, int start, int Size){
		while (2*start <= Size){
			int j = 2*start;
			if (j < Size && less(x,j,j+1)){ j++;}
			if (!less(x,start,j)) break;
			// if not less continue swapping
			exch(x,start,j);
			start = j;
		}
		
	}
	
	private static boolean less (String word [], int a, int b){
		
		if(a > word.length - 1 || b > word.length - 1) return false;
		if (word[a].length() < word[b].length()){
			return true;
		} else if ( word[a].length() > word[b].length()){
			return false;
		}
		for (int i = 0; i < word[a].length(); i++){
			if ((int) word[a].charAt(i) < (int) word[b].charAt(i)){ // if the ascii value of a is smaller then b
				return true;
			}
		}
		return false;
	}
}
