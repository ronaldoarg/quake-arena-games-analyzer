#!/bin/bash -e

if [ "$1" = "" ]; then
    echo "Favor informar o caminho para o arquivo de logs"
    exit -1
fi

echo #
echo ------------ Checking requirements -----------------
echo #
echo ----------------- Step 1/5 ------------------------
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
echo ----------------- step 2/5 ------------------------
echo #

# cd api && mvn clean package
# cd ..

echo #
echo ----------------- Executing API ---------------------
echo #
echo ----------------- step 3/5 ------------------------
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
echo ----------------- step 4/5 -----------------------
echo #

cd parser && mvn clean package
cd ..

echo #
echo ------------- Executing Parser ----------------------
echo #
echo ----------------- step 5/5 -----------------------
echo #

java -jar parser/target/games-parser-*.jar $1
