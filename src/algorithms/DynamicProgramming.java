import java.util.*;


public class DynamicProgramming{

   
    static final Map<String, Integer> PRIORITY_WEIGHT = Map.of(
            "Critical", 4,
            "High",     3,
            "Medium",   2,
            "Low",      1
    );

    
    static final int UNIT = 1000;

   
    static final List<String> TYPES =
            Arrays.asList("Maintenance", "Delivery", "Waste", "Security", "Grounds");

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
            return PRIORITY_WEIGHT.getOrDefault(priority, 1);
        }
    }

  
    static int[] buildBenefitCurve(List<ServiceRequest> reqs, int B) {
        int[] curve = new int[B + 1];             
        for (ServiceRequest r : reqs) {
            int w = (int) Math.ceil((double) r.cost / UNIT); 
            int v = r.value();                                
            
            for (int b = B; b >= w; b--) {
                curve[b] = Math.max(curve[b], curve[b - w] + v);
            }
        }
        return curve; 
        }

    
    public Map<String, Integer> optimize(List<ServiceRequest> requests, int totalBudget) {
        int n = TYPES.size();
        int B = totalBudget / UNIT;                 
        
        int[][] benefit = new int[n][];
        for (int t = 0; t < n; t++) {
            List<ServiceRequest> typeReqs = new ArrayList<>();
            for (ServiceRequest r : requests) {
                if (r.type.equals(TYPES.get(t))) typeReqs.add(r);
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

        Map<String, Integer> allocation = new LinkedHashMap<>();
        int remaining = B;
        for (int i = n; i >= 1; i--) {
            int a = give[i][remaining];
            allocation.put(TYPES.get(i - 1), a * UNIT); 
            remaining -= a;
        }

      
        Map<String, Integer> ordered = new LinkedHashMap<>();
        for (String ty : TYPES) ordered.put(ty, allocation.getOrDefault(ty, 0));
        return ordered;
    }

  
    public static void main(String[] args) {
        List<ServiceRequest> requests = new ArrayList<>();
        int id = 1;

        
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
        Map<String, Integer> plan = optimizer.optimize(requests, budget);

        System.out.println("=== DP BUDGET ALLOCATION (total budget = GHS " + budget + ") ===");
        int spent = 0;
        for (Map.Entry<String, Integer> e : plan.entrySet()) {
            System.out.printf("  %-12s : GHS %,d%n", e.getKey(), e.getValue());
            spent += e.getValue();
        }
        System.out.printf("  %-12s : GHS %,d%n", "TOTAL SPENT", spent);
        System.out.printf("  %-12s : GHS %,d%n", "RESERVE",     budget - spent);
    }
}
