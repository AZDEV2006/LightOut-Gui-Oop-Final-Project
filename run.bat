@echo off
echo ===============================
echo   LightsOutGame Build
echo ===============================

set SRC_DIR=src
set BUILD_DIR=build
set MAIN_CLASS=Main

if exist %BUILD_DIR% (
    rmdir /s /q %BUILD_DIR%
)

mkdir %BUILD_DIR%

echo Compiling source files...

for /r %SRC_DIR% %%f in (*.java) do (
    javac -d %BUILD_DIR% %%f
)

if %errorlevel% neq 0 (
    echo Compilation failed
    pause
    exit /b
)

echo Compilation successful

echo Launching LightsOutGame
java -cp %BUILD_DIR% %MAIN_CLASS%

if %errorlevel% neq 0 (
    echo Application crashed
)

pause
