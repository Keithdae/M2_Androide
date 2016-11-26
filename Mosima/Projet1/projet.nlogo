__includes ["agents.nls"]
breed [agents agent]
globals [high-effort low-effort]

patches-own [
  occupied?
  effort?
]

agents-own [
 typeAgent               ; type de l'agent
 direction               ; direction (gauche, droite, haut, bas)
 effort                  ; effort [0;2]
 profit                  ; profit, calculé avec la fonction deprofit
 otherEffort             ; effort du binôme
 otherProfit             ; profit du binôme
 numInt                  ; nombre d'interactions
 sumEffort               ; somme des efforts des partenaire rencontrés
 lastEffort              ; effort à l'interaction précédente
 lastProfit              ; profit à l'interaction précédente
 lastPartnerEffort       ; effort du binôme à l'interaction précédente
 lastPartnerProfit       ; profit du binôme à l'interaction précédente
 partner                 ; partner pour cette interaction
]

;------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

to setup
  clear-all
  ifelse effortWindow [
    resize-world 0 (((X - 1) * 2) + 2) 0 (Y - 1)
  ]
  ; else
  [
    resize-world 0 (X - 1) 0 (Y - 1)
  ]

  ask patches [
    set pcolor white
    set occupied? false
    set effort? false
  ]
  ask patches with [pxcor = X] [
    set pcolor grey + 1.5
    set occupied? true
    set effort? true
  ]
  ask patches with [pxcor > X] [
    set effort? true
  ]

  set high-effort 2.001
  set low-effort 0.0001

  setupAgents

  reset-ticks
end

;------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

