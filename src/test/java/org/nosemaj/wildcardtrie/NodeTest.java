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

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Test the Node.
 */
public class NodeTest {
    private static final Character TEST_CHAR = 't';
    private static final int EXPECTED_DEFAULT_CHILDREN_COUNT = 0;

    private Node testObject;

    /**
     * Setup the object under test.
     */
    @Before
    public void setup() {
        testObject = new Node(TEST_CHAR);
    }

    /**
     * Test that the character-arg constructor can construct a Node.
     */
    @Test
    public void testCharacterConstructorCanConstruct() {
        assertNotNull(testObject);
        assertTrue(testObject instanceof Node);
    }

    /**
     * Test that the no-args constructor can construct a Node.
     */
    @Test
    public void testNoArgsConstructorCanConstruct() {
        final Node node = new Node();
        assertNotNull(node);
        assertTrue(node instanceof Node);
    }

    /**
     * Test that, by default, Node contains an empty map of children.
     */
    @Test
    public void testGetChildrenDefault() {
        final Map<Character, Node> actualChildren = testObject.getChildren();

        assertNotNull(actualChildren);

        assertEquals(
            EXPECTED_DEFAULT_CHILDREN_COUNT,
            actualChildren.entrySet().size()
        );
    }

    /**
     * Test the getCharacter() getter.
     */
    @Test
    public void testGetCharacter() {
        assertEquals(
            TEST_CHAR,
            testObject.getCharacter()
        );
    }

    /**
     * Test the getting and setting of the completeWord field.
     */
    @Test
    public void testGetSetCompleteWord() {
        testObject.setCompleteWord(false);
        assertFalse(testObject.isCompleteWord());

        testObject.setCompleteWord(true);
        assertTrue(testObject.isCompleteWord());
    }

    /**
     * Test the toString().
     */
    @Test
    public void testToString() {
        assertNotNull(testObject.toString());

        assertTrue(testObject.toString().contains(String.valueOf(TEST_CHAR)));
    }

    /**
     * Test child mappings when a Node is hooked up to some others.
     */
    @Test
    public void testMappedNode() {
        testObject.getChildren().put('a', new Node('a'));
        testObject.getChildren().put('z', new Node('z'));

        assertTrue(
            testObject.getChildren().keySet().containsAll(
                ImmutableSet.of('a', 'z')
            )
        );

        assertEquals(
            Character.valueOf('a'),
            testObject.getChildren().get('a').getCharacter()
        );

        assertEquals(
            Character.valueOf('z'),
            testObject.getChildren().get('z').getCharacter()
        );

        assertFalse(
            testObject.getChildren().values().contains(null)
        );
    }
}

