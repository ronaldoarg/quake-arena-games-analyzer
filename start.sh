#!/bin/bash -e

if [ "$1" = "" ]; then
    echo "Favor informar o caminho para o arquivo de logs"
    exit -1
fi

echo #
echo ------------ Checking requirements -----------------
echo #
echo ----------------- Step 1/6 ------------------------
echo #

java -version

echo ----------------------------------------------------

mvn -v

echo ----------------------------------------------------

docker -v

echo ----------------------------------------------------

echo #
echo ----------------- Building API ---------------------
echo #
echo ----------------- step 2/6 ------------------------
echo #

cd api && mvn clean package
cd ..

echo #
echo ----------------- Executing API ---------------------
echo #
echo ----------------- step 3/6 ------------------------
echo #

containers=$(docker ps -q -f ancestor=luizalabs-desafio/api)

if [[ ! -z "$containers" ]]; then 
    docker stop $containers
fi

cd api
docker build -f Dockerfile -t luizalabs-desafio/api .
docker run -p 8080:8080 -d luizalabs-desafio/api
cd ..

echo #
echo ------------- Building Parser ---------------------
echo #
echo ----------------- step 4/6 -----------------------
echo #

cd parser && mvn clean package
cd ..

echo #
echo ------------- Executing Parser ----------------------
echo #
echo ----------------- step 5/6 -----------------------
echo #

java -jar parser/target/games-parser-*.jar $1

echo #
echo ------------- Executing Web ----------------------
echo #
echo ----------------- step 6/6 -----------------------
echo #

containers=$(docker ps -q -f ancestor=luizalabs-desafio/web)

if [[ ! -z "$containers" ]]; then 
    docker stop $containers
fi

cd web
docker build -f Dockerfile -t luizalabs-desafio/web .
docker run -p 4200:8080 -d luizalabs-desafio/web
cd ..

echo #
echo ----------- Finished with success --------------
echo #
