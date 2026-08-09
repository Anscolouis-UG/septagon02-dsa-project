"""
Lightweight Python implementations mirroring the team's Java data structures,
used purely to generate real, measured timing data for the performance report.
Not meant to replace the team's actual Java implementations -- this is
Priscilla's own benchmarking harness.
"""
import random


# ---------- Search & Sort (Lilian's algorithms) ----------

def linear_search(arr, target_id):
    for item in arr:
        if item["request_id"] == target_id:
            return item
    return None


def binary_search(sorted_arr, target_id):
    lo, hi = 0, len(sorted_arr) - 1
    while lo <= hi:
        mid = (lo + hi) // 2
        mid_id = sorted_arr[mid]["request_id"]
        if mid_id == target_id:
            return sorted_arr[mid]
        elif mid_id < target_id:
            lo = mid + 1
        else:
            hi = mid - 1
    return None


def selection_sort(arr, key):
    a = arr[:]
    n = len(a)
    for i in range(n):
        min_idx = i
        for j in range(i + 1, n):
            if key(a[j]) < key(a[min_idx]):
                min_idx = j
        a[i], a[min_idx] = a[min_idx], a[i]
    return a


def insertion_sort(arr, key):
    a = arr[:]
    for i in range(1, len(a)):
        cur = a[i]
        j = i - 1
        while j >= 0 and key(a[j]) > key(cur):
            a[j + 1] = a[j]
            j -= 1
        a[j + 1] = cur
    return a


def merge_sort(arr, key):
    if len(arr) <= 1:
        return arr[:]
    mid = len(arr) // 2
    left = merge_sort(arr[:mid], key)
    right = merge_sort(arr[mid:], key)
    result = []
    i = j = 0
    while i < len(left) and j < len(right):
        if key(left[i]) <= key(right[j]):
            result.append(left[i]); i += 1
        else:
            result.append(right[j]); j += 1
    result.extend(left[i:])
    result.extend(right[j:])
    return result


def quicksort(arr, key):
    a = arr[:]
    _quicksort(a, 0, len(a) - 1, key)
    return a


def _quicksort(a, lo, hi, key):
    if lo < hi:
        p = _partition(a, lo, hi, key)
        _quicksort(a, lo, p - 1, key)
        _quicksort(a, p + 1, hi, key)


def _partition(a, lo, hi, key):
    pivot = key(a[hi])
    i = lo - 1
    for j in range(lo, hi):
        if key(a[j]) <= pivot:
            i += 1
            a[i], a[j] = a[j], a[i]
    a[i + 1], a[hi] = a[hi], a[i + 1]
    return i + 1


# ---------- Hash Table with separate chaining (Michael's structure) ----------

class ChainingHashTable:
    """Custom hash table so we can control & measure load factor directly."""

    def __init__(self, capacity=16):
        self.capacity = capacity
        self.buckets = [[] for _ in range(capacity)]
        self.size = 0

    def _hash(self, key):
        return hash(key) % self.capacity

    def put(self, key, value):
        idx = self._hash(key)
        for i, (k, v) in enumerate(self.buckets[idx]):
            if k == key:
                self.buckets[idx][i] = (key, value)
                return
        self.buckets[idx].append((key, value))
        self.size += 1

    def get(self, key):
        idx = self._hash(key)
        for k, v in self.buckets[idx]:
            if k == key:
                return v
        return None

    def load_factor(self):
        return self.size / self.capacity


# ---------- BST vs Red-Black (balanced) tree (Benjamin's structures) ----------

class BSTNode:
    __slots__ = ("key", "val", "left", "right")

    def __init__(self, key, val):
        self.key = key
        self.val = val
        self.left = None
        self.right = None


class BST:
    """Plain, unbalanced BST -- degrades to O(n) on sorted-order insertion."""

    def __init__(self):
        self.root = None

    def insert(self, key, val):
        if self.root is None:
            self.root = BSTNode(key, val)
            return
        node = self.root
        while True:
            if key < node.key:
                if node.left is None:
                    node.left = BSTNode(key, val)
                    return
                node = node.left
            else:
                if node.right is None:
                    node.right = BSTNode(key, val)
                    return
                node = node.right

    def search(self, key):
        node = self.root
        while node is not None:
            if key == node.key:
                return node.val
            node = node.left if key < node.key else node.right
        return None


