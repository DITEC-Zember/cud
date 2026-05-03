FROM tomcat:8.5-jdk8

# Nainštaluj curl
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/* || true

# Stiahni a nainštaluj Maven priamo z Apache
ENV MAVEN_VERSION=3.8.8
ENV MAVEN_HOME=/usr/share/maven
ENV PATH=${MAVEN_HOME}/bin:${PATH}

RUN curl -fsSL https://archive.apache.org/dist/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz \
    | tar xz -C /usr/share && \
    mv /usr/share/apache-maven-${MAVEN_VERSION} ${MAVEN_HOME} && \
    ln -s ${MAVEN_HOME}/bin/mvn /usr/bin/mvn

# Vytvor adresár pre externú konfiguráciu
RUN mkdir -p /config

# Skopíruj entrypoint script
COPY entrypoint.sh /usr/local/bin/entrypoint.sh
RUN chmod +x /usr/local/bin/entrypoint.sh

# Nastav CATALINA_OPTS
ENV CATALINA_OPTS="-Dtorque.configuration=/usr/local/tomcat/webapps/cud/WEB-INF/classes/Torque.properties"

WORKDIR /app

# Exponuj Tomcat port
EXPOSE 8080

# Použij custom entrypoint
ENTRYPOINT ["/usr/local/bin/entrypoint.sh"]
