function test_grafic(eps)
	I0=besseli(0,3);
	x1=linspace(-pi,pi, 1001);
	y1=zeros(1,1001);
	y1=y1(:);
	y1(1:1001)=exp(3*cos(x1(1:1001)))/(2*pi*I0);
	Y=zeros(1,1001);
	m=zeros(6,1001);
	for i=1:6
		[Y,N]=eval_interpolator_c(i,eps);
		m(i,:)=Y;
	end
	subplot(1,2,1), plot(x1,y1, x1,m(1,:),'-r', x1, m(2,:),'-y', x1, m(3,:),'-m', x1, m(4,:),':c', x1, m(5,:),'g', x1,m(6,:), '.-k');
	date=load("sunspot.dat");
	x1=date(:,1);
	y1=date(:,2);
	Y=zeros(1,300);
	m=zeros(6,300);
	for i=1:6
		[Y,N]=eval_interpolator_d(i,eps);
		m(i,:)=Y;
		end
	subplot(1,2,2), plot(x1,y1, x1,m(1,:),'-r', x1, m(2,:),'-y', x1, m(3,:),'-m', x1, m(4,:),':c', x1, m(5,:),'g', x1,m(6,:), '.-k');
end