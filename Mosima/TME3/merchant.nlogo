;; A simple merchant simulation - By Adam Marchetti
;; Version 1.1
breed [merchants merchant]
breed [shoppers shopper]
breed [banks bank]

merchants-own [price visits stock my-bank selling?]
shoppers-own [money broke?]
banks-own [visits money borrow-rate bankrupt? lending?]

;;;;; Setup Methods
to setup
  ;; (for this model to work with NetLogo's new plotting features,
  ;; __clear-all-and-reset-ticks should be replaced with clear-all at
  ;; the beginning of your setup procedure and reset-ticks at the end
  ;; of the procedure.)
  __clear-all-and-reset-ticks
  random-seed (new-seed)
  create-banks number-banks [
    set money bank-seed
    set borrow-rate 1
    set bankrupt? false
    set lending? false
    set xcor random-xcor
    set ycor random-xcor
    set color green
  ]
  create-merchants number-merchants [
    set price start-price
    set stock start-stock
    set selling? false
    set my-bank nobody
    set xcor random-xcor
    set ycor random-ycor
    set color red
  ]
  create-shoppers number-shoppers [
    set money start-money
    set broke? false
    set xcor random-xcor
    set ycor random-ycor
    set color blue
  ]
end

;;;;; Updater methods
to go
  if (not any? shoppers or not any? merchants or not any? banks) [stop]
  ask-concurrent shoppers [run-shopper]

  if (do-plot?)[
      set-current-plot "Visits"
      set-current-plot-pen "banks"
      plot (mean [visits] of banks)
      set-current-plot-pen "merchants"
      plot (mean [visits] of merchants)
  ]

  ask-concurrent merchants [run-merchant]
  ask-concurrent banks [run-banks]

  if (not any? shoppers or not any? merchants or not any? banks) [stop]
  if (do-plot?) [plot-all]
  tick
end

to run-merchant
  if (my-bank = nobody) [set my-bank (nearest banks)]
  ifelse (selling?)
    [set price (price * 1.1)]
    [
      set price (price * .95)
      ;;show "Not selling!"
    ]
  if (price < out-of-business) [die]
  if (stock <= 0) [
    deposit my-bank (start-stock * price)
    set stock start-stock
  ]
  ifelse (visits = 0)
    [set selling? false]
    [set selling? true]
  set visits 0
end

to run-banks
  if (bankrupt?) [die]
  ifelse (lending?)
    [set borrow-rate (borrow-rate * 0.95)]
    [
      set borrow-rate (borrow-rate * 1.1)
      ;;show "Not lending!"
    ]
  if (borrow-rate > 1.5) [set borrow-rate 1.5]
  if (borrow-rate < 0.5) [set borrow-rate 0.5]
  set money money * (1 - tax-rate)
  ifelse (visits = 0)
    [set lending? false]
    [set lending? true]
  set visits 0
end

to run-shopper
  ifelse (broke?) [
     let bank (best-bank)
     if (not (bank = nobody)) [
       ifelse (close-to bank)
         [
           borrow bank (mean [price] of merchants) * ([borrow-rate] of bank)
           set broke? false
           set money (money + ((mean [price] of merchants) * ([borrow-rate] of bank)))
         ]
         [
           face bank
           fd 2
         ]
     ]
  ]
  [
    let best (best-merchant)
    if (not (best = nobody))
    [
      ifelse (close-to best)
        [if ([stock] of best > 0)[buy best]]
        [
          face best
          fd 2
        ]
    ]
    if (money <= 0) [set broke? true]
  ]
end

;; Various utility methods
to-report nearest [other-breed]
  report (min-one-of other-breed [distance myself])
end

to-report best-merchant
  ifelse ((min [price] of merchants) >= price-tolerance)
    [report nobody]
    [report (max-one-of merchants [(1 / price) / (distance myself + 0.1)])]
end

