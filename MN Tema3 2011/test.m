function t=test(eps)
	x=zeros(6,1);
	y=zeros(6,1);
	Y=zeros(1,1001);
	Y1=zeros(1,300);
	for i=1:6
		[Y,x(i)]=eval_interpolator_c(i,eps); %am pastrat valorile N returnate de functii in vectori
		[Y1,y(i)]=eval_interpolator_d(i,eps);
	end
t=[x,y]; % am concatenat vectorii pentru a obtine matricea cu rezultatele
t=t';
end