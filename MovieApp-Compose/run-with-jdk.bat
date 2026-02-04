@echo off
rem run-with-jdk.bat - tries common JDK locations (17/21/JBR) and runs gradlew with that JAVA_HOME
rem Usage: run-with-jdk.bat <gradle-args...>
setlocal enabledelayedexpansion

if "%~1"=="" (
  echo Usage: %~nx0 <gradle-args>
  echo Example: %~nx0 :app:kaptGenerateStubsDebugKotlin --stacktrace --no-daemon
  exit /b 1
)

rem Candidate JDK directories to try (order: prefer Java 17/21, then Android Studio JBR)
set "CAND0=%ProgramFiles%\Java\jdk-17"
set "CAND1=%ProgramFiles%\Java\jdk-17.0.8"
set "CAND2=%ProgramFiles%\Java\jdk-21"
set "CAND3=%ProgramFiles%\Java\jdk-21.0.0"
set "CAND4=%ProgramFiles%\Eclipse Adoptium\jdk-17"
set "CAND5=%ProgramFiles%\AdoptOpenJDK\jdk-17"
set "CAND6=%ProgramFiles%\Amazon Corretto\jdk-17"
set "CAND7=%ProgramFiles%\Android\Android Studio\jbr"
set "CAND8=%ProgramFiles%\Android\Android Studio\jbr\jre"
set "CAND9=%USERPROFILE%\.jdks\corretto-17.0.8"

echo Looking for a compatible JDK (Java 17 or 21 recommended)...
set "FOUND="

rem Loop through candidates
for /L %%i in (0,1,9) do (
  call set "P=%%CAND%%i%%"
  if defined P (
    if exist "!P!\bin\java.exe" (
      rem check version quickly and capture first output line
      for /f "usebackq tokens=* delims=" %%v in (`""!P!\bin\java.exe" -version 2^>^&1"`) do (
        set "VERLINE=%%v"
        goto got_verline
      )
      :got_verline
      echo !VERLINE! | findstr /r /c:"\"17\"" >nul
      if !errorlevel! equ 0 (
        set "FOUND=!P!"
        goto use_found
      )
      echo !VERLINE! | findstr /r /c:"\"21\"" >nul
      if !errorlevel! equ 0 (
        set "FOUND=!P!"
        goto use_found
      )
      rem accept as last resort if nothing matches exactly
      set "FOUND=!P!"
      goto use_found
    )
  )
)

rem Check existing JAVA_HOME as fallback
if not defined FOUND if defined JAVA_HOME (
  if exist "%JAVA_HOME%\bin\java.exe" (
    for /f "usebackq tokens=* delims=" %%v in (`""%JAVA_HOME%\bin\java.exe" -version 2^>^&1"`) do (
      set "VERLINE=%%v"
      goto got_verline2
    )
    :got_verline2
    echo %VERLINE% | findstr /r /c:"\"17\"" >nul
    if %errorlevel% equ 0 set "FOUND=%JAVA_HOME%"
    echo %VERLINE% | findstr /r /c:"\"21\"" >nul
    if %errorlevel% equ 0 set "FOUND=%JAVA_HOME%"
  )
)

:use_found
if defined FOUND (
  echo Using JAVA_HOME=%FOUND%
  set "JAVA_HOME=%FOUND%"
  set "PATH=%JAVA_HOME%\bin;%PATH%"
  rem Show actual java -version we will use
  "%JAVA_HOME%\bin\java.exe" -version
  echo.
  echo Running: .\gradlew.bat %*
  .\gradlew.bat %*
  endlocal
  exit /b %ERRORLEVEL%
) else (
  echo Could not find a suitable JDK in common locations.
  echo Please install JDK 17/21 or set JAVA_HOME to a JDK 17/21 installation and retry.
  echo Example (cmd.exe): set "JAVA_HOME=C:\\Program Files\\Java\\jdk-17.0.8" && .\\gradlew.bat %*
  exit /b 2
)
