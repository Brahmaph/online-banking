pipeline {

    agent any

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

        stage('Check Java and Maven') {
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
                    junit allowEmptyResults: true,
                          testResults: 'target/surefire-reports/*.xml'
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

                        if curl -f -s http://localhost:${APP_PORT}/api/account > /dev/null
                        then
                            echo "Application is UP"
                            exit 0
                        fi

                        echo "Waiting for application..."
                        sleep 2

                    done

                    echo "Application failed to start"

                    echo "===== APPLICATION LOG ====="
                    cat application.log

                    exit 1
                '''
            }
        }

        stage('Browser Tests') {
            steps {
                script {

                    if (fileExists('automation/pom.xml')) {

                        echo 'Running Selenium tests...'

                        dir('automation') {
                            sh 'mvn clean test'
                        }

                    } else {

                        echo 'automation/pom.xml not found.'
                        echo 'Skipping browser tests.'

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
