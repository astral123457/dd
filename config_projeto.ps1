# Solicitar o nome do projeto
$projectName = Read-Host "Digite o nome do projeto"

# Criar uma pasta para o projeto
if (-Not (Test-Path -Path $projectName)) {
    New-Item -ItemType Directory -Path $projectName
    Write-Host "Diret�rio $projectName criado com sucesso."
} else {
    Write-Host "Diret�rio $projectName j� existe. Continuando..."
}

# Navegar para o diret�rio do projeto
Set-Location -Path $projectName

# Executar o comando Gradle Init com os par�metros pr�-configurados
Write-Host "Configurando projeto Gradle..."
& gradle init --type java-application --dsl groovy --package "com.$projectName" --test-framework junit-jupiter --project-name $projectName --incubating

Write-Host "Projeto criado com sucesso na pasta $projectName."


$projectName = "app"


# Verificar se o diret�rio j� existe
if (-Not (Test-Path -Path "$projectName")) {
    # Criar a pasta do projeto e o subdiret�rio "app"
    New-Item -ItemType Directory -Path "$projectName" -Force
    Write-Host "Diret�rio $projectName criado com sucesso."
} else {
    Write-Host "Diret�rio $projectName j� existe. Continuando..."
}

# Navegar para o subdiret�rio "app"
Set-Location -Path "$projectName"

# Remover o arquivo build.gradle, se existir
if (Test-Path "build.gradle") {
    Remove-Item "build.gradle"
    Write-Host "Arquivo build.gradle existente foi removido."
}

# Criar o diret�rio src/main/resources
if (-Not (Test-Path -Path "src/main/resources")) {
    New-Item -ItemType Directory -Path "src/main/resources" -Force
    Write-Host "Estrutura de diret�rios src/main/resources criada."
} else {
    Write-Host "Diret�rio src/main/resources j� existe."
}

# Criar o arquivo plugin.yml
$pluginContent = @"
name: $projectName
version: 1.0
main: $projectName.$projectName
api-version: 1.19
author: Amauri
description: Este � um plugin personalizado para Minecraft.
"@
$pluginContent | Out-File -FilePath "src/main/resources/plugin.yml" -Encoding UTF8
Write-Host "Arquivo plugin.yml criado com sucesso."

# Criar o arquivo build.gradle
$buildGradleContent = @"
plugins {
    id 'java'
}

repositories {
    mavenCentral()
    maven { url 'https://repo.papermc.io/repository/maven-public/' }
}

dependencies {
    testImplementation 'org.junit.jupiter:junit-jupiter:5.9.1'
    compileOnly 'io.papermc.paper:paper-api:1.20-R0.1-SNAPSHOT'
    implementation 'com.google.guava:guava:31.1-jre'
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

tasks.withType(JavaCompile) {
    options.encoding = 'UTF-8'
    options.compilerArgs << "-Xlint:deprecation"
}

tasks.named('jar') {
    archiveBaseName.set('$projectName') // Nome personalizado
    archiveVersion.set('1.33') // Vers�o opcional
}
"@
$buildGradleContent | Out-File -FilePath "build.gradle" -Encoding UTF8
Write-Host "Arquivo build.gradle criado com sucesso."

# Mensagem final
Write-Host "Projeto $projectName configurado com sucesso!"
pause