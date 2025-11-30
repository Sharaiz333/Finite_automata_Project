package classtask.finite_automata;

import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;

public class Finite_automata extends JFrame {
    
    private JTextField inputField;
    private JTextArea outputArea;
    private JButton testButton, clearButton, showNFAButton, showDFAButton, showMinDFAButton;
    private JButton showNFADiagramButton, showDFADiagramButton, showMinDFADiagramButton;
    
    private final String REGEX = "(g + gg + ggg)* mm + hg + ggh";
    
    public Finite_automata() {
        setTitle("RE → NFA → DFA → Min-DFA Simulator");
        setSize(1200, 750);
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
        
        // Original buttons (TEXT OUTPUT ONLY)
        testButton = createButton("Test String", new Color(39, 174, 96));
        showNFAButton = createButton("Show NFA", new Color(52, 152, 219));
        showDFAButton = createButton("Show DFA", new Color(155, 89, 182));
        showMinDFAButton = createButton("Show Min-DFA", new Color(230, 126, 34));
        clearButton = createButton("Clear", new Color(231, 76, 60));
        
        // NEW Diagram buttons
        showNFADiagramButton = createButton(" NFA Diagram", new Color(46, 204, 113));
        showDFADiagramButton = createButton(" DFA Diagram", new Color(52, 152, 219));
        showMinDFADiagramButton = createButton(" Min-DFA Diagram", new Color(155, 89, 182));
        
        inputPanel.add(testButton);
        inputPanel.add(showNFAButton);
        inputPanel.add(showDFAButton);
        inputPanel.add(showMinDFAButton);
        inputPanel.add(clearButton);
        inputPanel.add(showNFADiagramButton);
        inputPanel.add(showDFADiagramButton);
        inputPanel.add(showMinDFADiagramButton);
        
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
                        "Reject Examples: gh, mmg, hhg" +
                        "\n Use Diagram buttons for visual representations | Text buttons show tables only");
        infoArea.setEditable(false);
        infoArea.setBackground(new Color(240, 240, 240));
        infoArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        bottomPanel.add(infoArea);
        
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        add(mainPanel);
        
        // Action Listeners - ORIGINAL BUTTONS (TEXT ONLY)
        testButton.addActionListener(e -> simulateString());
        clearButton.addActionListener(e -> outputArea.setText(""));
        showNFAButton.addActionListener(e -> displayNFA());           // TEXT ONLY
        showDFAButton.addActionListener(e -> displayDFA());           // TEXT ONLY  
        showMinDFAButton.addActionListener(e -> displayMinimizedDFA()); // TEXT ONLY
        inputField.addActionListener(e -> simulateString());
        
        // NEW DIAGRAM BUTTONS
        showNFADiagramButton.addActionListener(e -> showNFADiagram());
        showDFADiagramButton.addActionListener(e -> showDFADiagram());
        showMinDFADiagramButton.addActionListener(e -> showMinDFADiagram());
        
