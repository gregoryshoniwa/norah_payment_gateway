FROM openjdk:17

RUN mkdir /app
COPY . /app
WORKDIR /app
RUN mvn clean -P production package

CMD java -Duser.timezone=CAT -jar /app/target/norah-0.0.1-RELEASE.jar