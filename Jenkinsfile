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
            }
        }
        
        stage('Integration Tests') {
            steps {
                echo 'Integration tests - zatiaľ nie sú nakonfigurované'
                echo 'WAR súbor je pripravený v target/cud-1.0.0.war'
                // TODO: Pridať integračné testy s databázou a Tomcat
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
