turtles-own [sueldo cuenta-corriente ahorros meses-ahorros deudas]
globals [numero-personas ipc inflacion precio-cesta-basica-anterior consumo-mensual consumo-oferta mes]

to setup
  clear-all
  set numero-personas 500
  set ipc 0
  set inflacion ""
  set precio-cesta-basica-anterior precio-cesta-basica-mensual
  set consumo-mensual 0
  set consumo-oferta (numero-personas * precio-cesta-basica-mensual)
  set mes 1
  create-turtles numero-personas [
    set shape "person"
    set size 13
    set cuenta-corriente 0
    set ahorros 0
    set meses-ahorros 0
    set deudas 0
  ]
  let sueldo-bajo (sueldo-medio-mensual - (sueldo-medio-mensual * 0.4))
  let sueldo-alto (sueldo-medio-mensual + (sueldo-medio-mensual * 0.6666))
  ;; 300 Individuos con sueldo medio
  ask turtles [set sueldo sueldo-medio-mensual]
  ;; 125 Individuos con sueldo más bajo que la media
  ask n-of 125 turtles [set sueldo sueldo-bajo]
  ;; 75 Individuos con sueldo más bajo que la media
  ask n-of 75 turtles with [sueldo = sueldo-medio-mensual] [set sueldo sueldo-alto]
  ask turtles [setxy random-xcor random-ycor set color blue]
  reset-ticks
end

to go
  set consumo-mensual 0
  ask turtles [ejecucion-mensual]
  calcular-precio-cesta
  if mes = 1 [calcular-ipc-anual calcular-sueldo-medio set precio-cesta-basica-anterior precio-cesta-basica-mensual]
  calcular-inflacion
  ifelse mes = 12 [set mes 1] [set mes (mes + 1)]
  pintar-tortugas
  tick
end

to ejecucion-mensual
  cobrar-sueldo
  if ahorros > 0 [ifelse meses-ahorros = 0 [cobrar-ahorros] [set meses-ahorros (meses-ahorros - 1)]]
  if deudas > 0 [pagar-deudas]
  ;compra cesta basica
  let puede-comprar-cesta-completa true
  if precio-cesta-basica-mensual > cuenta-corriente [set puede-comprar-cesta-completa false]
  if puede-comprar-cesta-completa [comprar-cesta]
  if not puede-comprar-cesta-completa [comprar-cesta-con-prestamo]
  ;otros gastos y ahorros
  ifelse cuenta-corriente = 0 [gestionar-dinero-con-prestamo] [gestionar-dinero]
end

to cobrar-sueldo
 set cuenta-corriente (cuenta-corriente + sueldo)
end

to pagar-deudas
 let mensualidad-deudas (sueldo * generar-porcentaje-aleatorio (20) (50))
 if mensualidad-deudas > deudas [set mensualidad-deudas deudas]
 set deudas (deudas - mensualidad-deudas)
 set cuenta-corriente (cuenta-corriente - mensualidad-deudas)
end

to cobrar-ahorros
 set cuenta-corriente (cuenta-corriente + ahorros)
 set ahorros 0
end

to comprar-cesta
 gastar-importe (precio-cesta-basica-mensual)
end

to gastar-importe [importe]
 set cuenta-corriente (cuenta-corriente - importe)
 set consumo-mensual (consumo-mensual + importe)
end

to comprar-cesta-con-prestamo
 let importe-diferencia (precio-cesta-basica-mensual - cuenta-corriente)
 gastar-importe (cuenta-corriente)
 ;si el tipo de interes es bajo
 if obtener-nivel-tipo-interes = 1 and obtener-resultado-con-probabilidad(60) [pedir-prestamo(importe-diferencia)] ;añadir el tipo de interes como restriccion
 ;si el tipo de interes es alto
 if obtener-nivel-tipo-interes = 2 and obtener-resultado-con-probabilidad(8) [pedir-prestamo(importe-diferencia)]
 if cuenta-corriente > 0 [gastar-importe(cuenta-corriente)]
end

to pedir-prestamo [importe]
 set deudas (deudas + importe + (importe * tipo-interes))
 set cuenta-corriente (cuenta-corriente + importe)
end

to ahorrar [importe]
 set cuenta-corriente (cuenta-corriente - importe)
 set ahorros (ahorros + (importe + tipo-interes))
 if meses-ahorros = 0 [set meses-ahorros 18]
 if meses-ahorros > 0 and meses-ahorros < 9 [set meses-ahorros (meses-ahorros + 3)]
