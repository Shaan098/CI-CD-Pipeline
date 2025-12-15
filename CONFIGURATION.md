# Configuration Guide - Personalize Your CI/CD Pipeline

## 📝 Files That Need Your Input

Before deploying your CI/CD pipeline, you need to update the following configuration values with your actual credentials and usernames.

---

## 🔧 Required Changes

### 1. Docker Hub Username

**You need to update these 2 files:**

#### ✏️ File 1: `Jenkinsfile` (Line 10)

**Current:**
```groovy
DOCKER_IMAGE_NAME = 'YOURUSERNAME/devops-demo'
```

**Change to:**
```groovy
DOCKER_IMAGE_NAME = 'your-actual-username/devops-demo'
```

**Example:**
```groovy
DOCKER_IMAGE_NAME = 'johndoe/devops-demo'
DOCKER_IMAGE_NAME = 'shaan098/devops-demo'
DOCKER_IMAGE_NAME = 'mycompany/devops-demo'
```

---

#### ✏️ File 2: `k8s/deployment.yaml` (Line 36)

**Current:**
```yaml
image: YOURUSERNAME/devops-demo:latest
```

**Change to:**
```yaml
image: your-actual-username/devops-demo:latest
```

**Example:**
```yaml
image: johndoe/devops-demo:latest
image: shaan098/devops-demo:latest
image: mycompany/devops-demo:latest
```

---

## 🚀 Quick Update Commands

### Option 1: Manual Edit
1. Open `Jenkinsfile`
2. Find line 10: `DOCKER_IMAGE_NAME = 'YOURUSERNAME/devops-demo'`
3. Replace `YOURUSERNAME` with your Docker Hub username
4. Save file

5. Open `k8s/deployment.yaml`
6. Find line 36: `image: YOURUSERNAME/devops-demo:latest`
7. Replace `YOURUSERNAME` with your Docker Hub username
8. Save file

### Option 2: PowerShell Replace (All at Once)

```powershell
# Navigate to project directory
cd "c:\Users\DELL\Desktop\DEVOPS PROJECT"

# Replace YOURUSERNAME with your actual username (example: johndoe)
$username = "your-actual-username"  # CHANGE THIS!

# Update Jenkinsfile
(Get-Content Jenkinsfile) -replace 'YOURUSERNAME', $username | Set-Content Jenkinsfile

# Update deployment.yaml
(Get-Content k8s\deployment.yaml) -replace 'YOURUSERNAME', $username | Set-Content k8s\deployment.yaml

# Verify changes
Write-Host "Updated Docker image name to: $username/devops-demo" -ForegroundColor Green
```

---

## 📋 Pre-Deployment Checklist

Before running the pipeline, ensure you have:

### Docker Hub Setup
- [ ] Created Docker Hub account at https://hub.docker.com
- [ ] Created repository named `devops-demo`
- [ ] Noted your username for configuration
- [ ] Can login with `docker login`

### Configuration Updates
- [ ] Updated `Jenkinsfile` line 10 with your Docker Hub username
- [ ] Updated `k8s/deployment.yaml` line 36 with your Docker Hub username
- [ ] Both files use the **same** username

### Tools Installed (for full pipeline)
- [ ] Java 17 installed
- [ ] Maven installed
- [ ] Docker Desktop installed and running
- [ ] Minikube installed (optional, for local K8s)
- [ ] kubectl installed (optional, for K8s commands)
- [ ] Jenkins installed (optional, for automation)
- [ ] Git installed

---

## 🔍 Verification

After making changes, verify the configuration:

### Check Jenkinsfile
```powershell
# View the configured image name
Select-String -Path "Jenkinsfile" -Pattern "DOCKER_IMAGE_NAME"
```

**Expected output:**
```
DOCKER_IMAGE_NAME = 'yourusername/devops-demo'
```

### Check deployment.yaml
```powershell
# View the configured image
Select-String -Path "k8s\deployment.yaml" -Pattern "image:"
```

**Expected output:**
```
image: yourusername/devops-demo:latest
```

### Verify Both Match
```powershell
# Run this script to verify both use the same username
$jenkinsImage = (Select-String -Path "Jenkinsfile" -Pattern "DOCKER_IMAGE_NAME = '(.+)'" | ForEach-Object { $_.Matches.Groups[1].Value })
$k8sImage = (Select-String -Path "k8s\deployment.yaml" -Pattern "image: (.+):" | ForEach-Object { $_.Matches.Groups[1].Value })

Write-Host "Jenkinsfile image: $jenkinsImage"
Write-Host "K8s deployment image: $k8sImage"

if ($jenkinsImage -eq $k8sImage) {
    Write-Host "✅ Configuration matches!" -ForegroundColor Green
} else {
    Write-Host "❌ Configuration mismatch! Please ensure both files use the same username." -ForegroundColor Red
}
```

---

## 💡 Important Notes

1. **Username must match**: The Docker Hub username in both files must be identical
2. **Username must exist**: The Docker Hub account must be created before pushing images
3. **Repository name**: You can keep `devops-demo` or change it (update both files if changed)
4. **Case sensitive**: Docker Hub usernames are case-sensitive

---

## 🎯 What Happens After Configuration

Once you've updated these files:

1. **Jenkinsfile** will:
   - Build Docker images tagged as `yourusername/devops-demo:BUILD_NUMBER-COMMIT`
   - Push images to your Docker Hub repository
   - Deploy to Kubernetes using your image

2. **Kubernetes** will:
   - Pull images from `yourusername/devops-demo`
   - Deploy your application in pods
   - Manage rolling updates

---

## 🆘 Common Issues

### "unauthorized: authentication required"
- You forgot to run `docker login`
- Jenkins doesn't have Docker Hub credentials configured
- Username in files doesn't match your Docker Hub username

### "repository does not exist"
- You need to create the repository on Docker Hub first
- OR the first `docker push` will auto-create it (if using public repo)

### "image pull failed"
- Kubernetes can't access the image
- Check if image exists: `docker pull yourusername/devops-demo:latest`
- Verify image name is correct in deployment.yaml

---

## ✅ Quick Test

After configuration, test if the values are set correctly:

```powershell
# Test that placeholders are replaced
$jenkinsfile = Get-Content Jenkinsfile -Raw
$deployment = Get-Content k8s\deployment.yaml -Raw

if ($jenkinsfile -match "YOURUSERNAME") {
    Write-Host "❌ Jenkinsfile still contains YOURUSERNAME placeholder!" -ForegroundColor Red
} else {
    Write-Host "✅ Jenkinsfile configured!" -ForegroundColor Green
}

if ($deployment -match "YOURUSERNAME") {
    Write-Host "❌ deployment.yaml still contains YOURUSERNAME placeholder!" -ForegroundColor Red
} else {
    Write-Host "✅ deployment.yaml configured!" -ForegroundColor Green
}
```

---

## 📞 Need Help?

If the placeholders still show `YOURUSERNAME`:
1. Make sure you saved the files after editing
2. Check you're editing the correct files
3. Ensure your text editor has write permissions
4. Try the PowerShell replace command above

---

**Once configured, you're ready to deploy!** 🚀