        showWelcome();
    }

    private JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setMargin(new Insets(5, 10, 5, 10));
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
        outputArea.append("  [→] Transition Tables Display (TEXT buttons)\n");
        outputArea.append("  [→] Diagram Visualization (DIAGRAM buttons)\n");
        outputArea.append("  [→] String Simulation Module\n");
        outputArea.append("  [→] Step-by-Step Execution Trace\n\n");
        outputArea.append("INSTRUCTIONS:\n");
        outputArea.append("  • TEXT buttons: Show NFA/DFA/Min-DFA → View transition tables\n");
        outputArea.append("  • DIAGRAM buttons: NFA/DFA/Min-DFA Diagram → View Graphviz visuals\n");
        outputArea.append("  • Test String → Simulate input on Min-DFA\n");
        outputArea.append("══════════════════════════════════════════════════════════════════\n");
    }

    // ========== TEXT DISPLAY METHODS (UNCHANGED) ==========
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

    // ========== SIMULATION (UNCHANGED) ==========
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

    // ========== DIAGRAM GENERATION METHODS ==========
    
    private Map<String, Map<String, Set<String>>> getNFATransitions() {
        Map<String, Map<String, Set<String>>> nfa = new HashMap<>();
        
        // Branch A: (g|gg|ggg)* mm
        nfa.put("q0", Map.of("g", Set.of("q1"), "m", Set.of("q6")));
        nfa.put("q1", Map.of("g", Set.of("q1"), "m", Set.of("q6")));
        nfa.put("q6", Map.of("m", Set.of("q7")));
        nfa.put("q7", Map.of());
        
        // Branch B: hg
        nfa.put("q10", Map.of("h", Set.of("q11")));
        nfa.put("q11", Map.of("g", Set.of("q12")));
        nfa.put("q12", Map.of());
        
        // Branch C: ggh
        nfa.put("q20", Map.of("g", Set.of("q21")));
        nfa.put("q21", Map.of("g", Set.of("q22")));
        nfa.put("q22", Map.of("h", Set.of("q23")));
        nfa.put("q23", Map.of());
        
        return nfa;
    }

    private String generateNFADOT(Map<String, Map<String, Set<String>>> transitions,
                                 String startState, Set<String> finalStates) {
        StringBuilder dot = new StringBuilder();
        dot.append("digraph NFA {\n");
        dot.append("  rankdir=LR;\n");
        dot.append("  node [shape=circle, fontsize=12, fixedsize=true, width=0.8];\n");
        dot.append("  start [shape=point, color=green];\n");
        dot.append(startState + " [color=green, style=filled, fillcolor=lightgreen];\n");

        for (String fs : finalStates) {
            dot.append(fs + " [peripheries=2];\n");
        }

        dot.append("  start -> q0 [color=green];\n");
        dot.append("  start -> q10 [color=green];\n");
        dot.append("  start -> q20 [color=green];\n");

        for (String from : transitions.keySet()) {
            Map<String, Set<String>> edges = transitions.get(from);
            for (String input : edges.keySet()) {
                Set<String> targets = edges.get(input);
                for (String to : targets) {
                    dot.append(String.format("  %s -> %s [label=\"%s\"];\n", from, to, input));
                }
            }
        }

        dot.append("}\n");
        return dot.toString();
    }

    private Map<String, Map<Character, String>> getDFATransitions() {
        Map<String, Map<Character, String>> dfaTransitions = new HashMap<>();
        dfaTransitions.put("D0", Map.of('g', "D1", 'h', "D2", 'm', "D3"));
        dfaTransitions.put("D1", Map.of('g', "D4", 'h', "DEAD", 'm', "D3"));
        dfaTransitions.put("D2", Map.of('g', "D5", 'h', "DEAD", 'm', "DEAD"));
        dfaTransitions.put("D3", Map.of('g', "DEAD", 'h', "DEAD", 'm', "D6"));
        dfaTransitions.put("D4", Map.of('g', "D1", 'h', "D7", 'm', "D3"));
        dfaTransitions.put("D5", Map.of('g', "DEAD", 'h', "DEAD", 'm', "DEAD"));
        dfaTransitions.put("D6", Map.of('g', "DEAD", 'h', "DEAD", 'm', "DEAD"));
        dfaTransitions.put("D7", Map.of('g', "DEAD", 'h', "DEAD", 'm', "DEAD"));
        dfaTransitions.put("DEAD", Map.of('g', "DEAD", 'h', "DEAD", 'm', "DEAD"));
        return dfaTransitions;
    }

    private Set<String> getDFAFinalStates() {
        return Set.of("D5", "D6", "D7");
    }

    private String getDFAStartState() {
        return "D0";
    }

    private Map<String, Map<Character, String>> getMinDFATransitions() {
        Map<String, Map<Character, String>> minDfa = new HashMap<>();
        minDfa.put("M0", Map.of('g', "M1", 'h', "M2", 'm', "M3"));
        minDfa.put("M1", Map.of('g', "M4", 'h', "DEAD", 'm', "M3"));
        minDfa.put("M2", Map.of('g', "M5", 'h', "DEAD", 'm', "DEAD"));
        minDfa.put("M3", Map.of('g', "DEAD", 'h', "DEAD", 'm', "M5"));
        minDfa.put("M4", Map.of('g', "M1", 'h', "M5", 'm', "M3"));
        minDfa.put("M5", Map.of('g', "DEAD", 'h', "DEAD", 'm', "DEAD"));
        minDfa.put("DEAD", Map.of('g', "DEAD", 'h', "DEAD", 'm', "DEAD"));
        return minDfa;
    }

    private Set<String> getMinDFAFinalStates() {
        return Set.of("M5");
    }

    private String getMinDFAStartState() {
        return "M0";
    }

    private String generateDOT(Map<String, Map<Character, String>> transitions,
                           String startState, Set<String> finalStates, String title) {
    StringBuilder dot = new StringBuilder();
    
    dot.append("digraph Automaton {\n");
    dot.append("  rankdir=LR;\n");
    dot.append("  node [shape=circle, fontsize=14, fixedsize=true, width=0.9];\n");
    dot.append("  start [shape=point, color=green];\n");
    dot.append(startState + " [color=green, style=filled, fillcolor=lightgreen];\n");

    for (String fs : finalStates) {
        if (!fs.equals(startState)) {
            dot.append(fs + " [peripheries=2];\n");
        }
    }

    dot.append("  start -> " + startState + " [color=green];\n");

    for (String from : transitions.keySet()) {
        Map<Character, String> edges = transitions.get(from);
        for (Character input : edges.keySet()) {
            String to = edges.get(input);
            dot.append(String.format("  %s -> %s [label=\"%c\"];\n", from, to, input));
        }
    }

    // keep title only in label (this can have spaces)
    dot.append("  label=\"\\n" + title + " - " + REGEX + "\";\n");
    dot.append("  labelloc=\"t\";\n");
    dot.append("  fontsize=12;\n");
    dot.append("}\n");
    return dot.toString();
}


    private File writeDOTFile(String dotSource, String filename) throws IOException {
        File dotFile = new File(filename);
        try (PrintWriter out = new PrintWriter(dotFile)) {
            out.print(dotSource);
        }
        return dotFile;
    }

    private void generateGraphvizImage(File dotFile, File outputImage) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder("dot", "-Tpng", dotFile.getAbsolutePath(), "-o", outputImage.getAbsolutePath());
        Process p = pb.start();
        int exitCode = p.waitFor();
        if (exitCode != 0) {
            throw new IOException("Graphviz 'dot' command failed. Please ensure Graphviz is installed and in PATH.");
        }
    }

    private void showDiagram(String title, String dotFilename, String imageFilename) {
    try {
        File imageFile = new File(imageFilename);
        
        
        String dotSource;
        if (title.contains("NFA")) {
            var transitions = getNFATransitions();
            dotSource = generateNFADOT(transitions, "start", Set.of("q7", "q12", "q23"));
        } else if (title.contains("DFA") && !title.contains("Minimized")) {
            var transitions = getDFATransitions();
            dotSource = generateDOT(transitions, getDFAStartState(), getDFAFinalStates(), title);
        } else {  // Min-DFA or any other
            var transitions = getMinDFATransitions();
            dotSource = generateDOT(transitions, getMinDFAStartState(), getMinDFAFinalStates(), title);
        }
        
        File dotFile = writeDOTFile(dotSource, dotFilename);
        generateGraphvizImage(dotFile, imageFile);

        BufferedImage img = ImageIO.read(imageFile);
        ImageIcon icon = new ImageIcon(img);

        JFrame frame = new JFrame(title + " - Generated by Graphviz");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JLabel label = new JLabel(icon);
        frame.getContentPane().add(new JScrollPane(label), BorderLayout.CENTER);
        frame.pack();
        frame.setLocationRelativeTo(this);
        frame.setSize(900, 700);
        frame.setVisible(true);

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, 
            title + " Error:\n" + e.getMessage() + 
            "\n\nTo fix:\n1. Install Graphviz (https://graphviz.org/download/)\n2. Add 'dot' to system PATH",
            "Graphviz Required", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
}

    private void showNFADiagram() {
        showDiagram("NFA", "nfa.dot", "nfa.png");
    }

    private void showDFADiagram() {
        showDiagram("DFA", "dfa.dot", "dfa.png");
    }

    private void showMinDFADiagram() {
        showDiagram("Minimized DFA", "min_dfa.dot", "min_dfa.png");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Finite_automata().setVisible(true);
        });
    }
}
