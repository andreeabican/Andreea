;lista de reguli
;(define reguli
 ; '(reguli
  ; (R11 ((componenta-meniu curcan)) ((culoare-vin rosie 0.7) (culoare-vin alba 0.2)))
   ;(R12 ((componenta-meniu peste)) ((culoare-vin alba)))
   ;(R13 ((componenta-meniu sos-alb)) ((culoare-vin alba 0.6)))
   ;(R14 ((componenta-meniu porc)) ((culoare-vin rosie)))
   ;(R21 ((sos-meniu sos-alb)) ((tip-vin sec 0.8) (tip-vin demisec 0.6)))
   ;(R22 ((sos-meniu sos-tomat)) ((tip-vin dulce 0.8) (tip-vin demisec 0.5)))
   ;(R23 ((sos-meniu necunoscut)) ((tip-vin demisec)))
   ;(R24 ((componenta-meniu curcan)) ((tip-vin dulce 0.6) (tip-vin demisec 0.4)))
   ;(R31 ((culoare-vin rosie)(tip-vin dulce)) ((vin gamay)))
   ;(R32 ((culoare-vin rosie) (tip-vin sec)) ((vin cabernet-sauvignon)))
   ;(R33 ((culoare-vin rosie) (tip-vin demisec)) ((vin pinot-noir)))
   ;(R34 ((culoare-vin alba) (tip-vin dulce)) ((vin chemin-blanc)))
   ;(R35 ((culoare-vin alba) (tip-vin sec)) ((vin chardonnay)))
   ;(R36 ((culoare-vin alba) (tip-vin demisec)) ((vin riesling)))
  ;))

