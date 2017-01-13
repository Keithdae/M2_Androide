# -*- coding: utf-8 -*-
"""
Created on Fri Jan 13 00:08:59 2017

@author: daniel
"""

import nltk, operator
from nltk import probability
from nltk import tokenize
from nltk.tokenize import TweetTokenizer
from nltk import data

f = open('corpus.txt')
raw = f.read()

tokenizer = TweetTokenizer()
tokenizedCorpus = tokenizer.tokenize(raw)
 
corpusFreq = probability.FreqDist(tokenizedCorpus)
sortedList = sorted(corpusFreq.items(),key=operator.itemgetter(1),reverse=True)

longwords = [w for w in set(tokenizedCorpus) if len(w)>9]

tagged = nltk.pos_tag(tokenizedCorpus)

tagFreq = nltk.FreqDist(tag for (word, tag) in tagged)
sortedTag = sorted(tagFreq.items(),key=operator.itemgetter(1),reverse=True)
print sortedTag

#corpusFreq.plot(20,cumulative=True)