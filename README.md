# ComPortControl
## Steering Motor Control Program
+ **Step 1:**  Read in commands from **.txt** for controling the steering motor corresponding pins
+ **Step 2:**  Create threads corresponding number of COM ports, where one thread handles one COM ports
+ **Step 3:**   Distribute the Commands to each thread. Each thread only get and execute its own commands, while leave other commands unconsumed.