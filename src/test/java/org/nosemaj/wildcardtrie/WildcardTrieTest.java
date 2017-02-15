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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.ImmutableSet;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Test the WildcardTrie class.
 */
public class WildcardTrieTest {
    private static final Character EXPECTED_WILDCARD_CHAR = '*';

    private static final String NONSENSE_STRING = "#4-nonsense blah!Z";

    private static final Set<String> EXPECTED_TEST_WORDS = ImmutableSet.of(
        "fun",
        "fund",
        "funds",
        "funding",
        "farm",
        "tunafish",
        "crowdfunding",
        "fun farm"
    );

    private WildcardTrie testObject;

    /**
     * Sets up the object under test.
     */
    @Before
    public void setup() {
        testObject = new WildcardTrie();

        testObject.addWords(EXPECTED_TEST_WORDS);
    }

    /**
     * Test that the no-args constructor used in setup has constructed
     * a WildcardTrie.
     */
    @Test
    public void testNoArgsConstructorCanConstruct() {
        assertNotNull(testObject);
        assertTrue(testObject instanceof WildcardTrie);
    }

    /**
     * Test that the character-arg constructor can construct a
     * WildcardTrie.
     */
    @Test
    public void testCharacterConstructorCanConstruct() {
        final WildcardTrie trie = new WildcardTrie('$');
        assertNotNull(trie);
        assertTrue(trie instanceof WildcardTrie);
    }

    /**
     * Test that the character-arg constructor can construct a
     * WildcardTrie even if the wildcard char that is passed in is null.
     *
     * TODO: this should mean "don't use a wildcard," probably.
     */
    @Test
    public void testCharacterConstructoNullArg() {
        assertNotNull(new WildcardTrie(null));
    }

    /**
     * Test that addWords() on null doesn't except; it should simply add
     * no words.
     */
    @Test
    public void testAddWordsNull() {
        testObject.addWords(null);
    }

    /**
     * Test that addWords() on an empty collection doesn't except; it
     * should just add no word.
     */
    @Test
    public void testAddWordsEmpty() {
        testObject.addWords(ImmutableSet.of());
    }

    /**
     * Test that trying to add empty words throws a RuntimeException.
     */
    @Test(expected = RuntimeException.class)
    public void testAddWordsSetOfEmpty() {
        testObject.addWords(ImmutableSet.of("", ""));
    }

    /**
     * Test that trying to add a set containing an empty word and an
     * otherwise okay word still won't work -- lowest common
     * denominator.
     */
    @Test(expected = RuntimeException.class)
    public void testAddWordsSetOfEmptyAndOk() {
        testObject.addWords(ImmutableSet.of("", "tunafishsandwich"));
    }

    /**
     * Test that addWords() using some real actual words (RAWs) doesn't
     * except.
     *
     * NOTE: this was already tested in setup().
     */
    @Test
    public void testAddWordsRealWords() {
        testObject.addWords(ImmutableSet.of(
            "ham",
            "sandwich"
        ));
    }

    /**
     * Test that an attempt to add a word-set again should be okay; the
     * method should be "idempotent" to repeated sets.
     */
    @Test
    public void testAddWordsRepeat() {
        final Set<String> WORD_SET = ImmutableSet.of(
            "hey",
            "hay"
        );

        testObject.addWords(WORD_SET);
        testObject.addWords(WORD_SET);
    }

    /**
     * Test that an attempt to add a word-set, one of which contains a
     * wildcard, will throw a RuntimeException.
     */
    @Test(expected = RuntimeException.class)
    public void testAddWordsOneContainsWildcard() {
        testObject.addWords(ImmutableSet.of(
            "something",
            "*********",
            "*"
        ));
    }

    /**
     * Test that addWord() input of null throws a RuntimeException.
     */
    @Test(expected = RuntimeException.class)
    public void testAddWordNull() {
        testObject.addWord(null);
    }

    /**
     * Test that addWord() input of empty throws a RuntimeException.
     */
    @Test(expected = RuntimeException.class)
    public void testAddWordEmpty() {
        testObject.addWord("");
    }

    /**
     * Test that addWord() with wildcard in it throws a
     * RuntimeException.
     */
    @Test(expected = RuntimeException.class)
    public void testAddWordWildcard() {
        testObject.addWord("**");
    }

    /**
     * Test that null is not a prefix.
     */
    @Test
    public void testIsPrefixNull() {
        assertFalse(testObject.isPrefix(null));
    }

    /**
     * Test that an empty string is not a prefix.
     */
    @Test
    public void testIsPrefixEmpty() {
        assertFalse(testObject.isPrefix(""));
    }

    /**
     * Test that "fun" is a prefix (since "funds" is also in the
     * expected test words.)
     */
    @Test
    public void testFunIsPrefixForFunds() {
        assertTrue(testObject.isPrefix("fun"));
    }

    /**
     * Test that a nonsense string (not a prefix, not a word) is not a
     * prefix.
     */
    @Test
    public void testIsPrefixNonsense() {
        assertFalse(testObject.isPrefix(NONSENSE_STRING));
    }

    /**
     * "tunafish" is a legit word, but it isn't a prefix -- test it.
     */
    @Test
    public void testIsPrefixValidWordButNotPrefix() {
        assertFalse(testObject.isPrefix("tunafish"));
    }

