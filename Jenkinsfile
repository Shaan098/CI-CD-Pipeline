pipeline {
    agent any
    
    environment {
        // Docker Hub credentials (configure in Jenkins)
        DOCKER_REGISTRY = 'docker.io'
        DOCKER_CREDENTIALS_ID = 'dockerhub-credentials'
        // TODO: Replace 'YOURUSERNAME' with your actual Docker Hub username
        // Example: 'johndoe/devops-demo' or 'mycompany/devops-demo'
        DOCKER_IMAGE_NAME = 'YOURUSERNAME/devops-demo'
        
        // Kubernetes configuration
        KUBECONFIG_CREDENTIALS_ID = 'kubeconfig'
        K8S_NAMESPACE = 'devops-demo'
        
        // Application configuration
        APP_NAME = 'devops-demo'
    }
    
    stages {
        stage('Checkout') {
            steps {
                echo '========== Checking out source code =========='
                checkout scm
                script {
                    env.GIT_COMMIT_SHORT = sh(
                        script: "git rev-parse --short HEAD",
                        returnStdout: true
                    ).trim()
                }
                echo "Git commit: ${env.GIT_COMMIT_SHORT}"
            }
        }
        
        stage('Build') {
            steps {
                echo '========== Building application with Maven =========='
                script {
                    if (isUnix()) {
                        sh 'mvn clean compile'
                    } else {
                        bat 'mvn clean compile'
                    }
                }
            }
        }
        
        stage('Test') {
            steps {
                echo '========== Running unit tests =========='
                script {
                    if (isUnix()) {
                        sh 'mvn test'
                    } else {
                        bat 'mvn test'
                    }
                }
            }
            post {
                always {
                    // Publish test results
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }
        
        stage('Package') {
            steps {
                echo '========== Packaging application =========='
                script {
                    if (isUnix()) {
                        sh 'mvn package -DskipTests'
                    } else {
                        bat 'mvn package -DskipTests'
                    }
                }
            }
            post {
                success {
                    // Archive the JAR file
                    archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true
                }
            }
        }
        
        stage('Build Docker Image') {
            steps {
                echo '========== Building Docker image =========='
                script {
                    // Build image with multiple tags
                    env.DOCKER_IMAGE_TAG = "${BUILD_NUMBER}-${env.GIT_COMMIT_SHORT}"
                    
                    if (isUnix()) {
                        sh """
                            docker build -t ${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG} .
                            docker tag ${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG} ${DOCKER_IMAGE_NAME}:latest
                        """
                    } else {
                        bat """
                            docker build -t ${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG} .
                            docker tag ${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG} ${DOCKER_IMAGE_NAME}:latest
                        """
                    }
                    echo "Docker image built: ${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG}"
                }
            }
        }
        
        stage('Push Docker Image') {
            steps {
                echo '========== Pushing Docker image to registry =========='
                script {
                    docker.withRegistry("https://${DOCKER_REGISTRY}", DOCKER_CREDENTIALS_ID) {
                        if (isUnix()) {
                            sh """
                                docker push ${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG}
                                docker push ${DOCKER_IMAGE_NAME}:latest
                            """
                        } else {
                            bat """
                                docker push ${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG}
                                docker push ${DOCKER_IMAGE_NAME}:latest
                            """
                        }
                    }
                    echo "Docker image pushed: ${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG}"
                }
            }
        }
        
        stage('Deploy to Kubernetes') {
            steps {
                echo '========== Deploying to Kubernetes =========='
                script {
                    // Update deployment image
                    if (isUnix()) {
                        sh """
                            kubectl apply -f k8s/namespace.yaml
                            kubectl apply -f k8s/configmap.yaml
                            kubectl set image deployment/devops-demo-deployment \
                                devops-demo=${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG} \
                                -n ${K8S_NAMESPACE} --record
                            kubectl apply -f k8s/service.yaml
                        """
                    } else {
                        bat """
                            kubectl apply -f k8s/namespace.yaml
                            kubectl apply -f k8s/configmap.yaml
                            kubectl set image deployment/devops-demo-deployment devops-demo=${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG} -n ${K8S_NAMESPACE} --record
                            kubectl apply -f k8s/service.yaml
                        """
                    }
                }
            }
        }
        
        stage('Verify Deployment') {
            steps {
                echo '========== Verifying Kubernetes deployment =========='
                script {
                    if (isUnix()) {
                        sh """
                            kubectl rollout status deployment/devops-demo-deployment -n ${K8S_NAMESPACE}
                            kubectl get pods -n ${K8S_NAMESPACE}
                            kubectl get svc -n ${K8S_NAMESPACE}
                        """
                    } else {
                        bat """
                            kubectl rollout status deployment/devops-demo-deployment -n ${K8S_NAMESPACE}
                            kubectl get pods -n ${K8S_NAMESPACE}
                            kubectl get svc -n ${K8S_NAMESPACE}
                        """
                    }
                }
            }
        }
    }
    
    post {
        success {
            echo '========== Pipeline completed successfully! =========='
            echo "Application deployed: ${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG}"
            echo "Access the application using: minikube service devops-demo-service -n devops-demo"
        }
        failure {
            echo '========== Pipeline failed! =========='
            echo 'Check the logs above for error details.'
        }
        always {
            echo '========== Cleaning up =========='
            // Clean up old Docker images
            script {
                if (isUnix()) {
                    sh 'docker image prune -f'
                } else {
                    bat 'docker image prune -f'
                }
            }
        }
    }
}
