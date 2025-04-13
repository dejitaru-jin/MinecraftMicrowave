# Minecraft Forge Mod Makefile
# Provides simple wrappers around Gradle commands

# Default task when just running 'make'
.PHONY: all
all: build

# Clean the build directory
.PHONY: clean
clean:
	./gradlew clean

# Build the mod
.PHONY: build
build:
	./gradlew build

# Run client for testing
.PHONY: run
run:
	./gradlew runClient

# Run server for testing
.PHONY: run-server
run-server:
	./gradlew runServer

# Compile the mod
.PHONY: compile
compile:
	./gradlew compileJava

# Generate IDE files
.PHONY: setup-ide
setup-ide:
	./gradlew eclipse

# Generate runnable client environment
.PHONY: setup-game
setup-game:
	./gradlew genEclipseRuns

# Create mod jar file
.PHONY: jar
jar:
	./gradlew jar

# Clean and build from scratch
.PHONY: rebuild
rebuild: clean build

# Output project dependencies
.PHONY: deps
deps:
	./gradlew dependencies

# Check for updates to dependencies
.PHONY: check-updates
check-updates:
	./gradlew dependencyUpdates

# Help command
.PHONY: help
help:
	@echo "Minecraft Forge Mod Makefile"
	@echo "Available commands:"
	@echo "  make              - Build the mod (same as 'make build')"
	@echo "  make clean        - Clean build directories"
	@echo "  make build        - Build the mod"
	@echo "  make run          - Run the client for testing"
	@echo "  make run-server   - Run the server for testing"
	@echo "  make compile      - Compile Java code only"
	@echo "  make setup-ide    - Generate Eclipse project files"
	@echo "  make setup-game   - Generate runnable game environment"
	@echo "  make jar          - Create JAR file only"
	@echo "  make rebuild      - Clean and build from scratch"
	@echo "  make deps         - Show project dependencies"
	@echo "  make check-updates - Check for dependency updates"
