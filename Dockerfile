FROM openjdk:8-jre

USER root
ADD target/elasticsearch-1.0-SNAPSHOT.jar elasticsearch.jar
ENTRYPOINT ["sh", "-c","java -jar /elasticsearch.jar"]