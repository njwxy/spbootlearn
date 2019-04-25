#FROM java:8
FROM openjdk:8-jre-alpine
VOLUME /tmp
COPY ./target/pvfront-1.0.0.jar /app.jar
EXPOSE 8099/tcp
EXPOSE 12345/udp
EXPOSE 12344/udp
CMD ["/usr/bin/java","-jar","/app.jar"]