to setupAgents
  let currAgents X * Y ; Compteur du nombre d'agents (initialement égal au nombre de cases du monde)

  ; Création des agents black
  if currAgents > 0 and nbAgentTypes >= 1 [
    let nbCreate 0  ; Nombre d'agents que l'on va créer
    ifelse nbAgentsBlack > currAgents [ ; Le nombre d'agents demandé dépasse la limite de place, on crée ce qu'il reste
      set nbCreate currAgents
      set currAgents 0
    ]
    ; else
    [
      set currAgents currAgents - nbAgentsBlack ; On crée le nombre d'agents demandé
      set nbCreate nbAgentsBlack
    ]
    create-agents nbCreate [set color black]

    ask agents with [color = black] [ ; Positionnement aléatoire des agents que l'on vient de créer
      let targetPatch one-of patches with [not occupied? and not effort?]
      move-to targetPatch
      set occupied? true
      set direction one-of [0 90 180 270]  ; on tourne aléatoirement
      set heading direction

      set typeAgent typeBlack ; On assigne le type demandé
      initAgent
    ]
  ]

  ; Création des agents red
  if currAgents > 0 and nbAgentTypes >= 2 [
    let nbCreate 0  ; Nombre d'agents que l'on va créer
    ifelse nbAgentsRed > currAgents [ ; Le nombre d'agents demandé dépasse la limite de place, on crée ce qu'il reste
      set nbCreate currAgents
      set currAgents 0
    ]
    ; else
    [
      set currAgents currAgents - nbAgentsRed ; On crée le nombre d'agents demandé
      set nbCreate nbAgentsRed
    ]
    create-agents nbCreate [set color red]

    ask agents with [color = red] [ ; Positionnement aléatoire des agents que l'on vient de créer
      let targetPatch one-of patches with [not occupied? and not effort?]
      move-to targetPatch
      set occupied? true
      let dir one-of [0 90 180 270]  ; on tourne aléatoirement
      set heading dir

      set typeAgent typeRed
      initAgent
    ]
  ]

  ; Création des agents green
  if currAgents > 0 and nbAgentTypes >= 3 [
    let nbCreate 0  ; Nombre d'agents que l'on va créer
    ifelse nbAgentsGreen > currAgents [ ; Le nombre d'agents demandé dépasse la limite de place, on crée ce qu'il reste
      set nbCreate currAgents
      set currAgents 0
    ]
    ; else
    [
      set currAgents currAgents - nbAgentsGreen ; On crée le nombre d'agents demandé
      set nbCreate nbAgentsGreen
    ]
    create-agents nbCreate [set color green]

    ask agents with [color = green] [ ; Positionnement aléatoire des agents que l'on vient de créer
      let targetPatch one-of patches with [not occupied? and not effort?]
      move-to targetPatch
      set occupied? true
      let dir one-of [0 90 180 270]  ; on tourne aléatoirement
      set heading dir

      set typeAgent typeGreen
      initAgent
    ]
  ]

  ; Création des agents violet
  if currAgents > 0 and nbAgentTypes >= 4 [
    let nbCreate 0  ; Nombre d'agents que l'on va créer
    ifelse nbAgentsViolet > currAgents [ ; Le nombre d'agents demandé dépasse la limite de place, on crée ce qu'il reste
      set nbCreate currAgents
      set currAgents 0
    ]
    ; else
    [
      set currAgents currAgents - nbAgentsViolet ; On crée le nombre d'agents demandé
      set nbCreate nbAgentsViolet
    ]
    create-agents nbCreate [set color violet]

    ask agents with [color = violet] [ ; Positionnement aléatoire des agents que l'on vient de créer
      let targetPatch one-of patches with [not occupied? and not effort?]
      move-to targetPatch
      set occupied? true
      let dir one-of [0 90 180 270]  ; on tourne aléatoirement
      set heading dir

      set typeAgent typeViolet
      initAgent
    ]
  ]

  ; Création des agents yellow
  if currAgents > 0 and nbAgentTypes >= 5 [
    let nbCreate 0  ; Nombre d'agents que l'on va créer
    ifelse nbAgentsYellow > currAgents [ ; Le nombre d'agents demandé dépasse la limite de place, on crée ce qu'il reste
      set nbCreate currAgents
      set currAgents 0
    ]
    ; else
    [
      set currAgents currAgents - nbAgentsYellow ; On crée le nombre d'agents demandé
      set nbCreate nbAgentsYellow
    ]
    create-agents nbCreate [set color yellow - 1]

    ask agents with [color = yellow - 1] [ ; Positionnement aléatoire des agents que l'on vient de créer
      let targetPatch one-of patches with [not occupied? and not effort?]
      move-to targetPatch
      set occupied? true
      let dir one-of [0 90 180 270]  ; on tourne aléatoirement
      set heading dir

      set typeAgent typeYellow
      initAgent
    ]
  ]

  ; Création des agents cyan
  if currAgents > 0 and nbAgentTypes >= 6 [
    let nbCreate 0  ; Nombre d'agents que l'on va créer
    ifelse nbAgentsCyan > currAgents [ ; Le nombre d'agents demandé dépasse la limite de place, on crée ce qu'il reste
      set nbCreate currAgents
      set currAgents 0
    ]
    ; else
    [
      set currAgents currAgents - nbAgentsCyan ; On crée le nombre d'agents demandé
      set nbCreate nbAgentsCyan
    ]
    create-agents nbCreate [set color cyan]

    ask agents with [color = cyan] [ ; Positionnement aléatoire des agents que l'on vient de créer
      let targetPatch one-of patches with [not occupied? and not effort?]
      move-to targetPatch
      set occupied? true
      let dir one-of [0 90 180 270]  ; on tourne aléatoirement
      set heading dir

      set typeAgent typeCyan
      initAgent
    ]
  ]

end

;------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
to drawEfforts
  ask patches with [pxcor > X ] [
    set pcolor white
  ]
  ask agents[
    let couleur white
    ifelse effort < 0.2 and 0.0 <= effort [
      set couleur 105
    ]
    [
      ifelse effort < 0.4 and 0.2 <= effort [
        set couleur 95
      ]
      [
        ifelse effort < 0.6 and 0.4 <= effort [
          set couleur 85
        ]
        [
          ifelse effort < 0.8 and 0.6 <= effort [
            set couleur 75
          ]
          [
             ifelse effort < 1.0 and 0.8 <= effort [
              set couleur 65
            ]
            [
              ifelse effort < 1.2 and 1.0 <= effort [
                set couleur 55
              ]
              [
                ifelse effort < 1.4 and 1.2 <= effort [
                  set couleur 45
                ]
                [
                  ifelse effort < 1.6 and 1.4 <= effort [
                    set couleur 25
                  ]
                  [
                    ifelse effort < 1.8 and 1.6 <= effort [
                      set couleur 16
                    ]
                    [
                      set couleur 15
                    ]
                  ]
                ]
              ]
            ]
          ]
        ]
      ]
    ]
    ask patch-at (X + 1) 0 [
       set pcolor couleur
    ]
  ]
end

;------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

