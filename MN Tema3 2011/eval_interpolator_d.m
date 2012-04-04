function [Y,N] = eval_interpolator_d(tip, eps)
% eval_interpolator - determina cat de repede converge un polinom
% de interpolare
a1=3;
I0=besseli(0,a1);
k=1;
date=load("sunspot.dat");
datex=date(:,1);
datey=date(:,2);
nt=length(datex);% numarul total de puncte din vectorul datex
inc=datex(1);
sf=datex(nt);
x1=linspace(inc,sf, nt);
y1(1:nt)=datey(1:nt);
E=zeros(1,2);
% h=nt/nt=1 deci nu o sa-l mai scriu in formula
% tip -  1 lagrange
if tip==1
	
	for k=2:3
		x=datex(1:2^k+1);
		y=datey(1:2^k+1);
		% calcul coeficienti polinom Lagrange
		x = x( : );
		n = length( x );	%polinomul are gradul n-1;
		v = ones( n, n );
		for i = 2 : n
			v( :, i ) = v( :, i - 1 ) .* x;
		end
		a=v\y;
		b=zeros(1,nt);
		b=b(:);
		s=0;
		for i=1:nt
			for j=1:n
				b(i) = b(i) + a(j)*(x1(i)^(j-1));
			end
			s=s+(y1(i)-b(i))^2;
		end
		E(k-1)=s^0.5;
	end
	while E(1)-E(2)>eps
		k=k+1;
		if k>8 
			break;
		end
		x=datex(1:2^k+1);
		y=datey(1:2^k+1);

		% calcul coeficienti polinom Lagrange
		x = x( : );
		n = length( x );	%polinomul are gradul n-1;
		v = ones( n, n );
		for i = 2 : n
			v( :, i ) = v( :, i - 1 ) .* x;
		end
		a=v\y;
		s=0;
		b=zeros(1,nt);
		for i=1:nt
			for j=1:n
				b(i) = b(i) + a(j)*(x1(i)^(j-1));
			end
			s=s+(y1(i)-b(i))^2;
		end
		if E(2)>E(1)
			break;
		end
		E(1)=E(2);
		E(2)=s^0.5;
		
		
	end
	if (k>8)
		N=inf;
		Y=zeros(1,nt);
	else
		if E(1)>=E(2)
			N=2^k;
			Y(1:nt)=b(1:nt);
		end
	end
	if (k<=8)
		if E(1)<E(2)
			N=inf;
			Y=zeros(1,nt);
		end
	end
	
end



% 	 2 newton
if tip==2
	for k=2:3
		x=datex(1:2^k+1);
		y=datey(1:2^k+1);
		x=x(:);
		y=y(:);
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
		b=zeros(1,nt);
		b=b(:);
		for i=1:nt
			for j=1:n
				b(i) = b(i) + c(j)*(x1(i)^(n-j+1));
			end
			s=s+(y1(i)-b(i))^2;
		end
		E(k-1)=s^0.5;
	end
	while E(1)-E(2)>eps
		k=k+1;
		if (k>8) 
			break;
		end
		x=datex(1:2^k+1);
		y=datey(1:2^k+1);
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
		b=zeros(1,nt);
		for i=1:nt
			for j=1:n
				b(i) = b(i) + c(j)*(x1(i)^(n-j+1));
			end
			s=s+(y1(i)-b(i))^2;
		end
		E(1)=E(2);
		E(2)=s^0.5;
		
	end
	if (k>8)
		N=inf;
		Y=zeros(1,nt);
	else
		if E(1)>=E(2)
			N=2^k;
			Y(1:nt)=b(1:nt);
		end
	end
	if (k<=8)
		if E(1)<E(2)
			N=inf;
			Y=zeros(1,nt);
		end
	end
	