end

to gestionar-dinero
 ;si el tipo de interes es bajo
 if obtener-nivel-tipo-interes = 1 [gestionar-dinero-tipo-bajo]
 ;si el tipo de interes es medio
 if obtener-nivel-tipo-interes = 2 [gestionar-dinero-tipo-alto]
end

to gestionar-dinero-tipo-bajo
  let importe-ahorros 0
  let importe-deuda 0
  let importe-gastos 0
  if obtener-resultado-con-probabilidad(0.05 * tipo-interes) [set importe-ahorros (cuenta-corriente * generar-porcentaje-aleatorio (1) (2)) ahorrar(importe-ahorros)]
  if cuenta-corriente > 0 and obtener-resultado-con-probabilidad(8) [set importe-deuda (sueldo * generar-porcentaje-aleatorio (1) (60)) pedir-prestamo(importe-deuda)]
  if cuenta-corriente > 0 and obtener-resultado-con-probabilidad(50) [set importe-gastos (cuenta-corriente * generar-porcentaje-aleatorio (40) (60)) gastar-importe(importe-gastos)]
end

to gestionar-dinero-tipo-alto
  let importe-ahorros 0
  let importe-gastos 0
  if obtener-resultado-con-probabilidad(10) [set importe-ahorros (cuenta-corriente * generar-porcentaje-aleatorio (15) (35)) ahorrar(importe-ahorros)]
  if cuenta-corriente > 0 and obtener-resultado-con-probabilidad(0.5) [set importe-gastos (cuenta-corriente * generar-porcentaje-aleatorio (1) (2)) gastar-importe(importe-gastos)]
end

to gestionar-dinero-con-prestamo
 ;si el tipo de interes es bajo
 if obtener-nivel-tipo-interes = 1 [gestionar-dinero-con-prestamo-tipo-bajo]
 ;si el tipo de interes es alto
 if obtener-nivel-tipo-interes = 2 [gestionar-dinero-con-prestamo-tipo-alto]
end

to gestionar-dinero-con-prestamo-tipo-bajo
  let importe-deuda 0
  let importe-gastos 0
  if obtener-resultado-con-probabilidad(7) [set importe-deuda (sueldo * generar-porcentaje-aleatorio (1) (40)) pedir-prestamo(importe-deuda)]
  if cuenta-corriente > 0 and obtener-resultado-con-probabilidad(40) [set importe-gastos (cuenta-corriente * generar-porcentaje-aleatorio (15) (55)) gastar-importe(importe-gastos)]
end

to gestionar-dinero-con-prestamo-tipo-alto
  let importe-deuda 0
  let importe-gastos 0
  if obtener-resultado-con-probabilidad(1) [set importe-deuda (sueldo * generar-porcentaje-aleatorio (5) (25)) pedir-prestamo(importe-deuda)]
  if cuenta-corriente > 0 and obtener-resultado-con-probabilidad(2) [set importe-gastos (cuenta-corriente * generar-porcentaje-aleatorio (1) (3)) gastar-importe(importe-gastos)]
end

to-report obtener-nivel-tipo-interes
  ;1= tipos de interes bajos
  let nivel 1
  ;2= tipos de interes medio
  if tipo-interes > 8 [set nivel 2]
  report nivel
end

to calcular-precio-cesta
 let oferta-demanda ((consumo-mensual - consumo-oferta) / consumo-oferta)
 set precio-cesta-basica-mensual (precio-cesta-basica-mensual + (precio-cesta-basica-mensual * oferta-demanda * (1 / precio-cesta-basica-mensual)))
 set consumo-oferta ((numero-personas * precio-cesta-basica-mensual) + (consumo-oferta * oferta-demanda * obtener-nivel-tipo-interes * (1 / precio-cesta-basica-mensual)))
 if consumo-oferta < (numero-personas * precio-cesta-basica-mensual * 0.25) [set consumo-oferta (numero-personas * precio-cesta-basica-mensual * 0.25)]
 if precio-cesta-basica-mensual < 100 [set precio-cesta-basica-mensual 100]
 set precio-cesta-basica-mensual (precision precio-cesta-basica-mensual 2)
end

to calcular-ipc-anual
 set ipc ((precio-cesta-basica-mensual - precio-cesta-basica-anterior) / precio-cesta-basica-anterior)
end