to-report best-bank
  ifelse ((max [borrow-rate] of banks) <= rate-tolerance)
    [report nobody]
    [report (max-one-of banks [borrow-rate / (distance myself + 0.1)])]
end

to-report close-to [other-turtle]
  report ([distance myself] of other-turtle) <= 1
end

to plot-all
  set-current-plot "Average Money & Prices"
  set-current-plot-pen "merchants"
  plot (mean [price] of merchants)
  set-current-plot-pen "shoppers"
  plot (mean [money] of shoppers)
  set-current-plot-pen "baseline"
  plot price-tolerance

  set-current-plot "Average Bank Borrowing Rates"
  set-current-plot-pen "rate"
  plot (mean [borrow-rate] of banks)
  set-current-plot-pen "baseline"
  plot rate-tolerance
end

;; Various accessor methods
to buy [merchant]
  if ([stock] of merchant > 0)
  [
    ask merchant [
      set stock (stock - 1)
      set visits (visits + 1)
    ]
    let cost [price] of merchant
    ifelse (cost > money)
      [set broke? true]
      [set money (money - cost)]
  ]
end

to borrow [bank amount]
  ask bank [
    ifelse (money < amount)
      [set bankrupt? true]
      [set money (money - (amount * borrow-rate))]
    set visits (visits + 1)
  ]
end

to deposit [bank amount]
  ask bank [
    set money (money + amount)
    if (money >= 0) [set bankrupt? false]
  ]
end
@#$#@#$#@
GRAPHICS-WINDOW
9
10
397
419
16
16
11.455
1
10
1
1
1
0
1
1
1
-16
16
-16
16
0
0
1
ticks
30.0

BUTTON
406
11
566
44
Run
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
576
12
737
45
Setup
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

SLIDER
410
59
572
92
number-merchants
number-merchants
1
50
50
1
1
NIL
HORIZONTAL

SLIDER
417
99
527
132
start-price
start-price
1
50
20
1
1
NIL
HORIZONTAL

SLIDER
411
139
538
172
start-stock
start-stock
1
100
100
1
1
NIL
HORIZONTAL

SLIDER
500
254
660
287
number-shoppers
number-shoppers
1
5000
451
5
1
NIL
HORIZONTAL

SLIDER
519
294
642
327
start-money
start-money
10
100
100
1
1
NIL
HORIZONTAL

PLOT
8
439
462
605
Average Money & Prices
Time
Units
0.0
10.0
0.0
20.0
true
true
"" ""
PENS
"merchants" 1.0 0 -2674135 true "" ""
"shoppers" 1.0 0 -10899396 true "" ""
"baseline" 1.0 0 -16777216 true "" ""

SLIDER
588
62
710
95
number-banks
number-banks
1
25
10
1
1
NIL
HORIZONTAL

SLIDER
586
101
720
134
bank-seed
bank-seed
100
1000
1000
1
1
NIL
HORIZONTAL

SLIDER
587
141
703
174
tax-rate
tax-rate
0
1
0
0.05
1
NIL
HORIZONTAL

PLOT
9
609
463
729
Average Bank Borrowing Rates
Time
Percent
0.0
10.0
0.0
1.5
true
false
"" ""
PENS
"rate" 1.0 0 -2674135 true "" ""
"baseline" 1.0 0 -16777216 true "" ""

SLIDER
509
332
643
365
price-tolerance
price-tolerance
10
500
500
1
1
NIL
HORIZONTAL

SLIDER
506
375
648
408
rate-tolerance
rate-tolerance
0
1.5
0.95
0.05
1
NIL
HORIZONTAL

SWITCH
746
14
859
47
do-plot?
do-plot?
0
1
-1000

PLOT
10
733
463
892
Visits
Time
Visits
0.0
10.0
0.0
10.0
true
true
"" ""
PENS
"merchants" 1.0 0 -2674135 true "" ""
"banks" 1.0 0 -13345367 true "" ""

