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

import java.util.HashMap;
import java.util.Map;

/**
 * A node in the Trie.
 */
public class Node {
    private Character character;
    private boolean completeWord;
    private Map<Character, Node> children;

    /**
     * Constructs a new Node.
     */
    public Node() {
        this(null);
    }

    /**
     * Construct a new Node corresponding to a given character.
     *
     * @param character the character this Node represents
     */
    public Node(final Character character) {
        this.character = character;
        this.completeWord = false;
        this.children = new HashMap<>();
    }

    /**
     * Gets the children of this node.
     *
     * @return the key-value map of next characters to the nodes that
     *         represent them
     */
    public Map<Character, Node> getChildren() {
        return children;
    }

    /**
     * Gets the character that the node represents.
     *
     * @return the character that the node represents
     */
    public Character getCharacter() {
        return character;
    }

    /**
     * Sets this node to represent the end of a complete word.
     *
     * @param completeWord whether or not this node delimits a complete
     *                     word.
     */
    public void setCompleteWord(final boolean completeWord) {
        this.completeWord = completeWord;
    }

    /**
     * Checks whether or not this node delimits a complete word.
     *
     * @return true if this node delimits a complete word; false,
     *         otherwise
     */
    public boolean isCompleteWord() {
        return completeWord;
    }

    /**
     * Gets a string representation of this node.
     *
     * @return a string representation of this node
     */
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("[");
        builder.append(character);
        builder.append(" ->");
        
        for (final Character key : children.keySet()) {
            builder.append(" " + key);
        }

        builder.append("]");

        return builder.toString();
    }
}

