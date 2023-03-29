#stage 1
FROM diamol/maven AS builder

WORKDIR /usr/src/my_kb
COPY pom.xml .
#RUN mvn -B dependency:go-offline

COPY . .
RUN mvn clean package

#stage 2
FROM  diamol/openjdk AS app

WORKDIR /app
COPY --from=builder  /usr/src/my_kb/target/payment_kb.jar .
EXPOSE 6789

#ENTRYPOINT ["java", "-Dspring.data.mongodb.uri=mongodb://0.0.0.0:27017/test", "-jar", "/app/my_kb.jar"]
ENTRYPOINT ["java", "-jar", "/app/my_kb.jar"]