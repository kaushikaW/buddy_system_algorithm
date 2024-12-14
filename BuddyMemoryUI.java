import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class BuddyMemoryUI {

    private static BuddySystem buddySystem;
    private static JPanel memoryPanel;
    private static JTable memoryTable;
    private static DefaultTableModel tableModel;
    private static int totalMemory;
    private static JLabel totalMemoryLabel;
    private static JLabel allocatedMemoryLabel;
    private static JLabel freeMemoryLabel;
    private static JLabel paddingMemoryLabel;


    public static void main(String[] args) {
        buddySystem = null;  // Set buddySystem to null initially
        totalMemory = 0; // Initialize totalMemory to 0

        JFrame frame = new JFrame("Buddy Memory Allocation");
        frame.setSize(900, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        memoryPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                if (buddySystem == null) {
                    return; // Do nothing if buddySystem is not initialized
                }

                int width = getWidth();
                int height = 150; // Limit height of memory panel
                int x = 10;
                int y = 10;

                List<BuddySystem.Block> memoryBlocks = buddySystem.getMemoryBlocks();
                for (BuddySystem.Block block : memoryBlocks) {
                    int blockWidth = (int) ((block.blockSize / (double) totalMemory) * (width - 20));
                    g.setColor(block.isFree ? Color.GREEN : Color.RED);
                    g.fillRect(x, y, blockWidth, 30);
                    g.setColor(Color.BLACK);
                    g.drawRect(x, y, blockWidth, 30);
                    g.drawString(block.blockSize + "", x + 5, y + 20);
                    if (!block.isFree) {
                        Graphics2D g2d = (Graphics2D) g;
                        g.setColor(Color.BLUE);
                        g2d.setFont(new Font("Arial", Font.BOLD, 12));
                        g.drawString("" + block.allocatedSize, x + 5, y + 45);
                    }
                    x += blockWidth + 10;
                }
            }
        };

        memoryPanel.setPreferredSize(new Dimension(780, 150));

        JTextField sizeInput = new JTextField(5);
        sizeInput.setPreferredSize(new Dimension(100, 27));

        JButton allocateButton = new JButton("Allocate");

        JButton deallocateButton = new JButton("Deallocate");
        JLabel statusLabel = new JLabel("Status: Ready");
        statusLabel.setFont(new Font("Arial", Font.BOLD, 20));

        JTextField totalMemoryInput = new JTextField(5);
        totalMemoryInput.setPreferredSize(new Dimension(100, 27));

        JButton setMemoryButton = new JButton("Set Total Memory");
        JButton freeAllButton = new JButton("Free All");

        setMemoryButton.addActionListener(e -> {
            try {
                int newTotalMemory = Integer.parseInt(totalMemoryInput.getText());
                if (isPowerOfTwo(newTotalMemory)) {
                    totalMemory = newTotalMemory;
                    buddySystem = new BuddySystem(totalMemory); // Initialize buddy system only after user input
                    statusLabel.setText("Status: Total memory set to " + totalMemory);
                    memoryPanel.repaint();
                    updateTable();
                } else {
                    statusLabel.setText("Status: Total memory must be a power of 2");
                }
            } catch (Exception ex) {
                statusLabel.setText("Status: Invalid input for total memory");
            }
        });

        allocateButton.addActionListener(e -> {
            if (buddySystem == null) {
                statusLabel.setText("Status: Please set total memory first");
                return;
            }
            try {
                int size = Integer.parseInt(sizeInput.getText());
                boolean success = buddySystem.allocate(size);
                if (success) {
                    statusLabel.setText("Status: Allocation successful");
                } else {
                    statusLabel.setText("Status: Allocation failed - insufficient memory");
                }
            } catch (Exception ex) {
                statusLabel.setText("Status: Allocation failed - invalid input");
            }
            memoryPanel.repaint();
            updateTable();
        });

        deallocateButton.addActionListener(e -> {
            if (buddySystem == null) {
                statusLabel.setText("Status: Please set total memory first");
                return;
            }
            try {
                int size = Integer.parseInt(sizeInput.getText());
                boolean success = buddySystem.deallocate(size);
                if (success) {
                    statusLabel.setText("Status: Deallocation successful");
                } else {
                    statusLabel.setText("Status: Deallocation failed - block not found");
                }
            } catch (Exception ex) {
                statusLabel.setText("Status: Deallocation failed - invalid input");
            }
            memoryPanel.repaint();
            updateTable();
        });

        freeAllButton.addActionListener(e -> {
            if (buddySystem == null) {
                statusLabel.setText("Status: Please set total memory first");
                return;
            }
            buddySystem.freeAll();  // Free all memory blocks
            statusLabel.setText("Status: All memory deallocated");
            memoryPanel.repaint();
            updateTable();
        });

        // Left Panel (Allocate, Deallocate, Size Input)
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        leftPanel.add(new JLabel("Size (KB) :"));
        leftPanel.add(sizeInput);
        leftPanel.add(allocateButton);
        leftPanel.add(deallocateButton);

        // Right Panel (Set Total Memory, Add Memory, Free All)
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.add(new JLabel("Total Memory (KB) :"));
        rightPanel.add(totalMemoryInput);
        rightPanel.add(setMemoryButton);
        rightPanel.add(freeAllButton);  // Add Free All button

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BorderLayout());
        controlPanel.add(leftPanel, BorderLayout.WEST);
        controlPanel.add(rightPanel, BorderLayout.EAST);

        // Memory statistics panel
        JPanel statsPanel = new JPanel(new GridLayout(3, 1));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0)); // Add bottom margin

        totalMemoryLabel = new JLabel("Total Memory: 0 KB");
        totalMemoryLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        allocatedMemoryLabel = new JLabel("Allocated Memory: 0 KB");
        allocatedMemoryLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        freeMemoryLabel = new JLabel("Free Memory: 0 KB");
        freeMemoryLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        statsPanel.add(totalMemoryLabel);
        statsPanel.add(allocatedMemoryLabel);
        statsPanel.add(freeMemoryLabel);


        // Table to display memory status
        String[] columnNames = {"Block No", "Block Size", "Allocated Size", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0);
        memoryTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(memoryTable);
        tableScrollPane.setPreferredSize(new Dimension(780, 200));

        // Panel to combine graphical representation and table
        JPanel memoryDisplayPanel = new JPanel();
        memoryDisplayPanel.setLayout(new BorderLayout());
        memoryDisplayPanel.add(memoryPanel, BorderLayout.NORTH);
        memoryDisplayPanel.add(statsPanel, BorderLayout.CENTER);
        memoryDisplayPanel.add(tableScrollPane, BorderLayout.SOUTH);

        JScrollPane scrollPane = new JScrollPane(memoryDisplayPanel);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(controlPanel, BorderLayout.SOUTH);
        frame.add(statusLabel, BorderLayout.NORTH);

        frame.setVisible(true);

        updateTable();
    }

    private static void updateTable() {
        if (buddySystem == null) {
            return; // Don't update if buddySystem is not initialized
        }
        tableModel.setRowCount(0);
        List<BuddySystem.Block> memoryBlocks = buddySystem.getMemoryBlocks();
        int totalAllocated = 0;
        for (int i = 0; i < memoryBlocks.size(); i++) {
            BuddySystem.Block block = memoryBlocks.get(i);
            String status = block.isFree ? "Free" : "Allocated";
            tableModel.addRow(new Object[]{i + 1, block.blockSize, block.allocatedSize, status});
            if (!block.isFree) {
                totalAllocated += block.allocatedSize;
            }
        }
        int totalFree = totalMemory - totalAllocated;
        totalMemoryLabel.setText("Total Memory: " + totalMemory + " KB");
        allocatedMemoryLabel.setText("Allocated Memory: " + totalAllocated + " KB");
        freeMemoryLabel.setText("Free Memory: " + totalFree + " KB");
    }

    private static boolean isPowerOfTwo(int number) {
        return (number > 0) && ((number & (number - 1)) == 0);
    }
}
