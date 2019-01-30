#!/usr/bin/env groovy

pipeline {
    agent any

    environment {
        artifactoryUser = credentials('artifactoryUser')
        artifactoryPass = credentials('artifactoryPass')
    }    

    stages {
        stage('Prepare') {
            steps {
                git "https://github.com/edigonzales/ilivalidator-extension-functions.git"
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
         stage('Publish') {
            steps {
                sh './gradlew --no-daemon build jar artifactoryPublish -x test'                
            }
        }                 
    }
    post {
        always {
            deleteDir() 
        }
    }
}