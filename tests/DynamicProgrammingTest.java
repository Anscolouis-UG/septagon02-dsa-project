package tests;

import algorithms.DynamicProgramming;

public class DynamicProgrammingTest {

    static int passed = 0;
    static int failed = 0;

    public static void main(String[] args) {
        testPriorityWeight();
        testRequestArrayGrows();
        testBenefitCurveSimpleKnapsack();
        testAllocationGetLookup();
        testOptimizeRespectsBudget();
        testOptimizeKnownSample();
        testOptimizeZeroBudget();
        testOptimizeSingleCheapRequest();

        System.out.println();
        System.out.println("=== " + passed + " passed, " + failed + " failed ===");
        if (failed > 0) System.exit(1);
    }


    static void check(String name, boolean condition) {
        if (condition) {
            passed++;
            System.out.println("PASS - " + name);
        } else {
            failed++;
            System.out.println("FAIL - " + name);
        }
    }

    static void checkEquals(String name, int expected, int actual) {
        check(name + " (expected " + expected + ", got " + actual + ")", expected == actual);
    }


    static void testPriorityWeight() {
        checkEquals("priorityWeight Critical", 4, DynamicProgramming.priorityWeight("Critical"));
        checkEquals("priorityWeight High",     3, DynamicProgramming.priorityWeight("High"));
        checkEquals("priorityWeight Medium",   2, DynamicProgramming.priorityWeight("Medium"));
        checkEquals("priorityWeight Low",      1, DynamicProgramming.priorityWeight("Low"));
        checkEquals("priorityWeight unknown label defaults to 1", 1, DynamicProgramming.priorityWeight("Nonsense"));
    }

    static void testRequestArrayGrows() {
        DynamicProgramming.RequestArray arr = new DynamicProgramming.RequestArray();
        
        for (int i = 0; i < 10; i++) {
            arr.add(new DynamicProgramming.ServiceRequest(i, "Maintenance", "Low", 1000));
        }
        checkEquals("RequestArray size after 10 adds", 10, arr.size());
        boolean idsIntact = true;
        for (int i = 0; i < 10; i++) {
            if (arr.get(i).id != i) idsIntact = false;
        }
        check("RequestArray keeps insertion order after growing", idsIntact);
    }

    static void testBenefitCurveSimpleKnapsack() {
       
        DynamicProgramming.RequestArray reqs = new DynamicProgramming.RequestArray();
        reqs.add(new DynamicProgramming.ServiceRequest(1, "Waste", "Low", 1000));   
        reqs.add(new DynamicProgramming.ServiceRequest(2, "Waste", "Critical", 2000)); 

        int[] curve = DynamicProgramming.buildBenefitCurve(reqs, 3);
        checkEquals("benefit curve at budget 0", 0, curve[0]);
        checkEquals("benefit curve at budget 1", 1, curve[1]);
        checkEquals("benefit curve at budget 2", 4, curve[2]);
        checkEquals("benefit curve at budget 3", 5, curve[3]);
    }

    static void testAllocationGetLookup() {
        DynamicProgramming.Allocation alloc = new DynamicProgramming.Allocation(2);
        alloc.set(0, "Maintenance", 40000);
        alloc.set(1, "Delivery", 20000);
        checkEquals("Allocation.get finds Maintenance", 40000, alloc.get("Maintenance"));
        checkEquals("Allocation.get finds Delivery", 20000, alloc.get("Delivery"));
        checkEquals("Allocation.get returns 0 for unknown type", 0, alloc.get("Nonexistent"));
    }

