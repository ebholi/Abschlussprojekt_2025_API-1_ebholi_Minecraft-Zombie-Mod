# Author: Oliver Ebhardt
# Created: 08.06.2026
# Version: 0.2.2-alpha
# Description: Rusher Zombie Type with his own Ability implemented using a Goal.

# Run the Client cleanly# Run the Client cleanly

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

Write-Host "Updating Dependencies..." -ForegroundColor Cyan

./gradlew dependencies

if ($LASTEXITCODE -eq 0) {
    Write-Host "Successfully updated dependencies." -ForegroundColor Green
} else {
    Write-Host "Failed to update dependencies." -ForegroundColor Red
    Write-Host "Gradle exited with Code $LASTEXITCODE" -ForegroundColor Red
}

Write-Host "Launching Minecraft..." -ForegroundColor Cyan

./gradlew runClient

if ($LASTEXITCODE -eq 0)
{
    Write-Host "Successfully ran & exited Minecraft." -ForegroundColor Green
}
else
{
    Write-Host "Something went wrong." -ForegroundColor Red
    Write-Host "Minecraft exited with Code $LASTEXITCODE" -ForegroundColor Red
    Write-Host "Note: Terminating the game by ending the task or performing similar actions can produce an artificial error." -ForegroundColor White
}