class RedBlackTree:
    """
    Simplified red-black tree (insert + search) guaranteeing O(log n)
    height regardless of insertion order -- stands in for Benjamin's
    balanced structure for benchmarking purposes.
    """
    RED = True
    BLACK = False

    class Node:
        __slots__ = ("key", "val", "left", "right", "parent", "color")

        def __init__(self, key, val, color):
            self.key = key
            self.val = val
            self.left = None
            self.right = None
            self.parent = None
            self.color = color

    def __init__(self):
        self.root = None

    def _rotate_left(self, x):
        y = x.right
        x.right = y.left
        if y.left:
            y.left.parent = x
        y.parent = x.parent
        if x.parent is None:
            self.root = y
        elif x is x.parent.left:
            x.parent.left = y
        else:
            x.parent.right = y
        y.left = x
        x.parent = y

    def _rotate_right(self, x):
        y = x.left
        x.left = y.right
        if y.right:
            y.right.parent = x
        y.parent = x.parent
        if x.parent is None:
            self.root = y
        elif x is x.parent.right:
            x.parent.right = y
        else:
            x.parent.left = y
        y.right = x
        x.parent = y

    def insert(self, key, val):
        node = self.Node(key, val, self.RED)
        y = None
        x = self.root
        while x is not None:
            y = x
            if node.key < x.key:
                x = x.left
            else:
                x = x.right
        node.parent = y
        if y is None:
            self.root = node
        elif node.key < y.key:
            y.left = node
        else:
            y.right = node
        self._fix_insert(node)

    def _fix_insert(self, z):
        while z.parent and z.parent.color == self.RED:
            gp = z.parent.parent
            if z.parent is gp.left:
                y = gp.right
                if y and y.color == self.RED:
                    z.parent.color = self.BLACK
                    y.color = self.BLACK
                    gp.color = self.RED
                    z = gp
                else:
                    if z is z.parent.right:
                        z = z.parent
                        self._rotate_left(z)
                    z.parent.color = self.BLACK
                    gp.color = self.RED
                    self._rotate_right(gp)
            else:
                y = gp.left
                if y and y.color == self.RED:
                    z.parent.color = self.BLACK
                    y.color = self.BLACK
                    gp.color = self.RED
                    z = gp
                else:
                    if z is z.parent.left:
                        z = z.parent
                        self._rotate_right(z)
                    z.parent.color = self.BLACK
                    gp.color = self.RED
                    self._rotate_left(gp)
            if z is self.root:
                break
        self.root.color = self.BLACK

    def search(self, key):
        node = self.root
        while node is not None:
            if key == node.key:
                return node.val
            node = node.left if key < node.key else node.right
        return None


# ---------- Binary Heap (Benjamin's structure) ----------

class MaxHeap:
    def __init__(self):
        self.data = []

    def insert(self, priority, item):
        self.data.append((priority, item))
        self._sift_up(len(self.data) - 1)

    def extract_max(self):
        top = self.data[0]
        last = self.data.pop()
        if self.data:
            self.data[0] = last
            self._sift_down(0)
        return top

    def _sift_up(self, i):
        while i > 0:
            parent = (i - 1) // 2
            if self.data[i][0] > self.data[parent][0]:
                self.data[i], self.data[parent] = self.data[parent], self.data[i]
                i = parent
            else:
                break

    def _sift_down(self, i):
        n = len(self.data)
        while True:
            largest = i
            l, r = 2 * i + 1, 2 * i + 2
            if l < n and self.data[l][0] > self.data[largest][0]:
                largest = l
            if r < n and self.data[r][0] > self.data[largest][0]:
                largest = r
            if largest == i:
                break
            self.data[i], self.data[largest] = self.data[largest], self.data[i]
            i = largest


# ---------- Graph + graph algorithms (Ekow / Nana K.) ----------

class Graph:
    def __init__(self):
        self.adj = {}

    def add_vertex(self, v):
        self.adj.setdefault(v, [])

    def add_edge(self, u, v, weight):
        self.add_vertex(u)
        self.add_vertex(v)
        self.adj[u].append((v, weight))
        self.adj[v].append((u, weight))


def bfs(graph, start):
    visited = {start}
    order = [start]
    queue = [start]
    head = 0
    while head < len(queue):
        u = queue[head]; head += 1
        for v, _ in graph.adj[u]:
            if v not in visited:
                visited.add(v)
                order.append(v)
                queue.append(v)
    return order


def dfs(graph, start):
    visited = set()
    order = []

    def _visit(u):
        visited.add(u)
        order.append(u)
        for v, _ in graph.adj[u]:
            if v not in visited:
                _visit(v)

    _visit(start)
    return order


def dijkstra(graph, start):
    import heapq
    dist = {v: float("inf") for v in graph.adj}
    dist[start] = 0
    pq = [(0, start)]
    visited = set()
    while pq:
        d, u = heapq.heappop(pq)
        if u in visited:
            continue
        visited.add(u)
        for v, w in graph.adj[u]:
            nd = d + w
            if nd < dist[v]:
                dist[v] = nd
                heapq.heappush(pq, (nd, v))
    return dist


class DisjointSet:
    def __init__(self, nodes):
        self.parent = {n: n for n in nodes}
        self.rank = {n: 0 for n in nodes}

    def find(self, x):
        while self.parent[x] != x:
            self.parent[x] = self.parent[self.parent[x]]
            x = self.parent[x]
        return x

    def union(self, a, b):
        ra, rb = self.find(a), self.find(b)
        if ra == rb:
            return False
        if self.rank[ra] < self.rank[rb]:
            ra, rb = rb, ra
        self.parent[rb] = ra
        if self.rank[ra] == self.rank[rb]:
            self.rank[ra] += 1
        return True


def kruskal_mst(graph):
    edges = []
    seen = set()
    for u in graph.adj:
        for v, w in graph.adj[u]:
            key = tuple(sorted((u, v)))
            if key not in seen:
                seen.add(key)
                edges.append((w, u, v))
    edges.sort(key=lambda e: e[0])
    ds = DisjointSet(graph.adj.keys())
    mst_weight = 0
    mst_edges = []
    for w, u, v in edges:
        if ds.union(u, v):
            mst_weight += w
            mst_edges.append((u, v, w))
    return mst_weight, mst_edges


def prim_mst(graph, start):
    import heapq
    visited = {start}
    edges = [(w, start, v) for v, w in graph.adj[start]]
    heapq.heapify(edges)
    mst_weight = 0
    mst_edges = []
    while edges and len(visited) < len(graph.adj):
        w, u, v = heapq.heappop(edges)
        if v in visited:
            continue
        visited.add(v)
        mst_weight += w
        mst_edges.append((u, v, w))
        for nv, nw in graph.adj[v]:
            if nv not in visited:
                heapq.heappush(edges, (nw, v, nv))
    return mst_weight, mst_edges
