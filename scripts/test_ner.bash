#! /usr/bin/bash

curl -X POST http://localhost:4567/ner                         \
  -d '{"doc":["Jack and Jill did nt go up the hill ."],  \
       "props":{                                               \
         "annotators":"tokenize,ssplit,pos,lemma,ner,parse",   \
         "tokenize.whitespace":"true",                         \
         "ssplit.eolonly":"true"                               \
       }                                                       \
      }'


# \nHowever , Jill fell down .


