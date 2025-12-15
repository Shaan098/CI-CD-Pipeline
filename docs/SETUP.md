# Setup Guide - DevOps CI/CD Pipeline

This guide provides step-by-step instructions for setting up all the tools and configurations required for the CI/CD pipeline.

## 📑 Table of Contents

1. [Java Development Kit (JDK)](#1-java-development-kit-jdk)
2. [Apache Maven](#2-apache-maven)
3. [Docker Desktop](#3-docker-desktop)
4. [Minikube](#4-minikube)
5. [kubectl](#5-kubectl)
6. [Jenkins](#6-jenkins)
7. [Git](#7-git)
8. [Docker Hub Account](#8-docker-hub-account)
9. [GitHub Repository Setup](#9-github-repository-setup)
10. [Jenkins Configuration](#10-jenkins-configuration)
11. [Verification](#11-verification)

---

## 1. Java Development Kit (JDK)

### Install Java 17

**Windows:**
1. Download JDK 17 from [Oracle](https://www.oracle.com/java/technologies/downloads/#java17) or [Adoptium](https://adoptium.net/)
2. Run the installer
3. Set JAVA_HOME environment variable:
   ```powershell
   [System.Environment]::SetEnvironmentVariable("JAVA_HOME", "C:\Program Files\Java\jdk-17", "Machine")
   ```
4. Add to PATH:
   ```powershell
   $path = [System.Environment]::GetEnvironmentVariable("Path", "Machine")
   $path = "$path;%JAVA_HOME%\bin"
   [System.Environment]::SetEnvironmentVariable("Path", $path, "Machine")
   ```

### Verify Installation
```powershell
java -version
javac -version
```

Expected output: `java version "17.x.x"`

---

## 2. Apache Maven

### Install Maven

**Windows:**
1. Download Maven from [https://maven.apache.org/download.cgi](https://maven.apache.org/download.cgi)
2. Extract to `C:\Program Files\Apache\maven`
3. Set M2_HOME environment variable:
   ```powershell
   [System.Environment]::SetEnvironmentVariable("M2_HOME", "C:\Program Files\Apache\maven", "Machine")
   ```
4. Add to PATH:
   ```powershell
   $path = [System.Environment]::GetEnvironmentVariable("Path", "Machine")
   $path = "$path;%M2_HOME%\bin"
   [System.Environment]::SetEnvironmentVariable("Path", $path, "Machine")
   ```

### Verify Installation
```powershell
mvn -version
```

---

## 3. Docker Desktop

### Install Docker

**Windows:**
1. Download Docker Desktop from [https://www.docker.com/products/docker-desktop](https://www.docker.com/products/docker-desktop)
2. Run the installer
3. Enable WSL 2 backend if prompted
4. Start Docker Desktop

### Verify Installation
```powershell
docker --version
docker ps
```

### Configure Docker
- Allocate at least 4GB RAM to Docker
- Enable Kubernetes in Docker Desktop (optional, if not using Minikube)

---

## 4. Minikube

### Install Minikube

**Windows (using Chocolatey):**
```powershell
choco install minikube
```

**Windows (Manual):**
1. Download from [https://minikube.sigs.k8s.io/docs/start/](https://minikube.sigs.k8s.io/docs/start/)
2. Add to PATH

### Start Minikube
```powershell
# Start with Docker driver
minikube start --driver=docker --cpus=2 --memory=4096

# Verify status
minikube status
```

### Configure Minikube
```powershell
# Enable metrics
minikube addons enable metrics-server

# Enable dashboard (optional)
minikube addons enable dashboard
```

---

## 5. kubectl

### Install kubectl

**Windows (using Chocolatey):**
```powershell
choco install kubernetes-cli
```

**Windows (Manual):**
1. Download from [https://kubernetes.io/docs/tasks/tools/install-kubectl-windows/](https://kubernetes.io/docs/tasks/tools/install-kubectl-windows/)
2. Add to PATH

### Configure kubectl
```powershell
# Verify connection to Minikube
kubectl cluster-info
kubectl get nodes
```

---

## 6. Jenkins

### Option A: Install Jenkins Locally

**Windows:**
1. Download Jenkins WAR file from [https://www.jenkins.io/download/](https://www.jenkins.io/download/)
2. Install Java (if not already installed)
3. Run Jenkins:
   ```powershell
   java -jar jenkins.war --httpPort=8090
   ```
4. Access Jenkins at `http://localhost:8090`

### Option B: Run Jenkins in Docker (Recommended)

```powershell
# Create volume for Jenkins data
docker volume create jenkins_home

# Run Jenkins container
docker run -d `
  --name jenkins `
  -p 8090:8080 `
  -p 50000:50000 `
  -v jenkins_home:/var/jenkins_home `
  -v /var/run/docker.sock:/var/run/docker.sock `
  jenkins/jenkins:lts
```

### Initial Setup

1. **Get Initial Admin Password:**
   ```powershell
   # For local installation
   Get-Content C:\Users\<username>\.jenkins\secrets\initialAdminPassword
   
   # For Docker installation
   docker exec jenkins cat /var/jenkins_home/secrets/initialAdminPassword
   ```

2. **Access Jenkins:**
   - Open browser: `http://localhost:8090`
   - Enter initial admin password
   - Install suggested plugins

3. **Create Admin User:**
   - Username: `admin`
   - Password: (choose a secure password)
   - Full name: `Jenkins Admin`
   - Email: `your-email@example.com`

---

## 7. Git

### Install Git

**Windows:**
1. Download from [https://git-scm.com/download/win](https://git-scm.com/download/win)
2. Run installer with default options

### Configure Git
```powershell
git config --global user.name "Your Name"
git config --global user.email "your-email@example.com"
```

### Verify Installation
```powershell
git --version
```

---

## 8. Docker Hub Account

### Create Docker Hub Account

1. Go to [https://hub.docker.com/](https://hub.docker.com/)
2. Sign up for a free account
3. Verify your email
4. Create a repository:
   - Repository Name: `devops-demo`
   - Visibility: Public (or Private)

### Login to Docker Hub
```powershell
docker login
```
Enter your Docker Hub username and password.

---

## 9. GitHub Repository Setup

### Create GitHub Repository

1. Go to [https://github.com/](https://github.com/)
2. Create new repository:
   - Name: `devops-cicd-demo`
   - Visibility: Public
   - Initialize with README: No

### Push Code to GitHub

```powershell
cd "c:\Users\DELL\Desktop\DEVOPS PROJECT"

# Initialize Git repository
git init

# Add all files
git add .

# Commit
git commit -m "Initial commit - CI/CD pipeline setup"

# Add remote
git remote add origin https://github.com/<your-username>/devops-cicd-demo.git

# Push to GitHub
git push -u origin main
```

### Configure GitHub Webhook (Optional)

1. Go to repository settings
2. Click "Webhooks" → "Add webhook"
3. Payload URL: `http://<jenkins-url>:8090/github-webhook/`
4. Content type: `application/json`
5. Select "Just the push event"
6. Click "Add webhook"

---

## 10. Jenkins Configuration

### Install Required Plugins

1. Go to Jenkins → Manage Jenkins → Manage Plugins
2. Install the following plugins:
   - Git Plugin
   - Pipeline Plugin
   - Docker Pipeline Plugin
   - Kubernetes CLI Plugin
   - JUnit Plugin
   - Maven Integration Plugin

### Configure Jenkins Credentials

#### Docker Hub Credentials

1. Go to Jenkins → Manage Jenkins → Manage Credentials
2. Click "(global)" → "Add Credentials"
3. Kind: Username with password
4. Username: Your Docker Hub username
5. Password: Your Docker Hub password
6. ID: `dockerhub-credentials`
7. Description: Docker Hub Credentials
8. Click "Create"

#### GitHub Credentials (Optional)

1. Add Credentials
2. Kind: Username with password (or SSH key)
3. Username: Your GitHub username
4. Password: GitHub Personal Access Token
5. ID: `github-credentials`
6. Click "Create"

### Configure Tools

#### Maven Configuration

1. Go to Manage Jenkins → Global Tool Configuration
2. Maven section:
   - Click "Add Maven"
   - Name: `Maven-3.9`
   - Install automatically: ✓
   - Version: 3.9.x
3. Save

#### JDK Configuration

1. In Global Tool Configuration
2. JDK section:
   - Click "Add JDK"
   - Name: `Java-17`
   - JAVA_HOME: `C:\Program Files\Java\jdk-17` (or your JDK path)
3. Save

### Create Pipeline Job

1. **Create New Item:**
   - Click "New Item"
   - Name: `DevOps-CICD-Pipeline`
   - Type: Pipeline
   - Click "OK"

2. **Configure Pipeline:**
   - Description: `CI/CD Pipeline for Java Application`
   - Build Triggers:
     - ✓ GitHub hook trigger for GITScm polling (if using webhooks)
     - Or ✓ Poll SCM: `H/5 * * * *` (every 5 minutes)

3. **Pipeline Definition:**
   - Definition: Pipeline script from SCM
   - SCM: Git
   - Repository URL: `https://github.com/<your-username>/devops-cicd-demo.git`
   - Credentials: (select your GitHub credentials if private repo)
   - Branch: `*/main`
   - Script Path: `Jenkinsfile`
   - Click "Save"

### Configure kubectl in Jenkins

If Jenkins is running locally:
```powershell
# Copy kubeconfig
kubectl config view --raw > $env:USERPROFILE\.kube\config
```

If Jenkins is in Docker:
```powershell
# Copy kubeconfig into Jenkins container
docker cp $env:USERPROFILE\.kube\config jenkins:/var/jenkins_home/.kube/config

# Set permissions
docker exec jenkins chmod 600 /var/jenkins_home/.kube/config
```

---

## 11. Verification

### Verify All Tools

Run these commands to verify all installations:

```powershell
# Java
java -version

# Maven
mvn -version

# Docker
docker --version
docker ps

# Minikube
minikube status

# kubectl
kubectl version --client
kubectl get nodes

# Git
git --version

# Jenkins
# Access http://localhost:8090
```

### Test the Complete Pipeline

1. **Build Application Locally:**
   ```powershell
   cd "c:\Users\DELL\Desktop\DEVOPS PROJECT"
   mvn clean package
   ```

2. **Test Docker Build:**
   ```powershell
   docker build -t devops-demo:test .
   docker run -p 8080:8080 devops-demo:test
   ```
   Test: `http://localhost:8080/health`

3. **Test Kubernetes Deployment:**
   ```powershell
   kubectl apply -f k8s/
   kubectl get pods -n devops-demo
   minikube service devops-demo-service -n devops-demo
   ```

4. **Test Jenkins Pipeline:**
   - Go to Jenkins
   - Click on your pipeline job
   - Click "Build Now"
   - Watch the console output

---

## 🎯 Next Steps

After completing the setup:

1. Update `Jenkinsfile` with your Docker Hub username
2. Update `k8s/deployment.yaml` with your Docker image name
3. Push changes to GitHub
4. Trigger Jenkins build
5. Monitor deployment in Kubernetes

---

## 🔧 Troubleshooting

### Jenkins Cannot Connect to Minikube

```powershell
# Get Minikube IP
minikube ip

# Update kubeconfig to use IP instead of localhost
kubectl config set-cluster minikube --server=https://<minikube-ip>:8443
```

### Docker Permission Issues in Jenkins

```powershell
# Add Jenkins user to docker group (Linux/Mac)
sudo usermod -aG docker jenkins

# On Windows, ensure Docker Desktop is running
```

### Maven Build Fails

```powershell
# Clear Maven cache
Remove-Item -Recurse -Force $env:USERPROFILE\.m2\repository

# Rebuild
mvn clean install -U
```

---

## 📚 Additional Resources

- [Jenkins Documentation](https://www.jenkins.io/doc/)
- [Docker Documentation](https://docs.docker.com/)
- [Kubernetes Documentation](https://kubernetes.io/docs/)
- [Maven Documentation](https://maven.apache.org/guides/)
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)

---

**Setup Complete!** You're now ready to run the full CI/CD pipeline. 🚀
