FROM openjdk:21-slim

WORKDIR /java-app

COPY HelloWorld.java .

RUN javac HelloWorld.java

CMD ["java", "HelloWorld"]
