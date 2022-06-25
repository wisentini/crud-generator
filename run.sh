#!/bin/bash

# Remove os arquivos resultantes da execução do projeto
./purge.sh

# Sobe os containers
cd docker
docker compose up -d
cd ../

# Executa o projeto
mvn clean
mvn compile
mvn -q exec:java@main
mvn compile
mvn -q exec:java@application

# Desce os containers
cd docker
docker compose down