    static DynamicProgramming.RequestArray sampleRequests() {
        DynamicProgramming.RequestArray requests = new DynamicProgramming.RequestArray();
        int id = 1;
        requests.add(new DynamicProgramming.ServiceRequest(id++, "Maintenance", "Critical", 40000));
        requests.add(new DynamicProgramming.ServiceRequest(id++, "Maintenance", "High",     35000));
        requests.add(new DynamicProgramming.ServiceRequest(id++, "Maintenance", "High",     25000));
        requests.add(new DynamicProgramming.ServiceRequest(id++, "Maintenance", "Medium",   20000));
        requests.add(new DynamicProgramming.ServiceRequest(id++, "Maintenance", "Low",      15000));
        requests.add(new DynamicProgramming.ServiceRequest(id++, "Delivery", "High",   30000));
        requests.add(new DynamicProgramming.ServiceRequest(id++, "Delivery", "Medium", 25000));
        requests.add(new DynamicProgramming.ServiceRequest(id++, "Delivery", "Medium", 20000));
        requests.add(new DynamicProgramming.ServiceRequest(id++, "Delivery", "Low",    15000));
        requests.add(new DynamicProgramming.ServiceRequest(id++, "Waste", "High",   30000));
        requests.add(new DynamicProgramming.ServiceRequest(id++, "Waste", "Medium", 20000));
        requests.add(new DynamicProgramming.ServiceRequest(id++, "Waste", "Low",    15000));
        requests.add(new DynamicProgramming.ServiceRequest(id++, "Security", "Critical", 35000));
        requests.add(new DynamicProgramming.ServiceRequest(id++, "Security", "High",     20000));
        requests.add(new DynamicProgramming.ServiceRequest(id++, "Security", "Low",      10000));
        requests.add(new DynamicProgramming.ServiceRequest(id++, "Grounds", "Medium", 15000));
        requests.add(new DynamicProgramming.ServiceRequest(id++, "Grounds", "Low",    10000));
        requests.add(new DynamicProgramming.ServiceRequest(id++, "Grounds", "Low",     8000));
        return requests;
    }

    static void testOptimizeRespectsBudget() {
        int budget = 288000;
        DynamicProgramming.Allocation plan = new DynamicProgramming().optimize(sampleRequests(), budget);
        int spent = 0;
        boolean allMultiplesOfUnit = true;
        for (int i = 0; i < plan.size(); i++) {
            spent += plan.amounts[i];
            if (plan.amounts[i] % DynamicProgramming.UNIT != 0) allMultiplesOfUnit = false;
        }
        check("optimize never overspends the budget", spent <= budget);
        check("every allocation is a multiple of UNIT (1000)", allMultiplesOfUnit);
    }

    static void testOptimizeKnownSample() {
        
        DynamicProgramming.Allocation plan = new DynamicProgramming().optimize(sampleRequests(), 288000);
        checkEquals("Maintenance allocation matches known-good run", 85000, plan.get("Maintenance"));
        checkEquals("Delivery allocation matches known-good run",    75000, plan.get("Delivery"));
        checkEquals("Waste allocation matches known-good run",       50000, plan.get("Waste"));
        checkEquals("Security allocation matches known-good run",    55000, plan.get("Security"));
        checkEquals("Grounds allocation matches known-good run",     23000, plan.get("Grounds"));
    }

    static void testOptimizeZeroBudget() {
        DynamicProgramming.Allocation plan = new DynamicProgramming().optimize(sampleRequests(), 0);
        boolean allZero = true;
        for (int i = 0; i < plan.size(); i++) {
            if (plan.amounts[i] != 0) allZero = false;
        }
        check("zero budget produces an all-zero allocation", allZero);
    }

    static void testOptimizeSingleCheapRequest() {
        DynamicProgramming.RequestArray requests = new DynamicProgramming.RequestArray();
        requests.add(new DynamicProgramming.ServiceRequest(1, "Grounds", "Low", 1000));
        DynamicProgramming.Allocation plan = new DynamicProgramming().optimize(requests, 1000);
        checkEquals("single 1000-cost request gets fully funded from a 1000 budget",
                1000, plan.get("Grounds"));
        checkEquals("all other types get nothing", 0, plan.get("Maintenance"));
    }
}
