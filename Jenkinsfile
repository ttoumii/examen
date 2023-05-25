
pipeline {
    agent any
    stages {
        stage('Clone') {
            steps {
                git branch: 'master',
                url: 'https://github.com/ttoumii/examen.git',
                credentialsId: 'github_creds'
            }
        }
        stage('Build') {
            steps {
                sh 'mvn  clean install'
            }
        }
       stage('Sonar') {
            steps {
               sh 'mvn sonar:sonar -Dsonar.projectKey=devOps-project -Dsonar.login=2f0a6cb8549fbf8a3c4c41649b751fc41c194764'
            }
        }
        stage('Package') {
            steps {
                sh 'mvn package'
            }
        }

    }
}