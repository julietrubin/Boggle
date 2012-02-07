public class Node {
	private boolean value;
	private int numberOfChildren;
	private Node[] childArray; 

	public Node(boolean value){
		setValue(value);
		numberOfChildren = 0;
		childArray = new Node[26];
	}

	public boolean isLeaf(){
		return numberOfChildren == 0 ? true : false;
	}

	public boolean getValue(){
		return value;
	}

	public void setValue(boolean value){
		this.value = value;
	}

	public Node getChild(char c){
		return childArray[getIndex(c)];
	}
	

	public void addChild(char c, boolean value){
		numberOfChildren++;
		childArray[getIndex(c)] = new Node(value);
	}

	public void removeChild(char c){
		numberOfChildren--;
		childArray[getIndex(c)] = null;
	}

	private int getIndex(char c){
		return (int)c - (int)'a';
	}

}