to run-simulations
  let fig6 simulateFigure6
  let fig7 simulateFigure7
  simulateFigure9
  fill-plot "Figure 6" fig6
  fill-plot "Figure 7" fig7
  export-plot "Figure 6" "simulation_figure6.txt"
  export-plot "Figure 7" "simulation_figure7.txt"
end

; La penList est une liste contenant des listes de la forme [ "penName" [ [x1 y1] [x2 y2] ... ] ]
to fill-plot [ plotName penList ]
  set-current-plot plotName
  let colorPen 15
  foreach penList
  [
    ; On récupère le nom qui est le premier élément de la liste
    create-temporary-plot-pen first ?
    set-plot-pen-color colorPen
    ; Le deuxième élément est une liste de paires de points à dessiner que l'on parcourt
    foreach last ? [
      let px first ?
      let py last ?
      ; On trace le point
      plotxy px py
      ; Permet de tracer une croix autour du point
      plotxy px + (plot-x-max / 50) py
      plotxy px - (plot-x-max / 50) py
      plot-pen-up
      plotxy px py + (plot-y-max / 50)
      plot-pen-down
      plotxy px py - (plot-y-max / 50)
      ; on se replace sur le point pour bien tracer la lgine vers le point suivant
      plotxy px py
    ]
    ; On passe à la couleur suivante
    set colorPen colorPen + 10
  ]
end

; Lance le programme jusqu'à atteindre une situation stable et retourne la moyenne d'effort des agents
; Prends une liste de paramètres sur les agents à utiliser sur la forme : [ [typeBlack proportionOfAgentsBlack] [typeRed proportionOfAgentsRed] ... ]
; stdTolerance est la tolerance sur le changement d'écart-type d'un tick à l'autre
; nbTests est le nombre de fois où les ticks doivent réussir le test de tolérance successivement pour finir la simulation
to-report simulate [ paramList stdTolerance nbTests ]
  let result -1
  let i 0
  ; Initialisation des quantités et types d'agents
  foreach paramList
  [
    let currType first ?
    let currProportion last ?
    if i = 0
    [
      set typeBlack currType
      set nbAgentsBlack round (nbAgentsSimulation * currProportion)
    ]
    if i = 1
    [
      set typeRed currType
      set nbAgentsRed round (nbAgentsSimulation * currProportion)
    ]
    if i = 2
    [
      set typeGreen currType
      set nbAgentsGreen round (nbAgentsSimulation * currProportion)
    ]
    if i = 3
    [
      set typeViolet currType
      set nbAgentsViolet round (nbAgentsSimulation * currProportion)
    ]
    if i = 4
    [
      set typeYellow currType
      set nbAgentsYellow round (nbAgentsSimulation * currProportion)
    ]
    if i = 5
    [
      set typeCyan currType
      set nbAgentsCyan round (nbAgentsSimulation * currProportion)
    ]
    set i i + 1
  ]
  ; On active le nombre d'agents nécessaires à la simulation
  set nbAgentTypes i

  ; Setup pour créer et initialiser les agents
  setup


  let done false
  ; On fait un premier tick pour obtenir les premières valeurs
  go
  let lastSTD standard-deviation [effort] of turtles
  let currSTD lastSTD
  let n 0         ; Compteur de succès à la suite servant pour l'arrêt
  let meanList [] ;
  let meanEffort mean [effort] of turtles
  let loopCount 0
  let loopStdTolerance stdTolerance
  while [not done]
  [
    go
    set currSTD standard-deviation [effort] of turtles
    set meanEffort mean [effort] of turtles
    ; On compare l'écart-type actuel avec celui du tick précédent
    ifelse abs(currSTD - lastSTD) < loopStdTolerance
    [
      ; Succès, on augmente notre compteur et on ajoute la moyenne courante à notre liste
      set n n + 1
      set meanList lput meanEffort meanList
    ]
    ; else, échec la suite est brisée, on oublie nos valeurs et on remet le comteur à 0
    [
      set meanList []
      set n 0
    ]

    ; Dans le cas où notre compteur atteind le nombre demandé, on renvoie la moyenne des moyennes d'effort stockées dans notre liste
    if n >= nbTests
    [
       set result precision mean meanList 6
       set done true
    ]

    set lastSTD currSTD

    ; Après 2001 ticks sans succès on augmente la valeur de la tolérance
    set loopCount loopCount + 1
    if loopCount > 2000
    [
      set loopCount 0
      set loopStdTolerance loopStdTolerance + stdTolerance
    ]
  ]

  report result
end


