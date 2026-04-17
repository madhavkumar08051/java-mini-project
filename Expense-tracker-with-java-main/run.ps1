# Build and Run Script for Expense Tracker

Write-Host "Compiling source files..." -ForegroundColor Cyan
if (!(Test-Path "out")) { New-Item -ItemType Directory -Path "out" }

# Robust way to find all java files recursively in PowerShell
$javaFiles = Get-ChildItem -Path src -Filter *.java -Recurse | ForEach-Object { $_.FullName }

javac -d out $javaFiles
if ($LASTEXITCODE -ne 0) { 
    Write-Host "Compilation failed!" -ForegroundColor Red
    exit 
}

Write-Host "Successfully compiled." -ForegroundColor Green
Write-Host "Launching ExpenseTracker..." -ForegroundColor Cyan

# Run the Main class from the out directory
java -cp out Main
