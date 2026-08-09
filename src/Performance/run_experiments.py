import csv
import os
import random
import time
import statistics as stats

import matplotlib
matplotlib.use("Agg")
import matplotlib.pyplot as plt

from structures import (
    linear_search, binary_search, selection_sort, insertion_sort,
    merge_sort, quicksort, ChainingHashTable, BST, RedBlackTree,
    MaxHeap, Graph, bfs, dfs, dijkstra, kruskal_mst, prim_mst,
)

random.seed(7)

# Paths relative to this script — works on any machine
_SCRIPT_DIR = os.path.dirname(os.path.abspath(__file__))
_PROJECT_ROOT = os.path.join(_SCRIPT_DIR, "..", "..")
DATA_DIR = os.path.join(_PROJECT_ROOT, "data_base")   # real CSV files live here
OUT      = os.path.join(_SCRIPT_DIR, "output") + os.sep  # graphs/results go here
os.makedirs(OUT, exist_ok=True)


def load_requests():
    with open(os.path.join(DATA_DIR, "service_requests.csv")) as f:
        reader = csv.DictReader(f)
        rows = []
        for r in reader:
            rows.append({
                "request_id": int(r["request_id"]),
                "request_type": r["request_type"],
                "requester_location_id": int(r["requester_location_id"]),
                "priority": r["priority"],
                "deadline": r["deadline"],
            })
        return rows


def timeit_avg(fn, repeats=5):
    times = []
    for _ in range(repeats):
        t0 = time.perf_counter()
        fn()
        times.append(time.perf_counter() - t0)
    return stats.median(times)


def make_requests(n, base):
    # Extend the real 320-request dataset synthetically to test at scale,
    # cycling through realistic type/priority/location distributions.
    out = []
    for i in range(1, n + 1):
        src = base[(i - 1) % len(base)]
        out.append({
            "request_id": i,
            "request_type": src["request_type"],
            "requester_location_id": src["requester_location_id"],
            "priority": src["priority"],
            "deadline": f"2026-08-{(i % 28) + 1:02d} {(i % 14) + 6:02d}:00",
        })
    return out


# ---------------- Experiment 1: Linear vs Binary Search ----------------

def exp1_search():
    base = load_requests()
    sizes = [100, 500, 1000, 5000, 10000, 50000, 100000]
    linear_times, binary_times = [], []
    for n in sizes:
        data = make_requests(n, base)  # already sorted by request_id (1..n)
        targets = [random.randint(1, n) for _ in range(200)]

        def run_linear():
            for t in targets:
                linear_search(data, t)

        def run_binary():
            for t in targets:
                binary_search(data, t)

        linear_times.append(timeit_avg(run_linear, 3) / len(targets) * 1e6)   # microseconds/search
        binary_times.append(timeit_avg(run_binary, 3) / len(targets) * 1e6)

    plt.figure(figsize=(7, 5))
    plt.plot(sizes, linear_times, marker="o", label="Linear Search")
    plt.plot(sizes, binary_times, marker="o", label="Binary Search")
    plt.xlabel("Number of service requests (n)")
    plt.ylabel("Avg time per search (microseconds)")
    plt.title("Experiment 1: Linear vs Binary Search")
    plt.legend()
    plt.grid(alpha=0.3)
    plt.tight_layout()
    plt.savefig(OUT + "exp1_search.png", dpi=150)
    plt.close()
    return {"sizes": sizes, "linear_us": linear_times, "binary_us": binary_times}


# ---------------- Experiment 2: Sorting comparison ----------------

