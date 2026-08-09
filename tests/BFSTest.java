
import algorithms.BFS;
import datastructures.CustomArrayList;
import datastructures.CustomGraph;

public class BFSTest {
    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("  BFS.java - Unit Tests & Traces (6 tests)");
        System.out.println("==================================================\n");

        test1_linearChainVisitsInOrder();
        test2_branchingGraphVisitsAllInLevelOrder();
        test3_disconnectedComponentIsUnreachable();
        test4_singleIsolatedVertex();
        test5_cycleVisitsEachVertexExactlyOnce();
        test6_invalidStartVertexThrows();

        System.out.println("==================================================");
        System.out.println("RESULTS: " + passed + " passed, " + failed + " failed");
        System.out.println("==================================================");
    }

    private static void test1_linearChainVisitsInOrder() {
        System.out.println("Test 1: Linear chain 0-1-2-3");
        CustomGraph graph = new CustomGraph(4, false);
        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 1);
        graph.addEdge(2, 3, 1);

        CustomArrayList<Integer> order = BFS.run(graph, 0);
        System.out.println("  Trace: BFS.run(0) = " + order);

        // Expected: [0, 1, 2, 3]
        boolean ok = order.size() == 4
                && order.get(0) == 0
                && order.get(1) == 1
                && order.get(2) == 2
                && order.get(3) == 3;
        assertCondition("test1_linearChainVisitsInOrder", ok,
                "expected [0,1,2,3], got " + order);
        System.out.println();
    }

    private static void test2_branchingGraphVisitsAllInLevelOrder() {
        System.out.println("Test 2: Branching graph — level-order property");
        CustomGraph graph = new CustomGraph(6, false);
        graph.addEdge(0, 1, 1);
        graph.addEdge(0, 2, 1);
        graph.addEdge(1, 3, 1);
        graph.addEdge(2, 4, 1);
        graph.addEdge(4, 5, 1);

        CustomArrayList<Integer> order = BFS.run(graph, 0);
        System.out.println("  Trace: BFS.run(0) = " + order);

        boolean allSix = order.size() == 6;
        boolean levelOrder = indexOf(order, 1) < indexOf(order, 3)
                          && indexOf(order, 2) < indexOf(order, 4);
        assertCondition("test2_visitsAllSixVertices", allSix,
                "expected 6 vertices, got " + order.size());
        assertCondition("test2_levelOrderProperty", levelOrder,
                "expected level-1 nodes (1,2) before children (3,4), got " + order);
        System.out.println();
    }

    private static void test3_disconnectedComponentIsUnreachable() {
        System.out.println("Test 3: Disconnected graph — unreachable vertices skipped");
        CustomGraph graph = new CustomGraph(5, false);
        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 1);
        graph.addEdge(3, 4, 1);

        CustomArrayList<Integer> order = BFS.run(graph, 0);
        System.out.println("  Trace: BFS.run(0) = " + order);

        boolean ok = !containsValue(order, 3) && !containsValue(order, 4);
        assertCondition("test3_disconnectedComponentIsUnreachable", ok,
                "expected 3 and 4 to be unreachable from 0, got " + order);
        System.out.println();
    }

    private static void test4_singleIsolatedVertex() {
        System.out.println("Test 4: Isolated vertex, no edges");
        CustomGraph graph = new CustomGraph(3, false);
        graph.addVertex(0);

        CustomArrayList<Integer> order = BFS.run(graph, 0);
        System.out.println("  Trace: BFS.run(0) = " + order);
        boolean ok = order.size() == 1 && order.get(0) == 0;
        assertCondition("test4_singleIsolatedVertex", ok,
                "expected [0], got " + order);
        System.out.println();
    }

    private static void test5_cycleVisitsEachVertexExactlyOnce() {
        System.out.println("Test 5: Cyclic graph 0-1-2-3-0");
        CustomGraph graph = new CustomGraph(4, false);
        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 1);
        graph.addEdge(2, 3, 1);
        graph.addEdge(3, 0, 1);

        CustomArrayList<Integer> order = BFS.run(graph, 0);
        System.out.println("  Trace: BFS.run(0) = " + order);

        // all 4 distinct
        java.util.Set<Integer> unique = new java.util.HashSet<>();
        for (int i = 0; i < order.size(); i++) unique.add(order.get(i));
        assertCondition("test5_cycleVisitsEachVertexOnce", unique.size() == 4,
                "expected 4 unique vertices, got " + unique.size());
        System.out.println();
    }

    private static void test6_invalidStartVertexThrows() {
        System.out.println("Test 6: Invalid start vertex (out of range)");
        CustomGraph graph = new CustomGraph(3, false);
        graph.addEdge(0, 1, 1);

        boolean threw = false;
        try {
            BFS.run(graph, 10);
        } catch (IndexOutOfBoundsException e) {
            threw = true;
            System.out.println("  Trace: caught expected exception -> " + e.getMessage());
        }
        assertCondition("test6_invalidStartVertexThrows", threw,
                "expected IndexOutOfBoundsException for vertex 10 on a 3-vertex graph");
        System.out.println();
    }

    // ── Helpers ───────────────────────────────────────────────────────

    private static int indexOf(CustomArrayList<Integer> list, int value) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) == value) return i;
        }
        return -1;
    }

    private static boolean containsValue(CustomArrayList<Integer> list, int value) {
        return indexOf(list, value) >= 0;
    }

    private static void assertCondition(String testName, boolean condition, String detail) {
        if (condition) {
            System.out.println("  [PASS] " + testName);
            passed++;
        } else {
            System.out.println("  [FAIL] " + testName + " -- " + detail);
            failed++;
        }
    }
}