FROM openjdk:15 as products-api
CMD [ "mvn", "clean", "install", "-Dmaven.test.skip=true" ]
WORKDIR /opt/products-api
VOLUME /tmp
EXPOSE 8080
ENV JAVA_TOOL_OPTIONS -agentlib:jdwp=transport=dt_socket,address=8000,server=y,suspend=n
ADD ./target/dscatalog-0.0.1-SNAPSHOT.jar dscatalog.jar
ENTRYPOINT ["java","-jar","/products-mentoria.jar"]