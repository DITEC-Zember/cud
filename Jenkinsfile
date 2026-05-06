pipeline {
    agent any
    
    environment {
        // Cesta k Torque.properties na Jenkins serveri (uprav podľa svojho umiestnenia)
        TORQUE_CONFIG_PATH = '/var/jenkins_home/configs/Torque.properties'
        
        // Prihlasovacie údaje pre integračné testy (UPRAVTE PODĽA VAŠEJ KONFIGURÁCIE)
        // Môžete tiež použiť Jenkins Credentials Plugin namiesto hardcoded hodnôt
        TEST_ACCOUNT_ID = '136'  // Account ID pre integračné testy
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
                
                // Ulož WAR súbor pre Integration Tests stage
                stash includes: 'target/*.war', name: 'war-file'
            }
        }
        
        stage('Prepare Configuration') {
            steps {
                echo 'Preparing Torque.properties for integration tests...'
                sh """
                    echo "Copying Torque.properties from ${TORQUE_CONFIG_PATH}"
                    if [ -f ${TORQUE_CONFIG_PATH} ]; then
                        cp ${TORQUE_CONFIG_PATH} JavaSource/Torque.properties
                        echo "Torque.properties copied successfully"
                        ls -la JavaSource/Torque.properties
                    else
                        echo "ERROR: ${TORQUE_CONFIG_PATH} not found!"
                        exit 1
                    fi
                """
            }
        }
        
        stage('Integration Tests') {
            agent {
                dockerfile {
                    filename 'Dockerfile'
                    args '--entrypoint=\'\' -u 0:0 -v /root/.m2:/root/.m2'
                    reuseNode true
                }
            }
            steps {
                echo 'Deploying application and running integration tests...'
                
                script {
                    
                    // Prebuduj WAR so správnym Torque.properties (package spustí process-resources)
                    echo 'Rebuilding WAR with correct Torque.properties...'
                    sh 'mvn package -DskipTests'
                    
                    // Nasaď WAR do Tomcat
                    sh 'cp target/*.war /usr/local/tomcat/webapps/cud.war'
                    
                    // Spusti Tomcat na pozadí
                    sh 'catalina.sh start'
                    
                    // Počkaj kým sa aplikácia naštartuje (max 120 sekúnd)
                    echo 'Waiting for application to start...'
                    timeout(time: 120, unit: 'SECONDS') {
                        waitUntil {
                            script {
                                def result = sh(script: 'curl -s -o /dev/null -w "%{http_code}" http://localhost:8080/cud/CudWS?wsdl || true', returnStdout: true).trim()
                                return result == '200'
                            }
                        }
                    }
                    
                    echo 'Application started successfully!'
                    
                    // Spusti smoke test (nevyžaduje autentifikáciu)
                    echo 'Running smoke tests...'
                    sh 'mvn test -Dtest=CudWSAvailabilityTest -Dwsdl.url=http://localhost:8080/cud/CudWS?wsdl -DfailIfNoTests=false'
                    
                    // Spusti integračné testy s konfigurovateľnými credentials
                    echo 'Running integration tests...'
                    sh "mvn test -Dtest=CudWSIntegrationTest -Dwsdl.url=http://localhost:8080/cud/CudWS?wsdl -DaccountId=${TEST_ACCOUNT_ID} -DfailIfNoTests=false"
                }
            }
            post {
                always {
                    // Publikuj výsledky integračných testov
                    junit allowEmptyResults: true, testResults: '**/target/surefire-reports/*.xml'
                    
                    // Zastaviť Tomcat
                    sh 'catalina.sh stop || true'
                }
            }
        }
        
        stage('Archive') {
            steps {
                echo 'Archiving artifacts...'
                // Obnov WAR súbor z Build stage
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
