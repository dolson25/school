package client.notVisible;

public class Trie{
	
	Node root;
	
	private int wordCount;
	private int nodeCount;
	private int charIndex;
	private Node currNode;
	
	public Trie()
	{
		wordCount = 0;
		nodeCount = 1;
		root = new Node();
	}

	public void add(String word) 
	{
		charIndex = 0;
		currNode = root;	
		
		recursiveAdd(word);
	}
	
	public void recursiveAdd(String word)
	{
		char c = word.charAt(charIndex);
		
		if((c < 91) && (c > 64))
			c = (char)(c + 32);
		
		if(c == 32)
			c = 123;
		
		if(c == 45)
			c = 124;

		
		if(word.length() == (charIndex + 1))
		{
			if(currNode.alphabet[c - 'a'] != null)
			{
				currNode = currNode.alphabet[c - 'a'];
			}
			else
			{
				currNode.alphabet[c - 'a'] = new Node();
				nodeCount++;
				currNode = currNode.alphabet[c - 'a'];
			}
			currNode.frequency++;
			return;
		}
		else
		{
			if(currNode.alphabet[c - 'a'] != null)
			{
				currNode = currNode.alphabet[c - 'a'];
			}
			else
			{
				currNode.alphabet[c - 'a'] = new Node();
				nodeCount++;
				currNode = currNode.alphabet[c - 'a'];
			}
			
			charIndex++;
			recursiveAdd(word);
		}
	}

	public boolean find2(String word) 
	{
		if(word.equals(""))
		{
			return false;
		}
		
		charIndex = 0;
		currNode = root;	
		
		Trie.Node temp = recursiveFind(word);
		if(temp != null)
			return true;
		return false;
	}

	public Trie.Node find(String word) 
	{
		if(word.equals(""))
		{
			return null;
		}
		
		charIndex = 0;
		currNode = root;	
		
		return recursiveFind(word);
		
	}
	
	public Node recursiveFind(String word)
	{
		char c = word.charAt(charIndex);
		
		if(c == 32)
			c = 123;
		
		if(c == 45)
			c = 124;


		if(currNode.alphabet[c - 'a'] == null)
		{
			return null;			
		}
		else
		{
			currNode = currNode.alphabet[c - 'a'];
			if(word.length() == (charIndex + 1))
			{
				if(currNode.getValue() > 0)
				{
					return (Node)currNode;
				}
				else
					return null;
			}
			else
			{
				charIndex++;
				return recursiveFind(word);
			}
		}
	}

	public int getWordCount() 
	{
		return wordCount;
	}
	
	public int getNodeCount() 
	{
		return nodeCount;
	}

	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		currNode = root;	
		return (recursiveToString(sb, "", root)).toString();
	}
	
	public StringBuilder recursiveToString(StringBuilder alphaList,String thisString, Node currentNode) 
	{
		if(currentNode.getValue() > 0)
			alphaList.append(thisString + "\n");
		for(int i = 0; i < 26; i++)
		{
			if(currentNode.alphabet[i] != null)
			{
				String temp = thisString + (char)(i + 'a');
				recursiveToString(alphaList, temp, currentNode.alphabet[i]);
			}
		}
		return alphaList;
	}
	
	@Override
	public int hashCode()
	{
		return wordCount * nodeCount * 7919;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(o == null)
			return false;
		if(this == o)
			return true;
		if(!(this.getClass() == o.getClass()))
			return false;
		
		Trie temp = (Trie)o;
		
		if(this.wordCount != temp.wordCount)
			return false;
		if(this.nodeCount != temp.nodeCount)
			return false;
		
		Node currentNode1 = this.root;
		Node currentNode2 = temp.root;
		
		return(recursiveEquals(currentNode1, currentNode2));
	}
	
	public boolean recursiveEquals(Node cn1, Node cn2)
	{	
		
		if (cn1.getValue() != cn2.getValue())
			return false;
		
		for(int i = 0; i < 26; i++)
		{
			if(cn1.alphabet[i] == null)
			{
				if(cn2.alphabet[i] != null)
				{	
					return false;
				}
			}
			else if(cn2.alphabet[i] == null)
			{
				return false;
			}
			else
			{
				if(recursiveEquals(cn1.alphabet[i], cn2.alphabet[i]) == false)
					return false;
			}

		}
		
		return true;
	}

	public class Node
	{
		
		private int frequency;
		
		Node[] alphabet;
		
		public Node()
		{
			alphabet = new Node[28];
			frequency = 0;
		}
	

		public int getValue()
		{
			return frequency;
		}
	}

}


