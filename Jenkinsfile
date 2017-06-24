pipeline {
  agent any
  stages {
    stage('Build') {
      steps {
        withMaven(maven: '${Maven-3.3.9}') {
          sh 'mvn install -DskipTests'
        }
        
      }
    }
    stage('Test') {
      steps {
        withMaven(maven: '${Maven-3.3.9}') {
        sh 'mvn test'
        }
      }
    }
    stage('Code Coverage') {
      steps {
        withMaven(maven: '${Maven-3.3.9}') {
        sh 'mvn cobertura:coverage'
        } 
      }
    }
    stage('Static Analysis') {
      steps {
        withMaven(maven: '${Maven-3.3.9}') {
        sh 'mvn findbugs:findbugs'
        }
      }
    }
  }
}
