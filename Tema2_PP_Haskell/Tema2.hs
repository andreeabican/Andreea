data RegEx = Empty
			| Sim Char
			| Sau RegEx RegEx
			| Secv [RegEx]
			| Rep RegEx
			| Interv String
			| Exc String
			deriving (Show)
			
data Return = Match | NoMatch
	deriving(Show)
			
--verifica daca in paranteza avem pipe
verificaPipe (x:xs) n
	| (x == '(') = verificaPipe xs (n + 1)
	| (x == ')') && (n /= 0) = verificaPipe xs (n - 1)
	| (x == ')') && (n == 0) = False
	| (x == '|') && (n == 0) = True
	| (x == '|') && (n /= 0) = verificaPipe xs n
	| (x /= '|') = verificaPipe xs n

--ia tot ce e dupa pipe	
dupaPipe [] _ = []
dupaPipe (x:xs) n
	| (x == '(') = dupaPipe xs (n + 1)
	| (x == ')') && (n /= 0) = dupaPipe xs (n - 1)
	| (x == ')') && (n == 0) = []
	| (x == '|') && (n == 0) = xs
	| (x == '|') && (n /= 0) = dupaPipe xs n
	| (x /= '|') = dupaPipe xs n
	
--ia tot ce e inainte de pipe
beforePipe [] _ _= []
beforePipe (x:xs) n result
	| (x == '(') = beforePipe xs (n+1) (x:result)
	| (x == ')') && (n /= 0) = beforePipe xs (n - 1) (x:result)
	-- am ajuns la sfarsitul parantezei si nu am gasit niciun pipe :-?? - nu o sa existe cazul asta pentru ca deja am verificat daca are pipe
	| (x == ')') && ( n == 0) = reverse result
	| ( x == '|') && (n == 0) = reverse result
	| (x == '|') && (n /= 0) = beforePipe xs n (x:result)
	| (x /= '|') = beforePipe xs n (x:result)

--ia tot ce e dupa inchiderea parantezei	
afterP [] _ _ _ = []
afterP (x:xs) n tipPD tipPI
	| (n == 0) && (x == tipPI) = xs
	| (x == tipPD) = afterP xs (n+1) tipPD tipPI
	| (n /= 0) && ( x == tipPI) = afterP xs (n-1) tipPD tipPI
	| otherwise = afterP xs n tipPD tipPI

--ia tot ce e in paranteza
takeP (x:xs) n result tipPD tipPI
	| (n == 0) && (x == tipPI) = reverse result
	| (x == tipPD) = takeP xs (n+1) (x:result) tipPD tipPI
	| (n /= 0) && (x == tipPI) = (takeP xs (n - 1) (x:result) tipPD tipPI)
	| otherwise = takeP xs n (x:result) tipPD tipPI	

--intoarce stringul dictat de intervalul din paranteza patrata
dinPP [] lista = lista
dinPP (x:xs) lista
	| (xs /= []) && ((head xs) == '-') = dinPP (tail(tail xs)) (lista ++ [x..(head (tail xs))])
	| (xs /= []) && ((head xs) /= '-') = (x:xs)++lista
	| (length (x:xs) == 1) = (x:xs)++lista
	| otherwise = lista