to-report simulateFigure6
  let HighEffortProportions [ 0 0.6 5.6 33.3 66.7 100 ]
  let agentTypeList [ 0 1 2 4 7 8 9 ]
  let paramList []
  let penList []

  foreach agentTypeList
  [
    let currType ?

    ; On créé la liste de résultats pour ce type
    let typeName typeNbToName currType
    let currPen (list typeName [])

    foreach HighEffortProportions
    [
      let currProportion ?
      let params (list (list currType ((100 - currProportion) / 100) )  ; Le type d'agent que l'on teste
                       (list 5 (currProportion / 100) ) )               ; Les HighEffort

      ; On lance la simulation
      let meanEffort simulate params stdToleranceSimulation nbTestsSimulation

      ; On ajoute les résultats avec la proportion donnée de HighEffort agents
      let newValList lput (list currProportion meanEffort) (item 1 currPen)
      set currPen replace-item 1 currPen newValList
    ]

    show currPen

    set penList lput currPen penList
  ]

  show penList

  report penList
end

to-report simulateFigure7

  let HighEffortProportions [ 0 0.6 5.6 33.3 66.7 100 ]
  let agentTypeList [ 3 6 ]
  let paramList []
  let penList []

  foreach agentTypeList
  [
    let currType ?

    ; On créé la liste de résultats pour ce type
    let typeName typeNbToName currType
    let currPen (list typeName [])

    foreach HighEffortProportions
    [
      let currProportion ?
      let params (list (list currType ((100 - currProportion) / 100) )  ; Le type d'agent que l'on teste
                       (list 5 (currProportion / 100) ) )               ; Les HighEffort

      ; On lance la simulation
      let meanEffort simulate params stdToleranceSimulation nbTestsSimulation

      ; On ajoute les résultats avec la proportion donnée de HighEffort agents
      let newValList lput (list currProportion meanEffort) (item 1 currPen)
      set currPen replace-item 1 currPen newValList
    ]

    show currPen

    set penList lput currPen penList
  ]

  show penList

  report penList
end

to simulateFigure9

end

; Converts a type number to the corresponding name for plots
to-report typeNbToName [nbTypeConvert]
  if nbTypeConvert = 0 [report "Null Effort"]
  if nbTypeConvert = 1 [report "Shrinking Effort"]
  if nbTypeConvert = 2 [report "Replicator"]
  if nbTypeConvert = 3 [report "Rational"]
  if nbTypeConvert = 4 [report "Profit Comparator"]
  if nbTypeConvert = 5 [report "High Effort"]
  if nbTypeConvert = 6 [report "Average Rational"]
  if nbTypeConvert = 7 [report "Winner Imitator"]
  if nbTypeConvert = 8 [report "Effort Comparator"]
  if nbTypeConvert = 9 [report "Averager"]
end

;------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

to go
  randomMove
  workAgent
  calculateProfits
  if effortWindow [drawEfforts]
  adaptEffort
  tick
end
@#$#@#$#@
GRAPHICS-WINDOW
18
34
516
305
-1
-1
8.0
1
10
1
1
1
0
1
1
1
0
60
0
29
1
1
1
ticks
30.0

BUTTON
879
49
942
82
NIL
go
T
1
T
OBSERVER
NIL
NIL
NIL
NIL
1

BUTTON
878
90
951
123
NIL
setup
NIL
1
T
OBSERVER
NIL
NIL
NIL
NIL
1

TEXTBOX
1023
29
1173
47
World parameters :
12
0.0
1

SLIDER
1020
48
1112
81
X
X
5
50
30
1
1
NIL
HORIZONTAL

SLIDER
1020
86
1112
119
Y
Y
5
50
30
1
1
NIL
HORIZONTAL

TEXTBOX
98
11
248
29
Agents
12
0.0
1

TEXTBOX
377
10
527
28
Efforts
12
0.0
1

TEXTBOX
1182
29
1332
47
View parameters :
12
0.0
1

SWITCH
1178
47
1309
80
effortWindow
effortWindow
0
1
-1000

CHOOSER
1367
48
1505
93
nbAgentTypes
nbAgentTypes
1 2 3 4 5 6
1

TEXTBOX
1370
30
1520
48
Agent parameters :
12
0.0
1

TEXTBOX
1361
156
1427
174
# agents
12
15.0
0

TEXTBOX
1362
109
1429
127
# agents
12
0.0
1

TEXTBOX
1359
208
1439
226
# agents
12
55.0
1

