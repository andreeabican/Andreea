(defmodule MAIN (export ?ALL))

; in verificate retin id-urile pentru care deja am modificat scorul in functia de fitness
(deffacts fapte (scorev 0) (scoren 0) (verificate) (verificate1) (verificate2) (verificate3) (verificate4) (verificate5) (camere) (iterations) (ok -1) (vector) (vector1) (vector2) (ok1 -2))

(deftemplate item (slot id) (slot name) (slot instructor) (slot duration) (multislot groups))
(deftemplate instructor (slot name) (multislot intervals))

(deftemplate orar (slot id) (slot zi) (multislot ore) (slot sala) (slot tip))

;am permis si duplicate chiar daca nu o sa fie nevoie, cred...
(defrule init
=>
	(focus ORAR DETORAR)
	(set-fact-duplication TRUE)
	(load-facts "C:\input.txt")
	(set-fact-duplication FALSE))

;functia asta e pusa degeaba, dar fara ea nu mergea
(defrule init2
	(rooms $?rooms)
=>
	(assert (camere $?rooms)))
	
(deffunction randomroom ($?rooms)
	(bind ?l (length $?rooms))
	(bind ?nrcamera (random 1 ?l))
	;(assert (room (nth$ ?nrcamera $?rooms)))
	(return (nth$ ?nrcamera $?rooms)))
	
(deffunction randomintervals (?duration)
	(bind ?start (random 8 (- 20 ?duration)))
	(return (create$ ?start (+ ?start ?duration))))
	
(deffunction randomday ()
	(bind $?zile (create$ Mon Tue Wed Thu Fri))
	(bind ?n (random 1 5))
	(return (nth$ ?n $?zile)))

;__________________________________	
(defmodule ORAR (import MAIN ?ALL))
;__________________________________

(defrule afisare
	;(declare (salience 11))
	?f <- (item (id ?id) (name ?name) (instructor ?instructor) (duration ?duration) (groups $?groups))
	?g <- (instructor (name ?instructor) (intervals [ ?day $?ore ] $?))
	(test (eq (length $?ore) 2))
	(rooms $?rooms)
=>
	;aici aveam $?ore in loc de orele alea puse de mine
	(assert (orar (id ?id) (zi (randomday)) (ore (randomintervals ?duration)) (sala (randomroom $?rooms)) (tip vechi)))
	;(open "orar.txt" out "a")
	;(printout out ?id " " ?day " " $?ore " " ?sala  crlf)
	;(close)
)
	
(defrule afisare1
	;(declare (salience 10))
	?f <- (item (id ?id) (name ?name) (instructor ?instructor) (duration ?duration) (groups $?groups))
	(not (instructor (name ?instructor)))
	(rooms $?rooms)
=>
	(assert (orar (id ?id) (sala (randomroom $?rooms)) (zi (randomday)) (ore (randomintervals ?duration)) (tip vechi)))
	;(open "orar.txt" out "a")
	;(printout out "Acesta este un item simplu " ?id crlf)
	;(close)
)

;______________________________________
(defmodule DETORAR (import MAIN ?ALL))
;______________________________________
;primeste ca parametru numarul de iteratii pe care trebuie sa le faca
;(deffunction det-orar (?n)
;	(while (> ?n 0)
		;face copia, mutatiile, calculeaza scor pentru orar nou si orar vechi, stabileste care dintre cele doua este mai bun
		;(focus EVALUARE FITNESSNOU FITNESSVECHI MUTATIEORA1 MUTATIEORA MUTATIESALA COPIE)
;		(focus COPIE)
;		(printout t "n este: " ?n crlf)
;		(bind ?n (- ?n 1))))

(defrule apel-functie
	?f <- (iterations ?n)
	(test (> ?n 0))
=>
	;(printout t "In apel functie========================" crlf)
	(focus COPIE MUTATIESALA MUTATIEORA MUTATIEORA1 FITNESSNOU FITNESSVECHI EVALUARENOU EVALUAREVECHI MODIFICA RESET)
	;(printout t "n este: " ?n crlf)
	(retract ?f)
	(assert (iterations (- ?n 1))))
	
