package client.notVisible;

import static org.junit.Assert.*;
import org.junit.*;

public class SpellCorrectorTests{
	
	SpellCorrector sc;
	Trie trie;
	
	@Before
	public void before(){
		
		sc = new SpellCorrector();
		trie = new Trie();
		trie.add("Bill");
		trie.add("Matt");
		trie.add("Ray");
		trie.add("Tommy");
		trie.add("Jon");
		trie.add("Tyler");
		trie.add("Johnathon");
		trie.add("Sam");
		trie.add("Shoutouttothebesttasever");
	}
	
	@Test
	public void testCorrectWord(){
		
		assertNull(sc.suggestSimilarWord("Shoutouttothebesttasever", trie));
	}
	
	@Test
	public void testOneOffWord(){
		
		assertEquals(sc.suggestSimilarWord("Johnatho", trie).size(), 1);
	}
	
	@Test
	public void testTwoOffWord(){
		
		assertEquals(sc.suggestSimilarWord("bad", trie).size(), 2);
	}
	
	@Test
	public void testNoSuggestions(){
		
		assertEquals(sc.suggestSimilarWord("Habbukuk", trie).size(), 0);
	}
			
}
