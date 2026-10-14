# Performance & Efficiency Analysis Report

**Ghana Smart Service Operations Optimizer — University of Ghana, Legon Campus**

Prepared by: **Dzandu Priscilla Amewornu** (22390285) — Performance Specialist
Date: August 2026

---

## 1. Purpose & Scope

This report documents six efficiency experiments benchmarking the data structures and algorithms implemented by the team (Prince, Benjamin, Michael, Ekow, Lilian, Nana K., Denise) as they apply to the Legon Campus Facility Management scenario: 50 buildings, 100+ pathways, 300+ service requests, 30 resources, and 5 service types (Maintenance, Delivery, Waste, Security, Grounds).

Because the team's Java implementations and the final real dataset (owned by Samuella and Patience) were not yet available at the time of this report, all experiments were run on a Python benchmarking harness built to mirror the team's structures exactly (same operations, same Big-O characteristics), against a synthetic dataset generated from the real Legon building list and the project's CSV schema (`locations.csv`, `roads.csv`, `service_requests.csv`, `resources.csv`). Each experiment was run at least twice per data point and the median timing was recorded, per the team's performance-testing guidance.

> **Note:** This is a benchmarking placeholder to validate expected complexity trends early. Once the team's Java modules and real dataset are integrated (Week 3-4), these experiments should be re-run against the actual implementation for the final report.

---

## 2. Experiment 1 — Linear Search vs Binary Search

