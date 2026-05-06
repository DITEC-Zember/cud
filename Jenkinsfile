pipeline {
    agent any
    
    environment {
        TORQUE_CONFIG_PATH = '/var/jenkins_home/configs/Torque.properties'
    }
    
    stages {
        stage('Build') {
            agent {
                dockerfile {
                    filename 'Dockerfile'
                    args '--entrypoint=\'\' -v /root/.m2:/root/.m2'
                    reuseNode true
                }
            }
            steps {
                echo 'Building CUD application...'
                sh 'mvn clean package -DskipTests'
                
                // Ulož WAR súbor pre Integration Tests
                stash includes: 'target/*.war', name: 'war-file'
            }
        }
        
        stage('Prepare Configuration') {
            steps {
                echo 'Preparing configuration for integration tests...'
                sh '''
                    # Vytvor config adresár v workspace
                    mkdir -p config
                    
                    # Skopíruj Torque.properties z Jenkins
                    if [ -f ${TORQUE_CONFIG_PATH} ]; then
                        cp ${TORQUE_CONFIG_PATH} config/Torque.properties
                        echo "Torque.properties copied to workspace config/"
                        ls -la config/Torque.properties
                    else
                        echo "ERROR: ${TORQUE_CONFIG_PATH} not found!"
                        exit 1
                    fi
                '''
            }
        }
        
        stage('Integration Tests') {
            agent {
                dockerfile {
                    filename 'Dockerfile'
                    // Mount workspace config/ -> Docker /config/
                    // Vypneme entrypoint aby sme mali kontrolu nad spustením
                    args '--entrypoint=\'\' -v ${WORKSPACE}/config:/config:ro -v /root/.m2:/root/.m2'
                    reuseNode true
                }
            }
            steps {
                echo 'Running integration tests...'
                
                // Obnov WAR z Build stage
                unstash 'war-file'
                
                script {
                    // Použijem logiku z entrypoint.sh
                    sh '''
                        echo "Extracting WAR and configuring Torque.properties..."
                        mkdir -p /usr/local/tomcat/webapps/cud
                        cd /usr/local/tomcat/webapps/cud
                        jar -xf ${WORKSPACE}/target/*.war
                        
                        # Skopíruj Torque.properties (ako v entrypoint.sh)
                        if [ -f /config/Torque.properties ]; then
                            echo "Copying Torque.properties from /config/ to application..."
                            cp /config/Torque.properties /usr/local/tomcat/webapps/cud/WEB-INF/classes/Torque.properties
                            echo "Torque.properties configured successfully!"
                        else
                            echo "ERROR: /config/Torque.properties not found!"
                            exit 1
                        fi
                    '''
                    
                    // Spusti Tomcat na pozadí
                    sh 'catalina.sh start'
                    
                    // Počkaj na štart
                    echo 'Waiting for application to start...'
                    timeout(time: 120, unit: 'SECONDS') {
                        waitUntil {
                            script {
                                def result = sh(script: 'curl -s -o /dev/null -w "%{http_code}" http://localhost:8080/cud/CudWS?wsdl || true', returnStdout: true).trim()
                                return result == '200'
                            }
                        }
                    }
                    
                    echo 'Application started! Running tests...'
                    
                    // Spusti integračné testy
                    sh 'mvn test -Dtest=CudWSAvailabilityTest,CudWSIntegrationTest -Dwsdl.url=http://localhost:8080/cud/CudWS?wsdl -DaccountId=136 -DfailIfNoTests=false'
                }
            }
            post {
                always {
                    junit allowEmptyResults: true, testResults: '**/target/surefire-reports/*.xml'
                    
                    // Zastaviť Tomcat
                    sh 'pkill -f catalina || true'
                }
            }
        }
        
        stage('Archive') {
            steps {
                echo 'Archiving artifacts...'
                unstash 'war-file'
                archiveArtifacts artifacts: 'target/*.war', fingerprint: true
            }
        }
    }
    
    post {
        success {
            echo 'Build completed successfully!'
        }
        failure {
            echo 'Build failed!'
        }
        always {
            cleanWs()
        }
    }
}
