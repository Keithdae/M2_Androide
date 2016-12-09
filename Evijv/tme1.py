# -*- coding: utf-8 -*-
# tmeEvijv.py

import nltk, re, operator
from nltk.book import *
from nltk import probability
from nltk.corpus import udhr 

######################## Exercice 1 #################################

text5Freq = probability.FreqDist(text5)
sortedList = sorted(text5Freq.items(),key=operator.itemgetter(1),reverse=True)
mots4lettres = [w[0] for w in sortedList if len(w[0])==4]
#print mots4lettres


######################## Exercice 2 #################################

wordsHat = []
wordsZ = []
wordsPT = []
for i in set(text6):    
    reHat = re.search('.*(?i)hat$', i)
    if reHat != None:    
        wordsHat.append(reHat.group())
    
    reZ = re.search('.*(?i)z.*', i)
    if reZ != None:
        wordsZ.append(reZ.group())        
    
    rePT = re.search('.*(?i)pt.*', i)
    if rePT != None:
        wordsPT.append(rePT.group())

#print wordsHat
#print wordsZ
#print wordsPT


######################## Exercice 3 #################################

def countLetters(text):   
    letters = {}
    letterCount = 0
    for letter in text:
        if letter.isalpha():
            letter = letter.lower()
            letterCount += 1
            if letters.has_key(letter):
                letters[letter] += 1
            else:
                letters[letter] = 1
                
    print letters
    print letterCount
    
#countLetters(''.join(text5))
#print udhr.raw('French_Francais-Latin1')