-- University of Ghana Legon Campus
-- Navigation & Facilities Management System
-- Official Schema matching the Data Dictionary (M1)

PRAGMA foreign_keys = ON;

DROP TABLE IF EXISTS SERVICE_REQUESTS;
DROP TABLE IF EXISTS ROADS;
DROP TABLE IF EXISTS RESOURCES;
DROP TABLE IF EXISTS LOCATIONS;

-- ===================== LOCATIONS =====================
CREATE TABLE LOCATIONS (
    id              TEXT PRIMARY KEY,
    name            TEXT NOT NULL,
    category        TEXT NOT NULL,
    latitude        REAL NOT NULL,
    longitude       REAL NOT NULL
);

-- ===================== ROADS =====================
CREATE TABLE ROADS (
    source          TEXT NOT NULL,
    target          TEXT NOT NULL,
    distance_km     REAL NOT NULL,
    distance_m      REAL NOT NULL,
    FOREIGN KEY (source) REFERENCES LOCATIONS(id),
    FOREIGN KEY (target) REFERENCES LOCATIONS(id)
);

-- ===================== RESOURCES =====================
CREATE TABLE RESOURCES (
    resource_id         INTEGER PRIMARY KEY,
    resource_name       TEXT NOT NULL,
    resource_type       TEXT NOT NULL,
    capacity            INTEGER NOT NULL,
    current_location_id TEXT,
    availability_status TEXT NOT NULL,
    cost_per_hour       REAL NOT NULL CHECK (cost_per_hour >= 0),
    FOREIGN KEY (current_location_id) REFERENCES LOCATIONS(id)
);

-- ===================== SERVICE_REQUESTS =====================
CREATE TABLE SERVICE_REQUESTS (
    request_id               INTEGER PRIMARY KEY,
    request_type             TEXT NOT NULL,
    requester_location_id    TEXT NOT NULL,
    priority                 TEXT NOT NULL,
    status                   TEXT NOT NULL,
    date_created             TEXT NOT NULL,
    deadline                 TEXT NOT NULL,
    assigned_team            TEXT NOT NULL,
    required_resource_type   TEXT NOT NULL,
    estimated_duration_hours REAL NOT NULL,
    description              TEXT NOT NULL,
    FOREIGN KEY (requester_location_id) REFERENCES LOCATIONS(id)
);

-- Useful indexes
CREATE INDEX idx_roads_start ON ROADS(start_location);
CREATE INDEX idx_roads_end   ON ROADS(end_location);
CREATE INDEX idx_requests_status ON SERVICE_REQUESTS(status);
CREATE INDEX idx_requests_priority ON SERVICE_REQUESTS(priority);
CREATE INDEX idx_resources_status ON RESOURCES(availability_status);
