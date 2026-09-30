@echo off
rem ============================================================
rem  Build all modules in order (same style as the real project).
rem  CodeQL (build-mode: manual) traces every javac call made here.
rem ============================================================
setlocal
cd /d "%~dp0"

echo [1/3] fetch libraries
call ant -f build-libs.xml
if errorlevel 1 goto :error

echo [2/3] framework
call ant -f framework\build.xml clean dist
if errorlevel 1 goto :error

echo [3/3] webapp
call ant -f webapp\build.xml clean war
if errorlevel 1 goto :error

echo BUILD ALL SUCCESS
exit /b 0

:error
echo BUILD ALL FAILED
exit /b 1
