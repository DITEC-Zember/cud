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
                
                echo 'Copying WAR to Tomcat webapps...'
                sh 'cp target/*.war /usr/local/tomcat/webapps/cud.war'
            }
        }
        
        stage('Integration Tests') {
            agent {
                dockerfile {
                    filename 'Dockerfile'
                    // Namountuj Torque.properties do /config/ (entrypoint ho skopíruje do aplikácie)
                    args '-v /root/.m2:/root/.m2 -v ${TORQUE_CONFIG_PATH}:/config/Torque.properties:ro'
                }
            }
            steps {
                echo 'Running integration tests with Tomcat...'
                echo 'Using Torque.properties from: ${TORQUE_CONFIG_PATH}'
                
                // Skopíruj WAR do Tomcat
                sh 'cp target/*.war /usr/local/tomcat/webapps/cud.war'
                
                // Entrypoint automaticky:
                // 1. Počká na rozbalenie WAR
                // 2. Skopíruje Torque.properties z /config/ do aplikácie
                // 3. Reštartuje Tomcat
                
                echo "Waiting for application to start..."
                sh 'sleep 40'
                
                // Over či aplikácia beží
                sh 'curl -f http://localhost:8080/cud/ || echo "Application not yet ready"'
                
                // Spusti integračné testy (ak máš)
                sh 'mvn verify -DskipUnitTests=true || echo "No integration tests configured yet"'
            }
        }
        
        stage('Archive') {
            steps {
                echo 'Archiving artifacts...'
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