(defrule scrie-orar
	(iterations 0)
	(orar (id ?id) (zi ?zi) (ore $?ore) (sala ?sala) (tip vechi))
=>
	(open "out-orar.txt" out "a")
	(printout out ?id " " ?zi " " ?ore " " ?sala crlf)
	(close))
	
;face o copie a orarului, cu diferenta ca tipul din noul orar va fi "nou" in loc de "vechi"
;______________________________________
(defmodule COPIE (import MAIN ?ALL))
;______________________________________
(defrule copie-orar
	?f <- (orar (id ?id) (zi ?zi) (ore $?ore) (sala ?sala) (tip vechi))
	(not (orar (id ?id) (tip nou)))
=>
	;(printout t "Intra pe aici" crlf)
	(assert (orar (id ?id) (zi ?zi) (ore $?ore) (sala ?sala) (tip nou))))

;__________________________________________	
(defmodule MUTATIESALA (import MAIN ?ALL))
;__________________________________________
;(deffacts verif (vector))

;sterge tot ce avem in vector, inainte de a incepe sa il refoloseasca, asta daca nu se initializeaza el singur mereu
;(defrule clear
;	?f <- (vector $?x)
;=>
;	(retract ?f))

;se face mutatia daca gasim doua ore care au loc in acelasi timp in aceeasi sala
(defrule mutatie1
	?f <- (orar (id ?id) (ore ?start ?fin) (sala ?sala) (tip nou) (zi ?zi))
	?g <- (orar (id ?id1) (ore ?start1 ?fin1) (sala ?sala) (tip nou) (zi ?zi))
	;testam sa nu luam acelasi fapt
	(test (neq ?f ?g))
	(and (not (vector $?a&:(eq (mod (length $?a) 2) 0) ?id ?id1 $?b)) (not (vector $?x&:(eq (mod (length $?x) 2) 0) ?id1 ?id $?y)))
	?ver <- (vector $?v)
	(rooms $?rooms)
=>
	;(printout t "Se face mutatie pentru aceeasi sala in acelasi timp " crlf)
	(retract ?ver)
	(assert (vector $?v ?id ?id1))
	(if (or (and (>= ?start ?start1) (< ?start ?fin1)) (and (> ?fin ?start1) (<= ?fin ?fin1)))
		then
			(modify ?f (sala (randomroom $?rooms)))))
			
;________________________________________
(defmodule MUTATIEORA (import MAIN ?ALL))
;________________________________________
;(deffacts verif (vector1))

;da un alt interval orar atunci cand orele nu corespund cu doleantele profesorilor

(defrule mutatie2
	?f <- (orar (id ?id) (zi ?zi) (ore ?start ?fin) (tip nou))
	(item (id ?id) (instructor ?name) (duration ?duration))
	(instructor (name ?name) (intervals $? [ ?zi ?start1 ?fin1 ] $?))
	(test (or (< ?start ?start1) (> ?start ?fin1) (> ?fin ?fin1) (< ?fin ?start1)))
	(not (vector1 $? ?id $?))
	?ver <- (vector1 $?v)
=>
	;(printout t "Mutatie doleante profesori " ?id crlf)
	(retract ?ver)
	(assert (vector1 $?v ?id))
	(modify ?f (ore (randomintervals ?duration))))
	
;________________________________________
(defmodule MUTATIEORA1 (import MAIN ?ALL))
;________________________________________
;(deffacts verif (vector2))

;schimba ora daca sunt doua cursuri sau seminarii, in acelasi timp, la care participa aceeasi grupa
;(defrule nimic
;	?f <- (vector2 $?x)
;=>
;	(printout t "Ce avem in vector2 " $?x crlf))

