#!/bin/bash
# Script pour lancer le GamePanel Demo

cd "$(dirname "$0")"
mvn compile && \
mvn exec:java -Dexec.mainClass="com.hulefevr.swingy.view.gui.screens.GamePanelDemo"
