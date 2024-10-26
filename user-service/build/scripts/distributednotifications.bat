@rem
@rem Copyright 2015 the original author or authors.
@rem
@rem Licensed under the Apache License, Version 2.0 (the "License");
@rem you may not use this file except in compliance with the License.
@rem You may obtain a copy of the License at
@rem
@rem      https://www.apache.org/licenses/LICENSE-2.0
@rem
@rem Unless required by applicable law or agreed to in writing, software
@rem distributed under the License is distributed on an "AS IS" BASIS,
@rem WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@rem See the License for the specific language governing permissions and
@rem limitations under the License.
@rem
@rem SPDX-License-Identifier: Apache-2.0
@rem

@if "%DEBUG%"=="" @echo off
@rem ##########################################################################
@rem
@rem  distributednotifications startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%"=="" set DIRNAME=.
@rem This is normally unused
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Resolve any "." and ".." in APP_HOME to make it shorter.
for %%i in ("%APP_HOME%") do set APP_HOME=%%~fi

@rem Add default JVM options here. You can also use JAVA_OPTS and DISTRIBUTEDNOTIFICATIONS_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS=

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if %ERRORLEVEL% equ 0 goto execute

echo. 1>&2
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH. 1>&2
echo. 1>&2
echo Please set the JAVA_HOME variable in your environment to match the 1>&2
echo location of your Java installation. 1>&2

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto execute

echo. 1>&2
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME% 1>&2
echo. 1>&2
echo Please set the JAVA_HOME variable in your environment to match the 1>&2
echo location of your Java installation. 1>&2

goto fail

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\lib\distributednotifications-0.0.1-SNAPSHOT-plain.jar;%APP_HOME%\lib\spring-boot-starter-validation-3.3.5.jar;%APP_HOME%\lib\jersey-container-servlet-core-3.0.0.jar;%APP_HOME%\lib\jersey-hk2-3.0.0.jar;%APP_HOME%\lib\jersey-server-3.1.9.jar;%APP_HOME%\lib\jersey-client-3.1.9.jar;%APP_HOME%\lib\jersey-common-3.1.9.jar;%APP_HOME%\lib\jakarta.ws.rs-api-3.0.0.jar;%APP_HOME%\lib\jakarta.servlet-api-5.0.0.jar;%APP_HOME%\lib\spring-boot-starter-web-3.3.5.jar;%APP_HOME%\lib\spring-boot-starter-json-3.3.5.jar;%APP_HOME%\lib\spring-boot-starter-3.3.5.jar;%APP_HOME%\lib\spring-boot-starter-tomcat-3.3.5.jar;%APP_HOME%\lib\jakarta.annotation-api-2.0.0.jar;%APP_HOME%\lib\spring-kafka-3.2.4.jar;%APP_HOME%\lib\gson-2.8.9.jar;%APP_HOME%\lib\tomcat-embed-el-10.1.31.jar;%APP_HOME%\lib\hibernate-validator-8.0.1.Final.jar;%APP_HOME%\lib\hk2-locator-3.0.0-RC1.jar;%APP_HOME%\lib\hk2-api-3.0.0-RC1.jar;%APP_HOME%\lib\hk2-utils-3.0.0-RC1.jar;%APP_HOME%\lib\jakarta.inject-api-2.0.1.jar;%APP_HOME%\lib\javassist-3.25.0-GA.jar;%APP_HOME%\lib\spring-webmvc-6.1.14.jar;%APP_HOME%\lib\spring-web-6.1.14.jar;%APP_HOME%\lib\spring-boot-autoconfigure-3.3.5.jar;%APP_HOME%\lib\spring-boot-3.3.5.jar;%APP_HOME%\lib\spring-context-6.1.14.jar;%APP_HOME%\lib\spring-messaging-6.1.14.jar;%APP_HOME%\lib\spring-tx-6.1.14.jar;%APP_HOME%\lib\spring-retry-2.0.10.jar;%APP_HOME%\lib\kafka-clients-3.7.1.jar;%APP_HOME%\lib\micrometer-observation-1.13.6.jar;%APP_HOME%\lib\spring-boot-starter-logging-3.3.5.jar;%APP_HOME%\lib\spring-aop-6.1.14.jar;%APP_HOME%\lib\spring-beans-6.1.14.jar;%APP_HOME%\lib\spring-expression-6.1.14.jar;%APP_HOME%\lib\spring-core-6.1.14.jar;%APP_HOME%\lib\snakeyaml-2.2.jar;%APP_HOME%\lib\jakarta.validation-api-3.0.2.jar;%APP_HOME%\lib\jboss-logging-3.5.3.Final.jar;%APP_HOME%\lib\classmate-1.7.0.jar;%APP_HOME%\lib\osgi-resource-locator-1.0.3.jar;%APP_HOME%\lib\aopalliance-repackaged-3.0.0-RC1.jar;%APP_HOME%\lib\jackson-datatype-jsr310-2.17.2.jar;%APP_HOME%\lib\jackson-module-parameter-names-2.17.2.jar;%APP_HOME%\lib\jackson-annotations-2.17.2.jar;%APP_HOME%\lib\jackson-core-2.17.2.jar;%APP_HOME%\lib\jackson-datatype-jdk8-2.17.2.jar;%APP_HOME%\lib\jackson-databind-2.17.2.jar;%APP_HOME%\lib\tomcat-embed-websocket-10.1.31.jar;%APP_HOME%\lib\tomcat-embed-core-10.1.31.jar;%APP_HOME%\lib\zstd-jni-1.5.6-3.jar;%APP_HOME%\lib\lz4-java-1.8.0.jar;%APP_HOME%\lib\snappy-java-1.1.10.5.jar;%APP_HOME%\lib\logback-classic-1.5.11.jar;%APP_HOME%\lib\log4j-to-slf4j-2.23.1.jar;%APP_HOME%\lib\jul-to-slf4j-2.0.16.jar;%APP_HOME%\lib\slf4j-api-2.0.16.jar;%APP_HOME%\lib\micrometer-commons-1.13.6.jar;%APP_HOME%\lib\spring-jcl-6.1.14.jar;%APP_HOME%\lib\logback-core-1.5.11.jar;%APP_HOME%\lib\log4j-api-2.23.1.jar


@rem Execute distributednotifications
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %DISTRIBUTEDNOTIFICATIONS_OPTS%  -classpath "%CLASSPATH%"  %*

:end
@rem End local scope for the variables with windows NT shell
if %ERRORLEVEL% equ 0 goto mainEnd

:fail
rem Set variable DISTRIBUTEDNOTIFICATIONS_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
set EXIT_CODE=%ERRORLEVEL%
if %EXIT_CODE% equ 0 set EXIT_CODE=1
if not ""=="%DISTRIBUTEDNOTIFICATIONS_EXIT_CONSOLE%" exit %EXIT_CODE%
exit /b %EXIT_CODE%

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
