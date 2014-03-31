export PATH=$PATH:/usr/lib/jvm/default-java/bin
cd ../WEB-INF/sources/
find . -type f | xargs ls -dt | grep .java >sources.txt
rm -rf ../classes/
mkdir ../classes/
/usr/lib/jvm/default-java/bin/javac -classpath .:../../../../lib/servlet-api.jar -d ../classes/ @sources.txt
rm -f sources.txt
cd ../../../../bin/
./shutdown.sh
./startup.sh
cd ../webapps/BookStore/scripts/
