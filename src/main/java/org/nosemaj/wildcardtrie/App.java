/*
 * Copyright (C) 2017 nosemaj.org
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.nosemaj.wildcardtrie;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Set;

/**
 * Run the WildcardTrie from the command line.
 */
public class App {
 
    /**
     * Loads a WildCard trie from the UNIX standard words file in
     * args[0], searching for a term specified in args[1].
     *
     * @param args PATH_TO_DICTIONARY SEARCH_TERM
     */
    public static void main(String[] args) {
        final WildcardTrie trie = new WildcardTrie();
        final String dictionaryPath = args[0];
        final String searchTerm = args[1];

        loadTrie(trie, dictionaryPath);

        final Set<String> matchingWords =
            trie.getMatchingWords(searchTerm);

        System.out.println(
            matchingWords.size() + " words match "
            + searchTerm + " in provided dict:\n"
            + String.join("\n", matchingWords)
        );
    }

    /**
     * Loads the words in the dictionary file into the trie.
     *
     * @param trie the trie to load up
     * @param dictionaryPath path to UNIX standard dictionary file
     */
    public static void loadTrie(
            final WildcardTrie trie,
            final String dictionaryPath) {

        final File file = new File(dictionaryPath);

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = br.readLine()) != null) {
                trie.addWord(line);
            }
        } catch (Exception anyException) {
            System.err.println(anyException.getMessage());
        }
    }
}

