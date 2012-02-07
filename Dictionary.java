import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class Dictionary {
	private Node root;

	Dictionary(){
		root = new Node(false);
	}

	Dictionary(String filename){
		root = new Node(false);
		Scanner scan = null;
		try {
			scan = new Scanner(new File(filename));
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
		}

		while(scan.hasNext())
			add(scan.next());
	}

	public void add(String word){
		add(word.toLowerCase(), root);
	}
	public String suggest(String word){
		return suggest(word.toLowerCase(), "", root);
	}


	private String suggest(String word, String suggest, Node node) {
		if (word.length() == 0){
			if (node.getValue())
				return suggest;

			for (char a = 'a'; a <= 'z'; a++){
				Node n = node.getChild(a);
				if (n != null)
					return suggest("", suggest + a, n);
			}
		}

		Node child = node.getChild(word.charAt(0));

		if(child == null)
			return suggest("", suggest, node);

		return suggest(word.substring(1), suggest + word.charAt(0), child);
	}
	public void print(){
		print(root, "");
	}

	public boolean check(String word){
		return check(word.toLowerCase(), root);
	}

	public boolean checkPrefix(String prefix){
		return checkPrefix(prefix.toLowerCase(), root);
	}

	public void remove(String word){
		word = word.toLowerCase();
		Node last = getLastNode(word, root);
		if (last == null)
			return;
		last.setValue(false);
		if (last.isLeaf())
			removeExtraNodes(word);
	}

	private void removeExtraNodes(String word){
		int length = word.length();
		if(length == 0)
			return;
		String wordMinusLastChar = word.substring(0, length-1);
		char lastChar = word.charAt(length-1);

		Node secondToLast = getLastNode(wordMinusLastChar, root);
		Node last = secondToLast.getChild(lastChar);
		if (last.getValue() || !last.isLeaf())
			return;

		secondToLast.removeChild(lastChar);
		removeExtraNodes(wordMinusLastChar);
	}

	private Node getLastNode(String word, Node node){
		if (word.length() == 0)
			return node;

		Node child = node.getChild(word.charAt(0));
		if(child == null)
			return null;

		return getLastNode(word.substring(1), child);
	}

	private boolean check(String word, Node node){
		if (word.length() == 0)
			return node.getValue();

		Node child = node.getChild(word.charAt(0));

		if(child == null)
			return false;

		return check(word.substring(1), child);
	}

	private boolean checkPrefix(String prefix, Node node){
		if (prefix.length() == 0)
			return true;

		Node child = node.getChild(prefix.charAt(0));

		if(child == null)
			return false;

		return checkPrefix(prefix.substring(1), child);
	}


	private void print(Node node, String string){
		if (node.isLeaf()){
			System.out.println(string);
			return;
		}

		if (node.getValue())
			System.out.println(string);

		for (char a = 'a'; a <= 'z'; a++){
			Node n = node.getChild(a);
			if (n != null)
				print(n, string + a);
		}
	}

	private void add(String word, Node node){
		char c = word.charAt(0);
		Node nextNode = node.getChild(c);

		if (word.length() == 1){
			if (nextNode == null)
				node.addChild(c, true);
			else
				nextNode.setValue(true);
			return;
		}

		if (nextNode == null)
			node.addChild(c, false);

		add(word.substring(1), node.getChild(c));
	}


}
