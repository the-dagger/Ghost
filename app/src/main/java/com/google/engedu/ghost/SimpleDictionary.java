package com.google.engedu.ghost;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class SimpleDictionary implements GhostDictionary {
    private ArrayList<String> words;

    public SimpleDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        words = new ArrayList<>();
        String line = null;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            if (word.length() >= MIN_WORD_LENGTH)
              words.add(line.trim());
        }
    }

    @Override
    public boolean isWord(String word) {
        return words.contains(word);
    }

    @Override
    public String getAnyWordStartingWith(String prefix) {
        return null;
    }

    @Override
    public String getGoodWordStartingWith(String prefix) {
        return gawswBinarySearch(0, words.size() - 1, prefix);
    }

    private String gawswBinarySearch(int left, int right, String prefix){
        if ( left > right )
            return null;

        int mid = (left + right) / 2;

        if (words.get(mid).startsWith(prefix) )
            return words.get(mid);
        else if (prefix.compareToIgnoreCase(words.get(mid)) < 0 )
            return gawswBinarySearch(left, mid - 1, prefix);
        else
            return gawswBinarySearch(mid + 1, right, prefix);
    }
}
