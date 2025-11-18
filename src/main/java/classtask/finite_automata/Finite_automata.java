package classtask.finite_automata;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

/**
 * Simple DFA GUI for Regular Expression: (g + gg + ggg)* mm + hg + ggh
 * This program tests if a string is accepted by the DFA
 */
public class Finite_automata extends JFrame {
    
    // Input and output fields
    private JTextField inputField;
    private JTextArea outputArea;
    private JButton testButton;
    private JButton clearButton;
    private JButton showTableButton;
    
    // Constructor - sets up the window
    public Finite_automata() {
        setTitle("Group 7 FA - Theory of Automata Project");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setupGUI();
    }

    // Setup the GUI components
    private void setupGUI() {
        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(240, 240, 240));
        
        // Top section
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout(5, 5));
        topPanel.setBackground(new Color(70, 130, 180));
        
        // Title label
        JLabel titleLabel = new JLabel("Regular Expression: (g + gg + ggg)* mm + hg + ggh");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 15, 5, 15));
        topPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Input section
        JPanel inputPanel = new JPanel();
        inputPanel.setBackground(new Color(70, 130, 180));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(5, 15, 15, 15));
        
        JLabel inputLabel = new JLabel("Enter Test String:");
        inputLabel.setForeground(Color.WHITE);
        inputLabel.setFont(new Font("Arial", Font.BOLD, 14));
        inputPanel.add(inputLabel);
        
        inputField = new JTextField(20);
        inputField.setFont(new Font("Arial", Font.PLAIN, 14));
        inputPanel.add(inputField);
        
        // Buttons
        testButton = new JButton("Test String");
        testButton.setBackground(new Color(60, 179, 113));
        testButton.setForeground(Color.WHITE);
        testButton.setFont(new Font("Arial", Font.BOLD, 13));
        testButton.setFocusPainted(false);
        inputPanel.add(testButton);
        
        showTableButton = new JButton("Show Table");
        showTableButton.setBackground(new Color(100, 149, 237));
        showTableButton.setForeground(Color.WHITE);
        showTableButton.setFont(new Font("Arial", Font.BOLD, 13));
        showTableButton.setFocusPainted(false);
        inputPanel.add(showTableButton);
        
        clearButton = new JButton("Clear");
        clearButton.setBackground(new Color(220, 20, 60));
        clearButton.setForeground(Color.WHITE);
        clearButton.setFont(new Font("Arial", Font.BOLD, 13));
        clearButton.setFocusPainted(false);
        inputPanel.add(clearButton);
        
        topPanel.add(inputPanel, BorderLayout.CENTER);
        mainPanel.add(topPanel, BorderLayout.NORTH);
        
        // Output area
        outputArea = new JTextArea();
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        outputArea.setEditable(false);
        outputArea.setBackground(Color.WHITE);
        outputArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Output"));
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Bottom info panel
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(240, 240, 240));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        JTextArea infoArea = new JTextArea();
        infoArea.setText("Valid Symbols: g, h, m\n" +
                        "Examples that ACCEPT: mm, gmm, ggmm, gggmm, hg, ggh\n" +
                        "Examples that REJECT: gh, gghg, mmg, hhg");
        infoArea.setEditable(false);
        infoArea.setBackground(new Color(240, 240, 240));
        infoArea.setFont(new Font("Arial", Font.PLAIN, 12));
        bottomPanel.add(infoArea);
        
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        // Button actions
        testButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                testString();
            }
        });
        
        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                outputArea.setText("");
            }
        });
        
        showTableButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayTransitionTable();
            }
        });
        
        inputField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                testString();
            }
        });
        
        // Show welcome message
        showWelcome();
    }

    // Show welcome message
    private void showWelcome() {
        outputArea.setText("");
        outputArea.append("===================================================\n");
        outputArea.append("        DFA SIMULATOR - GROUP 7\n");
        outputArea.append("===================================================\n\n");
        outputArea.append("Regular Expression: (g + gg + ggg)* mm + hg + ggh\n\n");
        outputArea.append("This DFA has 3 branches:\n");
        outputArea.append("  Branch A: Any g's followed by mm\n");
        outputArea.append("  Branch B: Exactly 'hg'\n");
        outputArea.append("  Branch C: Exactly 'ggh'\n\n");
        outputArea.append("Click 'Show Table' to see transitions.\n");
        outputArea.append("Enter a string and click 'Test String'.\n");
        outputArea.append("===================================================\n");
    }

    // Display the transition table
    private void displayTransitionTable() {
        outputArea.setText("");
        outputArea.append("===================================================\n");
        outputArea.append("           DFA TRANSITION TABLE\n");
        outputArea.append("===================================================\n\n");
        
        outputArea.append("BRANCH A: (g+gg+ggg)* mm\n");
        outputArea.append("---------------------------------------------------\n");
        outputArea.append("State       | Input g   | Input h   | Input m\n");
        outputArea.append("---------------------------------------------------\n");
        outputArea.append("A0 (start)  | A_Gloop   | DEAD      | A_M1\n");
        outputArea.append("A_Gloop     | A_Gloop   | DEAD      | A_M1\n");
        outputArea.append("A_M1        | DEAD      | DEAD      | A_MM (ACCEPT)\n");
        outputArea.append("A_MM        | DEAD      | DEAD      | DEAD\n\n");

        outputArea.append("BRANCH B: hg\n");
        outputArea.append("---------------------------------------------------\n");
        outputArea.append("State       | Input g   | Input h   | Input m\n");
        outputArea.append("---------------------------------------------------\n");
        outputArea.append("B0 (start)  | DEAD      | B_H       | DEAD\n");
        outputArea.append("B_H         | B_HG (ACCEPT) | DEAD  | DEAD\n");
        outputArea.append("B_HG        | DEAD      | DEAD      | DEAD\n\n");

        outputArea.append("BRANCH C: ggh\n");
        outputArea.append("---------------------------------------------------\n");
        outputArea.append("State       | Input g   | Input h   | Input m\n");
        outputArea.append("---------------------------------------------------\n");
        outputArea.append("C0 (start)  | C_G1      | DEAD      | DEAD\n");
        outputArea.append("C_G1        | C_G2      | DEAD      | DEAD\n");
        outputArea.append("C_G2        | DEAD      | C_GGH (ACCEPT) | DEAD\n");
        outputArea.append("C_GGH       | DEAD      | DEAD      | DEAD\n\n");
        
        outputArea.append("===================================================\n");
    }

    // Test the input string
    private void testString() {
        String input = inputField.getText().trim();
        
        // Check if empty
        if (input.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a test string!");
            return;
        }

        // Check for invalid characters
        for (int i = 0; i < input.length(); i++) {
            char ch = input.charAt(i);
            if (ch != 'g' && ch != 'h' && ch != 'm') {
                JOptionPane.showMessageDialog(this, 
                    "Invalid character: " + ch + "\nOnly g, h, m are allowed!");
                return;
            }
        }

        // Start simulation
        outputArea.setText("");
        outputArea.append("===================================================\n");
        outputArea.append("Testing String: " + input + "\n");
        outputArea.append("===================================================\n\n");

        // Initialize states for 3 branches
        int stateA = 0;  // Branch A state
        int stateB = 0;  // Branch B state
        int stateC = 0;  // Branch C state
        
        boolean deadA = false;  // Is branch A dead?
        boolean deadB = false;  // Is branch B dead?
        boolean deadC = false;  // Is branch C dead?

        // Print header
        outputArea.append("Step   | Branch A           | Branch B       | Branch C\n");
        outputArea.append("-------+--------------------+----------------+----------------\n");

        // Process each character
        for (int i = 0; i < input.length(); i++) {
            char ch = input.charAt(i);
            
            // Get state names before transition
            String beforeA = getStateNameA(stateA, deadA);
            String beforeB = getStateNameB(stateB, deadB);
            String beforeC = getStateNameC(stateC, deadC);

            // Branch A transitions
            if (!deadA) {
                if (stateA == 0) {
                    if (ch == 'g') {
                        stateA = 1;  // Go to g-loop
                    } else if (ch == 'm') {
                        stateA = 2;  // Saw first m
                    } else {
                        deadA = true;
                    }
                } else if (stateA == 1) {
                    if (ch == 'g') {
                        stateA = 1;  // Stay in g-loop
                    } else if (ch == 'm') {
                        stateA = 2;  // Saw first m
                    } else {
                        deadA = true;
                    }
                } else if (stateA == 2) {
                    if (ch == 'm') {
                        stateA = 3;  // Accept! Saw mm
                    } else {
                        deadA = true;
                    }
                } else if (stateA == 3) {
                    deadA = true;  // Already accepted, extra chars = dead
                }
            }

            // Branch B transitions
            if (!deadB) {
                if (stateB == 0) {
                    if (ch == 'h') {
                        stateB = 1;  // Saw h
                    } else {
                        deadB = true;
                    }
                } else if (stateB == 1) {
                    if (ch == 'g') {
                        stateB = 2;  // Accept! Saw hg
                    } else {
                        deadB = true;
                    }
                } else if (stateB == 2) {
                    deadB = true;  // Already accepted, extra chars = dead
                }
            }

            // Branch C transitions
            if (!deadC) {
                if (stateC == 0) {
                    if (ch == 'g') {
                        stateC = 1;  // Saw first g
                    } else {
                        deadC = true;
                    }
                } else if (stateC == 1) {
                    if (ch == 'g') {
                        stateC = 2;  // Saw second g
                    } else {
                        deadC = true;
                    }
                } else if (stateC == 2) {
                    if (ch == 'h') {
                        stateC = 3;  // Accept! Saw ggh
                    } else {
                        deadC = true;
                    }
                } else if (stateC == 3) {
                    deadC = true;  // Already accepted, extra chars = dead
                }
            }

            // Get state names after transition
            String afterA = getStateNameA(stateA, deadA);
            String afterB = getStateNameB(stateB, deadB);
            String afterC = getStateNameC(stateC, deadC);

            // Print transition
            String step = "[" + (i + 1) + ":" + ch + "]";
            outputArea.append(String.format("%-7s| %-18s | %-14s | %-14s\n",
                step, 
                beforeA + "->" + afterA,
                beforeB + "->" + beforeB,
                beforeC + "->" + afterC));
        }

        // Check if any branch accepted
        boolean acceptA = (!deadA && stateA == 3);
        boolean acceptB = (!deadB && stateB == 2);
        boolean acceptC = (!deadC && stateC == 3);

        outputArea.append("\n===================================================\n");
        outputArea.append("RESULTS:\n");
        outputArea.append("  Branch A (mm):  " + (acceptA ? "ACCEPTED" : "REJECTED") + "\n");
        outputArea.append("  Branch B (hg):  " + (acceptB ? "ACCEPTED" : "REJECTED") + "\n");
        outputArea.append("  Branch C (ggh): " + (acceptC ? "ACCEPTED" : "REJECTED") + "\n");
        
        boolean finalResult = acceptA || acceptB || acceptC;
        outputArea.append("\n===================================================\n");
        if (finalResult) {
            outputArea.append("       STRING ACCEPTED!\n");
        } else {
            outputArea.append("       STRING REJECTED!\n");
        }
        outputArea.append("===================================================\n");
    }

    // Get state name for Branch A
    private String getStateNameA(int state, boolean dead) {
        if (dead) {
            return "DEAD";
        }
        if (state == 0) {
            return "A0";
        } else if (state == 1) {
            return "A_Gloop";
        } else if (state == 2) {
            return "A_M1";
        } else if (state == 3) {
            return "A_MM(ACC)";
        }
        return "A?";
    }

    // Get state name for Branch B
    private String getStateNameB(int state, boolean dead) {
        if (dead) {
            return "DEAD";
        }
        if (state == 0) {
            return "B0";
        } else if (state == 1) {
            return "B_H";
        } else if (state == 2) {
            return "B_HG(ACC)";
        }
        return "B?";
    }

    // Get state name for Branch C
    private String getStateNameC(int state, boolean dead) {
        if (dead) {
            return "DEAD";
        }
        if (state == 0) {
            return "C0";
        } else if (state == 1) {
            return "C_G1";
        } else if (state == 2) {
            return "C_G2";
        } else if (state == 3) {
            return "C_GGH(ACC)";
        }
        return "C?";
    }

    // Main method to run the program
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Finite_automata gui = new Finite_automata();
                gui.setVisible(true);
            }
        });
    }
}