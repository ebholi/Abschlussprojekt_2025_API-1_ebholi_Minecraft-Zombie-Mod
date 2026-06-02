# Gives .jar file to a modrinth instance for real-world testing

Write-Host "Deploying Mod to Modrinth Instance..." -ForegroundColor Cyan

# Path to test instance
$testInstancePath = "C:\Users\<username>\AppData\Roaming\ModrinthApp\profiles\Mod Environment\mods"

# Gets most recent .jar file (the one we want)
$jar = Get-ChildItem "build/libs/*.jar" |
        Sort-Object LastWriteTime -Descending |
        Select-Object -First 1

if (!$testInstancePath) {
    Write-Host "Couldn't find Modrinth Instance." -ForegroundColor Red
    Write-Host 'Make sure your Modrinth Instance Folder is named "Mod Environment".' -ForegroundColor Red
} elseif ($testInstancePath + $jar) {
    Write-Host "Overwriting previous .jar File..." -ForegroundColor Cyan
}

if ($LASTEXITCODE -eq 0) {
    $jar = Get-ChildItem "build/libs/*.jar" |
        Sort-Object LastWriteTime -Descending |
        Select-Object -First 1

    Write-Host "Build Succeeded" -ForegroundColor Green

    if ($jar) {
        Write-Host ".jar File:" -ForegroundColor Cyan
        Write-Host $jar.FullName -ForegroundColor Cyan
    } else {
        Write-Host "Couldn't find .jar File" -ForegroundColor Red
    }
} else {
    Write-Host "Build Failed" -ForegroundColor Red -ForegroundColor Red
    Write-Host "Gradle exited with code $LASTEXITCODE" -ForegroundColor Red
}