"""
Generate synthetic Legon Campus dataset matching the team's schema:
locations.csv, roads.csv, service_requests.csv, resources.csv

This gives Priscilla (Performance Specialist) realistic data to run the
6 efficiency experiments against, until Samuella delivers the real dataset.
"""
import csv
import random
import math

random.seed(42)

BUILDINGS = [
    "Registry", "Balme Library", "Great Hall", "SRC Union Building",
    "Commonwealth Hall", "Mensah Sarbah Hall", "Volta Hall", "Legon Hall",
    "Jubilee Hall", "Akuafo Hall", "Physics Block", "Chemistry Block",
    "Biology Block", "Engineering School", "Business School",
    "School of Performing Arts", "School of Pharmacy", "Law School",
    "Legon Clinic", "Central Cafeteria", "Athletic Oval", "Sports Complex",
    "Parking Lot A", "Parking Lot B", "Parking Lot C",
    "Security Gate 1 (Main)", "Security Gate 2 (Okponglo)", "Security Gate 3 (Haatso)",
    "Water Treatment Plant", "Electrical Substation", "Waste Management Center",
    "Vehicle Maintenance Garage", "Vice Chancellor's Office", "ISSER Building",
    "Institute of African Studies", "School of Public Health", "Dental School",
    "Nursing School", "Optometry School", "Info Studies Dept", "Computer Science Dept",
    "Statistics Dept", "Mathematics Dept", "Geography Dept", "Sociology Dept",
    "Political Science Dept", "History Dept", "Economics Dept", "Philosophy Dept",
    "Night Market",
]
assert len(BUILDINGS) == 50, len(BUILDINGS)

REQUEST_TYPES = {
    "Maintenance": 0.40, "Delivery": 0.30, "Waste": 0.15,
    "Security": 0.10, "Grounds": 0.05,
}
PRIORITY_BY_TYPE = {
    "Security": "Critical", "Maintenance": "High",
    "Delivery": "Medium", "Waste": "Medium", "Grounds": "Low",
}

def gen_locations(path, n=50):
    # Fake a rough lat/lon grid around Legon (5.6506 N, -0.1875 E)
    rows = []
    for i, name in enumerate(BUILDINGS[:n], start=1):
        lat = 5.6506 + random.uniform(-0.01, 0.01)
        lon = -0.1875 + random.uniform(-0.01, 0.01)
        rows.append([i, name, round(lat, 5), round(lon, 5)])
    with open(path, "w", newline="") as f:
        w = csv.writer(f)
        w.writerow(["location_id", "name", "latitude", "longitude"])
        w.writerows(rows)
    return rows

def gen_roads(path, n_locations=50, min_roads=110):
    # Build a connected graph first (spanning tree), then add extra edges for realism
    edges = set()
    nodes = list(range(1, n_locations + 1))
    random.shuffle(nodes)
    connected = [nodes[0]]
    remaining = nodes[1:]
    for node in remaining:
        other = random.choice(connected)
        a, b = sorted((node, other))
        edges.add((a, b))
        connected.append(node)
    # add extra random edges until we have enough
    while len(edges) < min_roads:
        a, b = random.sample(range(1, n_locations + 1), 2)
        a, b = sorted((a, b))
        if a != b:
            edges.add((a, b))
    rows = []
    for i, (a, b) in enumerate(sorted(edges), start=1):
        distance_km = round(random.uniform(0.2, 3.0), 2)
        travel_time = round(distance_km * random.uniform(4, 8), 1)  # ~walking pace mins/km
        rows.append([i, a, b, distance_km, travel_time])
    with open(path, "w", newline="") as f:
        w = csv.writer(f)
        w.writerow(["road_id", "from_location_id", "to_location_id", "distance_km", "travel_time_minutes"])
        w.writerows(rows)
    return rows

def gen_requests(path, n=320, n_locations=50):
    types = list(REQUEST_TYPES.keys())
    weights = list(REQUEST_TYPES.values())
    rows = []
    for i in range(1, n + 1):
        rtype = random.choices(types, weights=weights)[0]
        loc = random.randint(1, n_locations)
        priority = PRIORITY_BY_TYPE[rtype]
        day = random.randint(1, 28)
        hour = random.randint(6, 20)
        deadline = f"2026-08-{day:02d} {hour:02d}:00"
        rows.append([i, rtype, loc, priority, deadline])
    with open(path, "w", newline="") as f:
        w = csv.writer(f)
        w.writerow(["request_id", "request_type", "requester_location_id", "priority", "deadline"])
        w.writerows(rows)
    return rows

def gen_resources(path, n=30, n_locations=50):
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
        loc = random.randint(1, n_locations)
        rows.append([i, name, rtype, capacity, loc])
    with open(path, "w", newline="") as f:
        w = csv.writer(f)
        w.writerow(["resource_id", "resource_name", "resource_type", "capacity", "current_location_id"])
        w.writerows(rows)
    return rows

if __name__ == "__main__":
    locs = gen_locations("locations.csv")
    roads = gen_roads("roads.csv")
    reqs = gen_requests("service_requests.csv")
    res = gen_resources("resources.csv")
    print(f"locations: {len(locs)}, roads: {len(roads)}, requests: {len(reqs)}, resources: {len(res)}")
