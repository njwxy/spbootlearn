FROM java:8
VOLUME /tmp
ADD ./target/pvfront-1.0.0.jar
RUN bash -c 'touch /app.jar'
EXPOSE 8099/tcp
EXPOSE 12345/udp
EXPOSE 12344/udp
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","app.jar"]