;(define fapte
;  '(fapte (componenta-meniu curcan) (sos-meniu sos-tomat)))
;(define monovaloare
;  '(monovaloare componenta-meniu culoare-vin sos-meniu))
;(define multivaloare
;  '(multivaloare tip-vin vin))
;(define rezultat
;  '(rezultat vin))

;sterge atributele duplicat care apar in lista de atribute extrase in identificare
(define remove-duplicates
  (lambda (l)
    (cond
      ((null? l) '())
      ((null? (cdr l)) (list (car l)))
      ((member (car l) (cdr l)) (remove-duplicates (cdr l)))
      (else (cons (car l) (remove-duplicates (cdr l))))
      )
    ))

;extrage atributele dintr-o lista de fapte
(define extract-attributes
  (lambda (l)
    (cond
      ((null? l) '())
      (else (cons (caar l) (extract-attributes (cdr l))))
      )
    ))

;identifica toate atributele din partea stanga a regulilor care contin in 
;concluzie atributul primit ca parametru. Aceste atribute se memoreaza 
;intr-o lista alaturi de primul atribut primit
(define identificare 
  (lambda (atribut reguli)
    (cond
      ((null? reguli) '())
      ;adauga intr-o lista atributele care au atributul primit ca parametru in partea dreapta
      ((eq? (car (caaddr (car reguli))) atribut) (append (extract-attributes (cadr (car reguli))) (identificare atribut (cdr reguli))))
      (else (identificare atribut (cdr reguli)))
      )
    ))

;identifica toate atributele din partea stanga - recursiv - pana cand
;nu mai face match pe nimic - incepand de la primul atribut care trebuie cautat in partea dreapta -
;adica cel indicat in rezultat
(define identifica-complet
  (lambda (attributes reguli reguli-backup)
    (cond
      ((null? reguli-backup) '())
      ((null? attributes) '())
      (else (let
                ((new-attributes (remove-duplicates (identificare (car attributes) reguli))))
              (append (identifica-complet (append (cdr attributes) new-attributes) reguli-backup reguli-backup) new-attributes)
              )
            )
      )
    ))
 
;pentru fiecare element din lista de atribute obtinuta in pasul de identificare a conflictelor
;se cauta regulile care contin ipoteze ce pot fi construite folosind elemente din lista de fapte.
;Cand se gaseste o astfel de regula, se adauga in baza de fapte o pereche (atribut valoare incredere)
;care corespunde regulii identificate cu baza de fapte

;(din lista de atribute pentru care nu se cunosc valorile si factorii de incredere)

;returneaza in fapte o lista cu alte fapte care trebuie adaugate in lista de fapte
;faptele nou create au CF calculat in functe de ce exista deja in fapte combinat
;cu ce exista in reguli
(define get-new-facts
  (lambda (fapte atribut reguli fapte-noi coef-incredere)
    (cond
      ;aici updatez coeficientii de incredere pentru faptele noi
      ((null? reguli) ;(display (calculeaza-incredere fapte-noi coef-incredere)) ;(display fapte-noi) (display coef-incredere) ;(calculeaza-incredere fapte-noi coef-incredere) 
       fapte-noi)
      ;daca atributul se gaseste in concluzia regulii
      (
       (and (eq? (car (caaddr (car reguli))) atribut) (exista-in-fapte (cadr (car reguli)) fapte '()))
       ;construieste faptele care vor fi adaugate 
       (let 
           (
            (fapte-noi-update (calculeaza-incredere (caddr (car reguli)) (exista-in-fapte (cadr (car reguli)) fapte '())))
            )
         ;(display fapte-noi-update)
         ;(get-new-facts fapte atribut (cdr reguli) (append fapte-noi (caddr (car reguli))) (append coef-incredere (exista-in-fapte (cadr (car reguli)) fapte '())))
         (get-new-facts fapte atribut (cdr reguli) (append fapte-noi fapte-noi-update) (append coef-incredere (exista-in-fapte (cadr (car reguli)) fapte '())))
         )
       )
      (else (get-new-facts fapte atribut (cdr reguli) fapte-noi coef-incredere))
      )
    ))

;calculeaza increderea faptelor noi adaugate, in functie de premisa din care rezulta si factorul de incredere al acesteia
(define calculeaza-incredere
  (lambda (fapte-noi coeficienti)
    (let
        (
         (coef-min (minim coeficienti))
         )
      (cond 
        ((null? fapte-noi) '())
        ((eq? coef-min '1) fapte-noi)
        ((eq? (length (car fapte-noi)) 2) (cons (append (car fapte-noi) (list coef-min)) (calculeaza-incredere (cdr fapte-noi) coeficienti)))
        (else (let 
                  ((new-coef (* coef-min (caddar fapte-noi))))
                (cond
                  ((eq? new-coef 1) (cons (list (caar fapte-noi) (cadar fapte-noi)) (calculeaza-incredere (cdr fapte-noi) coeficienti)))
                  (else (cons (list (caar fapte-noi) (cadar fapte-noi) (* coef-min (caddar fapte-noi))) (calculeaza-incredere (cdr fapte-noi) coeficienti)))
                  )
                ))
        )
      )
    ))

(define minim
  (lambda (l)
    (cond
      ((null? l) '())
      ((null? (cdr l)) (car l))
      ((< (car l) (minim (cdr l))) (car l))
      (else (minim (cdr l)))
      )
    ))

;updateaza lista de fapte in functie de reguli si atributul primit ca parametru
;(din lista de atribute pentru care nu se cunosc valorile si factorii de incredere)
(define update-facts
  (lambda (fapte atribut reguli monovaloare multivaloare)
    (cond
      ((null? fapte) '())
      ((null? reguli) fapte)
      (else (let*
                (
                 (new-facts (get-new-facts fapte atribut reguli '() '()))
                 (unique-facts (combina-duplicate new-facts '()))
                 (final-facts (max-atribut-monovaloare unique-facts monovaloare multivaloare '()))
                 )
              (append fapte final-facts)
              )
            )
      )
    ))

;pentru fiecare atribut verifica daca este atribut monovaloare, iar daca este
;atunci pastreaza valoarea cu CF cel mai mare
(define max-atribut-monovaloare
  (lambda (l monovaloare multivaloare l-prelucrat)
    (cond
      ((null? l) l-prelucrat)
      (else
       (let 
           (
            (same-atts (filter (lambda (x) (eq? (caar l) (car x))) l))
            (diff-atts (filter (lambda (x) (not (eq? (caar l) (car x)))) l))
            )
         (cond
           ((not (member (caar same-atts) monovaloare)) (max-atribut-monovaloare diff-atts monovaloare multivaloare (append same-atts l-prelucrat)))
           (else (max-atribut-monovaloare diff-atts monovaloare multivaloare (append (list (fapta-max-incredere same-atts)) l-prelucrat)))
           )
         )
       )
      )
    ))

;minim
(define minim
  (lambda (l)
    (cond
      ((null? l) '())
      ((null? (cdr l)) (car l))
      ((< (car l) (minim (cdr l))) (car l))
      (else (minim (cdr l)))
      )
    ))

;alege dintre toate faptele, pe cea cu cel mai mare factor de incredere
(define fapta-max-incredere
  (lambda (l)
    (cond
      ((null? l) '())
      ((null? (cdr l)) (car l))
      ((cond
         ((eq? (length (car l)) 2) (car l)) 
         (else
          (let 
              ((value (fapta-max-incredere (cdr l))))
            (cond
              ((eq? (length value) 2) value)
              ((> (caddar l) (caddr (fapta-max-incredere (cdr l)))) (car l))
              (else (fapta-max-incredere (cdr l)))
              )
            ))
         ))
      )
    ))
    

;combina atributele cu valori duplicat
(define combina-duplicate
  (lambda (fapte-noi fapte-combinate)
    (cond
      ((null? fapte-noi) fapte-combinate)
      (else
       (let
           (
            (same-value-atts (filter (lambda (x) (eq? (cadar fapte-noi) (cadr x))) fapte-noi))
            (diff-value-atts (filter (lambda (x) (not (eq? (cadar fapte-noi) (cadr x)))) fapte-noi))
            )
         (combina-duplicate diff-value-atts (cons (combinaR3 same-value-atts) fapte-combinate)) 
         )
       )
    )))

;combina dupa regula 3(CF = CF1 + CF2(1 - CF1))
(define combinaR3
  (lambda (l)
    (cond
      ((eq? (length l) 1) 
       (cond
         ((eq? (length (car l)) 2) (car l))
         ((or (equal? (caddar l) 1) (equal? (caddar l) 1.0)) (list (caar l) (cadar l)))
         (else (car l))))
      ((eq? (length (car l)) 2) '(car l))
      (else (combinaR3 (cons (list (caar l) (cadar l) (+ (caddar l) (* (car (cddadr l)) (- 1 (caddar l))))) (cddr l))))
      )))

;verifica daca toate ipotezele din premisa sunt continute in fapte si intoarce
;valoare coeficientilor de incredere pentru fiecare ipoteza gasita in fapte
(define exista-in-fapte
  (lambda (premisa fapte factori-incredere)
    (cond
      ((null? fapte) #f)
      ((null? premisa) factori-incredere)
      ((exista-ipoteza-in-fapte (car premisa) fapte) (exista-in-fapte (cdr premisa) fapte (append (exista-ipoteza-in-fapte (car premisa) fapte) factori-incredere)))
      (else #f)
      )
    ))

;verifica daca lista de fapte contine o pereche de tipul (atribut valoare incredere),
;incredere nefiind luat in considerare
(define exista-ipoteza-in-fapte
  (lambda (ipoteza fapte)
    (cond
      ((null? fapte) #f)
      ((null? ipoteza) #f)
      ((and (eq? (car ipoteza) (caar fapte)) (eq? (cadr ipoteza) (cadar fapte))) (cond
                                                                                   ((eq? (length (car fapte)) 2) '(1))
                                                                                   (else (cddar fapte)))) ;TODO sa returneze si o lista de CF
      (else (exista-ipoteza-in-fapte ipoteza (cdr fapte)))
      )
    ))


;verifica daca lista de fapte contine cel putin un fapt pentru atributul primit ca parametru
;daca da intoarce true, altfel intoarce false
(define contine
  (lambda (l atribut)
    (cond
      ((null? l) #f)
      ((eq? (caar l) atribut) #t)
      (else (contine (cdr l) atribut))
      )
    ))

;adauga in memoria de lucru fapte noi pentru fiecare atribut pe care il gaseste in lista primita
;ca parametru - lista contine doar numele atributelor
(define update-mem-de-lucru
  (lambda (l fapte reguli monovaloare multivaloare)
    (cond
      ((null? reguli) '())
      ((null? l) fapte)
      (else (update-mem-de-lucru (cdr l) (update-facts fapte (car l) reguli monovaloare multivaloare) reguli monovaloare multivaloare))
      )
    ))

;read from file and get all the lists needed for solving the problem
;in aux o sa formez lista pe care o sa o adaug in una din cele 3 liste 
;la intalnirea "."
(define read-from-file
  (lambda (i reguli monovaloare multivaloare aux-lista aux-sublista)
    (let ((line (read-line i)))
      (cond
        ((eof-object? line) (close-input-port i) (list reguli monovaloare multivaloare))
        (else ;(display line)
              (set! line (substring line 0 (- (string-length line) 1)))
              (cond
                ((equal? (substring line 0 7) "Regula-") (let*
                                                             ((splitted (regexp-split #rx": daca | = | " line))
                                                              (prima-parte (car splitted))
                                                              (rest (cdr splitted))
                                                              (fapt (creeaza-fapte-din-lista rest)))
                                                           ;(display "rest ") (display rest)
                                                           ;(newline)
                                                           ;(display (car (list-tail rest (- (length rest) 1))))
                                                           (cond
                                                             ;mai urmeaza ipoteze, asa ca adaugam la sublista
                                                             ((equal? (car (list-tail rest (- (length rest) 1))) "si") 
                                                              ;(display "sunt egale") (newline)
                                                              (read-from-file i reguli monovaloare multivaloare (list (string->symbol prima-parte)) (append aux-sublista (list fapt))))
                                                             (else 
                                                              ;nu mai urmeaza ipoteze, asa ca adaugam in lista, iar sublista devine '()
                                                              (read-from-file i reguli monovaloare multivaloare (list (string->symbol prima-parte) (list fapt)) '()))  
                                                             )
                                                           )
                                                         )
                ((equal? (substring line 0 6) "atunci") (let*
                                                            ((splitted (regexp-split #rx" = | |\\." line))
                                                             (rest (cdr splitted))
                                                             (fapt (creeaza-fapte-din-lista rest)))
                                                          ;(display 'splitted) (newline) (display splitted) (newline)
                                                          ;(display 'fapt:) (newline) (display fapt)
                                                          ;(display "INTA AICI") (newline)
                                                          (cond
                                                            ;s-a terminat regula, deci o adaugam in reguli
                                                            ((equal? (car (list-tail rest (- (length rest) 1))) "")
                                                             ;(display "============ sunt egale!") (newline)
                                                             (read-from-file i (append reguli (list (append aux-lista (list (list fapt))))) monovaloare multivaloare '() '())
                                                             )
                                                            ;mai urmeaza alte fapte din concluzie, deci adaugam la sublista
                                                            ((equal? (car (list-tail rest (- (length rest) 1))) "si")
                                                             ;(display "sunt egale!") (newline)
                                                             (read-from-file i reguli monovaloare multivaloare aux-lista (list fapt))
                                                             ))
                                                          ))
                ((equal? (substring line 0 11) "multivalued") 
                 ;(display "multivalued") (newline)
                 (let 
                     ((att-value (substring line 12 (- (string-length line) 2))))
                   (read-from-file i reguli monovaloare (append multivaloare (list (string->symbol att-value))) aux-lista aux-sublista)
                   )
                 )
                ((equal? (substring line 0 10) "monovalued") 
                 ;(display "multivalued") (newline)
                 (let 
                     ((att-value (substring line 11 (- (string-length line) 2))))
                   (read-from-file i reguli (append monovaloare (list (string->symbol att-value))) multivaloare aux-lista aux-sublista)
                   )
                 )
                (else (let*
                          ((splitted (regexp-split #rx" = | |\\." line))
                           (rest splitted)
                           (fapt (creeaza-fapte-din-lista rest)))
                        ;(newline) (display "REST: ") (display rest) (newline)
                        (cond
                          ;inseamna ca urmeaza un atunci, deci adaugam aux-sublist la aux-list, iar aux-sublist devine '()
                          ((equal? (length rest) 2) 
                           (read-from-file i reguli monovaloare multivaloare (append aux-lista (list (append aux-sublista (list fapt)))) '()))
                          
                          ;mai adauga in aux-sublista un fapt
                          ((equal? (car (list-tail rest (- (length rest) 1))) "si") 
                           (read-from-file i reguli monovaloare multivaloare aux-lista (append aux-sublista (list fapt))))
                          ;adaugam totul in reguli pentru ca am format o regula completa
                          ((equal? (car (list-tail rest (- (length rest) 1))) "") 
                           ;(display "face match") (newline)
                           (read-from-file i (append reguli (list (append aux-lista (list (append aux-sublista (list fapt)))))) monovaloare multivaloare '() '()))
                          )
                        )
                      )
                )
        )
      )
    )))
                                                                                        
;inlocuieste virgula cu punct
(define decimal-string-format
 (lambda (s)
   (let ((x (list->string (string->list s))))
    (string-set! x 1 #\.)
    x)
   ))
 
;din lista de stringuri formeaza un fapt
(define creeaza-fapte-din-lista
 (lambda (l)
   (cond
     ((or (eq? (length l) 3) (eq? (length l) 2)) (list (string->symbol (car l)) (string->symbol (cadr l))))
     ((or (eq? (length l) 5) (eq? (length l) 4)) (list (string->symbol (car l)) (string->symbol (cadr l)) (string->number (decimal-string-format (cadddr l)))))
     )
   ))

;(define fapte '(fapte (has-sauce yes) (sauce spicy) (tastiness delicate) (main-component poultry) (has-turkey yes)))
(define fapte '(fapte (has-sauce no) (sauce spicy) (tastiness delicate) (main-component poultry) (has-turkey yes)))
(define rezultat '(rezultat wine))

(define selecteaza-rezultat
  (lambda (l atribut-rezultat rezultat-final)
    (cond
      ((null? l) (substring rezultat-final 0 (- (string-length rezultat-final)  2)))
      ((eq? atribut-rezultat (caar l))
       (cond
         ((eq? (length (car l)) 2)
          (selecteaza-rezultat (cdr l) atribut-rezultat (string-append rezultat-final (symbol->string (cadar l)) " ,")))
         (else (selecteaza-rezultat (cdr l) atribut-rezultat (string-append rezultat-final (symbol->string (cadar l)) " " (number->string (caddar l)) ", ")))
         ))
      (else (selecteaza-rezultat (cdr l) atribut-rezultat rezultat-final))
      )))

(define read-file
  (lambda ()
    (let* 
        (
         (rezultat-citire (read-from-file (open-input-file "wine.txt") '(reguli) '(monovaloare) '(multivaloare) '() '()))
         (reguli (car rezultat-citire))
         (monovaloare (cadr rezultat-citire))
         (multivaloare (caddr rezultat-citire))
         (lista-atribute (remove-duplicates (append (identifica-complet (cdr rezultat) (cdr reguli) (cdr reguli)) (list (cadr rezultat)))))
         (lista-rezultat (update-mem-de-lucru lista-atribute (cdr fapte) (cdr reguli) (cdr monovaloare) (cdr multivaloare)))
         )
      (selecteaza-rezultat lista-rezultat (cadr rezultat) (string-append (symbol->string (cadr rezultat)) " = "))
      )
    ))

;rezolva problema
(define rezolva
  (lambda()
    (read-file)
    ))