end
% 	 3 linear spline
if tip==3
	for k=2:3
		x=datex(1:2^k+1);
		y=datey(1:2^k+1);
		x=x(:);
		y=y(:);
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
		val=zeros(1,nt);
		for j=1:n-1
			while (x1(i)<x(j+1) & i<=nt)
				val(i)=a(j)*x1(i)+b(j);
				s=s+(y1(i)-val(i))^2;
				i=i+1;
			end
		end
		E(k-1)=s^0.5;
	end
	while E(1)-E(2)>eps
		k=k+1;
		if (k>8) 
			break;
		end
		x=datex(1:2^k+1);
		y=datey(1:2^k+1);
		y=y(:);
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
			while (x1(i)<x(j+1) & i<=nt)
				val(i)=a(j)*x1(i)+b(j);
				s=s+(y1(i)-val(i))^2;
				i=i+1;
			end
		end
		E(1)=E(2);
		E(2)=s^0.5;
		
	end
	if (k>8)
		N=inf;
		Y=zeros(1,nt);
	else
		if E(1)>=E(2)
			N=2^k;
			Y(1:nt)=val(1:nt);
		end
	end
	if (k<=8)
		if E(1)<E(2)
			N=inf;
			Y=zeros(1,nt);
		end
	end
	
end
% 	 4 natural
if tip==4
	for k=2:3
		x=datex(1:2^k+1);
		y=datey(1:2^k+1);
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
		val=zeros(1,nt);
		for j=1:n-1
			while (x1(i)<x(j+1) & i<=nt)
				val(i)=a(j)+b(j)*(x1(i)-x(j))+c(j)*((x1(i)-x(j))^2)+d(j)*((x1(i)-x(j))^3);
				s=s+(y1(i)-val(i))^2;
				i=i+1;
			end
		end
		E(k-1)=s^0.5;
	end
	while E(1)-E(2)>eps
		k=k+1;
		if k>8 break;
		end
		x=datex(1:2^k+1);
		y=datey(1:2^k+1);
		y=y(:);
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
			while (x1(i)<x(j+1) & i<=nt)
				val(i)=a(j)+b(j)*(x1(i)-x(j))+c(j)*((x1(i)-x(j))^2)+d(j)*((x1(i)-x(j))^3);
				s=s+(y1(i)-val(i))^2;
				i=i+1;
			end
		end
		E(1)=E(2);
		E(2)=s^0.5;
		
	end
	if (k>8)
		N=inf;
		Y=zeros(1,nt);
	else
		if E(1)>=E(2)
			N=2^k;
			Y(1:nt)=val(1:nt);
		end
	end
	if (k<=8)
		if E(1)<E(2)
			N=inf;
			Y=zeros(1,nt);
		end
	end
	
end
		
% 	 5 cubic spline

if tip==5
	for k=2:3
		x=datex(1:2^k+1);
		y=datey(1:2^k+1);
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
		B(1)=3*(a(2)-a(1))/h-3*((y(2)-y(1))/(x(2)-x(1)));
		B(n)=3*((y(n)-y(n-1))/(x(n)-x(n-1)))-3*(a(n)-a(n-1))/h;
		B(2:n-1)=(3*(a(3:n)-a(2:n-1))/h)-(3*(a(2:n-1)-a(1:n-2))/h);
		c=H\B;
		b(1:n-1)=((a(2:n)-a(1:n-1))/h)-((h/3)*(2*c(1:n-1)+c(2:n)));
		d(1:n-1)=(c(2:n)-c(1:n-1))/(3*h);
		i=1;
		s=0;
		val=zeros(1,nt);
		for j=1:n-1
			while (x1(i)<x(j+1) & i<=nt)
				val(i)=a(j)+b(j)*(x1(i)-x(j))+c(j)*((x1(i)-x(j))^2)+d(j)*((x1(i)-x(j))^3);
				s=s+(y1(i)-val(i))^2;
				i=i+1;
			end
		end
		E(k-1)=s^0.5;
	end
	while E(1)-E(2)>eps
		k=k+1;
		if (k>8)
			break;
		end
		x=datex(1:2^k+1);
		y=datey(1:2^k+1);
		y=y(:);
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
		H(n,n-1)=h;
		a(1:n)=y(1:n);
		
		for i=2:n-1
			H(i,i-1)=h;
			H(i,i+1)=h;
			H(i,i)=4*h;
		end
		B=zeros(n,1);
		B(1)=3*(a(2)-a(1))/h-3*((y(2)-y(1))/(x(2)-x(1)));
		B(n)=3*((y(n)-y(n-1))/(x(n)-x(n-1)))-3*(a(n)-a(n-1))/h;
		B(2:n-1)=(3*(a(3:n)-a(2:n-1))/h)-(3*(a(2:n-1)-a(1:n-2))/h);
		c=H\B;
		b(1:n-1)=((a(2:n)-a(1:n-1))/h)-((h/3)*(2*c(1:n-1)+c(2:n)));
		d(1:n-1)=(c(2:n)-c(1:n-1))/(3*h);
		i=1;
		s=0;
		for j=1:n-1
			while (x1(i)<x(j+1) & i<=nt)
				val(i)=a(j)+b(j)*(x1(i)-x(j))+c(j)*((x1(i)-x(j))^2)+d(j)*((x1(i)-x(j))^3);
				s=s+(y1(i)-val(i))^2;
				i=i+1;
			end
		end
		E(1)=E(2);
		E(2)=s^0.5;
		
	end
	if (k>8)
		N=inf;
		Y=zeros(1,nt);
	else
		if E(1)>=E(2)
			N=2^k;
			Y(1:nt)=val(1:nt);
		end
	end
	if (k<=8)
		if E(1)<E(2)
			N=inf;
			Y=zeros(1,nt);
		end
	end