    /**
     * So long as there's anything beyond a single char-word in the
     * trie (that /is/ how we set it up) wildcard should be a prefix.
     */
    @Test
    public void testIsWildcardAPrefix() {
        assertTrue(testObject.isPrefix("*"));
    }

    /**
     * Test that null is not a word.
     */
    @Test
    public void testIsWordNull() {
        assertFalse(testObject.isWord(null));
    }

    /**
     * Test that an empty string is not a word.
     */
    @Test
    public void testIsWordEmpty() {
        assertFalse(testObject.isWord(""));
    }

    /**
     * Test that a nonsense string is not a word.
     */
    @Test
    public void testNonsenseIsNotAWord() {
        assertFalse(testObject.isWord(NONSENSE_STRING));
    }

    /**
     * "fun" is a prefix and it is also a complete word.
     */
    @Test
    public void testIsPrefixAndCompleteWordStillAWord() {
        assertTrue(testObject.isWord("fun"));
    }

    /**
     * "tunafish" is a complete word.
     */
    @Test
    public void testIsCompleteWordACompleteWord() {
        assertTrue(testObject.isWord("tunafish"));
    }

    /**
     * Test if a single wildcard matches any complete words.
     *
     * This should be false, since there are no complete words of length
     * one in the test dict.
     */
    @Test
    public void testIsWordSingleWildcard() {
        assertFalse(testObject.isWord("*"));
    }

    /**
     * Test if three repeated wildcards matches any complete words.
     *
     * This should be true since (at least) "fun" is in the test dict,
     * and it is a three-letter word.
     */
    @Test
    public void testIsWordThreeLetterWildcard() {
        assertTrue(testObject.isWord("***"));
    }

    /**
     * "f***" should match "fund" and "farm", both of which are words --
     * anyway, this compound of literal and wildcard is definitely a
     * legit thing, should be true.
     */
    @Test
    public void testIsWordWildcardCompound() {
        assertTrue(testObject.isWord("f***")); // :-O Language!
    }

    /**
     * Test that toString() is returning some at-least-half-baked
     * representation of the trie.
     */
    @Test
    public void testToStringIsAtLeastHalfBaked() {
        final String representation = testObject.toString();

        assertNotNull(representation);

        Set<Character> representationSet = new HashSet<>();

        for (int index = 0; index < representation.length(); index++) {
            representationSet.add(representation.charAt(index));
        }

        assertTrue(representationSet.containsAll(ImmutableSet.of(
            't', 'u', 'n', 'a'
        )));
    }

    /**
     * Test that toString() is mentioning all of the characters we put
     * into the trie.
     */
    @Test
    public void testToStringMentionsAllCharacters() {
        Set<Character> trieCharacters = new HashSet<>();

        // Put all of the chars into the set.
        for (final String word : EXPECTED_TEST_WORDS) {
            for (int index = 0; index < word.length(); index++) {
                trieCharacters.add(word.charAt(index));
            }
        }

        Set<Character> toStringCharacters = new HashSet<>();

        final String toStringValue = testObject.toString();

        for (int index = 0; index < toStringValue.length(); index++) {
            toStringCharacters.add(toStringValue.charAt(index));
        }

        toStringCharacters.containsAll(trieCharacters);
    }

    /**
     * Test that input of null returns no match.
     */
    @Test
    public void testGetMatchingWordsNull() {
        final Set<String> matches =
            testObject.getMatchingWords(null);

        assertNotNull(matches);
        assertTrue(matches.isEmpty());
    }

    /**
     * Test that input of empty returns no match.
     */
    @Test
    public void testGetMatchingWordsEmpty() {
        final Set<String> matches =
            testObject.getMatchingWords("");

        assertNotNull(matches);
        assertTrue(matches.isEmpty());
    }

    /**
     * Test that input of a literal that exists in the dict returns that
     * same word as a match.
     */
    @Test
    public void testGetMatchingWordsFunIsFun() {
        final List<String> matches =
            testObject.getMatchingWords("fun").stream()
                .collect(Collectors.toList());

        assertNotNull(matches);
        assertEquals(1, matches.size());

        final String maybeFun = matches.get(0);
        assertEquals("fun", maybeFun);
    }

    /**
     * Test that the words matching four wildcard chars are the two
     * four-letter words that are present (fund, farm).
     */
    @Test
    public void testGetMatchingWordsCanGetFourLetterWords() {
        final Set<String> matches =
            testObject.getMatchingWords("****");

        assertNotNull(matches);
        assertEquals(2, matches.size());

        assertTrue(matches.containsAll(ImmutableSet.of(
            "fund", "farm"
        )));
    }

    /**
     * Test wildcard as first char.
     */
    @Test
    public void testGetMatchingWordsWildcardAtFront() {
        final Set<String> matches =
            testObject.getMatchingWords("*unafish");

        assertNotNull(matches);
        assertEquals(1, matches.size());
        assertTrue(matches.contains("tunafish"));
    }

    /**
     * Test wildcard was last char.
     */
    @Test
    public void testGetMatchingWordsWildcardAtBack() {
        final Set<String> matches =
            testObject.getMatchingWords("tunafis*");

        assertNotNull(matches);
        assertEquals(1, matches.size());
        assertTrue(matches.contains("tunafish"));
    }

    /**
     * Test getMatchingWords() on an empty trie.
     */
    @Test
    public void testGetMatchingWordsOnEmptyTrie() {
        WildcardTrie trie = new WildcardTrie();

        assertTrue(trie.getMatchingWords("*").isEmpty());
    }
}

