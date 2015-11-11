#!/bin/bash
#
# Performs a clean build and runs Infer (see fbinfer.com).
# The "infer" binary must be in $PATH.

rm -rf infer-out
./gradlew clean
infer -- ./gradlew assembleDebug

