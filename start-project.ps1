Write-Host "Stopping existing Java processes..."
Get-Process java -ErrorAction SilentlyContinue | Stop-Process -Force
Start-Sleep -Seconds 2

Write-Host "Starting Eureka Server..."
Start-Process -FilePath "$PSScriptRoot\mvnw.cmd" -ArgumentList "spring-boot:run" -WorkingDirectory "$PSScriptRoot\eureka-server" -RedirectStandardOutput "$PSScriptRoot\eureka-server\eureka.log" -RedirectStandardError "$PSScriptRoot\eureka-server\eureka.err" -WindowStyle Minimized

Write-Host "Waiting for Eureka Server to initialize (20s)..."
Start-Sleep -Seconds 20

Write-Host "Starting Auth Service..."
Start-Process -FilePath "$PSScriptRoot\mvnw.cmd" -ArgumentList "spring-boot:run" -WorkingDirectory "$PSScriptRoot\auth-service" -RedirectStandardOutput "$PSScriptRoot\auth-service\auth.log" -RedirectStandardError "$PSScriptRoot\auth-service\auth.err" -WindowStyle Minimized

Write-Host "Starting QMA Service..."
Start-Process -FilePath "$PSScriptRoot\mvnw.cmd" -ArgumentList "spring-boot:run" -WorkingDirectory "$PSScriptRoot\qma-service" -RedirectStandardOutput "$PSScriptRoot\qma-service\qma.log" -RedirectStandardError "$PSScriptRoot\qma-service\qma.err" -WindowStyle Minimized

Write-Host "Starting API Gateway..."
Start-Process -FilePath "$PSScriptRoot\mvnw.cmd" -ArgumentList "spring-boot:run" -WorkingDirectory "$PSScriptRoot\api-gateway" -RedirectStandardOutput "$PSScriptRoot\api-gateway\gateway.log" -RedirectStandardError "$PSScriptRoot\api-gateway\gateway.err" -WindowStyle Minimized

Write-Host "Waiting for services to register with Eureka (20s)..."
Start-Sleep -Seconds 20

Write-Host "Starting Frontend..."
Start-Process -FilePath "npm.cmd" -ArgumentList "run dev" -WorkingDirectory "d:\QMA-frontend\frontend"

Write-Host "Project started!"
