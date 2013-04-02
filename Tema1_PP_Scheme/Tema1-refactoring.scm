(include "Reflection.scm")

;; Misc

;; Testeaza daca un simbol incepe cu un anumit caracter
;; Ex (symbol-starts-with? #\? '?variable) => #t
(define (symbol-starts-with? first-char x)
    (and (symbol? x)
         (let ((s (symbol->string x)))
           (and (>= (string-length s) 1)
                (equal? first-char (string-ref s 0))))))


;; Testeaza ca argumentul este o entitate primitiva (care nu mai poate fi descompusa)
(define (atom? x)
  (or (symbol? x)
      (number? x)
      (char? x)
      (string? x)
      (boolean? x)))

;;functii ajutoare

;verifica daca o variabila exista deja in lista.
   ; - daca exista returnez ce valoare ii corespunde acelei variabile
   ; - daca nu exista returnez false

(define (verify x L)
  (cond ((null? L) #f)
        ((equal? x (caar L)) (cadar L))
        (else (verify x (cdr L)))))

;(verify 8 '((2 4) (6 8) (10 12)))

;;functia mea===============================

;functia inlocuire - face toate inlocuirile dictate de lista care il urmeaza pe THEN
; primeste lista de perechi (varibila valoare) si patternul dupa care trebuie recreata functia
;returneaza functia nou creata

 (define (inlocuire pattern l1 var)
  (cond ((null? pattern) l1)
        ;daca este atom?, atunci nu mai facem nicio prelucrare si intoarcem direct ce ii corespunde in lista (variabila valoare)
        ((atom? pattern) (verify pattern var))
        ;parcurgem pattern element cu element, si reconstruim, avand grija ca atunci cand gasim o variabila sa o inlocuim cu valoarea corespunzatoare din var
        ((list? (car pattern))
         (let iter ((L (car pattern)) (n (length (car pattern))) (new '()))
           ;daca avem o sublista, o reconstruim si apelam recursiv reconstruire de restul
           (if (null? L)
               (cons new (inlocuire (cdr pattern) l1 var))
               ; aici se reconstruieste, cu multe append si cons...cred ca m-am complicat, dar oricum iese
               (if (list? (car L))
                   (iter (cdr L) (- n 1) (append new (cons (inlocuire (car L) '() var) '())))
                   (if (equal? #t (symbol-starts-with? #\? (car L)))
                       (iter (cdr L) (- n 1) (append new (cons (verify (car L) var) '())))
                       (iter (cdr L) (- n 1) (append new (cons (car L) '()))))))))
        ;daca nu este o sublista, verificam daca elementul este variabila ( si daca este o inlocuim cu valoarea sa), iar daca este un simplu element, atunci
        ; reconstruim fix cu acel element
         (else
          (if (equal? #t (symbol-starts-with? #\? (car pattern)))
              (cons (verify (car pattern) var) (inlocuire (cdr pattern) l1 var))
              (cons (car pattern) (inlocuire (cdr pattern) l1 var))))))
 
;(inlocuire '(cond (?cond1 ?result1) (?cond2 ?result2) (else ?result3)) '() '((?result3 (filter-odd-numbers (cdr L))) (?result2 (append (list (car L)) (filter-odd-numbers (cdr L)))) (?cond2 (odd? (car L))) (?result1 '()) (?cond1 (null? L))))

;functia replace, verifica daca patternul se potriveste cu ce i-am dat noi in pattern, iar daca se potriveste, va face inlocuirile dictate de pattern1
; in var memoram perechi (variabila valoare) - este o lista de perechi
;=> functia inlocuita dupa pattern
 
(define (replace l pattern pattern1 var)
  ;daca pattern si lista au numar egal de elemente, continuam verificarea, daca nu, intoarcem exact ce primisem pentru a nu modifica functia initiala
  (let iter ((L l) (p pattern) (v var))
    (if (equal? (length L) (length p))
        ; dupa ce am terminat parcurgerea si pattern si lista s-au potrivit, inlocuim toate variabilele conform a ceea ce urmeaza dupa THEN
        (cond ((null? p) (inlocuire (car pattern1) '() v))
              ;daca avem subliste, facem append intre acestea si restul listei pentru a le parcurge ok
              ((and (list? (car p)) (list? (car L)))
               (iter (append (car L) (cdr L)) (append (car p) (cdr p)) v))
              ;daca in pattern urmeaza o sublista, iar in lista noastra nu, atunci inseamna ca nu se potriveste si returnam lista nemodificata
              ((and (list? (car p)) (not (list? (car L))))
               l)
              ; daca incepe cu un element, atunci verificam daca acesta este variabila sau nu
              (else
               ; daca este variabila, il retinem in lista de variabile
               (cond ((symbol-starts-with? #\? (car p))
                      ;daca varibila exista deja in lista de variabile si noi incercam sa ii atribuim o alta valoare, atunci inseamna ca cele doua liste nu corespund
                      ; iara daca nu exista deja, inseamna ca putem sa o adaugam
                      (let ((value (verify (car p) v)))
                        (if (equal? value #f)
                            (iter (cdr L) (cdr p) (cons (list (car p) (car L)) v)) 
                            (if (equal? (car L) value)
                                (iter (cdr L) (cdr p) v)
                                l))))
                     ;daca nu este un simbol, pur si simplu verificam daca cele doua elemente sunt egale
                     ;daca nu sunt egale, din nou nu corespund si intoarcem lista nemodificata
                     (else
                      (if (equal? (car L) (car p))
                          (iter (cdr L) (cdr p) v)
                          l)))))
        ;daca au lungimi diferite, este clar ca nu seamana, deci returnam lista nemodificata
        l)))

;(replace (get-function-body filter-odd-numbers) '(if ?cond1 ?result1 (if ?cond2 ?result2 ?result3)) '(cond (?cond1 ?result1) (?cond2 ?result2) (else ?result3)) '())
;(replace '(lambda (x) (odd? x)) '(lambda (?x) (?f ?x)) '(?f) '())

; in pattern1 avem ce e in sublista lui THEN, exceptand THEN
; in pattern avem tot ce e in lista lui WHEN, exceptand primul element, adica WHEN
; in l avem functia noastra, pe car eo parcurgem si o modificam
; cred ca in l1 se gaseste reconstructia, dar nu prea sunt sigura

 ;=> toata functia modificata dupa pattern
(define (recons l l1 pattern pattern1)
  (cond ((null? l) l1)
        ((and (list? (car l)) (not (equal? (car l) '())))
         ;daca primul element din sublista este egal cu primul element din pattern, atunci apelam functia replace, si mai departe prelucram ce a intors
         (if (equal? (caar l) (car pattern))  
             (let iter ((L (replace (car l) pattern pattern1 '())) (n (length (car l))) (new '()))
               ;daca am parcurs tot L, facem cons intre ce am obtinut si restul listei
               (if (null? L)
                   (cons new (recons (cdr l) l1 pattern pattern1))
                   ;daca este lista, atunci apelam din nou iter de restul sublistei si recons de lista (car L) pentru a se aplica patternurile si in interiorul
                   ;acesteia daca este nevoie
                   (if (list? (car L))
                       (iter (cdr L) (- n 1) (append new (cons (recons (car L) '() pattern pattern1) '())))
                       (iter (cdr L) (- n 1) (append new (cons (car L) '()))))))
             ;iter1 functioneaza ca si iter doar ca este in cazul in care nu facem replace
             (let iter1 ((L (car l)) (n (length (car l))) (new '()))
               (if (null? L)
                   (cons new (recons (cdr l) l1 pattern pattern1))
                   (if (list? (car L))
                       (iter1 (cdr L) (- n 1) (append new (cons (recons (car L) '() pattern pattern1) '())))
                       (iter1 (cdr L) (- n 1) (append new (cons (car L) '()))))))))
        (else
         ;daca un element este egal cu elementul corespunzator din pattern, apelam replace de lista la inceputul careia se afla acel element
         (if (equal? (car l) (car pattern))
             (let ((r (replace l pattern pattern1 '())))
               ;aici, intorcea odd? si nu puteam sa aplic "car" deci daca este un atom?, il intoarcem pur si simplu
               (if (atom? r)
                    r
                    ;daca este o lista, adaugam inceputul lui replace, si o parcurgem in continuare, in caz ca mai gasim ceva de modificat
                    (cons (car (replace l pattern pattern1 '())) (recons (cdr (replace l pattern pattern1 '())) l1 pattern pattern1))))
             ;daca nu sunt egale, cautam in continuare ceva care sa se potriveasca cu patternul nostru
             (cons (car l) (recons (cdr l) l1 pattern pattern1))))))


;(recons (get-function-body filter-odd-numbers) '() '(if ?cond1 ?result1 (if ?cond2 ?result2 ?result3)) '(cond (?cond1 ?result1) (?cond2 ?result2) (else ?result3)))

(define (refactor-code fun refactoring-pack)
  ;am o functie veche si una noua
  ;daca dupa ce parcurgem, cu toate patternurile, nu se modifica functia noua, inseamna ca nu se mai paote modifica, altfel reluam iter de intreg refactoring-pack
  ;pana cand facem toate modificarile posibile
  (let iter ((pattern refactoring-pack) (function-n (get-function-lambda fun)) (function-v (get-function-lambda fun)))
    ;cand am parcurs toate patternurile, daca functiile sunt diferite, reluam. Daca sunt identice, ne oprim
    (cond ((null? pattern)
           (cond ((equal? function-n function-v)
                 function-n)
           (else (iter refactoring-pack function-n function-n))))
          ;daca nu am parcurs inca toate patternurile, trecem la patternul urmator
          (else 
           (let ((f (recons function-n '() (cadaar pattern) (cdadar pattern))))
             (iter (cdr pattern) f function-v))))))
