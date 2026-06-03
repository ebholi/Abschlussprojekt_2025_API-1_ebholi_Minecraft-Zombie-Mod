# Run the Client cleanly

Write-Host "Clearing Cache..." -ForegroundColor Cyan

./gradlew clean

if ($LASTEXITCODE -eq 0)
{
    Write-Host "Successfully cleared Cache." -ForegroundColor Green
}
else
{
    Write-Host "Failed to clear Cache." -ForegroundColor Red
    Write-Host "Gradle exited with Code $LASTEXITCODE" -ForegroundColor Red
}

./gradlew runClient

if ($LASTEXITCODE -eq 0)
{
    Write-Host "Successfully ran Minecraft." -ForegroundColor Green
}
else
{
    Write-Host "Something went wrong." -ForegroundColor Red
    Write-Host "Minecraft exited with Code $LASTEXITCODE" -ForegroundColor Red
    Write-Host "Note: Leaving the game by ending the Task or performing similar actions can produce an artificial error." -ForegroundColor Cyan
}