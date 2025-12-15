# Quick Reference - CI/CD Pipeline Commands

## 🚀 Quick Start Commands

### Build and Run Locally
```powershell
# Navigate to project
cd "c:\Users\DELL\Desktop\DEVOPS PROJECT"

# Build
mvn clean package

# Run locally
mvn spring-boot:run

# Test endpoints
Invoke-WebRequest http://localhost:8080/health
```

### Docker Commands
```powershell
# Build image
docker build -t devops-demo:1.0 .

# Run container
docker run -d -p 8080:8080 --name devops-demo devops-demo:1.0

# View logs
docker logs -f devops-demo

# Stop and remove
docker stop devops-demo
docker rm devops-demo

# Push to Docker Hub (update username first)
docker tag devops-demo:1.0 your-username/devops-demo:1.0
docker push your-username/devops-demo:1.0
```

### Kubernetes Commands
```powershell
# Start Minikube
minikube start --driver=docker

# Deploy application
kubectl apply -f k8s/

# Check status
kubectl get all -n devops-demo
kubectl get pods -n devops-demo -w

# View logs
kubectl logs -f deployment/devops-demo-deployment -n devops-demo

# Access application
minikube service devops-demo-service -n devops-demo

# Update deployment
kubectl set image deployment/devops-demo-deployment `
    devops-demo=your-username/devops-demo:2.0 `
    -n devops-demo

# Rollback
kubectl rollout undo deployment/devops-demo-deployment -n devops-demo

# Delete all
kubectl delete namespace devops-demo
```

### Git Commands
```powershell
# Initialize repo
git init
git add .
git commit -m "Initial commit - CI/CD pipeline"

# Push to GitHub
git remote add origin https://github.com/your-username/devops-cicd-demo.git
git branch -M main
git push -u origin main
```

## 📋 File Update Checklist

Before deploying, update these files with your details:

- [ ] `Jenkinsfile` line 7: Update `your-dockerhub-username`
- [ ] `k8s/deployment.yaml` line 29: Update `your-dockerhub-username`
- [ ] Create Docker Hub repository
- [ ] Create GitHub repository

## 🔧 Troubleshooting Quick Fixes

### Application won't build
```powershell
mvn clean install -U
```

### Docker build fails
```powershell
docker system prune -a
docker build --no-cache -t devops-demo:1.0 .
```

### Kubernetes pods not starting
```powershell
kubectl describe pod <pod-name> -n devops-demo
kubectl logs <pod-name> -n devops-demo
```

### Jenkins can't connect to Kubernetes
```powershell
# Copy kubeconfig to Jenkins
minikube kubectl -- config view --raw
# Then paste into Jenkins credentials
```

## 📊 Verification Checklist

- [ ] Maven build: `mvn clean package` ✅
- [ ] Tests pass: `mvn test` ✅
- [ ] Docker builds: `docker build -t devops-demo:1.0 .` ✅
- [ ] Container runs: `docker run -p 8080:8080 devops-demo:1.0` ✅
- [ ] K8s deploys: `kubectl apply -f k8s/` ✅
- [ ] Pods running: `kubectl get pods -n devops-demo` ✅
- [ ] Service accessible: `minikube service devops-demo-service -n devops-demo` ✅

## 🎯 Key Endpoints

- **Health**: http://localhost:8080/health
- **Info**: http://localhost:8080/api/info
- **Welcome**: http://localhost:8080/
- **Jenkins**: http://localhost:8090
- **Minikube Dashboard**: `minikube dashboard`

## 📚 Documentation Links

- [Main README](../README.md)
- [Setup Guide](SETUP.md)
- [Pipeline Docs](PIPELINE.md)
- [Architecture](ARCHITECTURE.md)
