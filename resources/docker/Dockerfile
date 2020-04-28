FROM adoptopenjdk:8u222-b10-jdk-hotspot

COPY server/serverImpl-1.0-SNAPSHOT.jar server.jar
COPY server/libs libs

CMD java -Xms128m -Xmx256m -jar server.jar