def exp2_sorting():
    base = load_requests()
    sizes = [100, 500, 1000, 2000, 4000]
    results = {"selection": [], "insertion": [], "merge": [], "quick": []}
    key = lambda r: r["deadline"]
    for n in sizes:
        data = make_requests(n, base)
        random.shuffle(data)
        results["selection"].append(timeit_avg(lambda: selection_sort(data, key), 2) * 1000)
        results["insertion"].append(timeit_avg(lambda: insertion_sort(data, key), 2) * 1000)
        results["merge"].append(timeit_avg(lambda: merge_sort(data, key), 2) * 1000)
        results["quick"].append(timeit_avg(lambda: quicksort(data, key), 2) * 1000)

    plt.figure(figsize=(7, 5))
    for label, series in results.items():
        plt.plot(sizes, series, marker="o", label=label.capitalize() + " Sort")
    plt.xlabel("Number of service requests (n)")
    plt.ylabel("Time (milliseconds)")
    plt.title("Experiment 2: Sorting Algorithm Comparison")
    plt.legend()
    plt.grid(alpha=0.3)
    plt.tight_layout()
    plt.savefig(OUT + "exp2_sorting.png", dpi=150)
    plt.close()
    return {"sizes": sizes, **results}


# ---------------- Experiment 3: Hash table load factor ----------------

def exp3_hash_load_factor():
    n_keys = 5000
    load_factors = [0.5, 1.0, 2.0, 4.0, 6.0, 8.0, 12.0]
    avg_get_times = []
    for lf in load_factors:
        capacity = max(1, int(n_keys / lf))
        table = ChainingHashTable(capacity=capacity)
        for i in range(n_keys):
            table.put(i, f"request_{i}")
        sample = [random.randint(0, n_keys - 1) for _ in range(2000)]

        def run():
            for k in sample:
                table.get(k)

        t = timeit_avg(run, 3) / len(sample) * 1e6  # microseconds/get
        avg_get_times.append(t)

    plt.figure(figsize=(7, 5))
    plt.plot(load_factors, avg_get_times, marker="o", color="darkred")
    plt.xlabel("Load factor (n keys / capacity)")
    plt.ylabel("Avg time per get() (microseconds)")
    plt.title("Experiment 3: Hash Table Lookup Time vs Load Factor")
    plt.grid(alpha=0.3)
    plt.tight_layout()
    plt.savefig(OUT + "exp3_hash_load_factor.png", dpi=150)
    plt.close()
    return {"load_factors": load_factors, "avg_get_us": avg_get_times}


# ---------------- Experiment 4: BST vs Red-Black Tree ----------------

def exp4_bst_vs_rbt():
    sizes = [100, 500, 1000, 2000, 4000, 8000]
    bst_times, rbt_times = [], []
    for n in sizes:
        sorted_keys = list(range(n))  # worst case: sorted-order insertion

        bst = BST()
        for k in sorted_keys:
            bst.insert(k, f"loc_{k}")
        rbt = RedBlackTree()
        for k in sorted_keys:
            rbt.insert(k, f"loc_{k}")

        sample = [random.randint(0, n - 1) for _ in range(300)]

        def run_bst():
            for k in sample:
                bst.search(k)

        def run_rbt():
            for k in sample:
                rbt.search(k)

        bst_times.append(timeit_avg(run_bst, 3) / len(sample) * 1e6)
        rbt_times.append(timeit_avg(run_rbt, 3) / len(sample) * 1e6)

    plt.figure(figsize=(7, 5))
    plt.plot(sizes, bst_times, marker="o", label="Plain BST (sorted-order insert, worst case)")
    plt.plot(sizes, rbt_times, marker="o", label="Red-Black Tree (self-balancing)")
    plt.xlabel("Number of locations indexed (n)")
    plt.ylabel("Avg time per search (microseconds)")
    plt.title("Experiment 4: BST vs Red-Black Tree Search Time")
    plt.legend()
    plt.grid(alpha=0.3)
    plt.tight_layout()
    plt.savefig(OUT + "exp4_bst_vs_rbt.png", dpi=150)
    plt.close()
    return {"sizes": sizes, "bst_us": bst_times, "rbt_us": rbt_times}


# ---------------- Experiment 5: Heap operations ----------------

