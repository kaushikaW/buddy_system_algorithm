import java.util.ArrayList;
import java.util.List;

public  class BuddySystem {

    // Inner class to represent a memory block
    static class Block {

        // Total size of the block
        int blockSize;

        // Allocated size of the block
        int allocatedSize;

        // Indicates whether the block is free or not
        boolean isFree;

        Block(int blockSize) {
            this.blockSize = blockSize;
            this.allocatedSize = 0;
            this.isFree = true;
        }

        @Override
        public String toString() {
            return "Block {block_size: " + blockSize + ", allocated_size: " + allocatedSize + ", free_space: " + (blockSize - allocatedSize) + "}";
        }
    }

    // List to store memory blocks
    private List<Block> memoryBlocks;

    public BuddySystem(int totalMemorySize) {
        memoryBlocks = new ArrayList<>();
        memoryBlocks.add(new Block(totalMemorySize));
    }

    // Method to get the list of memory blocks
    public List<Block> getMemoryBlocks() {
        return memoryBlocks;
    }

    // Method to allocate memory of a specified size
    public boolean allocate(int size) {

        // Traverse the list of memory blocks to find a suitable free block
        for (int i = 0; i < memoryBlocks.size(); i++) {
            Block block = memoryBlocks.get(i);

            // Split the block if necessary, until it is large enough to allocate the memory
            if (block.isFree && block.blockSize >= size) {
                while (block.blockSize / 2 >= size) {
                    splitBlock(i);
                    block = memoryBlocks.get(i);
                }
                block.isFree = false;
                block.allocatedSize = size;
                return true;
            }
        }
        return false;
    }

    // Method to deallocate memory of a specified size
    public boolean deallocate(int size) {

        // Traverse the list of memory blocks to find the allocated block to deallocate
        for (int i = 0; i < memoryBlocks.size(); i++) {
            Block block = memoryBlocks.get(i);
            if (!block.isFree && block.allocatedSize == size) {
                block.isFree = true;
                block.allocatedSize = 0;
                mergeBlocks();
                return true;
            }
        }
        return false;
    }

    // Method to free all memory blocks
    public void freeAll() {
        for (Block block : memoryBlocks) {
            block.isFree = true;
            block.allocatedSize = 0;
        }
        mergeBlocks();
    }

    // Helper method to split a memory block into two smaller blocks
    private void splitBlock(int index) {
        Block block = memoryBlocks.get(index);
        int newSize = block.blockSize / 2;
        Block buddy = new Block(newSize);

        block.blockSize = newSize;
        memoryBlocks.add(index + 1, buddy);
    }

    // Helper method to merge adjacent free blocks of the same size
    private void mergeBlocks() {
        for (int i = 0; i < memoryBlocks.size() - 1; i++) {
            Block current = memoryBlocks.get(i);
            Block next = memoryBlocks.get(i + 1);

            if (current.isFree && next.isFree && current.blockSize == next.blockSize) {
                current.blockSize *= 2;
                memoryBlocks.remove(i + 1);
                i--;
            }
        }
    }
}
