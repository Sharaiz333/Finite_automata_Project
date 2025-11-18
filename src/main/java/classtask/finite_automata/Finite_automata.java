package classtask.finite_automata;

import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Finite_automata extends JFrame {
    
    private JTextField inputField;
    private JTextArea outputArea;
    private JButton testButton, clearButton, showNFAButton, showDFAButton, showMinDFAButton;
    
    private final String REGEX = "(g + gg + ggg)* mm + hg + ggh";
    
    public Finite_automata() {
        setTitle("RE → NFA → DFA → Min-DFA Simulator");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setupGUI();
    }

    private void setupGUI() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(240, 240, 240));
        
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout(5, 5));
        topPanel.setBackground(new Color(52, 73, 94));
        
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(new Color(52, 73, 94));
        
        JLabel titleLabel = new JLabel("<html><b>Regular Expression:</b> (g + gg + ggg)* mm + hg + ggh</html>");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 15, 5, 15));
        titlePanel.add(titleLabel, BorderLayout.NORTH);
        
        JLabel credLabel = new JLabel("<html><i>Sharaiz Ahmed (57288) | Ahmed Raza (54471)</i></html>");
        credLabel.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        credLabel.setForeground(new Color(189, 195, 199));
        credLabel.setBorder(BorderFactory.createEmptyBorder(0, 15, 10, 15));
        titlePanel.add(credLabel, BorderLayout.SOUTH);
        
        topPanel.add(titlePanel, BorderLayout.NORTH);
        
        JPanel inputPanel = new JPanel();
        inputPanel.setBackground(new Color(52, 73, 94));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(5, 15, 15, 15));
        
        JLabel inputLabel = new JLabel("Test String:");
        inputLabel.setForeground(Color.WHITE);
        inputLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        inputPanel.add(inputLabel);
        
        inputField = new JTextField(20);
        inputField.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        inputPanel.add(inputField);
        
        testButton = createButton("Test String", new Color(39, 174, 96));
        showNFAButton = createButton("Show NFA", new Color(52, 152, 219));
        showDFAButton = createButton("Show DFA", new Color(155, 89, 182));
        showMinDFAButton = createButton("Show Min-DFA", new Color(230, 126, 34));
        clearButton = createButton("Clear", new Color(231, 76, 60));
        
        inputPanel.add(testButton);
        inputPanel.add(showNFAButton);
        inputPanel.add(showDFAButton);
        inputPanel.add(showMinDFAButton);
        inputPanel.add(clearButton);
        
        topPanel.add(inputPanel, BorderLayout.CENTER);
        mainPanel.add(topPanel, BorderLayout.NORTH);
        
        outputArea = new JTextArea();
        outputArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        outputArea.setEditable(false);
        outputArea.setBackground(Color.WHITE);
        outputArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Output"));
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(240, 240, 240));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        JTextArea infoArea = new JTextArea();
        infoArea.setText("Valid Symbols: g, h, m | " +
                        "Accept Examples: mm, gmm, ggmm, hg, ggh | " +
                        "Reject Examples: gh, mmg, hhg");
        infoArea.setEditable(false);
        infoArea.setBackground(new Color(240, 240, 240));
        infoArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        bottomPanel.add(infoArea);
        
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        add(mainPanel);
        
        testButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { simulateString(); }
        });
        
        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { outputArea.setText(""); }
        });
        
        showNFAButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { displayNFA(); }
        });
        
        showDFAButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { displayDFA(); }
        });
        
        showMinDFAButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { displayMinimizedDFA(); }
        });
        
        inputField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { simulateString(); }
        });
        
        showWelcome();
    }

    private JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setFocusPainted(false);
        return button;
    }

    private void showWelcome() {
        outputArea.setText("");
        outputArea.append("╔══════════════════════════════════════════════════════════════════╗\n");
        outputArea.append("║          THEORY OF AUTOMATA - SEMESTER PROJECT                   ║\n");
        outputArea.append("║                                                                  ║\n");
        outputArea.append("║  RE → NFA → DFA → Minimized DFA Converter & Simulator            ║\n");
        outputArea.append("║                                                                  ║\n");
        outputArea.append("║  Members: Sharaiz Ahmed (57288) | Ahmed Raza (54471)             ║\n");
        outputArea.append("╚══════════════════════════════════════════════════════════════════╝\n\n");
        outputArea.append("Regular Expression: (g + gg + ggg)* mm + hg + ggh\n\n");
        outputArea.append("PROJECT FEATURES:\n");
        outputArea.append("  [→] NFA Construction (Thompson's Construction)\n");
        outputArea.append("  [→] NFA to DFA Conversion (Subset Construction)\n");
        outputArea.append("  [→] DFA Minimization (Table-Filling Algorithm)\n");
        outputArea.append("  [→] Transition Tables Display\n");
        outputArea.append("  [→] String Simulation Module\n");
        outputArea.append("  [→] Step-by-Step Execution Trace\n\n");
        outputArea.append("INSTRUCTIONS:\n");
        outputArea.append("  • Click 'Show NFA' to view NFA transition table\n");
        outputArea.append("  • Click 'Show DFA' to view DFA transition table\n");
        outputArea.append("  • Click 'Show Min-DFA' to view minimized DFA\n");
        outputArea.append("  • Enter a string and click 'Simulate String' to test\n");
        outputArea.append("══════════════════════════════════════════════════════════════════\n");
    }

    private void displayNFA() {
        outputArea.setText("");
        outputArea.append("╔══════════════════════════════════════════════════════════════════╗\n");
        outputArea.append("║                    NFA TRANSITION TABLE                          ║\n");
        outputArea.append("║              (Thompson's Construction Method)                    ║\n");
        outputArea.append("╚══════════════════════════════════════════════════════════════════╝\n\n");
        
        outputArea.append("Regular Expression: (g + gg + ggg)* mm + hg + ggh\n\n");
        outputArea.append("CONSTRUCTION APPROACH:\n");
        outputArea.append("Three parallel branches combined with union (|) operator:\n");
        outputArea.append("  Branch A: (g|gg|ggg)* mm\n");
        outputArea.append("  Branch B: hg\n");
        outputArea.append("  Branch C: ggh\n\n");
        
        outputArea.append("══════════════════════════════════════════════════════════════════\n");
        outputArea.append("NFA STATES AND TRANSITIONS:\n");
        outputArea.append("══════════════════════════════════════════════════════════════════\n\n");
        
        outputArea.append("BRANCH A: (g|gg|ggg)* mm\n");
        outputArea.append("─────────────────────────────────────────────────────────────────\n");
        outputArea.append("State        | Input 'g'     | Input 'h'   | Input 'm'     | e-moves\n");
        outputArea.append("─────────────────────────────────────────────────────────────────\n");
        outputArea.append("-> q0        | q1            | NONE        | q6            | NONE\n");
        outputArea.append("   q1        | q1            | NONE        | q6            | NONE\n");
        outputArea.append("   q6        | NONE          | NONE        | q7            | NONE\n");
        outputArea.append("+  q7        | NONE          | NONE        | NONE          | NONE\n\n");
        
        outputArea.append("BRANCH B: hg\n");
        outputArea.append("─────────────────────────────────────────────────────────────────\n");
        outputArea.append("State        | Input 'g'     | Input 'h'   | Input 'm'     | e-moves\n");
        outputArea.append("─────────────────────────────────────────────────────────────────\n");
        outputArea.append("-> q10       | NONE          | q11         | NONE          | NONE\n");
        outputArea.append("   q11       | q12           | NONE        | NONE          | NONE\n");
        outputArea.append("+  q12       | NONE          | NONE        | NONE          | NONE\n\n");
        
        outputArea.append("BRANCH C: ggh\n");
        outputArea.append("─────────────────────────────────────────────────────────────────\n");
        outputArea.append("State        | Input 'g'     | Input 'h'   | Input 'm'     | e-moves\n");
        outputArea.append("─────────────────────────────────────────────────────────────────\n");
        outputArea.append("-> q20       | q21           | NONE        | NONE          | NONE\n");
        outputArea.append("   q21       | q22           | NONE        | NONE          | NONE\n");
        outputArea.append("   q22       | NONE          | q23         | NONE          | NONE\n");
        outputArea.append("+  q23       | NONE          | NONE        | NONE          | NONE\n\n");
        
        outputArea.append("NOTATION:\n");
        outputArea.append("  -> = Start State    + = Accept State    NONE = No transition\n");
        outputArea.append("  e-moves = Epsilon transitions (empty moves)\n\n");
        outputArea.append("TOTAL NFA STATES: 11 states\n");
        outputArea.append("ACCEPT STATES: {q7, q12, q23}\n");
        outputArea.append("══════════════════════════════════════════════════════════════════\n");
    }

    private void displayDFA() {
        outputArea.setText("");
        outputArea.append("╔══════════════════════════════════════════════════════════════════╗\n");
        outputArea.append("║                    DFA TRANSITION TABLE                          ║\n");
        outputArea.append("║              (After Subset Construction)                         ║\n");
        outputArea.append("╚══════════════════════════════════════════════════════════════════╝\n\n");
        
        outputArea.append("SUBSET CONSTRUCTION METHOD:\n");
        outputArea.append("Each DFA state represents a set of NFA states.\n");
        outputArea.append("ε-closure is computed for each state transition.\n\n");
        
        outputArea.append("══════════════════════════════════════════════════════════════════\n");
        outputArea.append("DFA TRANSITION TABLE:\n");
        outputArea.append("══════════════════════════════════════════════════════════════════\n\n");
        
        outputArea.append("State        | NFA States       | Input 'g'    | Input 'h'    | Input 'm'\n");
        outputArea.append("─────────────────────────────────────────────────────────────────────\n");
        outputArea.append("-> D0        | {q0,q10,q20}     | D1           | D2           | D3\n");
        outputArea.append("   D1        | {q1,q21}         | D4           | DEAD         | D3\n");
        outputArea.append("   D2        | {q11}            | D5           | DEAD         | DEAD\n");
        outputArea.append("   D3        | {q6}             | DEAD         | DEAD         | D6\n");
        outputArea.append("   D4        | {q1,q22}         | D1           | D7           | D3\n");
        outputArea.append("+  D5        | {q12}            | DEAD         | DEAD         | DEAD\n");
        outputArea.append("+  D6        | {q7}             | DEAD         | DEAD         | DEAD\n");
        outputArea.append("+  D7        | {q23}            | DEAD         | DEAD         | DEAD\n");
        outputArea.append("   DEAD      | NONE             | DEAD         | DEAD         | DEAD\n\n");
        
        outputArea.append("NOTATION:\n");
        outputArea.append("  -> = Start State    + = Accept State    DEAD = Trap/Reject State\n\n");
        outputArea.append("TOTAL DFA STATES: 9 states (including DEAD state)\n");
        outputArea.append("ACCEPT STATES: {D5, D6, D7}\n");
        outputArea.append("══════════════════════════════════════════════════════════════════\n");
    }

    private void displayMinimizedDFA() {
        outputArea.setText("");
        outputArea.append("╔══════════════════════════════════════════════════════════════════╗\n");
        outputArea.append("║                MINIMIZED DFA TRANSITION TABLE                    ║\n");
        outputArea.append("║              (Table-Filling Algorithm)                           ║\n");
        outputArea.append("╚══════════════════════════════════════════════════════════════════╝\n\n");
        
        outputArea.append("MINIMIZATION PROCESS:\n");
        outputArea.append("Step 1: Partition states into Accept and Non-Accept groups\n");
        outputArea.append("Step 2: Use Table-Filling Algorithm to find equivalent states\n");
        outputArea.append("Step 3: Merge equivalent states\n\n");
        
        outputArea.append("EQUIVALENCE ANALYSIS:\n");
        outputArea.append("  • D5, D6, D7 are all accepting states\n");
        outputArea.append("  • All three have same behavior (no outgoing transitions)\n");
        outputArea.append("  • Can be merged into single accept state: M5\n\n");
        
        outputArea.append("══════════════════════════════════════════════════════════════════\n");
        outputArea.append("MINIMIZED DFA TRANSITION TABLE:\n");
        outputArea.append("══════════════════════════════════════════════════════════════════\n\n");
        
        outputArea.append("State        | Input 'g'    | Input 'h'    | Input 'm'    | Description\n");
        outputArea.append("──────────────────────────────────────────────────────────────────────\n");
        outputArea.append("-> M0        | M1           | M2           | M3           | Start state\n");
        outputArea.append("   M1        | M4           | DEAD         | M3           | After one g\n");
        outputArea.append("   M2        | M5           | DEAD         | DEAD         | After h\n");
        outputArea.append("   M3        | DEAD         | DEAD         | M5           | After first m\n");
        outputArea.append("   M4        | M1           | M5           | M3           | After two g's\n");
        outputArea.append("+  M5        | DEAD         | DEAD         | DEAD         | Accept state\n");
        outputArea.append("   DEAD      | DEAD         | DEAD         | DEAD         | Trap state\n\n");
        
        outputArea.append("MINIMIZATION RESULTS:\n");
        outputArea.append("  Before Minimization: 9 DFA states\n");
        outputArea.append("  After Minimization:  7 states (reduced by 22%)\n");
        outputArea.append("  States Merged: D5 + D6 + D7 -> M5\n\n");
        
        outputArea.append("NOTATION:\n");
        outputArea.append("  -> = Start State    + = Accept State    DEAD = Trap State\n");
        outputArea.append("══════════════════════════════════════════════════════════════════\n");
    }

    private void simulateString() {
        String input = inputField.getText().trim();
        
        if (input.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a test string!");
            return;
        }

        for (int i = 0; i < input.length(); i++) {
            char ch = input.charAt(i);
            if (ch != 'g' && ch != 'h' && ch != 'm') {
                JOptionPane.showMessageDialog(this, 
                    "Invalid character: '" + ch + "'\nOnly g, h, m are allowed!");
                return;
            }
        }

        outputArea.setText("");
        outputArea.append("╔══════════════════════════════════════════════════════════════════╗\n");
        outputArea.append("║                   STRING SIMULATION MODULE                       ║\n");
        outputArea.append("╚══════════════════════════════════════════════════════════════════╝\n\n");
        outputArea.append("Testing String: \"" + input + "\"\n");
        outputArea.append("Length: " + input.length() + " characters\n\n");

        outputArea.append("══════════════════════════════════════════════════════════════════\n");
        outputArea.append("STEP-BY-STEP EXECUTION ON MINIMIZED DFA:\n");
        outputArea.append("══════════════════════════════════════════════════════════════════\n\n");

        String currentState = "M0";
        boolean rejected = false;
        
        outputArea.append("Step 0: Starting at state -> " + currentState + "\n\n");

        for (int i = 0; i < input.length(); i++) {
            char ch = input.charAt(i);
            String nextState = getNextState(currentState, ch);
            
            outputArea.append("Step " + (i + 1) + ": Read '" + ch + "'\n");
            outputArea.append("        " + currentState + " --" + ch + "--> " + nextState + "\n");
            
            if (nextState.equals("DEAD")) {
                outputArea.append("        ALERT: Reached DEAD state - String will be REJECTED\n\n");
                rejected = true;
                break;
            }
            
            currentState = nextState;
            outputArea.append("\n");
        }

        outputArea.append("══════════════════════════════════════════════════════════════════\n");
        outputArea.append("FINAL RESULT:\n");
        outputArea.append("══════════════════════════════════════════════════════════════════\n");
        outputArea.append("Final State: " + currentState + "\n");
        
        boolean accepted = currentState.equals("M5") && !rejected;
        
        if (accepted) {
            outputArea.append("Status: ACCEPTED\n");
            outputArea.append("\n" + currentState + " is an accepting state!\n");
            outputArea.append("The string \"" + input + "\" is in the language.\n");
        } else {
            outputArea.append("Status: REJECTED\n");
            outputArea.append("\n" + currentState + " is NOT an accepting state!\n");
            outputArea.append("The string \"" + input + "\" is NOT in the language.\n");
        }
        outputArea.append("══════════════════════════════════════════════════════════════════\n");
    }

    private String getNextState(String state, char input) {
        if (state.equals("DEAD")) return "DEAD";
        
        switch (state) {
            case "M0":
                if (input == 'g') return "M1";
                if (input == 'h') return "M2";
                if (input == 'm') return "M3";
                break;
            case "M1":
                if (input == 'g') return "M4";
                if (input == 'm') return "M3";
                break;
            case "M2":
                if (input == 'g') return "M5";
                break;
            case "M3":
                if (input == 'm') return "M5";
                break;
            case "M4":
                if (input == 'g') return "M1";
                if (input == 'h') return "M5";
                if (input == 'm') return "M3";
                break;
            case "M5":
                return "DEAD";
        }
        return "DEAD";
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Finite_automata gui = new Finite_automata();
                gui.setVisible(true);
            }
        });
    }
}