def exp5_heap():
    sizes = [100, 1000, 10000, 50000, 100000]
    insert_times, extract_times = [], []
    for n in sizes:
        priorities = [random.randint(1, 5) for _ in range(n)]

        def run_insert():
            h = MaxHeap()
            for p in priorities:
                h.insert(p, f"req_{p}")
            return h

        t_insert = timeit_avg(run_insert, 2) / n * 1e6
        insert_times.append(t_insert)

        h = run_insert()

        def run_extract():
            hh = MaxHeap()
            hh.data = h.data[:]
            while hh.data:
                hh.extract_max()

        t_extract = timeit_avg(run_extract, 2) / n * 1e6
        extract_times.append(t_extract)

    plt.figure(figsize=(7, 5))
    plt.plot(sizes, insert_times, marker="o", label="Insert (avg per op)")
    plt.plot(sizes, extract_times, marker="o", label="Extract-Max (avg per op)")
    plt.xlabel("Number of requests in heap (n)")
    plt.ylabel("Avg time per operation (microseconds)")
    plt.title("Experiment 5: Heap Insert/Extract-Max Performance")
    plt.legend()
    plt.grid(alpha=0.3)
    plt.tight_layout()
    plt.savefig(OUT + "exp5_heap.png", dpi=150)
    plt.close()
    return {"sizes": sizes, "insert_us": insert_times, "extract_us": extract_times}


# ---------------- Experiment 6: Graph algorithms ----------------

def make_campus_graph(n_nodes):
    g = Graph()
    nodes = list(range(n_nodes))
    random.shuffle(nodes)
    connected = [nodes[0]]
    for node in nodes[1:]:
        other = random.choice(connected)
        w = round(random.uniform(0.2, 3.0), 2)
        g.add_edge(node, other, w)
        connected.append(node)
    # extra edges for realism (~2.2x nodes, matching 50 loc / 110 road ratio)
    extra = int(n_nodes * 1.2)
    for _ in range(extra):
        a, b = random.sample(range(n_nodes), 2)
        w = round(random.uniform(0.2, 3.0), 2)
        g.add_edge(a, b, w)
    return g


def exp6_graph_algorithms():
    sizes = [50, 100, 200, 500, 1000]
    results = {"bfs": [], "dfs": [], "dijkstra": [], "prim": [], "kruskal": []}
    for n in sizes:
        g = make_campus_graph(n)
        start = 0
        results["bfs"].append(timeit_avg(lambda: bfs(g, start), 3) * 1000)
        results["dfs"].append(timeit_avg(lambda: dfs(g, start), 3) * 1000)
        results["dijkstra"].append(timeit_avg(lambda: dijkstra(g, start), 3) * 1000)
        results["prim"].append(timeit_avg(lambda: prim_mst(g, start), 3) * 1000)
        results["kruskal"].append(timeit_avg(lambda: kruskal_mst(g), 3) * 1000)

    plt.figure(figsize=(7, 5))
    for label, series in results.items():
        plt.plot(sizes, series, marker="o", label=label.upper() if label in ("bfs", "dfs") else label.capitalize())
    plt.xlabel("Number of campus locations / graph nodes (n)")
    plt.ylabel("Time (milliseconds)")
    plt.title("Experiment 6: Graph Algorithm Comparison")
    plt.legend()
    plt.grid(alpha=0.3)
    plt.tight_layout()
    plt.savefig(OUT + "exp6_graph.png", dpi=150)
    plt.close()
    return {"sizes": sizes, **results}


if __name__ == "__main__":
    import json
    all_results = {}
    print("Running Experiment 1: Search...")
    all_results["exp1_search"] = exp1_search()
    print("Running Experiment 2: Sorting...")
    all_results["exp2_sorting"] = exp2_sorting()
    print("Running Experiment 3: Hash load factor...")
    all_results["exp3_hash"] = exp3_hash_load_factor()
    print("Running Experiment 4: BST vs Red-Black...")
    all_results["exp4_bst_rbt"] = exp4_bst_vs_rbt()
    print("Running Experiment 5: Heap...")
    all_results["exp5_heap"] = exp5_heap()
    print("Running Experiment 6: Graph algorithms...")
    all_results["exp6_graph"] = exp6_graph_algorithms()

    with open(OUT + "results.json", "w") as f:
        json.dump(all_results, f, indent=2)
    print("Done. Results saved to results.json, graphs saved as PNGs.")
