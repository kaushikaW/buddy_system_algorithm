
# Buddy System Algorithm


The **Buddy System Memory Allocation** is an efficient memory management algorithm designed to facilitate allocation and deallocation of memory blocks dynamically. It ensures efficient memory utilization and minimizes fragmentation by dividing memory into blocks of sizes that are powers of two.

This program simulates the Buddy System Algorithm using the Java programming language. It provides a graphical user interface (GUI) built with Swing to visualize and interact with memory allocation and deallocation processes.

### Key Features of the Buddy System Simulation:
- **Memory Management**:
    - Supports setting total memory size (must be a power of 2).
    - Dynamic memory allocation and deallocation of user-defined sizes.
    - Allows bulk deallocation using the "Free All" button.

- **Visualization**:
    - Displays memory blocks dynamically using color to indicate allocated and free blocks.
    - Includes a real-time table view of memory blocks, showing each block's size, allocated size, and status.
    - Shows aggregate statistics on total memory, allocated memory, and free memory.

- **Real-Time Feedback**:
    - A status label that informs the user about actions taken (e.g., memory allocation success or error messages).

- **Error Handling**:
  - Prevents invalid inputs (e.g., negative numbers, sizes not powers of two, or exceeding available memory).
  - Provides clear feedback for allocation or deallocation failures.

### Steps to Run the Buddy System Simulation

1. Install Java Development Kit (JDK): Ensure you have JDK 8 or a later version installed on your computer.

2. Download or Clone the Program Code:
Obtain the source code of the Buddy System Simulation program. Ensure all necessary .java files are in the same directory.

3. Set Up the Java Project:

- Open your IDE.
- Create a new Java project.
- Copy the source code files into the project directory 
- Compile the the java class  `BuddyMemoryUI`
- your can use command line or java support IDE to compile and run the class  `BuddyMemoryUI`
- Now you can run the program

### User Instructions
1. Set Total Memory:
    - Enter a power-of-two value in the "Total Memory (KB)" input field and click "Set Total Memory."
    - Only proceed to allocate memory after setting the total memory.

2. Allocate Memory:
    - Specify the block size in the "Size (KB)" input field and click the "Allocate" button.
    - The memory block is reserved if sufficient memory is available.

3. Deallocate Memory:
    - Enter the size of the block to be deallocated in "Size (KB)" and click "Deallocate."
    - This will release the block of the specified size if found in the active allocations.

4. View Memory Actions:
    - Observe the visualization panel and table for real-time updates on memory usage.

5. Free All Memory:
    - Click the "Free All" button to clear all allocated memory blocks.



