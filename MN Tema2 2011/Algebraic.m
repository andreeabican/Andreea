function pr=Algebraic(file_in,d)
  f=fopen(file_in,'r');
  n=fscanf(f, '%i', 1); % numarul de noduri, va reprezenta numarul de linii si de coloane din matricea de adiacenta
  a=zeros(n);
  k=zeros(n);
  for i=1:n
    nrnod=fscanf(f,'%i',1);
    nrad=fscanf(f,'%i',1); % numarul de noduri cu care nodul i se invecineaza(doar ca aici, daca de pe pagina i se poate ajunge
			   % de doua ori la pagina j, aceasta a fost numarata de doua ori
    for j=1:nrad
      nod=fscanf(f,'%i',1);
      if nod~=i
	a(i,nod)=1; % in matricea de adiacenta, daca exista legatura de la nodul i la nodul nod, in a(i,nod) punem valoarea 1.
      end
    end
  end
  l=zeros(n,1);
  for i=1:n
    for j=1:n
      l(i)=l(i)+a(i,j); % se numara la cate pagini poate trimite pagina i, fara a numara duplicatele
    end
    k(i,i)=l(i); % matricea k are pe diagonala numarul de noduri adiacente nodului i
    k1(i,i)=1/k(i,i); % k1 reprezinta inversa matricei k. Am calculat inversa astfel, deoarece matricea k este matrice diagonala
  end
%  r=zeros(n,1);
 % r(1:n)=1/n;
  m=(k1*a)';
  one=ones(n,1);
  I=eye(n);
  A=(I-d*m);
%algorimt Gram-Schmidt
% pentru determinarea lui Q si a lui R am folosit alogritmul Gram-Schmidt modificat din librariile puse la dispozitie pe site-ul cursului.
% Matricea Q, care se obtine in urma aplicarii acestui algortim este o matrice ortogonala(Q'=inv(Q)), iar matricea R este o matrice
% superior triunghiulara.
% Notam coloanele din matricea A cu a1, a2, ..., an si coloanele din Q cu Q1, Q2, ..., Qn
% Valorea lui R(i,i), i=1:n o sa fie norm(ai)
% prima coloana a lui Q este a1/r(1,1)
% Restul coloanelor din Q se pregatesc astfel: Pentru un k=1:n, din fiecare coloana j a lui Q, j>k, se scade (Qk*R(k,j)), iar
% R(k,j)=(ak/norm(ak))'*a(k+1)
  Q = A;
  R = zeros( n, n );
  for k = 1 : n
    R( k, k ) = norm( Q( 1 : n, k ) );
    Q( 1 : n, k ) = Q( 1 : n, k ) / R( k, k );
    for j = k + 1 : n
	R( k, j ) = Q( 1 : n, k )' * Q( 1 : n, j );
	Q( 1 : n, j ) = Q(1 : n, j ) - Q( 1 : n, k ) * R( k, j );
    end
  end
% rezolvare n sisteme superior triunghiulare( am folosit functia din librariile puse la dispozitie pe site-ul cursului)
% La rezolvarea acestui sistem am notat A1=inversa matricei A. Stim ca A*A1=I, iar A=Q*R.
% Deci Q*R*A1=I => Q'*Q*R*A1=Q'*I. Stim si ca matricea Q este ortogonala, deci Q'*Q=I. Din aceste relatii rezulta ca
% R*A1=Q'.
% Din aceasta egalitate rezulta n sisteme superior triunghiulare. coloana necunoscutelor va fi cate o coloana din A1,
% iar coloana termenilor liberi va fi coloana corespunzatoare din Q'.
% La rezolvarea unui sistem superior triunghiular, ultima necunoscuta se afla prima si este egala cu termenul liber 
% impartit la coeficientul necunoscutei. 
% Penultima ecuatie din sistem depinde doar de necunoscutele n si n-1, deci aflam necunoscuta n-1, deoarece pe n am aflat-o la pasul
% anterior. Calculele se fac dupa formula folosita mai jos(Adica se scad din termenul liber, toate necunoscutele determinate pana la
% momentul respectiv inmultite fiecare cu coeficientul sau, iar totalul se imparte la coeficientul necunoscutei pe care o calculam).
  A1=zeros(n);
  Q1=Q';
  for j=1:n
    b=zeros(n,1);
    b=Q1(1:n,j);
    A1(n,j)=b(n)/R(n,n);
    for i=n-1:-1:1
      A1(i,j)=(b(i)-R(i,i+1:n)*A1(i+1:n,j))/R(i,i);
    end
  end
% calculare page-rank, dupa formula
  pr=zeros(n,1);
  pr=A1*(((1-d)/n)*one);
  fclose(f);
 end