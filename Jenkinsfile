pipeline {

    agent any

    tools {
        jdk 'JDK17'
        maven 'Maven3'
    }

    environment {
        APP_NAME = 'online-banking'
        APP_PORT = '8080'
    }

    stages {

        stage('Checkout') {
            steps {
                echo 'Checking out source code...'
                checkout scm
            }
        }

        stage('Verify Environment') {
            steps {
                sh '''
                    echo "Java version:"
                    java -version

                    echo "Maven version:"
                    mvn -version
                '''
            }
        }

        stage('Build Backend') {
            steps {
                dir('backend') {
                    sh 'mvn clean package -DskipTests'
                }
            }
        }

        stage('Run Unit Tests') {
            steps {
                dir('backend') {
                    sh 'mvn test'
                }
            }

            post {
                always {
                    junit 'backend/target/surefire-reports/*.xml'
                }
            }
        }

        stage('Start Application') {
            steps {
                sh '''
                    echo "Starting Spring Boot application..."

                    nohup java -jar backend/target/*.jar \
                        > application.log 2>&1 &

                    echo $! > application.pid

                    echo "Application PID:"
                    cat application.pid
                '''
            }
        }

        stage('Health Check') {
            steps {
                sh '''
                    echo "Waiting for application..."

                    for i in {1..30}; do

                        if curl -s http://localhost:${APP_PORT}/api/account > /dev/null
                        then
                            echo "Application is UP"
                            exit 0
                        fi

                        echo "Waiting..."
                        sleep 2

                    done

                    echo "Application failed to start"

                    cat application.log

                    exit 1
                '''
            }
        }

        stage('Browser Tests') {
            steps {
                script {

                    if (fileExists('automation/pom.xml')) {

                        echo 'Running Selenium browser tests...'

                        dir('automation') {
                            sh 'mvn clean test'
                        }

                    } else {

                        echo 'Automation project not found.'
                        echo 'Skipping Selenium tests for now.'

                    }
                }
            }

            post {
                always {
                    junit allowEmptyResults: true,
                          testResults: 'automation/target/surefire-reports/*.xml'
                }
            }
        }
    }

    post {

        always {

            echo 'Stopping application...'

            sh '''
                if [ -f application.pid ]; then

                    kill $(cat application.pid) || true

                fi
            '''

            archiveArtifacts artifacts: 'application.log',
                             allowEmptyArchive: true
        }

        success {
            echo '======================================'
            echo 'ONLINE BANKING BUILD SUCCESSFUL'
            echo '======================================'
        }

        failure {
            echo '======================================'
            echo 'ONLINE BANKING BUILD FAILED'
            echo '======================================'
        }
    }
}
