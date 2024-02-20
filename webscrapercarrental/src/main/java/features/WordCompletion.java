package features;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

// TrieNode class datastructure
class TrieNode 
{
    TrieNode[] children;
    boolean wordEnd;

    public TrieNode() {
        // Initialize an array for using ASCII characters
        this.children = new TrieNode[128];
        this.wordEnd = false;
    }
}

// Trie class to execute  Trie operations
class Trie 
{
    public TrieNode root;

    // Constructor 
    public Trie() {
        this.root = new TrieNode();
    }

    // insert a word into the Trie
    public void insertNode(String word) {
        TrieNode nod = root;
        for (char ch : word.toCharArray()) {
            int index = ch;
            if (nod.children[index] == null) {
                nod.children[index] = new TrieNode();
            }
            nod = nod.children[index];
        }
        nod.wordEnd = true;
    }

    //  word suggestions for a given prefix
    public List<String> getSuggestions(String prefix) {
        TrieNode nod = findNode(prefix);
        System.out.println(nod);
        List<String> suggestions = new ArrayList<>();
        if (nod != null) {
            getAllWords(nod, prefix, suggestions);
        }

        // Sort based on (edit distance)
        suggestions.sort(Comparator.comparingInt(suggestion -> calculateEditDistance(prefix, suggestion)));


        return suggestions;
    }

    // help mtd  to find the node related  to a given prefix
    private TrieNode findNode(String prefix) {
        TrieNode nod = root;
        for (char ch : prefix.toCharArray()) {
            int index = ch;
            if (nod.children[index] == null) {
                return null;
            }
            nod = nod.children[index];
        }
        return nod;
    }

    // Helper mtd to retrieve all words
    private void getAllWords(TrieNode node, String currentPrefix, List<String> suggestions) {
        if (node.wordEnd) {
            String suggestionWithoutOrSimilar = removeOrSimilar(currentPrefix);
            suggestions.add(suggestionWithoutOrSimilar);
        }

        for (int i = 0; i < node.children.length; i++) {
            if (node.children[i] != null) {
                char ch = (char) i;
                getAllWords(node.children[i], currentPrefix + ch, suggestions);
            }
        }
    }

    // method 
    private String removeOrSimilar(String suggestion) {
        // Assuming "or similar" end of the suggestion
        return suggestion.replace(" or similar", "");
    }

    // mtd to find edit distance between two words
    private int calculateEditDistance(String word1, String word2) {
        int[][] dp = new int[word1.length() + 1][word2.length() + 1];

        for (int i = 0; i <= word1.length(); i++) {
            for (int j = 0; j <= word2.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    dp[i][j] = min(dp[i - 1][j - 1] + costOfSubstitution(word1.charAt(i - 1), word2.charAt(j - 1)),
                            dp[i - 1][j] + 1,
                            dp[i][j - 1] + 1);
                }
            }
        }

        return dp[word1.length()][word2.length()];
    }

    // mtd cost of substituting one character with another
    private int costOfSubstitution(char a, char b) {
        return a == b ? 0 : 1;
    }

    // find the minimum of a set of numbers
    private int min(int... numbers) {
        return java.util.Arrays.stream(numbers).min().orElse(Integer.MAX_VALUE);
    }
    

    public void helper() {
        suggesthelper(root, "");
    }
  
   static  ArrayList<String> a=new ArrayList<String>();
    private void suggesthelper(TrieNode node, String prefix)
    {
        if (node.wordEnd) {
          
            a.add(prefix);
        }
        for (char ch = 0; ch < 128; ch++) {
            if (node.children[ch] != null) {
                suggesthelper(node.children[ch], prefix + ch);
            }
        }
    }
    public   List<String> suggest(String input)
    {
    	
        helper();
       
        TrieNode node = root;
        List<String> suggestions = new ArrayList<>();
        for(int i=0;i<a.size();i++)
        {
           int n= calculateEditDistance(input,a.get(i));
          
            if(n==2|| n==1)
            	  suggestions.add(a.get(i));
        // Collect suggestions using Levenshtein distance
       
        }
        return suggestions;
    }

   
    
}

// WordCompletion class to provide word completion
public class WordCompletion 
{
    
    public static Trie trie;

    

    //  initialize the Trie with  a JSON file
    public static void initializeDictionaryFromJsonFile(String filename)
    {
        trie = new Trie();

        try {
            // Read the JSON file
            ObjectMapper objectMapper = new ObjectMapper();
            ArrayNode jsonArray = (ArrayNode) objectMapper.readTree(new File(filename));
            for (int i = 0; i < jsonArray.size(); i++) {
                trie.insertNode(jsonArray.get(i).get("name").asText().toLowerCase());
            }
        } catch (IOException e) {
           
            e.printStackTrace();
        }
    }
    // intial from filepath
    
    public static void initializeDictionary(String filePath) throws IOException 
    {
    	trie = new Trie();
		File file = new File(filePath);
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line;
		
		while ((line = reader.readLine()) != null) {
			// Split the line into words using non-word characters as delimiters
			for (String word : line.split("\\W+")) {
				if (!word.isEmpty()) {
					// Insert the lowercase version of the word into the trie
					trie.insertNode(word.toLowerCase());
				}
			}
		}
		reader.close();
	}

    // Method to get suggestions
    public static List<String> getSuggestions(String prefix) 
    
    {
        return trie.suggest(prefix);
    }

}
