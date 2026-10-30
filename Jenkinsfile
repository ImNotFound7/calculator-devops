pipeline {
    agent any

    tools {
        jdk 'JDK17'
        maven 'Maven3'
    }

    options {
        skipDefaultCheckout()
        disableConcurrentBuilds()
        timestamps()
        timeout(time: 20, unit: 'MINUTES')
        buildDiscarder(logRotator(numToKeepStr: '20'))
    }

    triggers {
        pollSCM('H/2 * * * *')
    }

    environment {
        IMAGE_REPOSITORY = 'imnotfound/calculator-devops'
        CALCULATOR_IMAGE = "${IMAGE_REPOSITORY}:${BUILD_NUMBER}"
        NOTIFY_EMAIL = 'digvijaysinhsp149@gmail.com'
        // macOS services do not inherit the interactive terminal's CLI search path.
        PATH = "/usr/local/bin:/opt/homebrew/bin:/opt/local/bin:${env.PATH}"
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        stage('Build') {
            steps {
                sh 'mvn -B -ntp clean package -DskipTests'
            }
        }
        stage('Test') {
            steps {
                sh 'mvn -B -ntp test'
            }
            post {
                always {
                    junit 'target/surefire-reports/TEST-*.xml'
                }
            }
        }
        stage('Docker Build') {
            steps {
                sh 'docker build -t "$CALCULATOR_IMAGE" .'
            }
        }
        stage('Push') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub',
                        usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_TOKEN')]) {
                    sh '''
                        set +x
                        export DOCKER_HOST="${DOCKER_HOST:-$(docker context inspect --format '{{.Endpoints.docker.Host}}')}"
                        export DOCKER_CONFIG="$(mktemp -d)"
                        trap 'rm -rf "$DOCKER_CONFIG"' EXIT
                        printf '%s' "$DOCKER_TOKEN" | docker login -u "$DOCKER_USERNAME" --password-stdin
                        docker push "$CALCULATOR_IMAGE"
                    '''
                }
            }
        }
        stage('Deploy') {
            steps {
                sh '''
                    docker compose up -d --no-build --wait --wait-timeout 120
                    docker compose exec -T proxy wget -q -O - http://127.0.0.1/actuator/health
                    docker ps --filter label=com.docker.compose.project=calculator-devops
                '''
            }
        }
    }

    post {
        always {
            emailext(
                to: env.NOTIFY_EMAIL,
                subject: "${currentBuild.currentResult}: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                body: "Build: ${env.BUILD_URL}\nImage: ${env.CALCULATOR_IMAGE}\nStatus: ${currentBuild.currentResult}"
            )
        }
        failure {
            sh 'docker compose ps; docker compose logs --tail=50 || true'
        }
    }
}
