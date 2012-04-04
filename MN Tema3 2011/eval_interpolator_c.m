function [Y,N] = eval_interpolator_c(tip, eps)
% eval_interpolator - determina cat de repede converge un polinom
% de interpolare
a1=3;
I0=besseli(0,a1);
k=1;
x1=linspace(-pi,pi, 1001);
y1=zeros(1,1001);
y1=y1(:);
y1(1:1001)=exp(a1*cos(x1(1:1001)))/(2*pi*I0);
E=zeros(1,2);
% tip -  1 lagrange
if tip==1
	
	for k=2:3
		x=linspace(-pi,pi, 2^k+1); % am generat 2^k+1 puncte in intervalul [-pi,pi] pe baza carora se va face interpolarea
		y=zeros(2^k+1,1);         % am calculat valoarea functiei in punctele generate mai sus
		y(1:2^k+1)=exp(a1*cos(x(1:2^k+1)))/(2*pi*I0);
		% calcul coeficienti polinom Lagrange
		x = x( : );
		n = length( x );	%polinomul are gradul n-1;
		v = ones( n, n );
		for i = 2 : n
			v( :, i ) = v( :, i - 1 ) .* x;
		end
		a=v\y;
		b=zeros(1,1001);
		b=b(:);
		s=0;
		for i=1:1001             %am calculat valoarea sumei din formula de calcul a erorii
			for j=1:n
				b(i) = b(i) + a(j)*(x1(i)^(j-1));
			end
			s=s+(y1(i)-b(i))^2;
		end
		E(k-1)=((2*pi/1001)*s)^0.5;  % am calculat eroarea
	end
	while E(1)-E(2)>eps   % se repeta tot ce am facut mai sus, pentru mai multe valori ale lui k, pana cand E(2)>E(1) sau pana cand
			      % interpolantul converge
		k=k+1;
		x=linspace(-pi,pi, 2^k+1);
		y=zeros(2^k+1,1);
		y=y(:);
		y(1:2^k+1)=exp(a1*cos(x(1:2^k+1)))/(2*pi*I0);

		% calcul coeficienti polinom Lagrange
		x = x( : );
		n = length( x );	%polinomul are gradul n-1;
		v = ones( n, n );
		for i = 2 : n
			v( :, i ) = v( :, i - 1 ) .* x;
		end
		a=v\y;
		s=0;
		b=zeros(1,1001);
		for i=1:1001
			for j=1:n
				b(i) = b(i) + a(j)*(x1(i)^(j-1));
			end
			s=s+(y1(i)-b(i))^2;
		end
		E(1)=E(2);
		E(2)=((2*pi/1001)*s)^0.5;
		
		
	end
	if E(1)>=E(2)
		  N=2^k;
		  Y(1:1001)=b(1:1001);

	end
	if E(1)<E(2)
		  N=inf;
		  Y=zeros(1,1001);
	end
end



% 	 2 newton
if tip==2
	for k=2:3
		x=linspace(-pi,pi, 2^k+1);
		y=zeros(2^k+1,1);
		y(1:2^k+1)=exp(a1*cos(x(1:2^k+1)))/(2*pi*I0);
		x=x(:);
		y=y(:);
		z=y;
		n=length(y);
