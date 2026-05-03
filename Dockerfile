FROM tomcat:7-jdk8

# Nainštaluj Maven a curl pre health check
RUN apt-get update && \
    apt-get install -y maven curl && \
    rm -rf /var/lib/apt/lists/*

# Nastavenie Maven
ENV MAVEN_HOME=/usr/share/maven
ENV PATH=${MAVEN_HOME}/bin:${PATH}

# Vytvor conf adresár pre Torque.properties (bude namountovaný zvonku)
RUN mkdir -p /usr/local/tomcat/conf

# Nastav CATALINA_OPTS aby Torque.properties bol viditeľný pre aplikáciu
ENV CATALINA_OPTS="-Dtorque.configuration=/usr/local/tomcat/conf/Torque.properties"

WORKDIR /app

# Exponuj Tomcat port
EXPOSE 8080

CMD ["catalina.sh", "run"]
