#!/bin/bash

scriptdir=`dirname /Users/nbowen/src/midimachine/run-ogw.sh`
outputfile=/dev/null

if [[ $# == 2 ]]; then
    if echo "$2" | grep '^/'; then
        outputfile=$2
    else
        startdir=`pwd`
        outputfile=${startdir}/$2
    fi

    echo "Output file is ${outputfile}."
fi

pushd $scriptdir
mvn exec:java -Dexec.mainClass=com.daveclay.midi.ogws.$1 -Dexec.args=$outputfile
popd
