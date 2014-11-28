@echo off
title Forge Helper
:home
echo.
echo Select a task:
echo =============
echo.
echo 1) Setup Workspace
echo 2) Setup Idea as IDE
echo 3) Setup Eclipse as IDE
echo 4) Clean Idea
echo 5) Clean Eclipse
echo 6) Build
echo 7) Setup Decomp Workspace
echo 8) Setup Workspace And Setup IDEA as IDE
echo 9) Setup Workspace And Setup Eclipse
echo 10) Exit
echo.
set /p task=Type option:
if "%task%"=="1" goto SetupWorkspace
if "%task%"=="2" goto SetupIdeaasIDE
if "%task%"=="3" goto SetupEclipseasIDE
if "%task%"=="4" goto CleanIdea
if "%task%"=="5" goto CleanEclipse
if "%task%"=="6" goto Build
if "%task%"=="7" goto setupDecompWorkspace
if "%task%"=="8" goto SetupWorkspaceAndSetupIDEA
if "%task%"=="9" goto SetupWorkspaceAndSetupEclipse
if "%task%"=="10" exit
goto home

:SetupWorkspace
gradlew setupDevWorkspace
goto home

:SetupIdeaasIDE
gradlew idea
goto home

:SetupEclipseasIDE
gradlew eclipse
goto home

:CleanIdea
gradlew cleanIdea
goto home

:CleanEclipse
gradlew cleanEclipse
goto home

:Build
gradlew build
goto home

:setupDecompWorkspace
gradlew setupDecompWorkspace
goto home

:SetupWorkspaceAndSetupIDEA
gradlew setupDevWorkspace idea
goto home

:SetupWorkspaceAndSetupEclipse
gradlew setupDevWorkspace Eclipse
goto home

Pause