SLIDER
410
179
542
212
out-of-business
out-of-business
1
10
1
1
1
NIL
HORIZONTAL

MONITOR
411
310
468
355
Banks
count banks
17
1
11

MONITOR
411
366
492
411
Merchants
count merchants
17
1
11

MONITOR
470
461
643
506
Average money per shopper
mean [money] of shoppers
2
1
11

MONITOR
472
516
575
561
Average price
mean [price] of merchants
2
1
11

MONITOR
485
637
662
682
Average bank borrowing rate
mean [borrow-rate] of banks
17
1
11

MONITOR
469
741
645
786
Average visits per merchant
mean [visits] of merchants
17
1
11

MONITOR
482
809
649
854
Average visits per bank
mean [visits] of banks
17
1
11

MONITOR
656
742
904
787
Standard deviation of visits per merchant
standard-deviation [visits] of merchants
17
1
11

MONITOR
648
461
896
506
Standard deviation of money per shopper
standard-deviation [money] of shoppers
2
1
11

MONITOR
584
516
820
561
NIL
standard-deviation [price] of merchants
2
1
11

@#$#@#$#@
## WHAT IS IT?

This is a simulation of three different elements - merchants, banks, and shoppers, and how they interact.

## HOW IT WORKS

Merchants:

    They simply want to sell their wares. Then are given a stock as the simulation begins, and adjust their price as Shoppers buy from them. (More Purchases -> Higher Price; Less Purchases -> Lower Price). When they areout of stock, they transfer their earnings to the nearest bank.

Banks:

    They lend money out to Shoppers, and recieve money from Merchants. They adjust their lending rates (ie if the Shopper asks for $100, and the lending rate is 75%, they get $75 from the bank) depending on how much Shoppers get from them (More Loans -> Lower Percentage; Less Loands -> Higher percantage). They are subject to a configurable tax rate, which acts every tick.

Shoppers:
  They have two functions:

  -  To buy from merchants. They choose which by figuring out the closest one with the best prices, or if no merchant matches their best price, then they don't purchase, forcing merchants to drive down their rates.
  -  To get money from banks. They choose which by figuring out the closest one with the highest lending rate, or if no bank matches their minimum lending rate, then they
        don't go to banks, forcing banks to drive up their rates.

## HOW TO USE IT

Simply click "Setup" and then "Go".

  - number-* : How many of that breed start out on the board.
  - start-price : What price merchants start out charging.
  - start-stock : How much stock merchants get when they run out.
  - out-of-business : When the merchant hits this price, he quits.
  - bank-seed : How much money each bank starts out with.
  - tax-rate : The tax rate effected on the banks each tick.
  - start-money : How much money each shopper starts out with.
  - price-tolerance : The maximum price a shopper will accept a merchant charging
  - rate-tolerance : The minimum rate a shopper will accept a bank using

## THINGS TO NOTICE

It's all in the graphs.
  - Average Money & Prices : How much money the merchants are charging, how much the average shopper has, and the "baseline" (ie the price-tolerance)

  - Average Bank Borrowing Rates : The banks' lending rates. The "baseline" is the

    rate-tolerance, in this case.

  - Visits: How many visits banks and merchants are getting on average.

## THINGS TO TRY

Try exploring different ratios of shoppers/banks/merchants and see how they manage.

Also, mess around with the shoppers' pickiness, and see how prices and rate fluctuate.

## CREDITS AND REFERENCES

Copyright Adam Marchetti, 2011. Licensed under CC Atribution 3.0.
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
0
Rectangle -7500403 true true 151 225 180 285
Rectangle -7500403 true true 47 225 75 285
Rectangle -7500403 true true 15 75 210 225
Circle -7500403 true true 135 75 150
Circle -16777216 true false 165 76 116

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

x
false
0
Polygon -7500403 true true 270 75 225 30 30 225 75 270
Polygon -7500403 true true 30 75 75 30 270 225 225 270

@#$#@#$#@
NetLogo 5.3
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
