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
                script {
                    // Vytvor config adresár v workspace  
                    sh 'mkdir -p config'
                    
                    // Skopíruj Torque.properties z Jenkins workspace
                    // Poznámka: ${WORKSPACE} je v Jenkins volume, čo je dostupné aj pre Docker host
                    sh """
                        if [ -f ${TORQUE_CONFIG_PATH} ]; then
                            cp ${TORQUE_CONFIG_PATH} config/Torque.properties
                            echo "✓ Torque.properties copied successfully"
                            ls -la config/Torque.properties
                        else
                            echo "⚠ WARNING: ${TORQUE_CONFIG_PATH} not found!"
                            echo "Creating placeholder Torque.properties..."
                            echo "Please configure: docker cp Torque.properties jenkins:/var/jenkins_home/configs/"
                            
                            # Skopíruj z JavaSource ako fallback
                            if [ -f JavaSource/Torque.properties ]; then
                                cp JavaSource/Torque.properties config/Torque.properties
                                echo "✓ Using Torque.properties from JavaSource/"
                            else
                                echo "ERROR: No Torque.properties found anywhere!"
                                exit 1
                            fi
                        fi
                        
                        # Overenie že súbor existuje
                        if [ ! -f config/Torque.properties ]; then
                            echo "ERROR: config/Torque.properties was not created!"
                            exit 1
                        fi
                        
                        echo "=== Torque.properties ready in workspace ==="
                        pwd
                        ls -la config/
                    """
                }
            }
        }
        
        stage('Integration Tests') {
            agent {
                dockerfile {
                    filename 'Dockerfile'
                    // Mount workspace config/ -> Docker /config/
                    // Vypneme entrypoint aby sme mali kontrolu nad spustením
                    // -u 0:0 spustí container ako root (potrebné pre zápis do /usr/local/tomcat/webapps/)
                    args '--entrypoint=\'\' -u 0:0 -v ${WORKSPACE}/config:/config:ro -v /root/.m2:/root/.m2'
                    reuseNode true
                }
            }
            steps {
                echo 'Running integration tests...'
                
                // DEBUG: Over či config adresár existuje
                sh '''
                    echo "=== DEBUG: Checking workspace and config ==="
                    echo "WORKSPACE: ${WORKSPACE}"
                    ls -la ${WORKSPACE}/ || true
                    echo "=== Checking config directory ==="
                    ls -la ${WORKSPACE}/config/ || true
                    echo "=== Checking /config in Docker ==="
                    ls -la /config/ || true
                '''
                
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
