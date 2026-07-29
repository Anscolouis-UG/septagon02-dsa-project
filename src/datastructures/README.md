# Custom Data Structures Module

This folder (`src/datastructures`) contains the custom, zero-dependency data structure implementations developed for the **Ghana Smart Service Operations Optimizer** project (**DCIT 204/308**).

---

## 📂 Included Components

* **`CustomDeque.java`**: Doubly-linked double-ended queue.
* **`CustomHeap.java`**: Dynamic array-backed Min-Heap priority queue.
* **`CustomBST.java`**: Binary Search Tree with recursive search, insert, and node deletion.
* **`CustomRedBlackTree.java`**: Self-balancing Red-Black Tree with rotation logging and structural trace tracking.

---

## 🛠 Compilation Instructions

Before running any application, compile all Java source files from the project root directory (`septagon02-dsa-project`):

```bash
javac src/datastructures/*.java
```

---

## 🚀 How to Run Each Data Structure Application

Run each driver application from the project root directory using the `-cp src` classpath flag:

### 1. Run Custom Deque (`CustomDeque.java`)
```bash
javac src/datastructures/*.java
java -cp src datastructures.CustomDeque
```

### 2. Run Custom Min-Heap (`CustomHeap.java`)
```bash
javac src/datastructures/*.java
java -cp src datastructures.CustomHeap
```

### 3. Run Custom Binary Search Tree (`CustomBST.java`)
```bash
javac src/datastructures/*.java
java -cp src datastructures.CustomBST
```

### 4. Run Custom Red-Black Tree (`CustomRedBlackTree.java`)
```bash
javac src/datastructures/*.java
java -cp src datastructures.CustomRedBlackTree
```

---
*Developed by Benjamin (Core Developer 2) — University of Ghana*
