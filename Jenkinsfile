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
                    // Namountuj Torque.properties z hosťovského systému
                    args '-v /root/.m2:/root/.m2 -v ${TORQUE_CONFIG_PATH}:/usr/local/tomcat/conf/Torque.properties:ro'
                }
            }
            steps {
                echo 'Running integration tests with Tomcat...'
                echo 'Using Torque.properties from: ${TORQUE_CONFIG_PATH}'
                
                // Over či Torque.properties existuje
                sh 'cat /usr/local/tomcat/conf/Torque.properties | head -n 5'
                
                // Skopíruj WAR do Tomcat
                sh 'cp target/*.war /usr/local/tomcat/webapps/cud.war'
                
                // Spusti Tomcat na pozadí
                sh '''
                    catalina.sh start
                    echo "Waiting for Tomcat to start..."
                    sleep 30
                    
                    # Over či Tomcat beží
                    curl -f http://localhost:8080/cud/ || echo "Application not yet ready"
                '''
                
                // Spusti integračné testy (ak máš)
                sh 'mvn verify -DskipUnitTests=true || echo "No integration tests configured yet"'
                
                // Zastav Tomcat
                sh 'catalina.sh stop || true'
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
