# ComPortControl
## Steering Motor Control Program

# Configuration
> Put `rxtxParallel.dll` and `rxtxSerial.dll` into `{$JAVA_JRE_PATH}/bin` 
> In Eclipse, Project->Properties->Java Build Path->Libraries->Add JARS,
add the `RXTXcomm.jar` into the Libraries. 

# Process and Procedure
+ **Step 1:**  Read in commands from **.txt** for controling the steering motor corresponding pins
+ **Step 2:**  Create the same number of threads as COM ports, where one thread handles one COM ports business
+ **Step 3:**  Distribute the Commands to each thread. Each thread only get and execute its own commands, while leave other commands unconsumed.

## Files and Directories
> **`cmdReader`**
>> `CmdReader.java`: The major file for **`.txt`** commands read in and storing;<br>
>> **`ReadInCmd.java`**: Read in the commands from **`.txt`** files; **Change the command `.txt` file path at here**<br>
>> `string2DataPacket.java`: Convert the string to standardlize commands.

> **`comPortWriter`**
>> `ContinueWrite.java`: The main file contains the **main** function for all program logic process; **Create threads corresponding to COM number at here** <br>
>> `ComPortDataPackage.java`: Define the data packet format for steering motor control;<br>
>> `Tuple.java`: Define the utilities data structure used by this projects.

> **`controlPanel`**
>> `ControlPanel.java`: **A legacy file** currently unused by this project, it is just a simple panel for controling the steering motors;<br>
>> `comPortWriter/ShowUtils.java`: **A legacy file** currently unused by this project.