pipeline {
  agent any
  stages {
    stage('Build') {
      steps {
        withMaven(maven: 'Maven-3.3.9') {
          sh 'mvn install -DskipTests'
        }
        
      }
    }
    stage('Test') {
      steps {
        withMaven(maven: 'Maven-3.3.9') {
          sh 'mvn clean test'
        }
        
      }
    }
  }
}