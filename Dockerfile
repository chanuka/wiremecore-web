FROM openjdk:17-oracle
ADD target/wiremecore-web.jar wiremecore-web.jar
ADD logback-spring.xml /opt/conf/wiremecoreweb/logback-spring.xml
EXPOSE 8090
ENTRYPOINT ["java", "-jar", "wiremecore-web.jar"]