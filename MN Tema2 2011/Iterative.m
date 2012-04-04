function pr=Iterative(file_in,d,eps)
  f=fopen(file_in,'r');
  n=fscanf(f, '%i', 1);
  a=zeros(n);
  k=zeros(n);
  for i=1:n
    nrnod=fscanf(f,'%i',1);
    nrad=fscanf(f,'%i',1); % nrad reprezinta numarul de noduri adiacente cu nodul i, incluzand duplicatele
    for j=1:nrad
      nod=fscanf(f,'%i',1);
      if nod~=i
	a(i,nod)=1; % in matricea de adiacenta, daca nodul j este adiacent cu nodul i, atunci a(i,j)=1, iar a(i,i)=0 intotdeauna
      end
    end
  end
  l=zeros(n,1);
  for i=1:n
    for j=1:n
      l(i)=l(i)+a(i,j); % am numarat catre cate pagini trimite pagina i( nu intra in calcul duplicatele, sau link-urile care
			% trimit tot la pagina i).
    end
  k(i,i)=l(i); % matricea k are pe pozitia k(i,i), valoarea lui l(i)
  k1(i,i)=1/k(i,i); % am calculat inversa matricei k. Am calculat inversa asa, deoarece matricea k este matrice diagonala
  end
  r=zeros(n,1);
  r(1:n)=1/n; % am initializat r, care reprezinta page-rank-ul
  m=(k1*a)';
  one=ones(n,1);
 % am calculat page-rank-ul aplicand formula urmatore pana cand valorile din ri nu difereau de cele din r prin valori mai mari decat eps
  ri=r; 
  r=d*m*r+((1-d)/n)*one;
  er=(abs(r-ri))<eps;
  s=sum(er);
  while s<n
    ri=r;
    r=d*m*r+((1-d)/n)*one;
    er=(abs(r-ri))<eps;
    s=sum(er);
  end
  pr=ri;
  fclose(f);
 end