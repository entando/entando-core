pipeline {
  agent any
  stages {
    stage('Build') {
      steps {
        withMaven(maven: '${M5}') {
          sh 'mvn install -DskipTests'
        }
        
      }
    }
    stage('Test') {
      steps {
        withMaven(maven: '${M5}') {
        sh 'mvn test'
        }
      }
    }
    stage('Code Coverage') {
      steps {
        withMaven(maven: '${M5}') {
        sh 'mvn cobertura:coverage'
        } 
      }
    }
    stage('Static Analysis') {
      steps {
        withMaven(maven: '${M5}') {
        sh 'mvn findbugs:findbugs'
        }
      }
    }
  }
}