% calcul coeficienti polinom Newton
		for i=1:n
			z(i+1:n)=(z(i+1:n)-z(i))./(x(i+1:n)-x(i));
		end
		n=length(x);
		A=zeros(n,n);
		A(1,n)=z(1);
		c=zeros(n,1);
		for i=2:n
			A(i,n-i+1:n)=poly(x(1:i-1))*z(i);
		end
		for i=1:n
			 c(i) = sum(A(:,i));
		end
		s=0;
		b=zeros(1,1001);
		b=b(:);
		%calcularea erorii
		for i=1:1001
			%am calculat valoarea polinomului in punctul x1(i)
			for j=1:n
				b(i) = b(i) + c(j)*(x1(i)^(n-j+1));
			end
			s=s+(y1(i)-b(i))^2;
		end
		E(k-1)=((2*pi/1001)*s)^0.5; %am calcular eroarea
	end
	while E(1)-E(2)>eps % se repeta intr-o bucla tot ce am facut mai sus
		k=k+1;
		x=linspace(-pi,pi, 2^k+1);
		y=zeros(2^k+1,1);
		y=y(:);
		y(1:2^k+1)=exp(a1*cos(x(1:2^k+1)))/(2*pi*I0);
		x=x(:);
		z=y;
		n=length(y);
		for i=1:n
			z(i+1:n)=(z(i+1:n)-z(i))./(x(i+1:n)-x(i));
		end
		n=length(x);
		A=zeros(n,n);
		A(1,n)=z(1);
		c=zeros(n,1);
		for i=2:n
			A(i,n-i+1:n)=poly(x(1:i-1))*z(i);
		end
		for i=1:n
			 c(i) = sum(A(:,i));
		end
		s=0;
		b=zeros(1,1001);
		for i=1:1001
			for j=1:n
				b(i) = b(i) + c(j)*(x1(i)^(n-j+1));
			end
			s=s+(y1(i)-b(i))^2;
		end
		E(1)=E(2);
		E(2)=((2*pi/1001)*s)^0.5;
		
	end
	if E(1)>=E(2)
		  N=2^k;
		  Y(1:1001)=b(1:1001);
	end
	if E(1)<E(2)
		  N=inf;
		  Y=zeros(1,1001);
	end

end
% 	 3 linear spline
if tip==3
	for k=2:3
		x=linspace(-pi,pi, 2^k+1);
		y=zeros(2^k+1,1);
		y(1:2^k+1)=exp(a1*cos(x(1:2^k+1)))/(2*pi*I0);
		x=x(:);
		y=y(:);
		a=zeros(1,2^k);
		b=zeros(1,2^k);
		a=a(:);
		b=b(:);
		n=length(x);
		% calcul coeficienti pentru cele n-1 spline-uri
		for i=1:n-1
		    a(i)=(y(i+1)-y(i))/(x(i+1)-x(i));
		    b(i)=(x(i+1)*y(i)-x(i)*y(i+1))/(x(i+1)-x(i));
		end
		i=1;
		s=0;
		val=zeros(1,1001);
		for j=1:n-1
			while (x1(i)<x(j+1) & i<=1001) %am determinat valoarea carui spline trebuie sa o calculez
				val(i)=a(j)*x1(i)+b(j); %am calculat valoarea in punctul x1(i)
				s=s+(y1(i)-val(i))^2;
				i=i+1;
			end
		end
		E(k-1)=((2*pi/1001)*s)^0.5;
	end
	while E(1)-E(2)>eps % se repeta ce am facut mai sus, cu conditiile pentru erori la fel ca la prima interpolare
		k=k+1;
		x=linspace(-pi,pi, 2^k+1);
		y=zeros(2^k+1,1);
		y=y(:);
		y(1:2^k+1)=exp(a1*cos(x(1:2^k+1)))/(2*pi*I0);
		x=x(:);
		a=zeros(1,2^k);
		b=zeros(1,2^k);
		a=a(:);
		b=b(:);
		n=length(x);
		for i=1:n-1
		    a(i)=(y(i+1)-y(i))/(x(i+1)-x(i));
		    b(i)=(x(i+1)*y(i)-x(i)*y(i+1))/(x(i+1)-x(i));
		end
		i=1;
		s=0;
		for j=1:n-1
			while (x1(i)<x(j+1) & i<=1001)
				val(i)=a(j)*x1(i)+b(j);
				s=s+(y1(i)-val(i))^2;
				i=i+1;
			end
		end
		E(1)=E(2);
		E(2)=((2*pi/1001)*s)^0.5;
		
	end
	if E(1)>=E(2)
		  N=2^k;
		  Y(1:1001)=val(1:1001);
	end
	if E(1)<E(2)
		  N=inf;
		  Y=zeros(1,1001);
	end

