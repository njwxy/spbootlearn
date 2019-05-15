#FROM java:8
FROM openjdk:8-jre-alpine
VOLUME /tmp
COPY ./pvfront-1.0.0.jar /app.jar
COPY ./application.yml /application.yml
COPY ./run.sh /run.sh
EXPOSE 8099/tcp
EXPOSE 12388/udp
EXPOSE 12344/udp
RUN chmod 777 /run.sh
CMD ["/bin/sh","-c","/run.sh"]
