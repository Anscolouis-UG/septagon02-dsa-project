# Septagon02 DSA Project Guide

Welcome to the **Ghana Smart Service Operations (DSA Project)** repository! This document serves as the central guide for team members to understand the architecture, data sources, backend implementations, and the frontend GUI.

---

## 1. Project Overview

This project models smart service operations for the **University of Ghana, Legon Campus**. It demonstrates the practical application of Data Structures and Algorithms (DSA) through a custom-built, dependency-free Java backend and an interactive web-based graphical user interface (GUI).

The project is built entirely from scratch, utilizing **zero built-in Java collections** (`java.util.*` is strictly avoided). Every data structure, from arrays to red-black trees, is custom-implemented to fulfill the course requirements.

---

## 2. Architecture & Components

The repository is structured into four main layers:

### Layer 1: Data & Database (`data_base/`)
The system is powered by real-world campus data extracted from OpenStreetMap.
* `nodes.csv`: 320+ campus locations with real GPS coordinates, categorized into academic, residence, and services.
* `edges_knn_sparse.csv`: Road networks and pedestrian paths connecting the nodes, including distances in meters.
* `service_requests.csv`: Generated dataset of campus service requests (maintenance, delivery, waste, security, grounds).
* `resources.csv`: Inventory of campus resources (personnel, vehicles, equipment) with capacities and hourly costs.
* **SQLite Database (`george.db`)**: `CSVLoader.java` parses the CSV files and seeds a relational SQLite database.

### Layer 2: Custom Data Structures (`src/datastructures/`)
14 custom structures form the backbone of the project:
* **Linear**: `CustomArrayList`, `CustomLinkedList`, `CustomStack` (for DFS), `CustomQueue` (for BFS), `DispatchDeque`.
* **Trees**: `ResourceBST`, `CustomRedBlackTree` (for location indexing), `BTree` (database index structure).
* **Heaps & Sets**: `CustomHeap` (priority queue), `CustomDisjointSet` (Union-Find for Kruskal's), `CustomSet`.
* **Maps & Hash Tables**: `HashTable` (for O(1) lookups), `CustomMap`.
* **Graphs**: `CustomGraph` (Adjacency-list based on the UG Campus network).

### Layer 3: Algorithms (`src/algorithms/`)
* **Graph Traversals**: Breadth-First Search (BFS) and Depth-First Search (DFS).
* **Shortest Path**: Dijkstra's Algorithm (routing across campus roads).
* **Minimum Spanning Tree (MST)**: Kruskal's Algorithm and Prim's Algorithm.
* **Searching & Sorting**: Linear/Binary search, and sorting via Bubble, Selection, Insertion, Merge, and Quick Sort.
* **Dynamic Programming (DP)**: A knapsack-based Budget Allocator to maximize service priority fulfillment within budget constraints.

### Layer 4: Frontend GUI (`gui/`)
* `gui/index.html`: A completely static, highly interactive Single Page Application (SPA) built with HTML, CSS, and Vanilla JS.
* It visualizes the campus graph, animates BFS/DFS traversals, draws Dijkstra's shortest paths, renders MSTs, and features an interactive DP budget allocator dashboard.

---

## 3. How to Run the Project

### Running the Java Console Demos
The project includes several interactive console interfaces. From the project root directory, run the comprehensive bash script to execute the full demo suite:
```bash
# Compiles all files and runs the database, structures, graph, and DP demos in sequence
bash ./run_all_demos.sh
```

Alternatively, you can compile and run specific modules manually:
```bash
# 1. Compile all Java files
javac -d out $(find src -name "*.java")

# 2. Run the Interactive Searching & Sorting Menu
java -cp out algorithms.UserInterface

# 3. Seed the SQLite Database from CSVs
java -cp "out:lib/sqlite-jdbc.jar" CSVLoader

# 4. Run the interactive Database Query Demo
java -cp "out:lib/sqlite-jdbc.jar" StructureDemo
```

### Running the Graphical User Interface (GUI)
Since the GUI is a static frontend with no build step, you can simply open it in your browser. On a Mac, you can run:
```bash
open gui/index.html
```

### Running Performance Benchmarks
We use a Python harness to scale-test the Java algorithms and plot performance graphs:
```bash
cd src/Performance
pip install -r requirements.txt
python run_experiments.py
```

---

## 4. Team Workflow & Guidelines

To ensure a smooth collaboration process, all team members must adhere to the following workflow:

1. **Branching Strategy**
   - **DO NOT push directly to `main`**.
   - Create a new branch for every feature or bug fix: `git checkout -b feature/your-feature-name`.
   - Keep branches short-lived and focused on a single task.

2. **Commit Messages**
   - Use clear, descriptive commit messages (e.g., `Add Dijkstra shortest path algorithm` rather than `update code`).

3. **Pull Requests (PRs)**
   - Open a Pull Request against `main` for review.
   - If your PR addresses an existing issue, include `Closes #<issue_number>` in the description.
   - Ensure your code compiles and all existing demos/tests pass before requesting a review.

4. **Code Constraints**
   - **Zero Java Utils**: Remember, you cannot import anything from `java.util.*` (except `Scanner` for UI inputs). If you need a List or a Map, use our custom `CustomArrayList` or `CustomMap` implementations!

Happy Coding!