end
% 	 4 natural
if tip==4
	for k=2:3
		x=linspace(-pi,pi, 2^k+1);
		y=zeros(2^k+1,1);
		y(1:2^k+1)=exp(a1*cos(x(1:2^k+1)))/(2*pi*I0);
		x=x(:);
		y=y(:);
		% am calculat coeficientii spline-urilor dupa formulele din cursuri/seminarii
		a=zeros(1,2^k+1);
		b=zeros(1,2^k);
		c=zeros(2^k+1,1);
		d=zeros(1,2^k);
		a=a(:);
		b=b(:);
		n=length(x);
		% am creat matricea H
		H=zeros(n,n);
		H(1,1)=1;
		H(n,n)=1;
		h=x(2)-x(1);
	      
		a(1:n)=y(1:n);
		
		for i=2:n-1
			H(i,i-1)=h;
			H(i,i+1)=h;
			H(i,i)=4*h;
		end
		% am creat matricea B
		% stim ca H*c=B, c este coloana coeficientilo lui (x-x0)^2
		B=zeros(n,1);
		B(1)=0;
		B(n)=0;
		B(2:n-1)=(3*(a(3:n)-a(2:n-1))/h)-(3*(a(2:n-1)-a(1:n-2))/h);
		c=H\B;
		b(1:n-1)=((a(2:n)-a(1:n-1))/h)-((h/3)*(2*c(1:n-1)+c(2:n)));
		d(1:n-1)=(c(2:n)-c(1:n-1))/(3*h);
		i=1;
		s=0;
		val=zeros(1,1001);
		for j=1:n-1
			while (x1(i)<x(j+1) & i<=1001)
				val(i)=a(j)+b(j)*(x1(i)-x(j))+c(j)*((x1(i)-x(j))^2)+d(j)*((x1(i)-x(j))^3);
				s=s+(y1(i)-val(i))^2;
				i=i+1;
			end
		end
		E(k-1)=((2*pi/1001)*s)^0.5; % am calculat eroarea la fel ca la toate celelalte interpolari
	end
	while E(1)-E(2)>eps  % am repetat ce am facut mai sus, cu aceleasi conditii de iesire, ca la celelalte interpolari
		k=k+1;
		x=linspace(-pi,pi, 2^k+1);
		y=zeros(2^k+1,1);
		y=y(:);
		y(1:2^k+1)=exp(a1*cos(x(1:2^k+1)))/(2*pi*I0);
		x=x(:);
		a=zeros(1,2^k+1);
		b=zeros(1,2^k);
		c=zeros(2^k+1,1);
		d=zeros(1,2^k);
		a=a(:);
		b=b(:);
		n=length(x);
		H=zeros(n,n);
		H(1,1)=1;
		H(n,n)=1;
		h=x(2)-x(1);
		a(1:n)=y(1:n);
		
		for i=2:n-1
			H(i,i-1)=h;
			H(i,i+1)=h;
			H(i,i)=4*h;
		end
		B=zeros(n,1);
		B(1)=0;
		B(n)=0;
		B(2:n-1)=(3*(a(3:n)-a(2:n-1))/h)-(3*(a(2:n-1)-a(1:n-2))/h);
		c=H\B;
		b(1:n-1)=((a(2:n)-a(1:n-1))/h)-((h/3)*(2*c(1:n-1)+c(2:n)));
		d(1:n-1)=(c(2:n)-c(1:n-1))/(3*h);
		i=1;
		s=0;
		for j=1:n-1
			while (x1(i)<x(j+1) & i<=1001)
				val(i)=a(j)+b(j)*(x1(i)-x(j))+c(j)*((x1(i)-x(j))^2)+d(j)*((x1(i)-x(j))^3);
				s=s+(y1(i)-val(i))^2;
				i=i+1;
			end
		end
		E(1)=E(2);
		E(2)=((2*pi/1001)*s)^0.5;
		
	end
	if E(1)>=E(2)
		  N=2^k;
		  Y(1:1001)=val(1:1001);
	end
	if E(1)<E(2)
		  N=inf;
		  Y=zeros(1,1001);
	end

end
		
