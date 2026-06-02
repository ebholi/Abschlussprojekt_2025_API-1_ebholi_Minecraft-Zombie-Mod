# Gives .jar file to a modrinth instance for real-world testing

Write-Host "Deploying Mod to Modrinth Instance..." -ForegroundColor Cyan

# Path to test instance
$testInstanceModPath = "$env:APPDATA\ModrinthApp\profiles\Mod Environment\mods"

if (!(Test-Path $testInstanceModPath)) {
    Write-Host "Couldn't find Modrinth Instance." -ForegroundColor Red
    Write-Host 'Make sure your Modrinth Instance Folder is named "Mod Environment".' -ForegroundColor Cyan
    exit 1
}

# Gets most recent .jar file (the one we want)
$jar = Get-ChildItem "build/libs/*.jar" |
        Sort-Object LastWriteTime -Descending |
        Select-Object -First 1

if (!$jar)
{
    Write-Host "Couldn't find .jar file." -ForegroundColor Red
    Write-Host "Make sure you build the new .jar file before deploying" -ForegroundColor Cyan
    exit 1
}

# Removes other (older) versions of the Mod
Get-ChildItem $testInstanceModPath -Filter "y_apocalypse_zombies*.jar" |
    Remove-Item -Force

# Places .jar file
Write-Host "Placing new Mod version..." -ForegroundColor Cyan
Copy-Item $jar.FullName $testInstanceModPath -Force
Write-Host "Successfully placed new Mod version in Modrinth Instance" -ForegroundColor Green