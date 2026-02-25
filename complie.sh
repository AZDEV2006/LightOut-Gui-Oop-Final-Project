#!/bin/bash

echo "==============================="
echo "Compiling Light Out"
echo "==============================="

SRC_DIR="src"
BUILD_DIR="build"
RELEASE_DIR="release"
JAR_FILE="LightOut.jar"
MAIN_CLASS="src.Main"

if [ -d "$BUILD_DIR" ]; then
    echo "Cleaning old build..."
    rm -rf "$BUILD_DIR"
fi

mkdir -p "$BUILD_DIR"
mkdir -p "$RELEASE_DIR"

echo "Compiling source files..."
javac -d "$BUILD_DIR" $(find "$SRC_DIR" -name "*.java")

if [ $? -ne 0 ]; then
    echo "Compilation failed!"
    exit 1
fi

echo "Compilation successful!"

echo "Copying assets..."
mkdir -p "$BUILD_DIR/src/assets"
cp -r "$SRC_DIR/assets/sounds" "$BUILD_DIR/src/assets/sounds"
cp -r "$SRC_DIR/assets/"*.png "$BUILD_DIR/src/assets/"

echo "Creating JAR file..."
echo "Main-Class: $MAIN_CLASS" > "$BUILD_DIR/manifest.txt"
jar cfm "$RELEASE_DIR/$JAR_FILE" "$BUILD_DIR/manifest.txt" -C "$BUILD_DIR" .

if [ $? -ne 0 ]; then
    echo "JAR creation failed!"
    exit 1
fi

echo "==============================="
echo "JAR created: $RELEASE_DIR/$JAR_FILE"
echo "Run with: java -jar $RELEASE_DIR/$JAR_FILE"
echo "==============================="