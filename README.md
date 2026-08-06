# Septagon02 DSA Project

Data Structures and Algorithms group assignment implementation by Team Septagon. The current module demonstrates custom in-memory structures using the Ghana Smart Service Operations dataset.

## Requirements

- Java Development Kit (JDK) 21 or later. Confirm it is available with:

  ```powershell
  java -version
  javac -version
  ```

## Run the data-structure demos manually

Run the following commands from the project root (`septagon02-dsa-project`) in PowerShell. The first command compiles all current implementations into the `out` directory; the remaining commands run each demo.

```powershell
javac -d out src/datastructures/CustomHeap.java src/datastructures/CustomRedBlackTree.java src/datastructures/DispatchDeque.java src/datastructures/ResourceBST.java

java -cp out datastructures.DispatchDeque
java -cp out datastructures.CustomHeap
java -cp out datastructures.ResourceBST
java -cp out datastructures.CustomRedBlackTree
```

Each program reads its input from the corresponding CSV in `data_base`: `roads.csv`, `service_requests.csv`, `resources.csv`, or `locations.csv`. No command-line arguments or database setup is needed; run the commands from the project root so these relative paths resolve correctly.

## Included implementations

- `DispatchDeque` — doubly linked dispatch queue for road segments.
- `CustomHeap` — array-backed maximum-priority queue for service requests.
- `ResourceBST` — binary search tree for resource lookup and ordered traversal.
- `CustomRedBlackTree` — self-balancing red-black tree for location lookup.

## Team workflow

- Do not push directly to `main`.
- Create your assigned branch from `main` and open a pull request for review.
- Add `Closes #<issue_number>` to the PR description when linking an issue.
