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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * WildCardTrie is an implementation of a Trie which additionally
 * supports search terms that include a wildcard character.
 */
public class WildcardTrie {
    private static final Character DEFAULT_WILDCARD = '*';

    private final Character wildcard;
    private final Node root;

    /**
     * Constructs a new WildcardTrie.
     *
     * @param wildcard the character to use as a single-character glob
     */
    public WildcardTrie(final Character wildcard) {
        this.wildcard = wildcard;
        this.root = new Node();
    }

    /**
     * Constructs a new WildcardTrie, using the default wildcard
     * character.
     */
    public WildcardTrie() {
        this(DEFAULT_WILDCARD);
    }

    /**
     * Adds a set of words to the trie.
     *
     * @param words the words to add to the trie. Each word must be
     *              non-empty and may not contain a wildcard character.
     *
     * @throws RuntimeException
     *         if any of the provided words cannot be added
     */
    public void addWords(final Set<String> words) {
        if (null != words) {
            words.forEach(word -> addWord(word));
        }
    }

    /**
     * Adds a word to the trie.
     *
     * @param word the word to add to the trie. Must be non-empty and
     *             may not contain a wildcard character.
     *
     * @throws RuntimeException
     *         if the provided {@code word} cannot be added
     */
    public void addWord(final String word) {
        if (null == word || word.isEmpty()
                || word.contains(String.valueOf(wildcard))) {

            throw new RuntimeException(
                "Passed invalid word (" + word + ") to addWord()."
            );
        }

        Node currentNode = root;

        for (int index = 0; index < word.length(); index++) {
            final Character currentChar = word.charAt(index);
            final Map<Character, Node> children = currentNode.getChildren();

            if (!children.containsKey(currentChar)) {
                children.put(currentChar, new Node(currentChar));
            }
            
            currentNode = children.get(currentChar);
        }

        currentNode.setCompleteWord(true);
    }

