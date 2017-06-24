pipeline {
  agent any
  stages {
    stage('Build') {
      steps {
        withMaven(maven: M5) {
          sh 'mvn install -DskipTests'
        }
      }
    }
    stage('Test') {
      steps {
        sh 'mvn clean test'
      }
    }
    stage('Code Coverage') {
      steps {
        sh 'mvn cobertura:coverage'
      }
    }
    stage('Static Analysis') {
      steps {
        sh 'mvn findbugs:findbugs'
      }
    }
  }
}
