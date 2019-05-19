#!/usr/bin/env bash
rm -rf src/main/resources/static/*
cd ./web

npm run build

cd ../

sh ./gradlew build

docker rmi project1_web

docker-compose up