(defrule mutatie3
	?f <- (orar (id ?id) (zi ?zi) (ore ?start ?fin) (tip nou))
	?g <- (orar (id ?id1) (zi ?zi) (ore ?start1 ?fin1) (tip nou))
	?h <- (item (id ?id) (groups $? ?grupa $?) (duration ?duration))
	?i <- (item (id ?id1) (groups $? ?grupa $?))
	(test (neq ?f ?g))
	(test (neq ?h ?i)) ; de cond asta nu cred ca mai era nevoie
	(and (not (vector2 $?a&:(eq (mod (length $?a) 2) 0) ?id ?id1 $?b)) (not (vector2 $?x&:(eq (mod (length $?x) 2) 0) ?id1 ?id $?y)))
	?ver <- (vector2 $?v)
	(test (or (and (>= ?start ?start1) (< ?start ?fin1)) (and (> ?fin ?start1) (<= ?fin ?fin1))))
=>
	;(printout t "Mutatie - aceleasi grupe in acelasi timp " ?id " " ?id1 crlf)
	(retract ?ver)
	(assert (vector2 $?v ?id ?id1))
	;modifica ora pentru primul id
	(modify ?f (ore (randomintervals ?duration))))
;_____________________________________
(defmodule FITNESSVECHI (import MAIN ?ALL))
;da un scor pentru orarul vechi
;_______________________________________

;doi profesori in aceeasi sala in acelasi timp
(defrule aceeasi-sala
	?f <- (orar (id ?id) (ore ?start ?fin) (sala ?sala) (tip vechi) (zi ?zi))
	?g <- (orar (id ?id1) (ore ?start1 ?fin1) (sala ?sala) (tip vechi) (zi ?zi))
	;testam sa nu luam acelasi fapt
	(test (neq ?f ?g))
	(and (not (verificate $?a&:(eq (mod (length $?a) 2) 0) ?id ?id1 $?b)) (not (verificate $?x&:(eq (mod (length $?x) 2) 0) ?id1 ?id $?y)))
	?sc <- (scorev ?scor)
	?verif <- (verificate $?v)
=>   
	;(printout t "A intrat si aici" ?id " " ?id1 crlf)
	(retract ?verif)
	(assert (verificate $?v ?id ?id1))
	(if (or (and (>= ?start ?start1) (< ?start ?fin1)) (and (> ?fin ?start1) (<= ?fin ?fin1)))
		then
			(retract ?sc)
			(assert (scorev (+ ?scor 5)))))

;mareste scorul daca ora nu se incadreaza in doleantele profesorului
(defrule doleante
	?f <- (orar (id ?id) (zi ?zi) (ore ?start ?fin) (tip vechi))
	?g <- (item (id ?id) (instructor ?name))
	?h <- (instructor (name ?name) (intervals $? [ ?zi ?start1 ?fin1 ] $?))
	(not (verificate1 $? ?id $?))
	?sc <- (scorev ?scor)
	?ver <- (verificate1 $?v)
=>
	;(printout t "Probleme doleante" crlf)
	(retract ?ver)
	(assert (verificate1 $?v ?id))
	(if (or (< ?start ?start1) (> ?start ?fin1) (> ?fin ?fin1) (< ?fin ?start1))
		then
			(retract ?sc)
			(assert (scorev (+ ?scor 5)))))
			
