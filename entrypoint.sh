#!/bin/bash
set -e

echo "Starting Tomcat with external Torque.properties configuration..."

# Ak WAR súbor existuje, rozbali ho manuálne
if [ -f "/usr/local/tomcat/webapps/cud.war" ]; then
    echo "Found WAR file, extracting to webapps/cud/..."
    mkdir -p /usr/local/tomcat/webapps/cud
    cd /usr/local/tomcat/webapps/cud
    jar -xf /usr/local/tomcat/webapps/cud.war
    echo "WAR extracted successfully!"
    
    # Skopíruj Torque.properties (ak existuje)
    if [ -f "/config/Torque.properties" ]; then
        echo "Copying Torque.properties from /config/ to application..."
        cp /config/Torque.properties /usr/local/tomcat/webapps/cud/WEB-INF/classes/Torque.properties
        echo "Torque.properties configured successfully!"
        
        # Zobraz prvých pár riadkov
        echo "Configuration preview:"
        head -n 10 /usr/local/tomcat/webapps/cud/WEB-INF/classes/Torque.properties | grep -v "^#" | grep -v "^$" | head -n 5
    else
        echo "WARNING: No Torque.properties found at /config/Torque.properties"
    fi
else
    echo "No WAR file found, Tomcat will use existing webapps content"
fi

# Spusti Tomcat v foreground mode
echo "Starting Tomcat..."
exec catalina.sh run
