#!/bin/zsh
# ============================================================
#  Septagon02 DSA Project — Full Demo Runner
#  University of Ghana, Legon Campus — DCIT 204
#
#  Run from the project root: septagon02-dsa-project/
#  Usage: zsh run_all_demos.sh
# ============================================================
set -e
cd "$(dirname "$0")"

JDBC="lib/sqlite-jdbc.jar"
DB_PATH="data_base/george.db"

echo "============================================================"
echo "  Septagon02 DSA — Full Demo Runner"
echo "  University of Ghana, Legon Campus  |  DCIT 204"
echo "============================================================"
echo ""

# ── Preflight checks ─────────────────────────────────────────────────
if [ ! -f "$JDBC" ]; then
  echo "[ERROR] SQLite JDBC driver not found at $JDBC"
  echo "        Run: curl -L -o lib/sqlite-jdbc.jar https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.46.1.3/sqlite-jdbc-3.46.1.3.jar"
  exit 1
fi

# ── Step 1: Compile ALL sources ──────────────────────────────────────
echo ">>> Step 1: Compiling all sources into out/"
mkdir -p out

# Pass 1: Data structures (no external deps)
javac -d out \
  src/datastructures/CustomArrayList.java \
  src/datastructures/CustomLinkedList.java \
  src/datastructures/CustomStack.java \
  src/datastructures/CustomQueue.java \
  src/datastructures/CustomSet.java \
  src/datastructures/CustomMap.java \
  src/datastructures/CustomDisjointSet.java \
  src/datastructures/CustomGraph.java \
  src/datastructures/CustomHeap.java \
  src/datastructures/CustomRedBlackTree.java \
  src/datastructures/DispatchDeque.java \
  src/datastructures/ResourceBST.java \
  src/datastructures/HashTable.java \
  src/datastructures/BTree.java

# Pass 2: Database layer + algorithms together (need out/ on classpath for datastructures)
javac -cp "out:$JDBC" -d out \
  src/AuditLogger.java \
  src/Location.java \
  src/Resource.java \
  src/ServiceRequest.java \
  src/Database.java \
  src/CRUDOperations.java \
  src/CSVLoader.java \
  src/CSVExporter.java \
  src/RunDemo.java \
  src/StructureDemo.java \
  src/algorithms/GraphEdge.java \
  src/algorithms/SearchAlgorithms.java \
  src/algorithms/SortAlgorithms.java \
  src/algorithms/SortingAlgorithms.java \
  src/algorithms/GraphLoader.java \
  src/algorithms/BFS.java \
  src/algorithms/DFS.java \
  src/algorithms/Dijkstra.java \
  src/algorithms/Kruskal.java \
  src/algorithms/Prim.java \
  src/algorithms/UserInterface.java \
  src/algorithms/SearchSortDemo.java

echo ">>> Compilation successful!"
echo ""

# ── Step 2: Seed SQLite database from CSV ────────────────────────────
echo ">>> Step 2: Seeding SQLite database (george.db) from CSV files"
java -cp "out:$JDBC" CSVLoader 2>&1 | grep -E "^(Initializing|Loading|Loaded|Done)" || true
echo ">>> Database ready: $DB_PATH"
echo ""

# ── Step 3: Database integration demo ────────────────────────────────
echo "============================================================"
echo " PART A — DATABASE INTEGRATION DEMO"
echo " (CSVLoader → george.db → BTree + HashTable + CustomSet + CustomMap)"
echo "============================================================"
echo ""
echo ">>> Querying george.db via custom data structures..."

# Feed inputs automatically:
#   1 → Query by ID → ID=5 (Medical request)
#   2 → Check location → ID=19 (University Hospital)
#   3 → Resource lookup → ID=18 (Emergency Ambulance)
#   4 → BTree in-order traversal
#   5 → Exit
printf "1\n5\n2\n19\n3\n18\n4\n5\n" | java -cp "out:$JDBC" StructureDemo

echo ""

# ── Step 4: Search & Sort on real CSV data ───────────────────────────
echo "============================================================"
echo " PART B — SEARCH & SORT ALGORITHMS ON REAL CSV DATA"
echo " (service_requests.csv + resources.csv)"
echo "============================================================"
echo ""
java -cp out algorithms.SearchSortDemo

echo ""

# ── Step 5: Data structure standalone demos ──────────────────────────
echo "============================================================"
echo " PART C — DATA STRUCTURE DEMOS (CSV-backed)"
echo "============================================================"
echo ""

echo "------------------------------------------------------------"
echo " [1] DispatchDeque — Doubly-Linked Deque (roads.csv)"
echo "------------------------------------------------------------"
java -cp out datastructures.DispatchDeque

echo ""
echo "------------------------------------------------------------"
echo " [2] CustomHeap — Max-Heap Priority Queue (service_requests.csv)"
echo "------------------------------------------------------------"
java -cp out datastructures.CustomHeap

echo ""
echo "------------------------------------------------------------"
echo " [3] ResourceBST — Binary Search Tree (resources.csv)"
echo "------------------------------------------------------------"
java -cp out datastructures.ResourceBST

echo ""
echo "------------------------------------------------------------"
echo " [4] CustomRedBlackTree — Balanced BST (locations.csv)"
echo "------------------------------------------------------------"
java -cp out datastructures.CustomRedBlackTree

echo ""

# ── Step 6: Graph algorithm demos ────────────────────────────────────
echo "============================================================"
echo " PART D — GRAPH ALGORITHM DEMOS (locations.csv + roads.csv)"
echo "============================================================"
echo ""

echo "------------------------------------------------------------"
echo " [5] BFS — Breadth-First Search"
echo "------------------------------------------------------------"
java -cp out algorithms.BFS

echo ""
echo "------------------------------------------------------------"
echo " [6] DFS — Depth-First Search"
echo "------------------------------------------------------------"
java -cp out algorithms.DFS

echo ""
echo "------------------------------------------------------------"
echo " [7] Dijkstra — Shortest Path (Main Gate → all locations)"
echo "------------------------------------------------------------"
java -cp out algorithms.Dijkstra

echo ""
echo "------------------------------------------------------------"
echo " [8] Kruskal — Minimum Spanning Tree"
echo "------------------------------------------------------------"
java -cp out algorithms.Kruskal

echo ""
echo "------------------------------------------------------------"
echo " [9] Prim — Minimum Spanning Tree"
echo "------------------------------------------------------------"
java -cp out algorithms.Prim

# ── Done ─────────────────────────────────────────────────────────────
echo ""
echo "============================================================"
echo "  All demos completed successfully!"
echo "  A: DB Integration  |  B: Search & Sort  |  C: Data Structures"
echo "  D: BFS / DFS / Dijkstra / Kruskal / Prim"
echo "============================================================"
echo ""
echo "  ── Interactive demos (run manually) ────────────────────"
echo "  Manual Search & Sort UI:"
echo "    java -cp out algorithms.UserInterface"
echo ""
echo "  Interactive DB demo (query, check, lookup):"
echo "    java -cp out:lib/sqlite-jdbc.jar StructureDemo"
echo ""
echo "  Re-seed database from CSV:"
echo "    java -cp out:lib/sqlite-jdbc.jar CSVLoader"
