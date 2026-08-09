public class DynamicProgramming {

    static final int UNIT = 1000;

   
    static final String[] TYPES = {"Maintenance", "Delivery", "Waste", "Security", "Grounds"};

    static int priorityWeight(String priority) {
        switch (priority) {
            case "Critical": return 4;
            case "High":     return 3;
            case "Medium":   return 2;
            case "Low":      return 1;
            default:         return 1;
        }
    }

  
    static class ServiceRequest {
        int    id;
        String type;      
        String priority;  
        int    cost;      

        ServiceRequest(int id, String type, String priority, int cost) {
            this.id = id;
            this.type = type;
            this.priority = priority;
            this.cost = cost;
        }

       
        int value() {
            return priorityWeight(priority);
        }
    }

    
    static class RequestArray {
        private ServiceRequest[] data;
        private int size;

        RequestArray() {
            data = new ServiceRequest[4];
            size = 0;
        }

        void add(ServiceRequest r) {
            if (size == data.length) {
                ServiceRequest[] bigger = new ServiceRequest[data.length * 2];
                for (int i = 0; i < size; i++) bigger[i] = data[i];
                data = bigger;
            }
            data[size++] = r;
        }

        ServiceRequest get(int i) {
            return data[i];
        }

        int size() {
            return size;
        }
    }

    static class Allocation {
        String[] types;
        int[] amounts;

        Allocation(int n) {
            types = new String[n];
            amounts = new int[n];
        }

        void set(int index, String type, int amount) {
            types[index] = type;
            amounts[index] = amount;
        }

        int get(String type) {
            for (int i = 0; i < types.length; i++) {
                if (types[i].equals(type)) return amounts[i];
            }
            return 0;
        }

        int size() {
            return types.length;
        }
    }

    
    static int[] buildBenefitCurve(RequestArray reqs, int B) {
        int[] curve = new int[B + 1];  

        for (int r = 0; r < reqs.size(); r++) {
            ServiceRequest req = reqs.get(r);
            int w = (int) Math.ceil((double) req.cost / UNIT);
            int v = req.value();
            for (int b = B; b >= w; b--) {
                curve[b] = Math.max(curve[b], curve[b - w] + v);
            }
        }
        return curve;
    }

   
    public Allocation optimize(RequestArray requests, int totalBudget) {
        int n = TYPES.length;
        int B = totalBudget / UNIT;                 

    
        int[][] benefit = new int[n][];
        for (int t = 0; t < n; t++) {
            RequestArray typeReqs = new RequestArray();
            for (int r = 0; r < requests.size(); r++) {
                ServiceRequest req = requests.get(r);
                if (req.type.equals(TYPES[t])) typeReqs.add(req);
            }
            benefit[t] = buildBenefitCurve(typeReqs, B);
        }

        
        int[][] dp   = new int[n + 1][B + 1];       
        int[][] give = new int[n + 1][B + 1];      

        for (int i = 1; i <= n; i++) {
            for (int b = 0; b <= B; b++) {
                int best = 0, bestGive = 0;
                for (int a = 0; a <= b; a++) {              
                    int candidate = dp[i - 1][b - a] + benefit[i - 1][a];
                    if (candidate > best) {
                        best = candidate;
                        bestGive = a;
                    }
                }
                dp[i][b]   = best;
                give[i][b] = bestGive;
            }
        }

       
        Allocation allocation = new Allocation(n);
        int remaining = B;
        for (int i = n; i >= 1; i--) {
            int a = give[i][remaining];
            allocation.set(i - 1, TYPES[i - 1], a * UNIT);  
            remaining -= a;
        }

        return allocation;
    }

   
    public static void main(String[] args) {
        RequestArray requests = new RequestArray();
        int id = 1;

        // Maintenance backlog (electrical / plumbing / AC ...)
        requests.add(new ServiceRequest(id++, "Maintenance", "Critical", 40000));
        requests.add(new ServiceRequest(id++, "Maintenance", "High",     35000));
        requests.add(new ServiceRequest(id++, "Maintenance", "High",     25000));
        requests.add(new ServiceRequest(id++, "Maintenance", "Medium",   20000));
        requests.add(new ServiceRequest(id++, "Maintenance", "Low",      15000));

        // Delivery
        requests.add(new ServiceRequest(id++, "Delivery", "High",   30000));
        requests.add(new ServiceRequest(id++, "Delivery", "Medium", 25000));
        requests.add(new ServiceRequest(id++, "Delivery", "Medium", 20000));
        requests.add(new ServiceRequest(id++, "Delivery", "Low",    15000));

        // Waste
        requests.add(new ServiceRequest(id++, "Waste", "High",   30000));
        requests.add(new ServiceRequest(id++, "Waste", "Medium", 20000));
        requests.add(new ServiceRequest(id++, "Waste", "Low",    15000));

        // Security
        requests.add(new ServiceRequest(id++, "Security", "Critical", 35000));
        requests.add(new ServiceRequest(id++, "Security", "High",     20000));
        requests.add(new ServiceRequest(id++, "Security", "Low",      10000));

        // Grounds
        requests.add(new ServiceRequest(id++, "Grounds", "Medium", 15000));
        requests.add(new ServiceRequest(id++, "Grounds", "Low",    10000));
        requests.add(new ServiceRequest(id++, "Grounds", "Low",     8000));

        int budget = 288000;

        DynamicProgramming optimizer = new DynamicProgramming();
        Allocation plan = optimizer.optimize(requests, budget);

        System.out.println("=== DP BUDGET ALLOCATION (total budget = GHS " + budget + ") ===");
        int spent = 0;
        for (int i = 0; i < plan.size(); i++) {
            System.out.printf("  %-12s : GHS %,d%n", plan.types[i], plan.amounts[i]);
            spent += plan.amounts[i];
        }
        System.out.printf("  %-12s : GHS %,d%n", "TOTAL SPENT", spent);
        System.out.printf("  %-12s : GHS %,d%n", "RESERVE",     budget - spent);
    }
}