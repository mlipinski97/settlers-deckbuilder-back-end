pipeline {
    agent any

    options {
        buildDiscarder logRotator(numToKeepStr: '15')
        disableConcurrentBuilds()
        timestamps()
    }

    stages {
        stage("Clean"){
            steps{
                bat "mvn clean"
            }
        }
        stage("Build"){
            steps{
                bat "mvn package -DskipTests"
            }
        }
        stage("Unit tests"){
            steps{
                bat "mvn test"
            }
        }
    }
    post {
        always { cleanWs() }
    }
}