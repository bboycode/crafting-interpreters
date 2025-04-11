# Compiler and runtime
JAVAC = javac
JAVA = java

# Directories
SRC_DIR = src
BIN_DIR = bin

# Main class (no .class or .java extension)
MAIN_CLASS = src.lox

# All Java source files
SOURCES := $(shell find $(SRC_DIR) -name "*.java")

# Default target: compile everything at once
all:
	@mkdir -p $(BIN_DIR)
	@echo "Compiling all Java files..."
	$(JAVAC) -d $(BIN_DIR) $(SOURCES)

# Run the interpreter in REPL mode
run: all
	@echo "Running Lox (REPL)..."
	$(JAVA) -cp $(BIN_DIR) $(MAIN_CLASS)

# Run with a file: make runfile FILE=test/test.lox
runfile: all
	@echo "Running Lox with file $(FILE)..."
	$(JAVA) -cp $(BIN_DIR) $(MAIN_CLASS) $(FILE)

# Clean compiled class files
clean:
	@echo "Cleaning bin directory..."
	rm -rf $(BIN_DIR)

