# Septagon02 DSA Project

> **Data Structures and Algorithms** group assignment — DCIT 204, University of Ghana, Legon  
> Team Septagon

A complete, zero-dependency Java implementation of 14 custom data structures and 10+ algorithms, applied to a **Ghana Smart Campus Operations Optimizer** for the University of Ghana, Legon Campus. The system models real campus locations, road networks, service requests, and resource management — all backed by custom-built data structures (no `java.util` collections).
🌐 **[Live GUI Dashboard →](https://septagon02-campus-optimizer.netlify.app/)**

📖 **[Full Technical Report →](TECHNICAL_REPORT.md)** · [PDF Version](DSA_Technical_Report_TEAM-SEPTAGON02.pdf) · [Performance Report](PERFORMANCE_REPORT.md)

---

## Table of Contents

- [Live Demo](#live-demo)
- [Documentation](#documentation)
- [Features at a Glance](#features-at-a-glance)
- [Project Structure](#project-structure)
- [Requirements](#requirements)
- [Quick Start](#quick-start)
- [Data Structures (14)](#data-structures-14)
- [Algorithms (10+)](#algorithms-10)
- [Database Integration](#database-integration)
- [GUI Dashboard](#gui-dashboard)
- [Testing](#testing)
- [Performance Analysis](#performance-analysis)
- [Dataset](#dataset)
- [Team Members](#team-members)
- [Team Workflow](#team-workflow)

---

## Live Demo

The interactive GUI dashboard is deployed on Netlify and visualises every data structure and algorithm running on real UG Legon Campus data:

🔗 **[https://septagon02-campus-optimizer.netlify.app/](https://septagon02-campus-optimizer.netlify.app/)**

Features include:
- Campus graph visualization with real geographic coordinates
- Live BFS/DFS traversal animations
- Dijkstra shortest-path finder between campus buildings
- Kruskal & Prim MST visualization
- Search & sort algorithm comparisons
- Database CRUD operations dashboard
- Dynamic Programming budget optimizer

---

## Documentation

| Document | Format | Description |
|----------|--------|-------------|
| **[Technical Report](TECHNICAL_REPORT.md)** | Markdown | Full 20-page technical report — readable directly on GitHub |
| **[Technical Report (PDF)](DSA_Technical_Report_TEAM-SEPTAGON02.pdf)** | PDF | Original formatted report with charts and figures |
| **[Performance Report](PERFORMANCE_REPORT.md)** | Markdown | Benchmark analysis & scalability charts — readable directly on GitHub |
| **[Performance Report (DOCX)](src/Performance/Priscilla_Performance_Report.docx)** | DOCX | Original benchmark document |

---

## Features at a Glance

| Module | What it does |
|--------|-------------|
| **14 Data Structures** | All built from scratch — no `java.util` collections |
| **Graph Algorithms** | BFS, DFS, Dijkstra, Kruskal, Prim on the campus road network |
| **Search & Sort** | Linear, Binary, Bubble, Selection, Insertion, Merge, Quick Sort |
| **Dynamic Programming** | Knapsack-based budget allocation optimizer |
| **SQLite Database** | CSV → SQLite pipeline with CRUD, audit logging, and export |
| **16 JUnit 5 Tests** | Full test coverage for every structure and algorithm |
| **Performance Benchmarks** | Python-based experiments with scalability analysis |
| **Interactive GUI** | Single-page dashboard deployed on Netlify |

---

## Project Structure

```
septagon02-dsa-project/
├── src/
│   ├── datastructures/          # 14 custom data structure implementations
│   │   ├── CustomArrayList.java
│   │   ├── CustomLinkedList.java
│   │   ├── CustomStack.java
│   │   ├── CustomQueue.java
│   │   ├── CustomSet.java
│   │   ├── CustomMap.java
│   │   ├── CustomDisjointSet.java
│   │   ├── CustomGraph.java
│   │   ├── CustomHeap.java
│   │   ├── CustomRedBlackTree.java
│   │   ├── DispatchDeque.java
│   │   ├── ResourceBST.java
│   │   ├── HashTable.java
│   │   └── BTree.java
│   ├── algorithms/              # Graph, search, sort & DP algorithms
│   │   ├── BFS.java
│   │   ├── DFS.java
│   │   ├── Dijkstra.java
│   │   ├── Kruskal.java
│   │   ├── Prim.java
│   │   ├── SearchAlgorithms.java
│   │   ├── SortAlgorithms.java
│   │   ├── SortingAlgorithms.java
│   │   ├── DynamicProgramming.java
│   │   ├── GraphLoader.java
│   │   ├── GraphEdge.java
│   │   ├── SearchSortDemo.java
│   │   ├── UserInterface.java
│   │   └── Main.java
│   ├── Database.java            # SQLite connection manager
│   ├── CSVLoader.java           # CSV → SQLite import pipeline
│   ├── CSVExporter.java         # SQLite → CSV export
│   ├── CRUDOperations.java      # Create, Read, Update, Delete operations
│   ├── AuditLogger.java         # Timestamped audit trail
│   ├── StructureDemo.java       # Full integration demo
│   ├── RunDemo.java             # Quick demo runner
│   ├── Location.java            # Domain model
│   ├── Resource.java            # Domain model
│   ├── ServiceRequest.java      # Domain model
│   └── Performance/             # Benchmark experiments
│       ├── generate_data.py
│       ├── run_experiments.py
│       ├── structures.py
│       ├── requirements.txt
│       └── Priscilla_Performance_Report.docx
├── tests/                       # 16 JUnit 5 test files
│   ├── CustomArrayListTest.java
│   ├── CustomLinkedListTest.java
│   ├── CustomStackTest.java
│   ├── CustomQueueTest.java
│   ├── CustomSetTest.java
│   ├── CustomMapTest.java
│   ├── CustomDisjointSetTest.java
│   ├── CustomGraphTest.java
│   ├── HashTableTest.java
│   ├── BTreeTest.java
│   ├── BFSTest.java
│   ├── DFSTest.java
│   ├── DijkstraTest.java
│   ├── KruskalTest.java
│   ├── PrimTest.java
│   └── DynamicProgrammingTest.java
├── data_base/                   # Dataset & schema
│   ├── schema.sql               # SQLite DDL
│   ├── locations.csv            # 30 UG Legon campus buildings
│   ├── roads.csv                # Road network with distances
│   ├── resources.csv            # Campus resources & equipment
│   ├── service_requests.csv     # Service request records
│   ├── nodes.csv                # Graph node definitions
│   ├── edges_knn_sparse.csv     # Sparse edge set (k-NN)
│   └── edges_full_pairwise.csv  # Full pairwise edge set
├── gui/
│   └── index.html               # Interactive dashboard (deployed)
├── run_all_demos.sh             # One-command full demo runner
├── pom.xml                      # Maven build configuration
├── netlify.toml                 # Netlify deployment config
└── README.md
```

---

## Requirements

- **Java Development Kit (JDK) 11** or later
- **SQLite JDBC driver** (for database demos only — auto-downloaded by the demo script)
- **Python 3.8+** (for performance experiments only)
- **Maven** (optional — for running JUnit tests via `mvn test`)

Verify Java is available:

```bash
java -version
javac -version
```

---

## Quick Start

### Option 1: Run all demos with one command

```bash
# Download the SQLite JDBC driver (one-time setup)
mkdir -p lib
curl -L -o lib/sqlite-jdbc.jar \
  https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.46.1.3/sqlite-jdbc-3.46.1.3.jar

# Run everything
zsh run_all_demos.sh
```

This runs **5 parts** sequentially:
| Part | What it runs |
|------|-------------|
| **A** | Database integration (CSVLoader → SQLite → BTree + HashTable queries) |
| **B** | Search & Sort algorithms on real CSV data |
| **C** | Data structure standalone demos (Deque, Heap, BST, Red-Black Tree) |
| **D** | Graph algorithms (BFS, DFS, Dijkstra, Kruskal, Prim) |
| **E** | Dynamic Programming (Knapsack budget allocator) |

### Option 2: Run individual demos

```bash
# Compile all sources
mkdir -p out
javac -d out src/datastructures/*.java
javac -cp out -d out src/algorithms/*.java src/*.java

# Run any individual demo
java -cp out datastructures.DispatchDeque
java -cp out datastructures.CustomHeap
java -cp out datastructures.ResourceBST
java -cp out datastructures.CustomRedBlackTree
java -cp out algorithms.BFS
java -cp out algorithms.DFS
java -cp out algorithms.Dijkstra
java -cp out algorithms.Kruskal
java -cp out algorithms.Prim
java -cp out algorithms.DynamicProgramming
java -cp out algorithms.SearchSortDemo
```

### Option 3: Run JUnit tests via Maven

```bash
mvn test
```

---

## Data Structures (14)

All structures are implemented from scratch without `java.util` collections.

| # | Structure | File | Description | Use Case |
|---|-----------|------|-------------|----------|
| 1 | **ArrayList** | `CustomArrayList.java` | Dynamic resizing array | General-purpose indexed collection |
| 2 | **LinkedList** | `CustomLinkedList.java` | Singly linked list with head/tail | Sequential traversal, insertions |
| 3 | **Stack** | `CustomStack.java` | LIFO stack backed by linked list | DFS traversal, expression evaluation |
| 4 | **Queue** | `CustomQueue.java` | FIFO queue backed by linked list | BFS traversal, task scheduling |
| 5 | **Set** | `CustomSet.java` | Hash-based unique element collection | Visited-node tracking |
| 6 | **Map** | `CustomMap.java` | Hash-based key-value store | Location name lookups |
| 7 | **Disjoint Set** | `CustomDisjointSet.java` | Union-Find with path compression & rank | Kruskal's MST cycle detection |
| 8 | **Graph** | `CustomGraph.java` | Adjacency list weighted graph | Campus road network |
| 9 | **Max-Heap** | `CustomHeap.java` | Array-backed max-priority queue | Service request prioritization |
| 10 | **Red-Black Tree** | `CustomRedBlackTree.java` | Self-balancing BST | Balanced location lookup |
| 11 | **Dispatch Deque** | `DispatchDeque.java` | Doubly-linked double-ended queue | Road segment dispatching |
| 12 | **Binary Search Tree** | `ResourceBST.java` | BST with in-order traversal | Resource lookup & sorted listing |
| 13 | **Hash Table** | `HashTable.java` | Open-addressing hash table | O(1) service request lookup |
| 14 | **B-Tree** | `BTree.java` | Balanced multi-way search tree | Disk-friendly indexed lookups |

---

## Algorithms (10+)

### Graph Algorithms

| Algorithm | File | Complexity | Description |
|-----------|------|-----------|-------------|
| **BFS** | `BFS.java` | O(V + E) | Breadth-first traversal of the campus graph |
| **DFS** | `DFS.java` | O(V + E) | Depth-first traversal of the campus graph |
| **Dijkstra** | `Dijkstra.java` | O((V + E) log V) | Shortest path from any building to all others |
| **Kruskal** | `Kruskal.java` | O(E log E) | Minimum spanning tree using Union-Find |
| **Prim** | `Prim.java` | O(E log V) | Minimum spanning tree using priority queue |

### Search Algorithms

| Algorithm | Complexity | Description |
|-----------|-----------|-------------|
| **Linear Search** | O(n) | Sequential scan through records |
| **Binary Search** | O(log n) | Sorted-array lookup |

### Sorting Algorithms

| Algorithm | Complexity | Description |
|-----------|-----------|-------------|
| **Bubble Sort** | O(n²) | Simple swap-based sort |
| **Selection Sort** | O(n²) | Min-element selection |
| **Insertion Sort** | O(n²) | Sorted-prefix insertion |
| **Merge Sort** | O(n log n) | Divide-and-conquer stable sort |
| **Quick Sort** | O(n log n) avg | Partition-based in-place sort |

### Dynamic Programming

| Algorithm | File | Description |
|-----------|------|-------------|
| **Knapsack (0/1)** | `DynamicProgramming.java` | Budget allocation optimizer — assigns limited budget across campus service requests to maximize priority-weighted impact |

---

## Database Integration

The project includes a complete SQLite database pipeline:

```
CSV files → CSVLoader → george.db (SQLite) → CRUDOperations → CSVExporter
                                                    ↓
                                              AuditLogger
```

| Component | Description |
|-----------|-------------|
| `CSVLoader.java` | Imports all CSV datasets into SQLite tables |
| `Database.java` | Connection manager (singleton pattern) |
| `CRUDOperations.java` | Full Create, Read, Update, Delete for all entities |
| `CSVExporter.java` | Export query results back to CSV |
| `AuditLogger.java` | Timestamped audit trail for all modifications |
| `schema.sql` | DDL with foreign keys, indexes, and constraints |

**Domain Models:** `Location.java`, `Resource.java`, `ServiceRequest.java`

---

## GUI Dashboard

The interactive GUI is a single-page web application deployed on Netlify:

🔗 **[https://septagon02-campus-optimizer.netlify.app/](https://septagon02-campus-optimizer.netlify.app/)**

**Features:**
- 📊 **Dashboard** — Overview stats for locations, roads, requests, and resources
- 🗺️ **Campus Graph** — Interactive visualization using real geographic coordinates
- 🔍 **BFS/DFS Traversals** — Animated step-by-step graph traversals
- 📍 **Dijkstra Pathfinder** — Shortest path between any two campus buildings
- 🌲 **MST Viewer** — Kruskal and Prim minimum spanning tree visualization
- 🔎 **Search & Sort** — Side-by-side algorithm comparison with timing
- 🗄️ **Database CRUD** — Live query, insert, update, and delete operations
- 💰 **DP Optimizer** — Knapsack-based budget allocation with visual breakdown

Built with vanilla HTML/CSS/JS (no frameworks). Deployed via Netlify from the `gui/` folder.

---

## Testing

16 JUnit 5 test files covering every data structure and algorithm:

| Test File | What it tests |
|-----------|--------------|
| `CustomArrayListTest.java` | Add, get, remove, resize operations |
| `CustomLinkedListTest.java` | Insert, delete, traverse, edge cases |
| `CustomStackTest.java` | Push, pop, peek, underflow |
| `CustomQueueTest.java` | Enqueue, dequeue, isEmpty |
| `CustomSetTest.java` | Add, contains, remove, uniqueness |
| `CustomMapTest.java` | Put, get, containsKey, collision handling |
| `CustomDisjointSetTest.java` | Union, find, path compression |
| `CustomGraphTest.java` | Add vertex/edge, adjacency, weighted edges |
| `HashTableTest.java` | Insert, search, delete, load factor |
| `BTreeTest.java` | Insert, search, in-order traversal |
| `BFSTest.java` | Level-order traversal correctness |
| `DFSTest.java` | Depth traversal, back-edge detection |
| `DijkstraTest.java` | Shortest path distances, path reconstruction |
| `KruskalTest.java` | MST weight, edge count, cycle avoidance |
| `PrimTest.java` | MST correctness, priority queue behavior |
| `DynamicProgrammingTest.java` | Priority weights, knapsack allocation |

Run all tests:

```bash
mvn test
```

---

## Performance Analysis

Located in `src/Performance/`:

- **`generate_data.py`** — Generates synthetic datasets of varying sizes (100 → 100,000 records)
- **`run_experiments.py`** — Benchmarks all data structures: insert, search, delete, traversal
- **`structures.py`** — Python re-implementations for cross-language comparison
- **[PERFORMANCE_REPORT.md](PERFORMANCE_REPORT.md)** — Full analysis report with benchmark data, readable directly on GitHub
- **`Priscilla_Performance_Report.docx`** — Original document with embedded charts and figures

```bash
cd src/Performance
pip install -r requirements.txt
python generate_data.py
python run_experiments.py
```

---

## Dataset

All data is based on the **University of Ghana, Legon Campus**:

| File | Records | Description |
|------|---------|-------------|
| `locations.csv` | 30 | Campus buildings (coordinates, categories) |
| `roads.csv` | 100+ | Road segments with distances (km and m) |
| `resources.csv` | 20+ | Campus resources (vehicles, equipment, staff) |
| `service_requests.csv` | 100+ | Service requests with priorities and deadlines |
| `nodes.csv` | 30 | Graph node definitions |
| `edges_knn_sparse.csv` | ~60 | K-nearest-neighbor sparse edge set |
| `edges_full_pairwise.csv` | ~870 | Full pairwise distance matrix |

---

## Team Members

Team Septagon02 comprises 14 members organized into functional engineering groups:

| # | Name | Student ID | Role | Module / Functional Focus | Oral Defense Topic |
|---|------|------------|------|---------------------------|-------------------|
| 1 | **Dzibolosu, Anneta Dziedzom** | 22130388 | Project Manager | Project Coordination, Milestones, Repository Management | CustomHeap & Dijkstra's Algorithm |
| 2 | **Ashwere, Ansah Louis Kwaku** | 22241242 | Technical Lead | System Architecture, Code Review, Module Integration | CustomRedBlackTree & BFS |
| 3 | **Quainoo, Prince Abeiku** | 22046275 | Core Developer 1 | ArrayList, LinkedList, Stack, Queue | ResourceBST & QuickSort |
| 4 | **Awuye, Benjamin Elorm** | 22018107 | Core Developer 2 | Deque, Min/Max Heap, BST, Red-Black Tree | DispatchDeque & Merge Sort |
| 5 | **Asante-Arhin, Michael** | 22241078 | Core Developer 3 | B-Tree, Hash Table, Set & Map | HashTable & Kruskal's MST |
| 6 | **Gyakye-Smith, Nana Ekow** | 22379633 | Graph Developer | Adjacency Graph, Disjoint Set (Union-Find) | BTree & DFS |
| 7 | **Lumor, Lilian Seyram** | 22412282 | Search & Sort Algorithms | Linear, Binary, Selection, Insertion, Merge, Quick Sort | CustomDisjointSet & Prim's MST |
| 8 | **Duah, Nana Kwabena Boakye** | 22263968 | Graph Algorithms | BFS, DFS, Dijkstra, Kruskal, Prim MST | CustomGraph & Dynamic Programming |
| 9 | **Roberts, Denise Elizabeth** | 22385683 | Optimization Algorithms | Greedy Resource Allocation, DP Knapsack Optimizer | CustomMap & Selection Sort |
| 10 | **Quarm, Patience** | 22405176 | Database Architect | SQLite Schema Design, Relational Modeling, JDBC | CustomSet & Insertion Sort |
| 11 | **Asiedu-Bekoe, Heartfield George** | 22414593 | Data Loader & CRUD | CSV Pipeline, DB Operations, Audit Logger, Export | CustomQueue & Binary Search |
| 12 | **Ali, Khadija** | 22302684 | Testing Lead | 16+ JUnit 5 Test Suites, Edge Cases, Coverage | CustomLinkedList & Linear Search |
| 13 | **Dzandu, Priscilla Amewornu** | 22390285 | Performance Specialist | Benchmarking Harness, Complexity Curves, Analysis | CustomStack & Graph Loaders |
| 14 | **Obuobie, Nana Otcherewaa** | 22300595 | Documentation & Report Lead | 20-Page Technical Report, Diagrams, Verification | CSV Loaders & SQLite Schema |

---

## Team Workflow

- Do not push directly to `main`.
- Create your assigned branch from `main` and open a pull request for review.
- Add `Closes #<issue_number>` to the PR description when linking an issue.
- All code must compile and pass tests before merging.

---

## License

This project was developed as a course assignment for **DCIT 204 — Data Structures and Algorithms** at the **University of Ghana, Legon**.