**Scenario:** Locating a service request by `request_id` (Lilian's search algorithms), used whenever a team member or the audit log needs to pull up a specific request.

**Method:** Searched for 200 random request IDs at each dataset size, sorted by ID for binary search, and recorded median time per search.

| n (requests) | Linear (µs/search) | Binary (µs/search) |
|-------------|--------------------|--------------------|
| 100 | 1.50 | 0.59 |
| 500 | 8.25 | 1.05 |
| 1,000 | 15.83 | 1.29 |
| 5,000 | 66.25 | 1.78 |
| 10,000 | 149.53 | 2.12 |
| 50,000 | 757.89 | 3.12 |
| 100,000 | 1,691.50 | 3.24 |

**Analysis:** Linear search time grows proportionally with n (O(n)) — at 100,000 requests it takes about 1,690 microseconds per search. Binary search stays almost flat (O(log n)) — only about 3.2 microseconds at the same size, roughly **520× faster at scale**. This confirms the expected complexity difference; the trade-off is that binary search requires the request list to stay sorted by ID, which has its own maintenance cost the team should account for when requests are inserted continuously.

---

## 3. Experiment 2 — Sorting Algorithm Comparison

**Scenario:** Ordering service requests by deadline before dispatch, comparing all four sorting algorithms the team implemented (Lilian: Selection, Insertion, Merge, Quicksort).

| n (requests) | Selection (ms) | Insertion (ms) | Merge (ms) | Quicksort (ms) |
|-------------|---------------|---------------|------------|----------------|
| 100 | 0.68 | 0.36 | 0.20 | 0.21 |
| 500 | 17.83 | 9.98 | 1.25 | 1.18 |
| 1,000 | 73.45 | 43.73 | 2.78 | 3.55 |
| 2,000 | 318.62 | 185.49 | 6.22 | 12.46 |
| 4,000 | 1,380.63 | 786.25 | 13.60 | 60.86 |

**Analysis:** Selection and Insertion Sort show clear O(n²) curves — Selection Sort takes 1,380 ms at n=4,000, about 100× slower than at n=500 despite only an 8× increase in n, confirming quadratic growth. Merge Sort and Quicksort both show O(n log n) growth and stay under 61 ms even at n=4,000. Since the team's real dataset is only ~320-500 requests, Insertion Sort's simplicity is acceptable at that scale, but Merge Sort or Quicksort should be used if the request queue is expected to grow past a few thousand entries (e.g., across a full semester).

---

## 4. Experiment 3 — Hash Table Load Factor Analysis

**Scenario:** Michael's Hash Table is used for instant lookup of requests/resources by ID. This experiment measures how lookup speed degrades as the table fills up (load factor = number of keys / table capacity), using separate chaining for collision handling.

| Load Factor | Avg `get()` time (µs) |
|------------|----------------------|
| 0.5 | 0.251 |
| 1.0 | 0.239 |
| 2.0 | 0.276 |
| 4.0 | 0.294 |
| 6.0 | 0.335 |
| 8.0 | 0.353 |
| 12.0 | 0.420 |

**Analysis:** Lookup time increases roughly linearly with load factor once it passes 1.0, growing from about 0.24 microseconds at load factor 1.0 to 0.42 microseconds at load factor 12 — a ~75% slowdown as the table gets nearly 12× over-full. This matches chaining theory (average chain length ≈ load factor). **Recommendation:** Resize (rehash) the table once load factor exceeds roughly 0.75–1.0 to keep lookups close to O(1), rather than letting it grow unbounded.

---

## 5. Experiment 4 — Binary Search Tree vs Red-Black Tree

**Scenario:** Comparing Benjamin's plain BST and Red-Black Tree for indexing locations, under the worst case for a plain BST: inserting keys already in sorted order (e.g., loading `locations.csv` in `location_id` order).

| n (locations indexed) | Plain BST (µs/search) | Red-Black Tree (µs/search) |
|----------------------|----------------------|---------------------------|
| 100 | 2.09 | 0.29 |
| 500 | 10.39 | 0.40 |
| 1,000 | 22.02 | 0.53 |
| 2,000 | 43.41 | 0.57 |
| 4,000 | 85.48 | 0.67 |
| 8,000 | 188.05 | 0.57 |

**Analysis:** This is the clearest result in the report. Under sorted-order insertion, the plain BST degrades into what is effectively a linked list — search time grows linearly with n, reaching 188 microseconds at n=8,000. The Red-Black Tree stays essentially flat (under 1 microsecond) at every size tested because it rebalances on every insert, guaranteeing O(log n) height. **Recommendation:** Since the real pipeline loads `locations.csv` sequentially by `location_id`, the plain BST should never be used for this — Benjamin's Red-Black Tree (or inserting into the BST in randomized order) is required to avoid worst-case behavior.

---

## 6. Experiment 5 — Heap Insert / Extract-Max Performance

**Scenario:** Benjamin's Heap is used to always dispatch the highest-priority request first (Security/Critical before Grounds/Low).

| n (requests in heap) | Insert (µs/op) | Extract-Max (µs/op) |
|---------------------|----------------|---------------------|
| 100 | 0.735 | 1.176 |
| 1,000 | 0.651 | 1.638 |
| 10,000 | 2.462 | 2.653 |
| 50,000 | 0.750 | 3.241 |
| 100,000 | 0.818 | 3.282 |

**Analysis:** Both operations stayed close to the low single-digit microsecond range even at 100,000 requests, consistent with O(log n) per operation — log n barely grows even as n grows 1000×. One anomaly worth flagging: insert time actually appeared to drop between n=10,000 and n=50,000 in this run. This is very likely measurement noise from Python/OS-level timing overhead dominating at these already-tiny durations, not a real algorithmic effect. Per the team's guidance to run experiments at least twice, this was re-run and the dip persisted at a smaller magnitude — recommend re-measuring with a higher-resolution profiler once the Java implementation is available, and average over more repeats (10+) rather than 2–3.

---

## 7. Experiment 6 — Graph Algorithm Comparison

**Scenario:** Ekow's Graph structure holds the campus network (buildings as nodes, pathways as weighted edges). This compares Nana K.'s BFS, DFS, Dijkstra, Prim, and Kruskal as the campus network scales up.

| n (nodes) | BFS (ms) | DFS (ms) | Dijkstra (ms) | Prim (ms) | Kruskal (ms) |
|----------|---------|---------|--------------|----------|-------------|
| 50 | 0.017 | 0.017 | 0.047 | 0.074 | 0.188 |
| 100 | 0.033 | 0.032 | 0.101 | 0.150 | 0.319 |
| 200 | 0.062 | 0.140 | 0.217 | 0.298 | 0.640 |
| 500 | 0.259 | 0.340 | 0.733 | 1.014 | 1.895 |
| 1,000 | 0.848 | 0.933 | 2.178 | 3.387 | 7.288 |

**Analysis:** BFS and DFS (both O(V+E)) are cheapest and scale nearly linearly, useful for the team's simple reachability checks (e.g., "can Security reach every building from Gate 1"). Dijkstra costs more (O(E log V) with a priority queue) but is still fast at campus scale (2.2 ms at 1,000 nodes) — appropriate for real-time routing since the real campus only has 50 buildings. Prim and Kruskal (both for minimum spanning tree / infrastructure planning) were the most expensive at scale, with Kruskal costing the most due to its edge-sorting step (O(E log E)); Prim's priority-queue approach scaled somewhat better on this denser synthetic graph. At the real project's scale (50 nodes, 110 edges) all five algorithms complete in well under 1 millisecond, so algorithm choice will not be a bottleneck for the actual deliverable — this comparison matters more if the team later extends the dataset city-wide.

---

## 8. Summary of Recommendations

1. **Use Binary Search (not Linear)** once the request list is sorted by ID — over 500× faster at scale with negligible extra complexity.
2. **Prefer Merge Sort or Quicksort** over Selection/Insertion Sort if the request queue is expected to exceed a few thousand entries.
3. **Resize Michael's Hash Table** once load factor passes ~0.75–1.0 to keep lookups close to O(1).
4. **Avoid inserting into the plain BST in sorted order** — use the Red-Black Tree (or randomize insert order) to prevent worst-case O(n) degradation.
5. **Heap operations are cheap** and not a performance concern at any realistic project scale; re-measure with more repeats once ported to Java for the final report.
6. **At the real project's scale (50 buildings), all graph algorithms are effectively instant** — algorithm choice should be driven by correctness/use-case fit (BFS/DFS for reachability, Dijkstra for routing, Prim/Kruskal for infrastructure planning) rather than speed.

---

## 9. Methodology Notes & Limitations

- Benchmarks were run on a Python harness mirroring the team's Java structures' operations and complexity, not the team's actual Java code — timings are illustrative of relative/asymptotic behavior, not absolute Java performance.
- The dataset used was synthetically generated from the real 50 Legon building names and the project's CSV schema, since the final dataset from Samuella/Patience was not yet available.
- Each data point is the median of 2–5 repeated runs, per the team's "run experiments at least twice" guidance.
- **Next step:** Re-run all six experiments against the team's actual Java implementation and the final Ghana dataset once Week 3–4 integration is complete, and substitute these graphs into the final technical report (Nana O.'s deliverable).
