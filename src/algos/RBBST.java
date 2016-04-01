package algos;

import java.util.LinkedList;





public class RBBST {

		private Node Root;
		boolean RED = true;

			// node classes

			private class Node{
				// what to eat
				private LinkedList key;
				private String value;
				private Node left, right;
				private boolean colour;
			
				public Node(LinkedList key, String value, boolean colour){
					this.key = key;
					this.value = value;
					this.colour = colour;
				}
				
			}
			private Node rotateRight (Node h ){
				Node x = h.left;
				h.left = x.right;
				x.right = h;
				x.colour = x.right.colour;
				return x;
			}
			private Node rotateLeft (Node h){
				Node x = h.right;
				h.right = x.left;
				x.left = h;
				x.colour = x.left.colour;
				x.left.colour = RED;
				return x;
			}
			private boolean isRed (Node node){
				if (node == null) return false;
				return (node.colour == RED);
			}
			private void flipColours(Node h){
				h.colour = !h.colour;
				h.left.colour = !h.left.colour;
				h.right.colour = !h.right.colour;
			}
			public void put(LinkedList key, String value){
				Root = put(Root, key, value);
				
			}
			private Node put(Node node, LinkedList Key, String value){
				
				if (node == null){
					return new Node(Key, value, true);
				}
				int cmp = compare(Key, node);
				if (cmp < 0){
					node.left = put(node.left, Key, value);
				}else if (cmp > 0){
					node.right = put(node.right, Key, value);
				}else{
					node.value = value;
				}
				
				// for RB
				if (isRed(node.right) && !isRed(node.left))  node = rotateLeft(node);
				if (isRed(node.left) && isRed(node.left.left)) node = rotateRight(node);
				if (isRed(node.left) && isRed(node.right)) flipColours(node);
				return node;
			}
			
			
			private int compare (LinkedList<Integer> Key, Node M){
				LinkedList<Integer> a = Key;
				LinkedList<Integer> b = M.key;
				if (a.size() > b.size()){
					return 1;
				} else if (b.size() > a.size()){
					return -1;
				}else{
					for (int i = 0; i < Key.size(); i++){
						if (a.get(i).compareTo(b.get(i)) > 0){
							return 1;
						}else if(b.get(i).compareTo(a.get(i)) > 0){
							return -1;
						}
					}
				}return 0;
			}


			public String get (String value){
				return get(Root, value);
			}
			private String get (Node node, String value){
				LinkedList<Integer> Key = new LinkedList<Integer>();
				// ascii value of words
				for (int i = 0; i < value.length(); i ++){
					Key.add(new Integer(value.charAt(i)));
				}
				 while (node != null) {
			            int cmp = compare (Key, node);
			            if      (cmp < 0) node = node.left;
			            else if (cmp > 0) node = node.right;
			            else              return node.value;
			        }
			        return null;
			    }
			

			


		}