; mareste scorul daca o grupa are ore in acelasi timp, in aceeasi zi
(defrule aceleasi-grupe
	?f <- (orar (id ?id) (zi ?zi) (ore ?start ?fin) (tip vechi))
	?g <- (orar (id ?id1) (zi ?zi) (ore ?start1 ?fin1) (tip vechi))
	?h <- (item (id ?id) (groups $? ?grupa $?))
	?i <- (item (id ?id1) (groups $? ?grupa $?))
	(test (neq ?f ?g))
	(test (neq ?h ?i)) ; de cond asta nu cred ca mai era nevoie
	(and (not (verificate2 $?a&:(eq (mod (length $?a) 2) 0) ?id ?id1 $?b)) (not (verificate2 $?x&:(eq (mod (length $?x) 2) 0) ?id1 ?id $?y)))
	?sc <- (scorev ?scor)
	?ver <- (verificate2 $?v)
=>
	;(printout t "O grupa are ore in acelasi timp" crlf)
	(retract ?ver)
	(assert (verificate2 $?v ?id ?id1))
	(if (or (and (>= ?start ?start1) (< ?start ?fin1)) (and (> ?fin ?start1) (<= ?fin ?fin1)))
		then
		(retract ?sc)
		(assert (scorev (+ ?scor 5)))))
	
;_____________________________________
(defmodule FITNESSNOU (import MAIN ?ALL))
;da un scor pentru orarul nou
;_______________________________________

;doi profesori in aceeasi sala in acelasi timp
(defrule aceeasi-sala
	?f <- (orar (id ?id) (ore ?start ?fin) (sala ?sala) (tip nou) (zi ?zi))
	?g <- (orar (id ?id1) (ore ?start1 ?fin1) (sala ?sala) (tip nou) (zi ?zi))
	;testam sa nu luam acelasi fapt
	(test (neq ?f ?g))
	(and (not (verificate3 $?a&:(eq (mod (length $?a) 2) 0) ?id ?id1 $?b)) (not (verificate3 $?x&:(eq (mod (length $?x) 2) 0) ?id1 ?id $?y)))
	?sc <- (scoren ?scor)
	?ver <- (verificate3 $?v)
=>   
	;(printout t "A intrat si aici nou " ?id " " ?id1 crlf)
	(retract ?ver)
	(assert (verificate3 $?v ?id ?id1))
	(if (or (and (>= ?start ?start1) (< ?start ?fin1)) (and (> ?fin ?start1) (<= ?fin ?fin1))) 
		then
			(retract ?sc)
			(assert (scoren (+ ?scor 5)))))

;mareste scorul daca ora nu se incadreaza in doleantele profesorului
(defrule doleante
	?f <- (orar (id ?id) (zi ?zi) (ore ?start ?fin) (tip nou))
	?g <- (item (id ?id) (instructor ?name))
	?h <- (instructor (name ?name) (intervals $? [ ?zi ?start1 ?fin1 ] $?))
	(not (verificate4 $? ?id $?))
	?sc <- (scoren ?scor)
	?ver <- (verificate4 $?v)
=>
	;(printout t "Probleme doleante" crlf)
	(retract ?ver)
	(assert (verificate4 $?v ?id))
	(if (or (< ?start ?start1) (> ?start ?fin1) (> ?fin ?fin1) (< ?fin ?start1))
		then
			(retract ?sc)
			(assert (scoren (+ ?scor 5)))))
			
; mareste scorul daca o grupa are ore in acelasi timp, in aceeasi zi
(defrule aceleasi-grupe
	?f <- (orar (id ?id) (zi ?zi) (ore ?start ?fin) (tip nou))
	?g <- (orar (id ?id1) (zi ?zi) (ore ?start1 ?fin1) (tip nou))
	?h <- (item (id ?id) (groups $? ?grupa $?))
	?i <- (item (id ?id1) (groups $? ?grupa $?))
	(test (neq ?f ?g))
	(test (neq ?h ?i)) ; de cond asta nu cred ca mai era nevoie
	(and (not (verificate5 $?a&:(eq (mod (length $?a) 2) 0) ?id ?id1 $?b)) (not (verificate5 $?x&:(eq (mod (length $?x) 2) 0) ?id1 ?id $?y)))
	?sc <- (scoren ?scor)
	?ver <- (verificate5 $?v)
=>
	;(printout t "O grupa are ore in acelasi timp" crlf)
	(retract ?ver)
	(assert (verificate5 $?v ?id ?id1))
	(if (or (and (>= ?start ?start1) (< ?start ?fin1)) (and (> ?fin ?start1) (<= ?fin ?fin1)))
		then
		(retract ?sc)
		(assert (scoren (+ ?scor 5)))))
		
