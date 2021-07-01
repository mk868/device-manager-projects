pipeline {
    agent {
        dockerfile true
    }
    options {
        buildDiscarder logRotator(numToKeepStr: '10')
    }
    stages {
        stage('Gradle build') {
            steps {
                sh './gradlew build'
            }
        }
        // TODO sonarqube
        // TODO integration tests
        stage('jpackage Linux') {
            // TODO when { release | forcedSnapshot }
            steps {
                sh './gradlew jpackage'
                archiveArtifacts artifacts: '**/build/dist/*.deb'
            }
        }
        // TODO jpackage on windows agent
        // try to use "openjdk:16.0.1-jdk-windowsservercore-1809" image + WiX Toolset
    }
}