TEXTBOX
1359
259
1441
277
# agents
12
115.0
1

TEXTBOX
1359
312
1440
330
# agents
12
44.0
1

TEXTBOX
1360
364
1436
382
# agents
12
85.0
1

SLIDER
1443
108
1590
141
nbAgentsBlack
nbAgentsBlack
0
X * Y
0
1
1
NIL
HORIZONTAL

SLIDER
1441
157
1588
190
nbAgentsRed
nbAgentsRed
0
X * Y
500
1
1
NIL
HORIZONTAL

SLIDER
1440
209
1586
242
nbAgentsGreen
nbAgentsGreen
0
X * Y
0
1
1
NIL
HORIZONTAL

SLIDER
1440
256
1588
289
nbAgentsViolet
nbAgentsViolet
0
X * Y
0
1
1
NIL
HORIZONTAL

SLIDER
1439
311
1586
344
nbAgentsYellow
nbAgentsYellow
0
X * Y
0
1
1
NIL
HORIZONTAL

SLIDER
1438
366
1587
399
nbAgentsCyan
nbAgentsCyan
0
X * Y
0
1
1
NIL
HORIZONTAL

CHOOSER
1597
102
1689
147
typeBlack
typeBlack
0 1 2 3 4 5 6 7 8 9
6

CHOOSER
1597
153
1689
198
typeRed
typeRed
0 1 2 3 4 5 6 7 8 9
5

CHOOSER
1597
204
1689
249
typeGreen
typeGreen
0 1 2 3 4 5 6 7 8 9
9

CHOOSER
1597
257
1689
302
typeViolet
typeViolet
0 1 2 3 4 5 6 7 8 9
9

CHOOSER
1598
308
1690
353
typeYellow
typeYellow
0 1 2 3 4 5 6 7 8 9
9

CHOOSER
1598
364
1690
409
typeCyan
typeCyan
0 1 2 3 4 5 6 7 8 9
9

SWITCH
1176
244
1299
277
noiseSwitch
noiseSwitch
1
1
-1000

TEXTBOX
1176
217
1220
235
Noise :
12
0.0
1

SLIDER
1175
288
1300
321
noiseValue
noiseValue
1
50
5
1
1
%
HORIZONTAL

PLOT
21
429
464
617
Effort
NIL
NIL
0.0
10.0
0.0
2.1
true
true
"" ""
PENS
"Mean" 1.0 0 -16777216 true "" "plot mean [effort] of agents"
"Std" 1.0 0 -7500403 true "" "plot standard-deviation [effort] of agents"

PLOT
492
429
892
617
Profit
NIL
NIL
0.0
10.0
0.0
8.0
true
true
"" ""
PENS
"Average" 1.0 0 -16777216 true "" "plot mean [profit] of turtles"

TEXTBOX
1715
117
1865
271
Agent types :\n0 - null effort\n1 - shrinking effort\n2 - replicator\n3 - rational\n4 - profit comparator\n5 - high effort\n6 - average rational\n7 - winner imitator\n8 - effort comparator\n9 - averager
11
0.0
1

PLOT
51
760
664
1096
Figure 6
High effort agents proportion (%)
Average effort
0.0
100.0
0.0
2.1
false
true
"" ""
PENS

TEXTBOX
74
692
224
712
Simulation
16
0.0
1

BUTTON
192
685
306
718
NIL
run-simulations
NIL
1
T
OBSERVER
NIL
NIL
NIL
NIL
1

SLIDER
344
684
516
717
nbAgentsSimulation
nbAgentsSimulation
168
X * Y
500
1
1
NIL
HORIZONTAL

SLIDER
545
683
740
716
stdToleranceSimulation
stdToleranceSimulation
0
1
5.0E-4
0.0001
1
NIL
HORIZONTAL

SLIDER
765
690
937
723
nbTestsSimulation
nbTestsSimulation
2
500
500
1
1
NIL
HORIZONTAL

PLOT
777
761
1370
1096
Figure 7
High effort agents proportion (%)
Average effort
0.0
100.0
0.0
2.1
false
true
"" ""
PENS

MONITOR
912
430
1008
475
Average effort
mean [effort] of agents
6
1
11

MONITOR
912
480
1009
525
STD effort
standard-deviation [effort] of agents
6
1
11

MONITOR
912
531
1009
576
Average profit
mean [profit] of agents
6
1
11