;_______________________________________
(defmodule EVALUARENOU (import MAIN ?ALL))

;evalueaza cele doua scoruri obtinute pentru cele doua orare, sterge faptele care au tipul orarului care a obtinut scorul mai mare si, daca orarul cu scor mai bun, este orarul nou, ii modifica tipul la vechi :)
;________________________________________

;daca orarul nou este mai bun decat cel vechi
(defrule nou-mai-bun
	?o <- (ok $?)
	?z <- (ok1 $?)
	?scor1 <- (scorev ?sv)
	?scor2 <- (scoren ?sn)
	(test (or (neq ?sv 0) (neq ?sn 0)))
	(test (>= ?sv ?sn))
=>
	;(printout t "Orarul nou este mai bun " ?sv " " ?sn crlf)
	(retract ?scor1 ?scor2)
	(assert (scoren 0))
	(assert (scorev 0))
	(retract ?o)
	(retract ?z)
	(assert (ok 1))
	(assert (ok1 -2)))
	
(defrule sterge-vechi
	(ok  1)
	?f <- (orar (tip vechi))
=>
	(retract ?f))
	
;___________________________________________
(defmodule EVALUAREVECHI (import MAIN ?ALL))
;____________________________________________
	
(defrule vechi-mai-bun
	?o <- (ok $?)
	?scor1 <- (scorev ?sv&:(neq ?sv 0))
	?scor2 <- (scoren ?sn&:(neq ?sn 0))
	(test (< ?sv ?sn))
	?z <- (ok1 $?)
=>
	;(printout t "Orarul vechi este mai bun " ?sv " " ?sn crlf)
	(retract ?scor1 ?scor2)
	(assert (scorev 0))
	(assert (scoren 0))
	(retract ?o)
	(retract ?z)
	(assert (ok1 -2))
	(assert (ok 0)))
	
(defrule sterge-nou
	(ok 0)
	?f <- (orar (tip nou))
=>
	(retract ?f))
	
;______________________________________
(defmodule MODIFICA (import MAIN ?ALL))
;______________________________________
	
	
(defrule modifica-tip
	(ok 1)
	?f <- (orar (tip nou))
=>
	;(printout t "Stergem tot ce are tipul nou" crlf)
	(modify ?f (tip vechi)))
	
;________________________________________
(defmodule RESET (import MAIN ?ALL))
;________________________________________

(defrule sterge-ok
	?a <- (ok ?x)
	?b <- (verificate $?)
	?c <- (verificate1 $?)
	?d <- (verificate2 $?)
	?e <- (verificate3 $?)
	?f <- (verificate4 $?)
	?g <- (verificate5 $?)
	?h <- (vector $?)
	?i <- (vector1 $?)
	?j <- (vector2 $?)
	?z <- (ok1 -2)
=>
	(retract ?a ?b ?c ?d ?e ?f ?g ?h ?i ?j ?z)
	(assert (ok1 -1))
	(assert (ok -1))
	(assert (verificate 0 0))
	(assert (verificate1 0 0))
	(assert (verificate2 0 0))
	(assert (verificate3 0 0))
	(assert (verificate4 0 0))
	(assert (verificate5 0 0))
	(assert (vector 0 0))
	(assert (vector1 0 0))
	(assert (vector2 0 0)))
	
;___________________________________
(defmodule SCRIE (import MAIN ?ALL))
;___________________________________

(defrule orar
	?f <- (orar (id ?id) (zi ?zi) (ore ?start ?fin) (sala ?sala) (tip vechi))
=>
	(open "orarbun.txt" descris "a")
	(printout descris ?id " " ?zi " [" ?start " " ?fin "] " ?sala crlf)
	(close))