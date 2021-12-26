#!/usr/bin/env sh
BASEDIR=$(dirname "$0")
cd "$BASEDIR" || exit 1

if ! [ -f "BuildTools.jar" ] || ! zip -T "BuildTools.jar" 1>/dev/null 2>&1; then
  echo "Going to download"
  rm "BuildTools.jar" 2>/dev/null
  wget https://hub.spigotmc.org/jenkins/job/BuildTools/lastSuccessfulBuild/artifact/target/BuildTools.jar || exit 1
else
  echo "Already downloaded"
fi