    /**
     * Checks if the specified search expression (including zero or more
     * wildcard characters) matches one or more prefixes.
     *
     * @param searchExpression the search expression to evaluate as
     *                         potentially being mapped to one or more
     *                         word prefixes.
     *
     * @return true if a matching prefix exists; false, otherwise.
     */
    public boolean isPrefix(final String prefix) {
        /*
         * A prefix isn't just anything that isn't a complete word.
         * Consider "potato" and "potatos", both complete words.
         * "potato" is a complete word AND a prefix, to "potatos".
         */
        for (final Node node : findNodes(prefix)) {
            if (null != node && !node.getChildren().keySet().isEmpty()) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks if the specified search expression (including zero or more
     * wildcard characters) matches one or more complete words.
     *
     * @param searchExpression the search expression to evaluate as
     *                         potentially mapped to one or more
     *                         complete words.
     *
     * @return true if there is at least one complete, matching word;
     *         false, otherwise.
     */
    public boolean isWord(final String searchExpression) {
        /*
         * This says: "The collection of nodes (that end complete words)
         * is not an empty collection."
         */
        return !findNodes(searchExpression).stream()
            .filter(node -> node.isCompleteWord())
            .collect(Collectors.toSet())
            .isEmpty();
    }

    /**
     * Walks the Trie to find the collection of nodes reachable by a
     * specified search term.
     *
     * @param searchTerm the term to lookup in the trie -- may contain
     *                   zero or more wildcard characters.
     *
     * @return the set of nodes reachable by the search term
     */
    private Set<Node> findNodes(final String searchTerm) {
        return findNodes(root, searchTerm, 0);
    }

    /**
     * Walks the Trie to find the collection of nodes reachable by a
     * specified search term.
     *
     * @param startNode the node from which to start the walk
     * @param searchTerm the term we are using to search
     * @param startIndex the index into the searchTerm for the current
     *                   recursion
     *
     * @return The set of nodes reachable when walking the trie
     *         according to the specified search term.
     */
    private Set<Node> findNodes(
            final Node startNode,
            final String string,
            final int index) {

        final Set<Node> matchingNodes = new HashSet<>();

        if (startNode == null || string == null
                || string.isEmpty() || index >= string.length()) {
            return matchingNodes;
        }

        final Character currentChar = string.charAt(index);

        // Base case: there is one character remaining to process, so
        // just do it.
        if (string.length() - index == 1) {
            if (wildcard.equals(currentChar)) {
                matchingNodes.addAll(startNode.getChildren().values());
            } else {
                matchingNodes.add(startNode.getChildren().get(currentChar));
            }

            return matchingNodes;
        }

        // Otherwise, more than one element left. So recurse.
        if (!wildcard.equals(currentChar)) {
            return findNodes(
                startNode.getChildren().get(currentChar),
                string,
                index + 1
            );
        }

        // The character being processed is a wildcard. Find all nodes
        // reachable by its children.
        for (final Node nextNode : startNode.getChildren().values()) {
            matchingNodes.addAll(findNodes(
                nextNode,
                string,
                index + 1
            ));
        }

        return matchingNodes;
    }

    /**
     * Gets the set of complete words that match the given search term.
     *
     * @param searchTerm the term to lookup -- may contain zero or more
     *                   wildcard characters
     *
     * @return the set of complete words which match the search term;
     *         may be empty, if none match.
     */
    public Set<String> getMatchingWords(final String searchTerm) {
        return getMatchingWords(root, searchTerm, "", 0);
    }

    /**
     * Gets the set of complete words that match the given search term.
     *
     * @param startNode the node at which to start the search
     * @param searchTerm the term to lookup -- may contain zero or more
     *                   wildcard characters
     * @param prefix the valid prefix of a complete word being processed
     * @param index the index into {@code searchTerm}, used during
     *              recurion
     */
    private Set<String> getMatchingWords(
            final Node startNode,
            final String searchTerm,
            final String prefix,
            final int index) {

        final Set<String> matchingWords = new HashSet<>();

        if (null == searchTerm || searchTerm.isEmpty()) {
            return matchingWords;
        }

        // Base case: we are done processing characters in the search
        // term, so if we have found a complete word, just return it.
        if (searchTerm.length() == index) {
            if (startNode.isCompleteWord()) {
                matchingWords.add(prefix);
            }

            return matchingWords;
        }

        final Character character = searchTerm.charAt(index);

        // We're not done processing characters, so continue the
        // recursion for the next child, from this non-wildcard
        // character.
        if (!wildcard.equals(character)) {
            if (startNode.getChildren().containsKey(character)) {
                matchingWords.addAll(getMatchingWords(
                    startNode.getChildren().get(character),
                    searchTerm,
                    prefix + character,
                    index + 1
                ));
            }

            return matchingWords;
        }

        // We're not done processing characters, and we got a wildcard.
        // Get all matching words of this node's children.
        for (final Map.Entry<Character, Node> entry : startNode.getChildren().entrySet()) {
            matchingWords.addAll(getMatchingWords(
                entry.getValue(),
                searchTerm,
                prefix + entry.getKey(),
                index + 1
            ));
        }

        return matchingWords;
    }

    /**
     * Gets a string representation of the WildcardTrie.
     *
     * @return a string representation of the trie
     */
    public String toString() {
        final StringBuilder builder = new StringBuilder();

        for (final Node node : traverse()) {
            builder.append(node);
        }

        return builder.toString();
    }

    /**
     * Performs a breadth-first traversal of the Trie and returns the
     * list of nodes encountered.
     * 
     * @return a level-ordered list of the nodes in the Trie
     */
    private List<Node> traverse() {
        final Queue<Node> queue = new LinkedList<>();
        final List<Node> nodes = new ArrayList<>();

        queue.add(root);

        while (!queue.isEmpty()) {
            final Node current = queue.remove();
            nodes.add(current);

            for (final Node child : current.getChildren().values()) {
                if (null != child) {
                    queue.add(child);
                }
            }
        }

        return nodes;
    }
}

