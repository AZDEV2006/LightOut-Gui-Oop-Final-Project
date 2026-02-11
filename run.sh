#!/bin/bash

echo "==============================="
echo "⚡ LightsOutGame Build Script ⚡"
echo "==============================="

SRC_DIR="src"
BUILD_DIR="build"
MAIN_CLASS="src.Main"

if [ -d "$BUILD_DIR" ]; then
    echo "Cleaning old build..."
    rm -rf "$BUILD_DIR"
fi

mkdir -p "$BUILD_DIR"

echo "Compiling source files..."
javac -d "$BUILD_DIR" $(find "$SRC_DIR" -name "*.java")

if [ $? -ne 0 ]; then
    echo "Compilation failed!"
    exit 1
fi

echo "Compilation successful!"

echo "Launching LightsOutGame..."
java -cp "$BUILD_DIR" "$MAIN_CLASS"

if [ $? -ne 0 ]; then
    echo "Application crashed!"
    exit 1
fi
