Write-Host "Building Mod..." -ForegroundColor Cyan

./gradlew build

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