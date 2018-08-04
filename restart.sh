#!/bin/sh

pkill java
java -cp target/RabBot-0.0.1.jar com.github.lrhel.rabbot.Main >> rabbot.log
