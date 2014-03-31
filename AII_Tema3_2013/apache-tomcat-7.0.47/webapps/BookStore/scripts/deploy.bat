cd ..\\WEB-INF\\sources\\

dir /b /s *.java >sources.txt
mkdir ..\\classes\\
javac -classpath .;..\\..\\..\\..\\lib\\servlet-api.jar -d ..\\classes\\ @sources.txt
del sources.txt

cd ..\\..\\..\\..\\bin\\
call shutdown.bat
call startup.bat
cd ..\\webapps\\BookStore\\scripts\\