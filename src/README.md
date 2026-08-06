# Custom Data Structures Module

This folder contains the current zero-dependency Java implementations used by the Ghana Smart Service Operations Optimizer demo.

## Compile and run

From the project root, compile the four Java applications into `out`:

```powershell
javac -d out src/datastructures/CustomHeap.java src/datastructures/CustomRedBlackTree.java src/datastructures/DispatchDeque.java src/datastructures/ResourceBST.java
```

Then run any demo with the compiled output on the classpath:

```powershell
java -cp out datastructures.DispatchDeque
java -cp out datastructures.CustomHeap
java -cp out datastructures.ResourceBST
java -cp out datastructures.CustomRedBlackTree
```

No arguments are required. Each `main` method reads every record from its matching CSV file in `data_base`; run the command from the project root so the file paths resolve correctly.

## Components

- `DispatchDeque.java`: doubly linked deque for road-segment dispatching.
- `CustomHeap.java`: array-backed maximum-priority queue for service requests.
- `ResourceBST.java`: binary search tree for resources.
- `CustomRedBlackTree.java`: red-black tree for locations.