BUTTON
879
10
965
43
step once
go
NIL
1
T
OBSERVER
NIL
NIL
NIL
NIL
1

@#$#@#$#@
## WHAT IS IT?

(a general understanding of what the model is trying to show or explain)

## HOW IT WORKS

(what rules the agents use to create the overall behavior of the model)

## HOW TO USE IT

(how to use the model, including a description of each of the items in the Interface tab)

## THINGS TO NOTICE

(suggested things for the user to notice while running the model)

## THINGS TO TRY

(suggested things for the user to try to do (move sliders, switches, etc.) with the model)

## EXTENDING THE MODEL

(suggested things to add or change in the Code tab to make the model more complicated, detailed, accurate, etc.)

## NETLOGO FEATURES

(interesting or unusual features of NetLogo that the model uses, particularly in the Code tab; or where workarounds were needed for missing features)

## RELATED MODELS

(models in the NetLogo Models Library and elsewhere which are of related interest)

## CREDITS AND REFERENCES

(a reference to the model's URL on the web if it has one, as well as any other necessary credits, citations, and links)
@#$#@#$#@
default
true
0
Polygon -7500403 true true 150 5 40 250 150 205 260 250

airplane
true
0
Polygon -7500403 true true 150 0 135 15 120 60 120 105 15 165 15 195 120 180 135 240 105 270 120 285 150 270 180 285 210 270 165 240 180 180 285 195 285 165 180 105 180 60 165 15

arrow
true
0
Polygon -7500403 true true 150 0 0 150 105 150 105 293 195 293 195 150 300 150

box
false
0
Polygon -7500403 true true 150 285 285 225 285 75 150 135
Polygon -7500403 true true 150 135 15 75 150 15 285 75
Polygon -7500403 true true 15 75 15 225 150 285 150 135
Line -16777216 false 150 285 150 135
Line -16777216 false 150 135 15 75
Line -16777216 false 150 135 285 75

bug
true
0
Circle -7500403 true true 96 182 108
Circle -7500403 true true 110 127 80
Circle -7500403 true true 110 75 80
Line -7500403 true 150 100 80 30
Line -7500403 true 150 100 220 30

butterfly
true
0
Polygon -7500403 true true 150 165 209 199 225 225 225 255 195 270 165 255 150 240
Polygon -7500403 true true 150 165 89 198 75 225 75 255 105 270 135 255 150 240
Polygon -7500403 true true 139 148 100 105 55 90 25 90 10 105 10 135 25 180 40 195 85 194 139 163
Polygon -7500403 true true 162 150 200 105 245 90 275 90 290 105 290 135 275 180 260 195 215 195 162 165
Polygon -16777216 true false 150 255 135 225 120 150 135 120 150 105 165 120 180 150 165 225
Circle -16777216 true false 135 90 30
Line -16777216 false 150 105 195 60
Line -16777216 false 150 105 105 60

car
false
0
Polygon -7500403 true true 300 180 279 164 261 144 240 135 226 132 213 106 203 84 185 63 159 50 135 50 75 60 0 150 0 165 0 225 300 225 300 180
Circle -16777216 true false 180 180 90
Circle -16777216 true false 30 180 90
Polygon -16777216 true false 162 80 132 78 134 135 209 135 194 105 189 96 180 89
Circle -7500403 true true 47 195 58
Circle -7500403 true true 195 195 58

circle
false
0
Circle -7500403 true true 0 0 300

circle 2
false
0
Circle -7500403 true true 0 0 300
Circle -16777216 true false 30 30 240

cow
false
0
Polygon -7500403 true true 200 193 197 249 179 249 177 196 166 187 140 189 93 191 78 179 72 211 49 209 48 181 37 149 25 120 25 89 45 72 103 84 179 75 198 76 252 64 272 81 293 103 285 121 255 121 242 118 224 167
Polygon -7500403 true true 73 210 86 251 62 249 48 208
Polygon -7500403 true true 25 114 16 195 9 204 23 213 25 200 39 123

cylinder
false
0
Circle -7500403 true true 0 0 300

dot
false
0
Circle -7500403 true true 90 90 120

face happy
false
0
Circle -7500403 true true 8 8 285
Circle -16777216 true false 60 75 60
Circle -16777216 true false 180 75 60
Polygon -16777216 true false 150 255 90 239 62 213 47 191 67 179 90 203 109 218 150 225 192 218 210 203 227 181 251 194 236 217 212 240

face neutral
false
0
Circle -7500403 true true 8 7 285
Circle -16777216 true false 60 75 60
Circle -16777216 true false 180 75 60
Rectangle -16777216 true false 60 195 240 225

face sad
false
0
Circle -7500403 true true 8 8 285
Circle -16777216 true false 60 75 60
Circle -16777216 true false 180 75 60
Polygon -16777216 true false 150 168 90 184 62 210 47 232 67 244 90 220 109 205 150 198 192 205 210 220 227 242 251 229 236 206 212 183

fish
false
0
Polygon -1 true false 44 131 21 87 15 86 0 120 15 150 0 180 13 214 20 212 45 166
Polygon -1 true false 135 195 119 235 95 218 76 210 46 204 60 165
Polygon -1 true false 75 45 83 77 71 103 86 114 166 78 135 60
Polygon -7500403 true true 30 136 151 77 226 81 280 119 292 146 292 160 287 170 270 195 195 210 151 212 30 166
Circle -16777216 true false 215 106 30

flag
false
0
Rectangle -7500403 true true 60 15 75 300
Polygon -7500403 true true 90 150 270 90 90 30
Line -7500403 true 75 135 90 135
Line -7500403 true 75 45 90 45

flower
false
0
Polygon -10899396 true false 135 120 165 165 180 210 180 240 150 300 165 300 195 240 195 195 165 135
Circle -7500403 true true 85 132 38
Circle -7500403 true true 130 147 38
Circle -7500403 true true 192 85 38
Circle -7500403 true true 85 40 38
Circle -7500403 true true 177 40 38
Circle -7500403 true true 177 132 38
Circle -7500403 true true 70 85 38
Circle -7500403 true true 130 25 38
Circle -7500403 true true 96 51 108
Circle -16777216 true false 113 68 74
Polygon -10899396 true false 189 233 219 188 249 173 279 188 234 218
Polygon -10899396 true false 180 255 150 210 105 210 75 240 135 240

house
false
0
Rectangle -7500403 true true 45 120 255 285
Rectangle -16777216 true false 120 210 180 285
Polygon -7500403 true true 15 120 150 15 285 120
Line -16777216 false 30 120 270 120

leaf
false
0
Polygon -7500403 true true 150 210 135 195 120 210 60 210 30 195 60 180 60 165 15 135 30 120 15 105 40 104 45 90 60 90 90 105 105 120 120 120 105 60 120 60 135 30 150 15 165 30 180 60 195 60 180 120 195 120 210 105 240 90 255 90 263 104 285 105 270 120 285 135 240 165 240 180 270 195 240 210 180 210 165 195
Polygon -7500403 true true 135 195 135 240 120 255 105 255 105 285 135 285 165 240 165 195

line
true
0
Line -7500403 true 150 0 150 300

line half
true
0
Line -7500403 true 150 0 150 150

pentagon
false
0
Polygon -7500403 true true 150 15 15 120 60 285 240 285 285 120

person
false
0
Circle -7500403 true true 110 5 80
Polygon -7500403 true true 105 90 120 195 90 285 105 300 135 300 150 225 165 300 195 300 210 285 180 195 195 90
Rectangle -7500403 true true 127 79 172 94
Polygon -7500403 true true 195 90 240 150 225 180 165 105
Polygon -7500403 true true 105 90 60 150 75 180 135 105

plant
false
0
Rectangle -7500403 true true 135 90 165 300
Polygon -7500403 true true 135 255 90 210 45 195 75 255 135 285
Polygon -7500403 true true 165 255 210 210 255 195 225 255 165 285
Polygon -7500403 true true 135 180 90 135 45 120 75 180 135 210
Polygon -7500403 true true 165 180 165 210 225 180 255 120 210 135
Polygon -7500403 true true 135 105 90 60 45 45 75 105 135 135
Polygon -7500403 true true 165 105 165 135 225 105 255 45 210 60
Polygon -7500403 true true 135 90 120 45 150 15 180 45 165 90

sheep
false
15
Circle -1 true true 203 65 88
Circle -1 true true 70 65 162
Circle -1 true true 150 105 120
Polygon -7500403 true false 218 120 240 165 255 165 278 120
Circle -7500403 true false 214 72 67
Rectangle -1 true true 164 223 179 298
Polygon -1 true true 45 285 30 285 30 240 15 195 45 210
Circle -1 true true 3 83 150
Rectangle -1 true true 65 221 80 296
Polygon -1 true true 195 285 210 285 210 240 240 210 195 210
Polygon -7500403 true false 276 85 285 105 302 99 294 83
Polygon -7500403 true false 219 85 210 105 193 99 201 83

square
false
0
Rectangle -7500403 true true 30 30 270 270

square 2
false
0
Rectangle -7500403 true true 30 30 270 270
Rectangle -16777216 true false 60 60 240 240

star
false
0
Polygon -7500403 true true 151 1 185 108 298 108 207 175 242 282 151 216 59 282 94 175 3 108 116 108

target
false
0
Circle -7500403 true true 0 0 300
Circle -16777216 true false 30 30 240
Circle -7500403 true true 60 60 180
Circle -16777216 true false 90 90 120
Circle -7500403 true true 120 120 60

tree
false
0
Circle -7500403 true true 118 3 94
Rectangle -6459832 true false 120 195 180 300
Circle -7500403 true true 65 21 108
Circle -7500403 true true 116 41 127
Circle -7500403 true true 45 90 120
Circle -7500403 true true 104 74 152

triangle
false
0
Polygon -7500403 true true 150 30 15 255 285 255

triangle 2
false
0
Polygon -7500403 true true 150 30 15 255 285 255
Polygon -16777216 true false 151 99 225 223 75 224

truck
false
0
Rectangle -7500403 true true 4 45 195 187
Polygon -7500403 true true 296 193 296 150 259 134 244 104 208 104 207 194
Rectangle -1 true false 195 60 195 105
Polygon -16777216 true false 238 112 252 141 219 141 218 112
Circle -16777216 true false 234 174 42
Rectangle -7500403 true true 181 185 214 194
Circle -16777216 true false 144 174 42
Circle -16777216 true false 24 174 42
Circle -7500403 false true 24 174 42
Circle -7500403 false true 144 174 42
Circle -7500403 false true 234 174 42

turtle
true
0
Polygon -10899396 true false 215 204 240 233 246 254 228 266 215 252 193 210
Polygon -10899396 true false 195 90 225 75 245 75 260 89 269 108 261 124 240 105 225 105 210 105
Polygon -10899396 true false 105 90 75 75 55 75 40 89 31 108 39 124 60 105 75 105 90 105
Polygon -10899396 true false 132 85 134 64 107 51 108 17 150 2 192 18 192 52 169 65 172 87
Polygon -10899396 true false 85 204 60 233 54 254 72 266 85 252 107 210
Polygon -7500403 true true 119 75 179 75 209 101 224 135 220 225 175 261 128 261 81 224 74 135 88 99

wheel
false
0
Circle -7500403 true true 3 3 294
Circle -16777216 true false 30 30 240
Line -7500403 true 150 285 150 15
Line -7500403 true 15 150 285 150
Circle -7500403 true true 120 120 60
Line -7500403 true 216 40 79 269
Line -7500403 true 40 84 269 221
Line -7500403 true 40 216 269 79
Line -7500403 true 84 40 221 269

wolf
false
0
Polygon -16777216 true false 253 133 245 131 245 133
Polygon -7500403 true true 2 194 13 197 30 191 38 193 38 205 20 226 20 257 27 265 38 266 40 260 31 253 31 230 60 206 68 198 75 209 66 228 65 243 82 261 84 268 100 267 103 261 77 239 79 231 100 207 98 196 119 201 143 202 160 195 166 210 172 213 173 238 167 251 160 248 154 265 169 264 178 247 186 240 198 260 200 271 217 271 219 262 207 258 195 230 192 198 210 184 227 164 242 144 259 145 284 151 277 141 293 140 299 134 297 127 273 119 270 105
Polygon -7500403 true true -1 195 14 180 36 166 40 153 53 140 82 131 134 133 159 126 188 115 227 108 236 102 238 98 268 86 269 92 281 87 269 103 269 113

x
false
0
Polygon -7500403 true true 270 75 225 30 30 225 75 270
Polygon -7500403 true true 30 75 75 30 270 225 225 270

@#$#@#$#@
NetLogo 5.3.1
@#$#@#$#@
@#$#@#$#@
@#$#@#$#@
@#$#@#$#@
@#$#@#$#@
default
0.0
-0.2 0 0.0 1.0
0.0 1 1.0 0.0
0.2 0 0.0 1.0
link direction
true
0
Line -7500403 true 150 150 90 180
Line -7500403 true 150 150 210 180

@#$#@#$#@
0
@#$#@#$#@