end
% 	 6 fourrier

if tip==6
	x1=linspace(-pi,pi,nt);% am generat tot 300 de puncte dar intre -pi si pi pentru ca fiecare valoare din acest interval
				% sa corespunda unui an
	% y1 are valorile pe care le-am incarcat din fisierul sunspot.dat
	for k=2:3
		x=x1(1:2^k+1);
		y=y1(1:2^k+1);
		x=x(:);
		y=y(:);
		a=zeros(1,2^k/2);
		b=zeros(1,2^k/2);
		n=length(x);

		teta(1:n)=2*(0:n-1)*pi/n;
		a0=0;
		for i=1:n
			a0=a0+(2^0.5/n)*y1(i);
		end
		for i=1:(n-1)/2
			b(i)=0;
			a(i)=y1(1)*cos(teta(1)*i);		
			for j=1:n-1
				b(i)=b(i)+y1(j+1)*sin(teta(j+1)*i);
				a(i)=a(i)+y1(j+1)*cos(teta(j+1)*i);
			end
			b(i)=2/n*b(i);
			a(i)=2/n*a(i);
		end
		s=0;
		p=zeros(1,nt);
		for i=1:nt
			p(i)=a0/(2^0.5);
			for j=1:(n-1)/2
				p(i)=p(i)+a(j)*cos(x1(i)*j)+b(j)*sin(x1(i)*j);
			end
			s=s+(y1(i)-p(i))^2;
		end
		E(k-1)=s^0.5;
	end
	while E(1)-E(2)>eps
		k=k+1;
		if k>8
			break;
		end
		x=x1(1:2^k+1);
		y=y1(1:2^k+1);
		x=x(:);
		y=y(:);
		a=zeros(1,(2^k)/2);
		b=zeros(1,(2^k)/2);
		n=length(x);

		teta(1:n)=2*(0:n-1)*pi/n;
		a0=0;
		for i=1:n
			a0=a0+(2^0.5/n)*y1(i);
		end
		for i=1:(n-1)/2
			b(i)=0;
			a(i)=y1(1)*cos(teta(1)*i);		
			for j=1:n-1
				b(i)=b(i)+y1(j+1)*sin(teta(j+1)*i);
				a(i)=a(i)+y1(j+1)*cos(teta(j+1)*i);
			end
			b(i)=2/n*b(i);
			a(i)=2/n*a(i);
		end
		s=0;
		for i=1:nt
			p(i)=a0/(2^0.5);
			for j=1:(n-1)/2
				p(i)=p(i)+a(j)*cos(x1(i)*j)+b(j)*sin(x1(i)*j);
			end
			s=s+(y1(i)-p(i))^2;
		end
		E(1)=E(2);
		E(2)=s^0.5;
		
	end
	if (k>8)
		N=inf;
		Y=zeros(1,nt);
	else
		if E(1)>=E(2)
			N=2^k;
			Y(1:nt)=p(1:nt);
		end
	end
	if (k<=8)
		if E(1)<E(2)
			N=inf;
			Y=zeros(1,nt);
		end
	end

end


			    
		
% eps - toleranta acceptata pentru convergenta
end