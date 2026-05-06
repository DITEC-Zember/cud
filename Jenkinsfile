pipeline {
    agent any
    
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
                
                // Ulož WAR súbor pre Archive stage
                stash includes: 'target/*.war', name: 'war-file'
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
