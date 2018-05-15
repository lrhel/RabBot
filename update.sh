#!/bin/sh

git pull
mvn clean install
java -cp target/RabBot-0.0.1-jar-with-dependencies.jar com.github.lrhel.rabbot.Main
