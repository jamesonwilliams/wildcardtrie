[![Build Status](https://travis-ci.org/jamesonwilliams/wildcardtrie.svg?branch=master)](https://travis-ci.org/jamesonwilliams/wildcardtrie)

# wildcardtrie
A Trie data structure which can be searched using wild characters.

## Build and Run
```
mvn package
./lookup.sh '*oor'
```

Outputs:
```
5 words match *oor in provided dict:
Moor
door
moor
poor
boor
```

## Lookup Usage
```
./lookup.sh <SEARCH_TERM> [DICTIONARY_FILE_PATH]

  SEARCH_TERM           Any string of literals along with zero or more *
                        characters. * is the wild character (a single
                        character glob.)

  DICTIONARY_FILE_PATH  Path to a dictionary file, e.g. /usr/share/dict/words.
```