% 	 5 cubic spline
% la spline tensionat am procedat la fel ca la cel natural doar ca am schimbat cateva valori in matricile H si B
if tip==5
	for k=2:3
		x=linspace(-pi,pi, 2^k+1);
		y=zeros(2^k+1,1);
		y(1:2^k+1)=exp(a1*cos(x(1:2^k+1)))/(2*pi*I0);
		x=x(:);
		y=y(:);
		a=zeros(1,2^k+1);
		b=zeros(1,2^k);
		c=zeros(2^k+1,1);
		d=zeros(1,2^k);
		a=a(:);
		b=b(:);
		n=length(x);
		H=zeros(n,n);
		
		h=x(2)-x(1);
		H(1,1)=2*h;
		H(n,n)=2*h;
		H(1,2)=h;
		H(n-1,n)=h;
		a(1:n)=y(1:n);
		
		for i=2:n-1
			H(i,i-1)=h;
			H(i,i+1)=h;
			H(i,i)=4*h;
		end
		B=zeros(n,1);
		B(1)=3*(a(2)-a(1))/h-3*((-3)/(2*pi*I0)*exp(3*cos(x(1)))*sin(x(1)));
		B(n)=3*(-3)/(2*pi*I0)*exp(3*cos(x(n)))*sin(x(n))-3*(a(n)-a(n-1))/h;
		B(2:n-1)=(3*(a(3:n)-a(2:n-1))/h)-(3*(a(2:n-1)-a(1:n-2))/h);
		c=H\B;
		b(1:n-1)=((a(2:n)-a(1:n-1))/h)-((h/3)*(2*c(1:n-1)+c(2:n)));
		d(1:n-1)=(c(2:n)-c(1:n-1))/(3*h);
		i=1;
		s=0;
		val=zeros(1,1001);
		for j=1:n-1
			while (x1(i)<x(j+1) & i<=1001)
				val(i)=a(j)+b(j)*(x1(i)-x(j))+c(j)*((x1(i)-x(j))^2)+d(j)*((x1(i)-x(j))^3);
				s=s+(y1(i)-val(i))^2;
				i=i+1;
			end
		end
		E(k-1)=((2*pi/1001)*s)^0.5;
	end
	while E(1)-E(2)>eps
		k=k+1;
		x=linspace(-pi,pi, 2^k+1);
		y=zeros(2^k+1,1);
		y=y(:);
		y(1:2^k+1)=exp(a1*cos(x(1:2^k+1)))/(2*pi*I0);
		x=x(:);
		a=zeros(1,2^k+1);
		b=zeros(1,2^k);
		c=zeros(2^k+1,1);
		d=zeros(1,2^k);
		a=a(:);
		b=b(:);
		n=length(x);
		H=zeros(n,n);
		h=x(2)-x(1);
		H(1,1)=2*h;
		H(n,n)=2*h;
		H(1,2)=h;
		H(n,n-1)=h;s
		a(1:n)=y(1:n);
		
		for i=2:n-1
			H(i,i-1)=h;
			H(i,i+1)=h;
			H(i,i)=4*h;
		end
		B=zeros(n,1);
		B(1)=3*(a(2)-a(1))/h-3*((-3)/(2*pi*I0)*exp(3*cos(x(1)))*sin(x(1)));
		B(n)=3*(-3)/(2*pi*I0)*exp(3*cos(x(n)))*sin(x(n))-3*(a(n)-a(n-1))/h;
		B(2:n-1)=(3*(a(3:n)-a(2:n-1))/h)-(3*(a(2:n-1)-a(1:n-2))/h);
		c=H\B;
		b(1:n-1)=((a(2:n)-a(1:n-1))/h)-((h/3)*(2*c(1:n-1)+c(2:n)));
		d(1:n-1)=(c(2:n)-c(1:n-1))/(3*h);
		i=1;
		s=0;
		for j=1:n-1
			while (x1(i)<x(j+1) & i<=1001)
				val(i)=a(j)+b(j)*(x1(i)-x(j))+c(j)*((x1(i)-x(j))^2)+d(j)*((x1(i)-x(j))^3);
				s=s+(y1(i)-val(i))^2;
				i=i+1;
			end
		end
		E(1)=E(2);
		E(2)=((2*pi/1001)*s)^0.5;
		
	end
	if E(1)>=E(2)
		  N=2^k;
		  Y(1:1001)=val(1:1001);
	end
	if E(1)<E(2)
		  N=inf;
		  Y=zeros(1,1001);
	end

