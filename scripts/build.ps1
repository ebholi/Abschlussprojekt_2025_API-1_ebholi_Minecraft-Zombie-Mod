# Builds the .jar file

Write-Host "Building Mod..." -ForegroundColor Cyan

# Command to build .jar file of the Mod
./gradlew build

if ($LASTEXITCODE -eq 0) {
    # Gets most recent .jar file
    $jar = Get-ChildItem "build/libs/*.jar" |
        Sort-Object LastWriteTime -Descending |
        Select-Object -First 1

    Write-Host "Build Succeeded" -ForegroundColor Green

    if ($jar) {
        # Logs path to .jar file
        Write-Host ".jar File:" -ForegroundColor Green
        Write-Host $jar.FullName -ForegroundColor White
    } else {
        Write-Host "Couldn't find .jar File" -ForegroundColor Red
    }
} else {
    # Error
    Write-Host "Build Failed" -ForegroundColor Red -ForegroundColor Red
    Write-Host "Gradle exited with code $LASTEXITCODE" -ForegroundColor Red
}