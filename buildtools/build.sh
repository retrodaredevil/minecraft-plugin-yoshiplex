#!/usr/bin/env sh
BASEDIR=$(dirname "$0")
cd "$BASEDIR" || exit 1

./download.sh || exit 1

cd builddata || exit 1
# You need to use Java 8: "sdk install java 8.0.292.j9-adpt" may help
# sdk use java 8.0.292.j9-adpt
java -jar ../BuildTools.jar --rev 1.9.4 || exit 1

echo Success!
