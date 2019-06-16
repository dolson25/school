package client.notVisible;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class SpellCorrector {
	
	Trie trie;
	ArrayList<String> correctWords = new ArrayList<String>();
	ArrayList<Trie.Node> correctWordsNodes = new ArrayList<Trie.Node>();
	ArrayList<StringBuilder> editedOnceWords = new ArrayList<StringBuilder>();
	
	public SpellCorrector()
	{
		
	}

	public void useDictionary(String dictionaryFileName) throws IOException
	{
		StringBuilder sb = new StringBuilder();
        try {
        	URL url = new URL(dictionaryFileName);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            in.close();
        }
        catch (MalformedURLException e) {
            System.out.println("Malformed URL: " + e.getMessage());
        }
        catch (IOException e) {
            System.out.println("I/O Error: " + e.getMessage());
        }

        String knownValues = sb.toString();
        
        String[] words = knownValues.split(",");
        
        trie = new Trie();
        
        for(int i = 0; i < words.length;i++)
        {
        	String temp = words[i].toLowerCase();
        	trie.add(temp);
        }
	}

	public Set<String> suggestSimilarWord(String inputWord, Trie trie)
		{
		Set<String> output = new HashSet<String>();

		inputWord = inputWord.toLowerCase();
		
		if(trie.find2(inputWord))
		{
			return null;
		}
		else
		{
			StringBuilder badWord = new StringBuilder(inputWord);
			firstDeletions(badWord, trie);
			firstTranspositions(badWord, trie);
			firstAlterations(badWord, trie);
			firstInsertions(badWord, trie);

			for(int i = 0; i<editedOnceWords.size(); i++)
			{
				secondDeletions(editedOnceWords.get(i), trie);
				secondTranspositions(editedOnceWords.get(i), trie);
				secondAlterations(editedOnceWords.get(i), trie);
				secondInsertions(editedOnceWords.get(i), trie);		
			}
				
			for(int i = 0; i< correctWords.size();i++)
			{
				output.add(correctWords.get(i));
			}
			return output;					
		}
	}
		
	public String getBestWord()
	{
		int highestFrequency = 0;
		int highestFrequencyValue = 0;
	
		for(int i = 0; i < correctWords.size(); i++)
		{
			if(correctWordsNodes.get(i).getValue() == highestFrequency)
			{
				if(correctWords.get(i).compareTo(correctWords.get(highestFrequencyValue)) < 0)
				{
					highestFrequency = correctWordsNodes.get(i).getValue();
					highestFrequencyValue = i;
				}
			}
			if(correctWordsNodes.get(i).getValue() > highestFrequency)
			{
				highestFrequency = correctWordsNodes.get(i).getValue();
				highestFrequencyValue = i;
			}
		}
		
		return correctWords.get(highestFrequencyValue);
	}
	
	public void firstDeletions(StringBuilder word, Trie trie)
	{
		for(int i = 0; i < word.length(); i++)
		{
			StringBuilder temp = new StringBuilder(word);
			StringBuilder possibleWord = temp.deleteCharAt(i);
			
			if(trie.find(possibleWord.toString()) != null)
			{
				correctWordsNodes.add(trie.find(possibleWord.toString()));
				correctWords.add(possibleWord.toString());
			}
			else
				editedOnceWords.add(possibleWord);
				
		}
	}
	
	public void firstTranspositions(StringBuilder word, Trie trie)
	{
		for(int i = 0; i < word.length() - 1; i++)
		{
			StringBuilder temp = new StringBuilder(word);

			String s1 = temp.charAt(i) + "";
			String s2 = temp.charAt(i+1) + "";
			
			temp.replace(i, i+1, s2);
			temp.replace(i + 1, i + 2, s1);
			
			if(trie.find(temp.toString()) != null)
			{
				correctWordsNodes.add(trie.find(temp.toString()));
				correctWords.add(temp.toString());
			}
			else
				editedOnceWords.add(temp);
		}
	}
	
	public void firstAlterations(StringBuilder word, Trie trie)
	{
		for(int i = 0; i < word.length(); i++)
		{
			for(int j = 97; j < 123; j++)
			{
				char c = (char)j;
				String replaceString = c + ""; 
				
				StringBuilder temp = new StringBuilder(word);
				StringBuilder possibleWord = temp.replace(i, i + 1, replaceString);
				
				
				if(trie.find(possibleWord.toString()) != null)
				{
					correctWordsNodes.add(trie.find(possibleWord.toString()));
					correctWords.add(possibleWord.toString());
				}
				else
					editedOnceWords.add(possibleWord);			
			}
			
		

				
		}
	}
	
	public void firstInsertions(StringBuilder word, Trie trie)
	{
		for(int i = 0; i < word.length() + 1; i++)
		{
			for(int j = 97; j < 123; j++)
			{
				char c = (char)j;
				
				StringBuilder temp = new StringBuilder(word);
				StringBuilder possibleWord = temp.insert(i,c);

				if(trie.find(possibleWord.toString()) != null)
				{
					correctWordsNodes.add(trie.find(possibleWord.toString()));
					correctWords.add(possibleWord.toString());
				}
				else
					editedOnceWords.add(possibleWord);				
			}
			
		

				
		}
	}
	
	
	public void secondDeletions(StringBuilder word, Trie trie)
	{
		for(int i = 0; i < word.length(); i++)
		{
			StringBuilder temp = new StringBuilder(word);
			StringBuilder possibleWord = temp.deleteCharAt(i);
			
			if(trie.find(possibleWord.toString()) != null)
			{
				correctWordsNodes.add(trie.find(possibleWord.toString()));
				correctWords.add(possibleWord.toString());
			}				
		}
	}
	
	public void secondTranspositions(StringBuilder word, Trie trie)
	{
		for(int i = 0; i < word.length() - 1; i++)
		{
			StringBuilder temp = new StringBuilder(word);

			String s1 = temp.charAt(i) + "";
			String s2 = temp.charAt(i+1) + "";
			
			temp.replace(i, i+1, s2);
			temp.replace(i + 1, i + 2, s1);
			
			if(trie.find(temp.toString()) != null)
			{
				correctWordsNodes.add(trie.find(temp.toString()));
				correctWords.add(temp.toString());
			}
		}
	}
	
	public void secondAlterations(StringBuilder word, Trie trie)
	{
		for(int i = 0; i < word.length(); i++)
		{
			for(int j = 97; j < 123; j++)
			{
				char c = (char)j;
				String replaceString = c + ""; 
				
				StringBuilder temp = new StringBuilder(word);
				StringBuilder possibleWord = temp.replace(i, i + 1, replaceString);
				
				
				if(trie.find(possibleWord.toString()) != null)
				{
					correctWordsNodes.add(trie.find(possibleWord.toString()));
					correctWords.add(possibleWord.toString());
				}			
			}
				
		}
	}
	
	public void secondInsertions(StringBuilder word, Trie trie)
	{
		for(int i = 0; i < word.length() + 1; i++)
		{
			for(int j = 97; j < 123; j++)
			{
				char c = (char)j;
				
				StringBuilder temp = new StringBuilder(word);
				StringBuilder possibleWord = temp.insert(i,c);
				
				
				if(trie.find(possibleWord.toString()) != null)
				{
					correctWordsNodes.add(trie.find(possibleWord.toString()));
					correctWords.add(possibleWord.toString());
				}			
			}	
				
		}
	}

}
