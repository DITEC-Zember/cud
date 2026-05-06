pipeline {
    agent any
    
    environment {
        // Cesta k Torque.properties na Jenkins serveri (uprav podľa svojho umiestnenia)
        TORQUE_CONFIG_PATH = '/var/jenkins_home/configs/Torque.properties'
    }
    
    stages {
        stage('Checkout') {
            steps {
                echo 'Checking out source code...'
                checkout scm
            }
        }
        
        stage('Build & Test') {
            agent {
                dockerfile {
                    filename 'Dockerfile'
                    args '-v /root/.m2:/root/.m2'
                }
            }
            steps {
                echo 'Building CUD application...'
                sh 'mvn clean package'
                
                // Ulož WAR súbor pre neskoršie použitie
                stash includes: 'target/*.war', name: 'war-file'
            }
            post {
                always {
                    // Publikuj výsledky testov (JUnit reports)
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }
        
        stage('Unit Tests') {
            agent {
                dockerfile {
                    filename 'Dockerfile'
                    args '-v /root/.m2:/root/.m2'
                }
            }
            steps {
                echo 'Running unit tests...'
                sh 'mvn test'
            }
            post {
                always {
                    // Publikuj výsledky unit testov
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }
        
        stage('Integration Tests') {
            agent {
                dockerfile {
                    filename 'Dockerfile'
                    args '-v /root/.m2:/root/.m2 -v ${TORQUE_CONFIG_PATH}:/config/Torque.properties:ro'
                }
            }
            steps {
                echo 'Deploying application for integration tests...'
                
                // Obnov WAR súbor
                unstash 'war-file'
                
                script {
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
                    sh 'mvn test -Dtest=CudWSAvailabilityTest -Dwsdl.url=http://localhost:8080/cud/CudWS?wsdl'
                    
                    // Pre integračné testy by bolo potrebné nastaviť prihlasovacie údaje
                    // sh 'mvn test -Dtest=CudWSIntegrationTest -Dwsdl.url=http://localhost:8080/cud/CudWS?wsdl'
                }
            }
            post {
                always {
                    // Publikuj výsledky integračných testov
                    junit '**/target/surefire-reports/*.xml'
                    
                    // Zastaviť Tomcat
                    sh 'catalina.sh stop || true'
                }
            }
            }
        }
        
        stage('Archive') {
            steps {
                echo 'Archiving artifacts...'
                // Obnov WAR súbor z Build & Test stage
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
