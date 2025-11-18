package classtask.finite_automata;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

/**
 * Group 7 DFA GUI for RE: (g + gg + ggg)* mm + hg + ggh
 * Implements three parallel branches:
 *   Branch A: (g+gg+ggg)* mm - accepts any sequence of g's followed by mm
 *   Branch B: hg - accepts exactly "hg"
 *   Branch C: ggh - accepts exactly "ggh"
 */
public class Finite_automataGUI extends JFrame {
    // UI Components
    private JTextField inputField;
    private JTextArea outputArea;
    private JButton testButton, clearButton, tableButton;
    
    // Color scheme
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color SUCCESS_COLOR = new Color(46, 204, 113);
    private static final Color DANGER_COLOR = new Color(231, 76, 60);
    private static final Color BG_COLOR = new Color(236, 240, 241);
    private static final Color OUTPUT_BG = new Color(255, 255, 255);

    public Finite_automataGUI() {
        setTitle("Group 7 - DFA Simulator");
        setSize(850, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initUI();
    }

    private void initUI() {
        Container c = getContentPane();
        c.setLayout(new BorderLayout(10, 10));
        c.setBackground(BG_COLOR);

        // Header Panel
        JPanel headerPanel = createHeaderPanel();
        c.add(headerPanel, BorderLayout.NORTH);

        // Output Area
        outputArea = new JTextArea();
        outputArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        outputArea.setEditable(false);
        outputArea.setBackground(OUTPUT_BG);
        outputArea.setMargin(new Insets(10, 10, 10, 10));
        JScrollPane scroll = new JScrollPane(outputArea);
        scroll.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
            "Simulation Output",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 13),
            PRIMARY_COLOR
        ));
        c.add(scroll, BorderLayout.CENTER);

        // Footer Panel
        JPanel footerPanel = createFooterPanel();
        c.add(footerPanel, BorderLayout.SOUTH);

        // Button Actions
        testButton.addActionListener(e -> simulateString());
        clearButton.addActionListener(e -> outputArea.setText(""));
        tableButton.addActionListener(e -> showTransitionTable());
        inputField.addActionListener(e -> simulateString());
        
        // Show welcome message
        showWelcomeMessage();
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(PRIMARY_COLOR);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Title
        JLabel titleLabel = new JLabel("Regular Expression: (g + gg + ggg)* mm + hg + ggh");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);
        panel.add(titleLabel, BorderLayout.NORTH);

        // Input Panel
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        inputPanel.setBackground(PRIMARY_COLOR);
        
        JLabel inputLabel = new JLabel("Test String:");
        inputLabel.setForeground(Color.WHITE);
        inputLabel.setFont(new Font("Arial", Font.BOLD, 13));
        inputPanel.add(inputLabel);

        inputField = new JTextField(25);
        inputField.setFont(new Font("Arial", Font.PLAIN, 14));
        inputPanel.add(inputField);

        testButton = createStyledButton("Simulate", SUCCESS_COLOR);
        tableButton = createStyledButton("Show Table", new Color(52, 152, 219));
        clearButton = createStyledButton("Clear", DANGER_COLOR);

        inputPanel.add(testButton);
        inputPanel.add(tableButton);
        inputPanel.add(clearButton);

        panel.add(inputPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createFooterPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_COLOR);
        panel.setBorder(new EmptyBorder(10, 15, 10, 15));

        JTextArea hints = new JTextArea(
            "Valid Symbols: g, h, m\n" +
            "Accepted: mm, gmm, ggmm, gggmm, hg, ggh\n" +
            "Rejected: gh, gghg, mmg, ghg"
        );
        hints.setEditable(false);
        hints.setBackground(BG_COLOR);
        hints.setFont(new Font("Arial", Font.PLAIN, 12));
        hints.setBorder(BorderFactory.createTitledBorder("Examples"));
        panel.add(hints, BorderLayout.CENTER);

        return panel;
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void showWelcomeMessage() {
        outputArea.setText(
            "╔════════════════════════════════════════════════════════════╗\n" +
            "║       DFA Simulator - Theory of Automata Project          ║\n" +
            "║                    Group 7                                 ║\n" +
            "╚════════════════════════════════════════════════════════════╝\n\n" +
            "Regular Expression: (g + gg + ggg)* mm + hg + ggh\n\n" +
            "This DFA accepts strings from three branches:\n" +
            "  • Branch A: (g+gg+ggg)* mm - Any g's followed by mm\n" +
            "  • Branch B: hg - Exactly 'hg'\n" +
            "  • Branch C: ggh - Exactly 'ggh'\n\n" +
            "Click 'Show Table' to view the transition table.\n" +
            "Enter a test string and click 'Simulate' to begin.\n"
        );
    }

    private void showTransitionTable() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n╔════════════════════════════════════════════════════════════╗\n");
        sb.append("║              DFA TRANSITION TABLE                          ║\n");
        sb.append("╚════════════════════════════════════════════════════════════╝\n\n");
        
        sb.append("BRANCH A: (g+gg+ggg)* mm\n");
        sb.append("─────────────────────────────────────────────────────\n");
        sb.append("State       │ Input 'g' │ Input 'h' │ Input 'm'\n");
        sb.append("────────────┼───────────┼───────────┼──────────\n");
        sb.append("→A0 (start) │ A_Gloop   │ DEAD      │ A_M1\n");
        sb.append("A_Gloop     │ A_Gloop   │ DEAD      │ A_M1\n");
        sb.append("A_M1        │ DEAD      │ DEAD      │ ✓A_MM (accept)\n");
        sb.append("✓A_MM       │ DEAD      │ DEAD      │ DEAD\n\n");

        sb.append("BRANCH B: hg\n");
        sb.append("─────────────────────────────────────────────────────\n");
        sb.append("State       │ Input 'g' │ Input 'h' │ Input 'm'\n");
        sb.append("────────────┼───────────┼───────────┼──────────\n");
        sb.append("→B0 (start) │ DEAD      │ B_H       │ DEAD\n");
        sb.append("B_H         │ ✓B_HG     │ DEAD      │ DEAD\n");
        sb.append("✓B_HG       │ DEAD      │ DEAD      │ DEAD\n\n");

        sb.append("BRANCH C: ggh\n");
        sb.append("─────────────────────────────────────────────────────\n");
        sb.append("State       │ Input 'g' │ Input 'h' │ Input 'm'\n");
        sb.append("────────────┼───────────┼───────────┼──────────\n");
        sb.append("→C0 (start) │ C_G1      │ DEAD      │ DEAD\n");
        sb.append("C_G1        │ C_G2      │ DEAD      │ DEAD\n");
        sb.append("C_G2        │ DEAD      │ ✓C_GGH    │ DEAD\n");
        sb.append("✓C_GGH      │ DEAD      │ DEAD      │ DEAD\n\n");

        sb.append("Legend: → = Start State  │  ✓ = Accept State  │  DEAD = Reject\n");
        
        outputArea.setText(sb.toString());
    }

    private void simulateString() {
        String input = inputField.getText().trim();
        
        if (input.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please enter a test string", 
                "Input Required", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validate input
        for (char ch : input.toCharArray()) {
            if (ch != 'g' && ch != 'h' && ch != 'm') {
                JOptionPane.showMessageDialog(this, 
                    "Invalid symbol: '" + ch + "'\nAllowed: g, h, m", 
                    "Invalid Input", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        StringBuilder out = new StringBuilder();
        out.append("\n╔════════════════════════════════════════════════════════════╗\n");
        out.append("║  SIMULATION: \"" + input + "\"\n");
        out.append("╚════════════════════════════════════════════════════════════╝\n\n");

        // Initialize states for three branches
        int stateA = 0, stateB = 0, stateC = 0;
        boolean deadA = false, deadB = false, deadC = false;

        out.append(String.format("%-8s %-25s %-25s %-25s\n", 
            "Step", "Branch A (mm)", "Branch B (hg)", "Branch C (ggh)"));
        out.append("─".repeat(85) + "\n");

        // Process each character
        for (int i = 0; i < input.length(); i++) {
            char ch = input.charAt(i);
            String step = "[" + (i + 1) + ":" + ch + "]";

            String beforeA = getStateName('A', stateA, deadA);
            String beforeB = getStateName('B', stateB, deadB);
            String beforeC = getStateName('C', stateC, deadC);

            // Branch A transitions
            if (!deadA) {
                stateA = transitionA(stateA, ch);
                if (stateA == -1) { deadA = true; stateA = 0; }
            }

            // Branch B transitions
            if (!deadB) {
                stateB = transitionB(stateB, ch);
                if (stateB == -1) { deadB = true; stateB = 0; }
            }

            // Branch C transitions
            if (!deadC) {
                stateC = transitionC(stateC, ch);
                if (stateC == -1) { deadC = true; stateC = 0; }
            }

            String afterA = getStateName('A', stateA, deadA);
            String afterB = getStateName('B', stateB, deadB);
            String afterC = getStateName('C', stateC, deadC);

            out.append(String.format("%-8s %-25s %-25s %-25s\n",
                step, beforeA + "→" + afterA, beforeB + "→" + afterB, beforeC + "→" + afterC));
        }

        // Check acceptance
        boolean acceptA = (!deadA && stateA == 3);
        boolean acceptB = (!deadB && stateB == 2);
        boolean acceptC = (!deadC && stateC == 3);

        out.append("\n" + "─".repeat(85) + "\n");
        out.append("RESULTS:\n");
        out.append(String.format("  Branch A: %s\n", acceptA ? "✓ ACCEPTED" : "✗ REJECTED"));
        out.append(String.format("  Branch B: %s\n", acceptB ? "✓ ACCEPTED" : "✗ REJECTED"));
        out.append(String.format("  Branch C: %s\n", acceptC ? "✓ ACCEPTED" : "✗ REJECTED"));

        boolean finalAccept = acceptA || acceptB || acceptC;
        out.append("\n╔════════════════════════════════════════════════════════════╗\n");
        out.append(String.format("║  FINAL: %s\n", 
            finalAccept ? "✓✓✓ STRING ACCEPTED ✓✓✓" : "✗✗✗ STRING REJECTED ✗✗✗"));
        out.append("╚════════════════════════════════════════════════════════════╝\n");

        outputArea.setText(out.toString());
    }

    // Branch A: (g+gg+ggg)* mm
    // States: 0=A0, 1=A_Gloop, 2=A_M1, 3=A_MM(accept)
    private int transitionA(int state, char ch) {
        switch (state) {
            case 0: return ch == 'g' ? 1 : ch == 'm' ? 2 : -1;
            case 1: return ch == 'g' ? 1 : ch == 'm' ? 2 : -1;
            case 2: return ch == 'm' ? 3 : -1;
            case 3: return -1; // Accept state, any extra char = dead
            default: return -1;
        }
    }

    // Branch B: hg
    // States: 0=B0, 1=B_H, 2=B_HG(accept)
    private int transitionB(int state, char ch) {
        switch (state) {
            case 0: return ch == 'h' ? 1 : -1;
            case 1: return ch == 'g' ? 2 : -1;
            case 2: return -1; // Accept state, any extra char = dead
            default: return -1;
        }
    }

    // Branch C: ggh
    // States: 0=C0, 1=C_G1, 2=C_G2, 3=C_GGH(accept)
    private int transitionC(int state, char ch) {
        switch (state) {
            case 0: return ch == 'g' ? 1 : -1;
            case 1: return ch == 'g' ? 2 : -1;
            case 2: return ch == 'h' ? 3 : -1;
            case 3: return -1; // Accept state, any extra char = dead
            default: return -1;
        }
    }

    private String getStateName(char branch, int state, boolean dead) {
        if (dead) return "DEAD";
        
        switch (branch) {
            case 'A':
                switch (state) {
                    case 0: return "A0";
                    case 1: return "A_Gloop";
                    case 2: return "A_M1";
                    case 3: return "✓A_MM";
                    default: return "A?";
                }
            case 'B':
                switch (state) {
                    case 0: return "B0";
                    case 1: return "B_H";
                    case 2: return "✓B_HG";
                    default: return "B?";
                }
            case 'C':
                switch (state) {
                    case 0: return "C0";
                    case 1: return "C_G1";
                    case 2: return "C_G2";
                    case 3: return "✓C_GGH";
                    default: return "C?";
                }
            default: return "?";
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            Finite_automataGUI app = new Finite_automataGUI();
            app.setVisible(true);
        });
    }
}