#!/usr/bin/env groovy

pipeline {
    agent any

    stages {
        stage('Prepare') {
            steps {
                git "https://github.com/edigonzales/ilivalidator-fn-http-ressource-check.git"
            }
        }
        
        stage('Build') {
            steps {
                sh './gradlew --no-daemon clean classes'
            }
        }

        stage('Test') {
            steps {
                sh './gradlew --no-daemon test'
                publishHTML target: [
                    reportName : 'Gradle Tests',
                    reportDir:   'build/reports/tests/test',
                    reportFiles: 'index.html',
                    keepAll: true,
                    alwaysLinkToLastBuild: true,
                    allowMissing: false
                ]                
            }
        }
    }
    post {
        always {
            deleteDir() 
        }
    }
}