#!/bin/bash

readonly DEFAULT_SEARCH_TERM='pot*to'
readonly DEFAULT_DICTIONARY='/usr/share/dict/words'

readonly dictionary="${2-$DEFAULT_DICTIONARY}"
readonly search_term="${1-$DEFAULT_SEARCH_TERM}"

java \
    -cp target/wildcardtrie-1.0-SNAPSHOT.jar \
    org.nosemaj.wildcardtrie.App \
    "$dictionary" \
    "$search_term"