makeList:: String -> [RegEx] -> [RegEx]
--aici se creeaza o lista de RegEx, in urma parcurgerii expresiei regulate primite
makeList "" lista = reverse lista
makeList (x:xs) lista
	--daca este un caracter simplu si dupa el urmeaza steluta
	| (not (elem x "()|[]*")) && (xs /= []) && ((head xs) == '*') = makeList (tail xs) ((Rep (Sim x)):lista)
	--daca este un caracter si dupa el nu urmeaza steluta
	| (not (elem x "()|[]*")) = makeList xs ((Sim x):lista)
	--daca avem o paranteze cu pipe in interior si dupa paranteza avem *, construim recursiv tot ce trebuie sa fie in elementul de  Rep (adica un element de tip Sau care contine 2 Secvente cu alte elemente...
	| (elem x "(") && (verificaPipe xs 0) && (a /= []) && (head a == '*')  = makeList (tail a) ((Rep(Sau (Secv (makeList (beforePipe xs 0 []) [])) (Secv (makeList (dupaPipe t 0) [])))):lista)
	--daca avem paranteza fara pipe si cu steluta dupa
	| (elem x "(") && (a /= []) && (head a == '*') = makeList (tail a) ((Rep (Secv (makeList t []))):lista)
	--daca avem paranteza si nu avem * dupa, iar in interiorul parantezei avem | adaugam in lista un element de tip Sau care are 2 elemente de tip Secv
	| (elem x "(") && (verificaPipe xs 0) && ((a == []) || (head a /= '*'))  = makeList a ((Sau (Secv (makeList (beforePipe xs 0 []) [])) (Secv (makeList (dupaPipe t 0) []))):lista)
	--paranteza fara pipe si fara steluta dupa
	| (elem x "(") && ((a == []) || (head a /= '*')) = makeList a ((Secv (makeList t [])):lista)
	--paranteza patrata fara ^ si cu steluta dupa
	| (elem x "[") && (head tpp /= '^') && (app /= []) && (head app == '*') = makeList (tail app) ((Rep (Interv (dinPP tpp []))):lista)
	--daca avem paranteza patrata, fara ^ si fara * dupa sfarsitul parantezei
	| (elem x "[") && (head tpp /= '^') && ((app == []) || (head app /= '*')) = makeList app ((Interv (dinPP tpp [])):lista)
	--cand avem ^ si *
	| (elem x "[") && (head tpp == '^') && (app /= []) && (head app == '*') = makeList (tail app) ((Rep (Exc (dinPP (tail tpp) []))):lista)
	--avem ^ dar nu avem *
	| (elem x "[") && (head tpp == '^') && ((app == []) || (head app /= '*')) = makeList app ((Exc (dinPP (tail tpp) [])):lista)
	|otherwise = reverse lista
		where
			--ia ce e dupa paranteza rotunda
			a = afterP xs 0 '(' ')'
			--ia continutul parantezei rotunde
			t = takeP xs 0 "" '(' ')'
			--ia ce e dupa paranteza patrata
			app = afterP xs 0 '[' ']'
			--ia continutul parantezei patrate
			tpp = takeP xs 0 "" '[' ']'


createTree:: String -> RegEx
createTree regEx = (Secv (makeList regEx [])) 

--intoarce tipul constructorului
inst (Sim x) = "Sim"
inst (Interv x) = "Interv"
inst (Sau x y) = "Sau"
inst (Exc s) = "Exc"
inst (Rep x) = "Rep"
inst (Secv x) = "Secv"

compile::RegEx->String->(Bool, String)

compile Empty "" = (True, "")
compile Empty sir = (False, sir)

compile (Sim x) sir 
	--daca x este egal cu primul caracter din sir, sau daca x este punct
	| (sir /= []) && (x == (head sir) || (x == '.')) = (True, (tail sir))
	| otherwise = (False, sir)

compile (Interv s) sir
	--daca primul element din sir se gaseste in interval (in s)
	| (sir /= []) && (elem (head sir) s) = (True, (tail sir))
	| otherwise = (False, sir)
	
compile (Sau x y) sir 
	--apelam compile pe ambele RegEx
	|(fst (compile x sir)) = (True, (snd (compile x sir))) 
	|(fst (compile y sir)) = (True, (snd (compile y sir)))
	| otherwise = (False, sir)

compile (Exc s) sir
	--intoarce True daca primul element din sir nu face parte din sirul s
	| (sir /= []) && (not (elem (head sir) s)) = (True, (tail sir))
	| otherwise = (False, sir)
	
compile (Rep r) [] = (True, [])
compile (Rep r) sir
	--intoarce True daca r da match si false daca nu
	|(fst c) = (True, (snd c))
	|otherwise = (False, sir)
		where
			c = (compile r sir)

compile (Secv s) [] = (True, [])
compile (Secv []) sir = (True, sir) 
compile (Secv (x:xs)) sir
	-- daca e de tip Rep si dupa el nu mai urmeaza nimic in RegEx facem compile de chestia asta pana cand ajungem la sfarsitul sirului sir sau pana cand ne da False
	|(fst c) && ((inst x) == "Rep") && ((length xs) == 0) = (compile (Secv (x:[])) (snd c))
	--daca e de tip Rep si mai avem elemente in secventa, atunci, daca obtinem True pentru urmatorul element din secventa, apelam compile de x:(tail xs) si sir din care scoatem elementul pentru care a dat match (head xs) 
	|(fst c) && ((inst x) == "Rep") && (fst (compile (head xs) sir)) = (compile (Secv (x:(tail xs))) (snd c))
	--daca e de tip Rep dar nu da match, trecem mai departe in secventa
	| (not (fst c)) && ((inst x) == "Rep") = (compile (Secv xs) sir)
	--daca e de tip Rep si da Match, iar urmatorul element nu da match, apelam iar cu aceeasi secventa, pana cand (compile Rep nu mai da Match)
	| (fst c) && ((inst x) == "Rep") && ( not (fst (compile (head xs) sir))) = (compile (Secv (x:xs)) (snd c))
	-- daca da Match si nu este de tip Rep, trecem mai departe
	| (fst c) && (not ((inst x) == "Rep")) = (compile (Secv xs) (snd c))
	--daca nu da Match si nu este de tip Rep => False
	| otherwise = (False, sir)
			where
				c = (compile x sir)
				
compileRegex:: String -> String -> Return

compileRegex re sir
	| (fst c) = Match
	| otherwise = NoMatch
		where
			c = (compile (createTree re) sir)