to calcular-inflacion
  if ipc < 0 [set inflacion "Deflación"]
  if ipc >= 0 and ipc < 0.005 [set inflacion "Inflación baja"]
  if ipc >= 0.005 and ipc <= 0.05 [set inflacion "Inflación controlada"]
  if ipc > 0.05 [set inflacion "Inflación no controlada"]
end

to calcular-sueldo-medio
  let sueldo-medio-anterior mean [sueldo] of turtles
  if sueldo-medio-mensual != sueldo-medio-anterior [reiniciar-sueldo-medio]
  ask n-of 175 turtles [set sueldo (sueldo + (sueldo * ipc))]
  ask turtles with [sueldo < 400] [set sueldo 400]
  set sueldo-medio-mensual mean [sueldo] of turtles
  set sueldo-medio-mensual (precision sueldo-medio-mensual 2)
end

to reiniciar-sueldo-medio
  let sueldo-bajo (sueldo-medio-mensual - (sueldo-medio-mensual * 0.4))
  let sueldo-alto (sueldo-medio-mensual + (sueldo-medio-mensual * 0.6666))
  ask turtles [set sueldo sueldo-medio-mensual]
  ask n-of 125 turtles [set sueldo sueldo-bajo]
  ask n-of 75 turtles with [sueldo = sueldo-medio-mensual] [set sueldo sueldo-alto]
end

to pintar-tortugas
  ask turtles with [ahorros > deudas] [set color green]
  ask turtles with [ahorros < deudas] [set color red]
  ask turtles with [ahorros = deudas] [set color blue]
  ask turtles [setxy random-xcor random-ycor]
end

to-report generar-porcentaje-aleatorio [extremo-inferior extremo-superior]
 report (random (extremo-superior - extremo-inferior + 1) + extremo-inferior) / 100
end

to-report obtener-resultado-con-probabilidad [probabilidad]
  let resultado false
  if random-float 100 < probabilidad [set resultado true]
  report resultado
end

to-report obtener-ahorros
  report sum [ahorros] of turtles
end

to-report obtener-deudas
  report sum [deudas] of turtles
end
@#$#@#$#@
GRAPHICS-WINDOW
227
10
760
229
-1
-1
1.048
1
10
1
1
1
0
0
0
1
0
500
0
200
1
1
1
ticks
30.0

BUTTON
7
46
96
79
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

BUTTON
112
46
197
79
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
0

PLOT
10
307
326
502
Ahorro y Deuda
Meses
Miles de Euros (1.000 €)
0.0
10.0
0.0
10.0
true
true
"" ""
PENS
"Ahorro" 1.0 0 -10899396 true "" "plot (obtener-ahorros / 1000)"
"Deuda" 1.0 0 -2674135 true "" "plot (obtener-deudas / 1000)"

TEXTBOX
563
176
718
206
Total wealth = $50,000
11
0.0
1

SLIDER
7
108
196
141
sueldo-medio-mensual
sueldo-medio-mensual
700
3000
1890.23
100
1
€
HORIZONTAL

SLIDER
7
151
197
184
tipo-interes
tipo-interes
0
25
2.5
0.5
1
%
HORIZONTAL

SLIDER
7
194
197
227
precio-cesta-basica-mensual
precio-cesta-basica-mensual
100
3000
670.36
100
1
€
HORIZONTAL

MONITOR
643
252
763
297
Tipo Inflación
inflacion
17
1
11

PLOT
337
307
537
502
Consumo
Meses
Miles de Euros (1.000€)
0.0
10.0
0.0
10.0
true
false
"" ""
PENS
"default" 1.0 0 -13791810 true "" "plot (consumo-mensual / 1000)"

MONITOR
172
252
325
297
Deuda (miles de Euros)
obtener-deudas / 1000
2
1
11

MONITOR
10
252
162
297
Ahorro (miles de Euros)
obtener-ahorros / 1000
2
1
11

MONITOR
543
252
636
297
IPC Anual (%)
ipc * 100
2
1
11

MONITOR
336
252
491
297
Consumo (miles de Euros)
consumo-mensual / 1000
2
1
11

PLOT
544
307
765
503
IPC Anual
Años
%
0.0
10.0
0.0
10.0
true
false
"" ""
PENS
"default" 1.0 0 -16777216 true "" "if mes = 2 [plot (ipc * 100) ]"

@#$#@#$#@
## ACKNOWLEDGMENT

This model is from Chapter Two of the book "Introduction to Agent-Based Modeling: Modeling Natural, Social and Engineered Complex Systems with NetLogo", by Uri Wilensky & William Rand.

