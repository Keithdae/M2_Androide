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


enemyObserved(agent) :- jpl_get('sma.agents.LogicAgent', 'enemyObserved', R), jpl_is_true(R).
enemyInSight(agent) :- jpl_get('sma.agents.LogicAgent', 'enemyInSight', R), jpl_is_true(R), !.
enemyInSight(agent) :- enemyObserved(agent), lookAtEnemy().

highGround(agent) :- jpl_get('sma.agents.LogicAgent', 'highGround', R), jpl_is_true(R).
smallFoW(agent) :- jpl_get('sma.agents.LogicAgent', 'smallFoW', R), jpl_is_true(R).
largeFoW(agent) :- jpl_get('sma.agents.LogicAgent', 'largeFoW', R), jpl_is_true(R).
lowHealth(agent) :- jpl_get('sma.agents.LogicAgent', 'lowHealth', R), jpl_is_true(R).
heightOverAverage(agent) :- jpl_get('sma.agents.LogicAgent', 'heightOverAverage', R), jpl_is_true(R).



%perfectSituation(X)
%On peut tirer sur l'ennemi

perfectSituation(X) :- enemyInSight(X), attack(), !.


% goodSituation(X)
% 
goodSituation(X) :- \+ heightOverAverage(X), climb(), !.
goodSituation(X) :- highGround(X), lowHealth(X), camping(), !.
goodSituation(X) :- highGround(X), \+ lowHealth(X), explore(), !.
goodSituation(X) :- \+ highGround(X), knownHighPoint(X), goto(), !.
goodSituation(X) :- \+ highGround(X), \+ knownHighPoint(X), explore(), !.
