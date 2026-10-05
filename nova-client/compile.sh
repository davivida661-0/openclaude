#!/bin/bash

# =============================================================================
# Nova Client - Script de Compilação Local
# =============================================================================
# Este script compila o Nova Client localmente sem depender do GitHub Actions
# Requer: Java 8, Maven, wget
# =============================================================================

set -e  # Sair em caso de erro

echo "=========================================="
echo " Nova Client - Compilation Script"
echo "=========================================="
echo ""

# Cores para o terminal
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Função para mostrar erros
function error_exit() {
    echo -e "${RED}[ERROR]${NC} $1"
    exit 1
}

# Função para mostrar sucesso
echo_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

# Função para mostrar informações
echo_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

# Função para mostrar avisos
echo_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

# Verifica se está no diretório correto
if [ ! -f "pom.xml" ]; then
    error_exit "Execute este script a partir do diretório nova-client/"
fi

echo_info "Verificando dependências..."

# Verifica se o Java 8 está instalado
if ! command -v java &> /dev/null; then
    error_exit "Java não encontrado. Instale o Java 8: sudo apt install openjdk-8-jdk"
fi

JAVA_VERSION=$(java -version 2>&1 | head -1 | cut -d'"' -f2)
echo_info "Java version: $JAVA_VERSION"

# Verifica a versão do Java
if [[ ! "$JAVA_VERSION" == *"1.8"* ]]; then
    echo_warning "Recomendado: Java 8. Você está usando: $JAVA_VERSION"
    read -p "Continuar mesmo assim? (s/n): " -n 1 -r
    echo
    if [[ ! $REPLY =~ ^[Ss]$ ]]; then
        error_exit "Java 8 é necessário para Minecraft 1.8.9"
    fi
fi

# Verifica se o Maven está instalado
if ! command -v mvn &> /dev/null; then
    error_exit "Maven não encontrado. Instale: sudo apt install maven"
fi

MVN_VERSION=$(mvn -version | head -1)
echo_info "Maven version: $MVN_VERSION"

# Verifica se o wget está instalado
if ! command -v wget &> /dev/null; then
    error_exit "wget não encontrado. Instale: sudo apt install wget"
fi

echo ""
echo_info "Baixando dependências do Minecraft 1.8.9..."

# Cria pasta para JARs
mkdir -p ~/.m2/repository/com/mojang/minecraft/1.8.9
mkdir -p ~/.m2/repository/org/lwjgl/lwjgl/2.9.4
mkdir -p ~/.m2/repository/com/google/code/gson/2.8.9
mkdir -p ~/.m2/repository/org/spongepowered/mixin/0.8.5

# Baixa o JAR do Minecraft 1.8.9
echo_info "Baixando Minecraft 1.8.9..."
if [ ! -f ~/.m2/repository/com/mojang/minecraft/1.8.9/minecraft-1.8.9-client.jar ]; then
    wget -q -O ~/.m2/repository/com/mojang/minecraft/1.8.9/minecraft-1.8.9-client.jar \
        https://libraries.minecraft.net/com/mojang/minecraft/1.8.9/minecraft-1.8.9-client.jar || \
        error_exit "Falha ao baixar Minecraft 1.8.9 JAR"
fi

# Baixa o LWJGL
echo_info "Baixando LWJGL 2.9.4..."
if [ ! -f ~/.m2/repository/org/lwjgl/lwjgl/2.9.4/lwjgl-2.9.4.jar ]; then
    wget -q -O ~/.m2/repository/org/lwjgl/lwjgl/2.9.4/lwjgl-2.9.4.jar \
        https://build.lwjgl.org/release/lwjgl/2.9.4/lwjgl-2.9.4.jar || \
        error_exit "Falha ao baixar LWJGL JAR"
fi

if [ ! -f ~/.m2/repository/org/lwjgl/lwjgl/2.9.4/lwjgl_util-2.9.4.jar ]; then
    wget -q -O ~/.m2/repository/org/lwjgl/lwjgl/2.9.4/lwjgl_util-2.9.4.jar \
        https://build.lwjgl.org/release/lwjgl/2.9.4/lwjgl_util-2.9.4.jar || \
        error_exit "Falha ao baixar LWJGL Util JAR"
