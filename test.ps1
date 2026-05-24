$body = @{
    category   = "LENGTH"
    value1     = 1.0
    unit1      = "YARD"
    targetUnit = "INCH"
} | ConvertTo-Json

Invoke-WebRequest -Uri "http://localhost:8080/api/quantity/convert" -Method POST -Headers @{"Content-Type" = "application/json" } -Body $body
