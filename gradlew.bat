@echo off
setlocal EnableExtensions
where gradle >nul 2>nul
if %errorlevel% equ 0 (gradle %* & exit /b %errorlevel%)
for /f "delims=" %%G in ('powershell -NoProfile -Command "Get-ChildItem -Path (Join-Path $env:USERPROFILE '.gradle\wrapper\dists') -Filter gradle.bat -Recurse -ErrorAction SilentlyContinue | Select-Object -First 1 -ExpandProperty FullName"') do set "LOCAL_GRADLE=%%G"
if defined LOCAL_GRADLE (call "%LOCAL_GRADLE%" %* & exit /b %errorlevel%)
echo Gradle is not installed and no cached Gradle distribution was found.
echo Install Gradle 8+ or run: gradle wrapper
exit /b 1
