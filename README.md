# Septagon DSA Project

This project contains simple Java implementations of common data structures. The structures were written from scratch to show how they work internally, instead of using Java's built-in collection classes.

## What is in the project?

The code is in `src/structures`.

- `CustomArrayList.java` stores items in an array. When the array becomes full, it creates a larger array and copies the items into it. It supports adding, getting, inserting, changing, and removing items.
- `CustomLinkedList.java` stores items as linked nodes. Each node points to the next one. It can add items at the beginning or end, insert after an item, remove an item, and get an item by index.
- `CustomQueue.java` uses an array as a circular queue. Items are added with `enqueue()` and removed in the same order with `dequeue()`.
- `CustomStack.java` uses the linked list to make a stack. Items are added with `push()` and removed from the top with `pop()`.

There is also a small example in `RunDemo.java`. It creates a `CustomArrayList`, adds values, inserts a value, and removes it.

## How to run the demo

You need Java 11 or newer installed on your computer.

1. Open the project folder in File Explorer.
2. Click the address bar at the top of File Explorer, type `powershell`, and press **Enter**. A blue PowerShell window will open in the project folder.
3. Copy and paste this command into PowerShell, then press **Enter**:

```powershell
javac -d out src\structures\CustomArrayList.java src\structures\CustomLinkedList.java src\structures\CustomQueue.java src\structures\CustomStack.java src\RunDemo.java
```

This compiles the Java files and places the compiled files in the `out` folder.

4. When the first command finishes, copy and paste this command, then press **Enter**:

```powershell
java -cp out RunDemo
```

This starts the example program in `RunDemo.java`.

You should see output similar to this:

```text
size=3
get(1)=20
after insert=25
removed=25
final size=3
```

## How to run the tests

The project uses Maven and JUnit 5 for its tests.

1. Open PowerShell in the project folder using steps 1 and 2 above.
2. Type the command below and press **Enter**:

```powershell
mvn test
```

The test files are in the `tests` folder.

## Project folders

```text
src/        Java source code
  RunDemo.java       Example program
  structures/        Custom data-structure classes
tests/      JUnit test files
out/        Compiled classes created when the demo is run
pom.xml     Maven project settings (must stay in the project folder)
```
