"""
Generate synthetic Legon Campus dataset matching the team's schema:
service_requests.csv, resources.csv

This reads the valid string IDs from nodes.csv and generates the requests and resources using those IDs.
"""
import csv
import random
import os

random.seed(42)

DATA_DIR = os.path.join(os.path.dirname(__file__), "..", "..", "data_base")
NODES_FILE = os.path.join(DATA_DIR, "nodes.csv")
REQ_FILE = os.path.join(DATA_DIR, "service_requests.csv")
RES_FILE = os.path.join(DATA_DIR, "resources.csv")

REQUEST_TYPES = {
    "Maintenance": 0.40, "Delivery": 0.30, "Waste": 0.15,
    "Security": 0.10, "Grounds": 0.05,
}
PRIORITY_BY_TYPE = {
    "Security": "Critical", "Maintenance": "High",
    "Delivery": "Medium", "Waste": "Medium", "Grounds": "Low",
}

def load_node_ids(path):
    ids = []
    with open(path, "r") as f:
        reader = csv.reader(f)
        next(reader) # skip header
        for row in reader:
            if row:
                ids.append(row[0])
    return ids

def gen_requests(path, valid_ids, n=320):
    types = list(REQUEST_TYPES.keys())
    weights = list(REQUEST_TYPES.values())
    rows = []
    for i in range(1, n + 1):
        rtype = random.choices(types, weights=weights)[0]
        loc = random.choice(valid_ids)
        priority = PRIORITY_BY_TYPE[rtype]
        day = random.randint(1, 28)
        hour = random.randint(6, 20)
        deadline = f"2026-08-{day:02d} {hour:02d}:00"
        
        # We also need to map these to the exact CSV columns for SERVICE_REQUESTS
        # request_id,request_type,requester_location_id,priority,status,date_created,deadline,assigned_team,required_resource_type,estimated_duration_hours,description
        status = random.choice(["Pending", "Assigned", "In Progress"])
        date_created = f"2026-08-{day-1:02d} {hour:02d}:00" if day > 1 else f"2026-07-28 {hour:02d}:00"
        team = "Maintenance Team"
        resource_type = "Personnel"
        duration = random.randint(1, 5)
        description = f"Routine {rtype} task at {loc}"
        
        rows.append([i, rtype, loc, priority, status, date_created, deadline, team, resource_type, duration, description])
        
    with open(path, "w", newline="") as f:
        w = csv.writer(f)
        w.writerow(["request_id", "request_type", "requester_location_id", "priority", "status", "date_created", "deadline", "assigned_team", "required_resource_type", "estimated_duration_hours", "description"])
        w.writerows(rows)
    return rows

def gen_resources(path, valid_ids, n=30):
    types = ["Personnel", "Vehicle", "Equipment"]
    names = {
        "Personnel": ["Maintenance Team", "Cleaning Crew", "Security Team"],
        "Vehicle": ["Waste Truck", "Delivery Van", "Maintenance Truck", "Ambulance"],
        "Equipment": ["Power Generator", "Water Pump", "Hydraulic Lift"],
    }
    rows = []
    for i in range(1, n + 1):
        rtype = random.choices(types, weights=[0.4, 0.35, 0.25])[0]
        name = f"{random.choice(names[rtype])} {i}"
        capacity = random.choice([3, 4, 2000, 5000, 1]) if rtype != "Personnel" else random.randint(2, 4)
        loc = random.choice(valid_ids)
        
        # resource_id,resource_name,resource_type,capacity,current_location_id,availability_status,cost_per_hour
        status = random.choice(["Available", "In Use", "Maintenance"])
        cost = random.randint(10, 50)
        
        rows.append([i, name, rtype, capacity, loc, status, cost])
    with open(path, "w", newline="") as f:
        w = csv.writer(f)
        w.writerow(["resource_id", "resource_name", "resource_type", "capacity", "current_location_id", "availability_status", "cost_per_hour"])
        w.writerows(rows)
    return rows

if __name__ == "__main__":
    valid_ids = load_node_ids(NODES_FILE)
    reqs = gen_requests(REQ_FILE, valid_ids)
    res = gen_resources(RES_FILE, valid_ids)
    print(f"Generated {len(reqs)} requests and {len(res)} resources based on {len(valid_ids)} nodes.")
