# Performs a clean build and runs Infer (see fbinfer.com). 
# The "infer" binary must be in $PATH.

./gradlew clean
infer -- ./gradlew assembleDebug

