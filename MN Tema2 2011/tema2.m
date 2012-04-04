function tema2(file_in,d,eps)
  file_out=strcat(file_in,'.out');
  f=fopen(file_in,'r');
% citirea valorilor din f pentru a ajunge la val1 si val2
  n=fscanf(f, '%i', 1);
  for i=1:n
  nrnod=fscanf(f,'%i',1);
  nrad=fscanf(f,'%i',1);
  for j=1:nrad
    nod=fscanf(f,'%i',1);
  end
end
% citire val1 si val2
  val1=fscanf(f, '%f', 1);
  val2=fscanf(f,'%f',1);
% am deschis fisierul g pentru scriere si am scris in el rezultatele intoarse de Iterative si Algebraic, asa cum s-a specificat in PDF
  g=fopen(file_out, 'w');
  fprintf(g, '%i\n', n);
  fprintf(g, '\n');
  PR=Iterative(file_in,d,eps);
  fprintf(g, '%f\n', PR);
  fprintf(g,'\n');
  PR=Algebraic(file_in,d);
  fprintf(g, '%f\n', PR);
  fprintf(g,'\n');
  
% sortare vector PR, obtinut cu functia Algebraic
% fie vv un vector care pe pozitia i are valoarea i. Voi folosi
% acest vector pentru a retine ce nod avea valoarea respectiva pentru PR  
  vv=zeros(1,n);
  for i=1:n
    vv(i)=i;
  end
% sortarea vectorului PR	
  for i=1:n
    for j=i+1:n
      if PR(j)>PR(i)
	%interschimbare elemente din PR
	aux=PR(i);
	PR(i)=PR(j);
	PR(j)=aux;
	%interschimbare vector vv
	aux=vv(i);
	vv(i)=vv(j);
	vv(j)=aux;
	end
    end
  end
% am afisat i, care reprezinta indicele din vectorul PR1, apoi nodul care are page-rank-ul PR(i), si valoarea functiei F=u(PR(i))
  PR1=PR;
  a=1/(val2-val1);
  b=-a*val1;
  for i=1:n
    fprintf(g,'%i ',i);
    fprintf(g,'%i ',vv(i));
    if (0<=PR1(i))&(PR1(i)<val1)
      fprintf(g, '%f\n', 0)
    else
    if (val1<=PR1(i)) & (PR1(i)<=val2)
      fprintf(g, '%f\n', a*PR1(i)+b);
    else
    if (val2<PR1(i)) & (PR1(i)<=1)
      fprintf(g, '%f\n', 1);
    end
    end
    end
  end
% am inchis fisierele
  fclose(f);
  fclose(g);
end
  