end
% 	 6 fourrier
% interpolarea trigonometrica este cea mai exacta din cate am observat eu.
% am folosit formulele din seminar(parca:-??)
if tip==6
	for k=2:3
		x=linspace(-pi,pi, 2^k+1);
		y=zeros(2^k+1,1);
		y(1:2^k+1)=exp(a1*cos(x(1:2^k+1)))/(2*pi*I0);
		x=x(:);
		y=y(:);
		% am calculat coeficientii polinomului de interpolare
		a=zeros(1,2^k/2);
		b=zeros(1,2^k/2);
		n=length(x);

		teta(1:n)=2*(0:n-1)*pi/n; % am creat un vector in care am pus valorile lui  teta
		a0=0;
		for i=1:n
			a0=a0+(2^0.5/n)*exp(a1*cos(teta(i)))/(2*pi*I0); % am calculat valoarea lui a0
		end
		for i=1:(n-1)/2      %am calcular valorile pentru a(i) si b(i)
			b(i)=0;
			a(i)=exp(a1*cos(teta(1)))/(2*pi*I0)*cos(teta(1)*i);		
			for j=1:n-1
				b(i)=b(i)+exp(a1*cos(teta(j+1)))/(2*pi*I0)*sin(teta(j+1)*i);
				a(i)=a(i)+exp(a1*cos(teta(j+1)))/(2*pi*I0)*cos(teta(j+1)*i);
			end
			b(i)=2/n*b(i);
			a(i)=2/n*a(i);
		end
		s=0;
		p=zeros(1,1001);
		for i=1:1001 % am calculat valoarea polinomului de interpolare in punctul x1(i)
			p(i)=a0/(2^0.5);
			for j=1:(n-1)/2
				p(i)=p(i)+a(j)*cos(x1(i)*j)+b(j)*sin(x1(i)*j);
			end
			s=s+(y1(i)-p(i))^2;
		end
		E(k-1)=((2*pi/1001)*s)^0.5; % am calculat eroarea
	end
	while E(1)-E(2)>eps % am repetat intr-o bucla pasii de mai sus, cu aceleasi conditii ca la celelalte interpolari pentru iesirea
			    % din bucla
		k=k+1;
		x=linspace(-pi,pi, 2^k+1);
		y=zeros(2^k+1,1);
		y=y(:);
		y(1:2^k+1)=exp(a1*cos(x(1:2^k+1)))/(2*pi*I0);
		x=x(:);
		y=y(:);
		a=zeros(1,(2^k)/2);
		b=zeros(1,(2^k)/2);
		n=length(x);

		teta(1:n)=2*(0:n-1)*pi/n;
		a0=0;
		for i=1:n
			a0=a0+2^0.5/n*exp(a1*cos(teta(i)))/(2*pi*I0);
		end
		for i=1:(n-1)/2
			b(i)=0;
			a(i)=exp(a1*cos(teta(1)))/(2*pi*I0)*cos(teta(1)*i);		
			for j=1:n-1
				b(i)=b(i)+exp(a1*cos(teta(j+1)))/(2*pi*I0)*sin(teta(j+1)*i);
				a(i)=a(i)+exp(a1*cos(teta(j+1)))/(2*pi*I0)*cos(teta(j+1)*i);
			end
			b(i)=2/n*b(i);
			a(i)=2/n*a(i);
		end
		s=0;
		for i=1:1001
			p(i)=a0/(2^0.5);
			for j=1:(n-1)/2
				p(i)=p(i)+a(j)*cos(x1(i)*j)+b(j)*sin(x1(i)*j);
			end
			s=s+(y1(i)-p(i))^2;
		end
		E(1)=E(2);
		E(2)=((2*pi/1001)*s)^0.5;
		
	end
	if E(1)>=E(2)
		  N=2^k;
		  Y(1:1001)=p(1:1001);
	end
	if E(1)<E(2)
		  N=inf;
		  Y=zeros(1,1001);
	end

end


			    
		
% eps - toleranta acceptata pentru convergenta
end