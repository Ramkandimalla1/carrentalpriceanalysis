package features;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

// SpellChecking class 
public class SpellChecking {

	// Define alphabet size
	private static final int ALPHABET_S = 26;

	// TrieNode class representing a node in the trie
	private static TrieNode tRoot = new TrieNode();

	// Inner TrieNode class
	private static class TrieNode {
		TrieNode[] tChildren = new TrieNode[ALPHABET_S];
		boolean wordEnding = false;
	}

	// Insert word into trie
	public static void insertingWord(String word) {
		TrieNode nod = tRoot;
		for (int i = 0; i < word.length(); i++) {
			char c = word.charAt(i);
			// condition for char weather lowercase or not
			if (c < 'a' || c > 'z') {
				continue;
			}
			int indexOfNode = c - 'a';
			// Creating node if not present
			if (nod.tChildren[indexOfNode] == null) {
				nod.tChildren[indexOfNode] = new TrieNode();
			}
			nod = nod.tChildren[indexOfNode];
		}
		nod.wordEnding = true;
	}

	// search for a word in the trie
	public static boolean search(String word) {
		TrieNode tNode = tRoot;
		for (int i = 0; i < word.length(); i++) {
			int indexOfNode = word.charAt(i) - 'a';
			// Return false if the node for the current char doesn't exist
			if (tNode.tChildren[indexOfNode] == null) {
				return false;
			}
			tNode = tNode.tChildren[indexOfNode];
		}
		// Check if the node represents the end of a word
		return tNode != null && tNode.wordEnding;
	}

	// initialize the dictionary from a file
	public static void initializeDictionary(String filePath) throws IOException {
		File file = new File(filePath);
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line;
		// Read each line from the file
		while ((line = reader.readLine()) != null) {
			// Split the line into words using non-word characters as delimiters
			for (String word : line.split("\\W+")) {
				if (!word.isEmpty()) {
					// Insert the lowercase version of the word into the trie
					insertingWord(word.toLowerCase());
				}
			}
		}
		reader.close();
	}

	// Method to check the spelling of a word
	public static boolean checkSpelling(String word) {
		// Search for the lowercase version of the word in the trie
		return search(word.toLowerCase());
	}

	
}
