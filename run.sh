#!/bin/bash

if [[ $# != 3 && $# != 4 ]]; then
    echo "Usage: ./run.sh BoardXML CardXML numberOfTotalPlayers numberOfComputerPlayers"
    exit 1
fi

java -jar deadwood.jar $1 $2 $3 $4