* Wilensky, U. & Rand, W. (2015). Introduction to Agent-Based Modeling: Modeling Natural, Social and Engineered Complex Systems with NetLogo. Cambridge, MA. MIT Press.

This model is in the IABM Textbook folder of the NetLogo Models Library. The model, as well as any updates to the model, can also be found on the textbook website: http://www.intro-to-abm.com/.

## WHAT IS IT?

This model is a very simple model of economic exchange.  It is a thought experiment of  a world where, in every time step, each person gives one dollar to one other person (at random) if they have any money to give.  If they have no money then they do not give out any money.

## HOW IT WORKS

The SETUP for the model creates 500 agents, and then gives them each 100 dollars.  At each tick, they give one dollar to another agent if they can.  If they have no money then they do nothing. Each agent also moves to an x-coordinate equal to its wealth.

## HOW TO USE IT

Press SETUP to setup the model, then press GO to watch the model develop.

## THINGS TO NOTICE

Examine the various graphs and see how the model unfolds. Let it run for many ticks. The WEALTH DISTRIBUTION graph will change shape dramatically as time goes on. What happens to the WEALTH BY PERCENT graph over time?

## THINGS TO TRY

Try running the model for many thousands of ticks. Does the distribution stabilize? How can you measure stabilization? Keep track of some individual agents. How do they move?

## EXTENDING THE MODEL

Change the number of turtles.  Does this affect the results?

Change the rules so agents can go into debt. Does this affect the results?

Change the basic transaction rule of the model.  What happens if the turtles exchange more than one dollar? How about if they give a random amount to another agent at each tick? Change the rules so that the richer agents have a better chance of being given money? Or a smaller chance? How does this change the results?

## NETLOGO FEATURES

NetLogo plots have an auto scaling feature that allows a plot's x range and y range to grow automatically, but not to shrink. We do, however, want the y range of the WEALTH DISTRIBUTION histogram to shrink since we start with all 500 turtles having the same wealth (producing a single high bar in the histogram), but the distribution of wealth eventually flattens to a point where no particular bin has more than 40 turtles in it.

To get NetLogo to correctly adjust the histogram's y range, we use [`set-plot-y-range 0 40`](http://ccl.northwestern.edu/netlogo/docs/dictionary.html#set-plot-y-range) in the histogram's pen update commands and let auto scaling set the maximum higher if needed.

## RELATED MODELS

Wealth Distribution.

## CREDITS AND REFERENCES

Models of this kind are described in:

* Dragulescu, A. & V.M. Yakovenko, V.M. (2000).  Statistical Mechanics of Money. European Physics Journal B.

## HOW TO CITE

This model is part of the textbook, “Introduction to Agent-Based Modeling: Modeling Natural, Social and Engineered Complex Systems with NetLogo.”

If you mention this model or the NetLogo software in a publication, we ask that you include the citations below.

For the model itself:

* Wilensky, U. (2011).  NetLogo Simple Economy model.  http://ccl.northwestern.edu/netlogo/models/SimpleEconomy.  Center for Connected Learning and Computer-Based Modeling, Northwestern Institute on Complex Systems, Northwestern University, Evanston, IL.

Please cite the NetLogo software as:

* Wilensky, U. (1999). NetLogo. http://ccl.northwestern.edu/netlogo/. Center for Connected Learning and Computer-Based Modeling, Northwestern University, Evanston, IL.

Please cite the textbook as:

* Wilensky, U. & Rand, W. (2015). Introduction to Agent-Based Modeling: Modeling Natural, Social and Engineered Complex Systems with NetLogo. Cambridge, MA. MIT Press.

## COPYRIGHT AND LICENSE

Copyright 2011 Uri Wilensky.

![CC BY-NC-SA 3.0](http://ccl.northwestern.edu/images/creativecommons/byncsa.png)

This work is licensed under the Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License.  To view a copy of this license, visit https://creativecommons.org/licenses/by-nc-sa/3.0/ or send a letter to Creative Commons, 559 Nathan Abbott Way, Stanford, California 94305, USA.

Commercial licenses are also available. To inquire about commercial licenses, please contact Uri Wilensky at uri@northwestern.edu.

<!-- 2011 -->
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
NetLogo 6.0.3
@#$#@#$#@
resize-world 0 500 0 500 setup ask turtles [ set size 5 ] repeat 150 [ go ]
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
