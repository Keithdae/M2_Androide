use_module(library(jpl)).

/* 
	L'objectif est ici de definir si notre agent est dans un bonne situation, et quelle action choisir pour ameliorer notre situation
	
	Situations :
	badSituation(X)
	goodSituation(X)
	perfectSituation(X)
	
	Predicats :
	highGround(X) : indique que l'agent est en haute altitude
	smallFoW(X) : indique que l'agent dispose d'un champ de vision tres limite
	largeFoW(X) : indique que l'agent dispose d'un champ de vision large
	enemyInSight(X) : indique que l'agent voit un ennemy
	heightOverAverage(X) : indique si l'agent dispose d'une altitude plus eleve que l'environnement qu'il observe (maxima local), non lie a highGround
	lowHealth(X) : indique si l'agent meurt au prochain tir
	
	Non confirmes : (Utilite discutable)
	highHealth(X) : indique si l'agent n'a pas subit plus d'un tir
*/


:-dynamic
	badSituation/1,
	goodSituation/1,
	perfectSituation/1,
	highGround/1,
	smallFoW/1,
	largeFoW/1,
	enemyInSight/1,
	lowHealth/1,
	heightOverAverage/1.


% badSituation(X)
%
badSituation(X) :- smallFoW(X), !.
badSituation(X) :-
	lowHealth(X),
	\+ highGround(X), !.
badSituation(X) :-
	lowHealth(X),
	\+ heightOverAverage(X), !.

% goodSituation(X)
% On considere que l'agent est dans une bonne situation si n'importe lequel de ces predicats est verifie
goodSituation(X) :- highGround(X), !.
goodSituation(X) :- largeFoW(X), !.
goodSituation(X) :- heightOverAverage(X), !.
goodSituation(X) :- enemyInSight(X), !.

%perfectSituation(X)
%
perfectSituation(X) :-
	highGround(X),
	largeFoW(X),
	heightOverAverage(X), !.
	
	
	