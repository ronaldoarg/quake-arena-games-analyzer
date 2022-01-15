FROM azul/zulu-openjdk-alpine:8

# Java variables
ENV PATH $PATH:$JAVA_HOME/bin

# App
CMD mkdir /app
COPY target/*.jar /app/app.jar

# Ports exposing
EXPOSE 8080

# Entry
CMD java -jar /app/app.jar