fi

# Baixa o GSON
echo_info "Baixando GSON 2.8.9..."
if [ ! -f ~/.m2/repository/com/google/code/gson/2.8.9/gson-2.8.9.jar ]; then
    wget -q -O ~/.m2/repository/com/google/code/gson/2.8.9/gson-2.8.9.jar \
        https://repo1.maven.org/maven2/com/google/code/gson/gson/2.8.9/gson-2.8.9.jar || \
        error_exit "Falha ao baixar GSON JAR"
fi

# Baixa o Mixins
echo_info "Baixando Mixins 0.8.5..."
if [ ! -f ~/.m2/repository/org/spongepowered/mixin/0.8.5/mixin-0.8.5.jar ]; then
    wget -q -O ~/.m2/repository/org/spongepowered/mixin/0.8.5/mixin-0.8.5.jar \
        https://repo.spongepowered.org/repository/maven-public/org/spongepowered/mixin/0.8.5/mixin-0.8.5.jar || \
        error_exit "Falha ao baixar Mixins JAR"
fi

echo ""
echo_info "Instalando JARs no repositório local do Maven..."

# Instala os JARs no repositório local
mvn install:install-file -q \
    -Dfile=~/.m2/repository/com/mojang/minecraft/1.8.9/minecraft-1.8.9-client.jar \
    -DgroupId=com.mojang \
    -DartifactId=minecraft \
    -Dversion=1.8.9 \
    -Dpackaging=jar || \
    error_exit "Falha ao instalar Minecraft no Maven"

mvn install:install-file -q \
    -Dfile=~/.m2/repository/org/lwjgl/lwjgl/2.9.4/lwjgl-2.9.4.jar \
    -DgroupId=org.lwjgl.lwjgl \
    -DartifactId=lwjgl \
    -Dversion=2.9.4 \
    -Dpackaging=jar || \
    error_exit "Falha ao instalar LWJGL no Maven"

mvn install:install-file -q \
    -Dfile=~/.m2/repository/org/lwjgl/lwjgl/2.9.4/lwjgl_util-2.9.4.jar \
    -DgroupId=org.lwjgl.lwjgl \
    -DartifactId=lwjgl_util \
    -Dversion=2.9.4 \
    -Dpackaging=jar || \
    error_exit "Falha ao instalar LWJGL Util no Maven"

mvn install:install-file -q \
    -Dfile=~/.m2/repository/com/google/code/gson/2.8.9/gson-2.8.9.jar \
    -DgroupId=com.google.code.gson \
    -DartifactId=gson \
    -Dversion=2.8.9 \
    -Dpackaging=jar || \
    error_exit "Falha ao instalar GSON no Maven"

mvn install:install-file -q \
    -Dfile=~/.m2/repository/org/spongepowered/mixin/0.8.5/mixin-0.8.5.jar \
    -DgroupId=org.spongepowered \
    -DartifactId=mixin \
    -Dversion=0.8.5 \
    -Dpackaging=jar || \
    error_exit "Falha ao instalar Mixins no Maven"

echo ""
echo_info "Compilando o Nova Client..."

# Compila o projeto
mvn clean compile -q

if [ $? -eq 0 ]; then
    echo_success "Compilação bem-sucedida!"
    echo ""
    echo_info "Gerando JAR..."
    mvn package -DskipTests -q
    
    if [ $? -eq 0 ]; then
        echo_success "JAR gerado com sucesso!"
        echo ""
        echo_info "=========================================="
        echo " JAR gerado em: target/nova-client-1.0.0.jar"
        echo "=========================================="
        echo ""
        echo_info "Para executar:"
        echo "  java -jar target/nova-client-1.0.0.jar"
        echo ""
    else
        error_exit "Falha ao gerar JAR"
    fi
else
    error_exit "Falha na compilação"
fi
