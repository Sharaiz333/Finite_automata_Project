/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package classtask.finite_automata;
import java.util.*;

/**
 * DFA conversion, printing, minimization and simulation utilities.
 */
public class DFAConverter {

    public static class DFA {
        public int start;
        public HashSet<Integer> finals = new HashSet<>();
        // transitions: from -> (symbol -> to)
        public HashMap<Integer, HashMap<Character, Integer>> trans = new HashMap<>();
        public int numStates = 0;
    }

    // epsilon closure
    public static HashSet<Integer> epsilonClosure(NFA nfa, HashSet<Integer> set) {
        HashSet<Integer> closure = new HashSet<>(set);
        Stack<Integer> st = new Stack<>();
        for (int s : set) st.push(s);
        while (!st.isEmpty()) {
            int cur = st.pop();
            for (int nxt : nfa.states.get(cur).eps) {
                if (!closure.contains(nxt)) {
                    closure.add(nxt);
                    st.push(nxt);
                }
            }
        }
        return closure;
    }

    // move on symbol
    public static HashSet<Integer> moveOn(NFA nfa, HashSet<Integer> set, char sym) {
        HashSet<Integer> res = new HashSet<>();
        for (int s : set) {
            ArrayList<Integer> list = nfa.states.get(s).trans.get(sym);
            if (list != null) res.addAll(list);
        }
        return res;
    }

    // key for sets
    public static String setKey(HashSet<Integer> s) {
        ArrayList<Integer> a = new ArrayList<>(s);
        Collections.sort(a);
        StringBuilder sb = new StringBuilder();
        for (int x : a) {
            if (sb.length() > 0) sb.append(",");
            sb.append(x);
        }
        return sb.toString();
    }

    // result: DFA and mapping id->NFA subset
    public static class DFAResult {
        public DFA dfa;
        public ArrayList<HashSet<Integer>> idToSet = new ArrayList<>();
    }

    // subset construction
    public static DFAResult nfaToDfa(NFA nfa, ArrayList<Character> alphabet) {
        DFAResult res = new DFAResult();
        DFA dfa = new DFA();

        HashMap<String, Integer> setToId = new HashMap<>();
        ArrayList<HashSet<Integer>> idToSet = new ArrayList<>();

        HashSet<Integer> startSet = new HashSet<>();
        startSet.add(nfa.start);
        startSet = epsilonClosure(nfa, startSet);
        String startKey = setKey(startSet);
        setToId.put(startKey, 0);
        idToSet.add(startSet);

        Queue<Integer> q = new LinkedList<>();
        q.add(0);
        dfa.numStates = 1;
        dfa.start = 0;

        while (!q.isEmpty()) {
            int id = q.poll();
            HashSet<Integer> curSet = idToSet.get(id);
            for (char sym : alphabet) {
                HashSet<Integer> moved = moveOn(nfa, curSet, sym);
                if (moved.isEmpty()) continue;
                HashSet<Integer> closure = epsilonClosure(nfa, moved);
                String key = setKey(closure);
                Integer toId = setToId.get(key);
                if (toId == null) {
                    toId = dfa.numStates++;
                    setToId.put(key, toId);
                    idToSet.add(closure);
                    q.add(toId);
                }
                dfa.trans.computeIfAbsent(id, k -> new HashMap<>()).put(sym, toId);
            }
        }

        for (int i = 0; i < idToSet.size(); i++) {
            if (idToSet.get(i).contains(nfa.accept)) dfa.finals.add(i);
        }
        dfa.numStates = idToSet.size();
        res.dfa = dfa;
        res.idToSet = idToSet;
        return res;
    }

    // print DFA table with mapping
    public static void printDFA(DFA dfa, ArrayList<Character> alphabet, ArrayList<HashSet<Integer>> idToSet, String title) {
        System.out.println("\n=== " + title + " ===");
        System.out.println("DFA state -> NFA subset mapping:");
        for (int i = 0; i < idToSet.size(); i++) {
            System.out.println("{" + i + "} => " + idToSet.get(i));
        }
        System.out.println("\nTransitions:");
        System.out.println("From\tInput\tTo");
        System.out.println("--------------------------");
        for (int s = 0; s < dfa.numStates; s++) {
            HashMap<Character, Integer> row = dfa.trans.get(s);
            for (char c : alphabet) {
                Integer to = (row == null) ? null : row.get(c);
                if (to == null) System.out.println("{" + s + "}\t" + c + "\t-");
                else System.out.println("{" + s + "}\t" + c + "\t{" + to + "}");
            }
        }
        System.out.print("Start: {" + dfa.start + "}   Finals: ");
        System.out.println(dfa.finals);
    }

    // table-filling minimization
    public static DFA minimizeDFA(DFA orig, ArrayList<Character> alphabet) {
        int n = orig.numStates;
        boolean[][] table = new boolean[n][n];

        // mark final vs non-final pairs
        for (int i = 0; i < n; i++) for (int j = i+1; j < n; j++) {
            boolean fi = orig.finals.contains(i);
            boolean fj = orig.finals.contains(j);
            if (fi != fj) table[i][j] = true;
        }

        boolean changed = true;
        while (changed) {
            changed = false;
            for (int i = 0; i < n; i++) {
                for (int j = i+1; j < n; j++) {
                    if (table[i][j]) continue;
                    for (char sym : alphabet) {
                        Integer ti = (orig.trans.containsKey(i)) ? orig.trans.get(i).get(sym) : null;
                        Integer tj = (orig.trans.containsKey(j)) ? orig.trans.get(j).get(sym) : null;
                        if (ti == null || tj == null) {
                            // if both null, continue; if one null and other not, consideration will come via finals or existing markings
                            continue;
                        }
                        int a = Math.min(ti, tj), b = Math.max(ti, tj);
                        if (a != b && table[a][b]) {
                            table[i][j] = true;
                            changed = true;
                            break;
                        }
                    }
                }
            }
        }

        // build classes
        int[] cls = new int[n];
        Arrays.fill(cls, -1);
        int classes = 0;
        for (int i = 0; i < n; i++) {
            if (cls[i] != -1) continue;
            cls[i] = classes;
            for (int j = i+1; j < n; j++) {
                if (!table[i][j]) cls[j] = classes;
            }
            classes++;
        }

        // build minimized DFA
        DFA md = new DFA();
        md.numStates = classes;
        md.start = cls[orig.start];
        for (int i = 0; i < n; i++) if (orig.finals.contains(i)) md.finals.add(cls[i]);

        for (int i = 0; i < n; i++) {
            HashMap<Character, Integer> row = orig.trans.get(i);
            if (row == null) continue;
            for (Map.Entry<Character, Integer> e : row.entrySet()) {
                char sym = e.getKey();
                int toOld = e.getValue();
                int ri = cls[i];
                int rto = cls[toOld];
                md.trans.computeIfAbsent(ri, k -> new HashMap<>()).put(sym, rto);
            }
        }
        return md;
    }

    // interactive simulation
    public static void simulateDFAInteractive(DFA dfa, ArrayList<Character> alphabet) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.print("\nEnter test string (g/h/m) or 'exit': ");
            String s = sc.nextLine().trim();
            if (s.equalsIgnoreCase("exit")) break;
            int cur = dfa.start;
            System.out.println("Start at {" + cur + "}");
            boolean dead = false;
            for (char ch : s.toCharArray()) {
                if (!alphabet.contains(ch)) {
                    System.out.println("Invalid symbol '" + ch + "'. Use g/h/m only.");
                    dead = true;
                    break;
                }
                HashMap<Character, Integer> row = dfa.trans.get(cur);
                if (row == null || !row.containsKey(ch)) {
                    System.out.println("{" + cur + "} --" + ch + "--> - (no transition) => Rejected");
                    dead = true;
                    break;
                } else {
                    int nxt = row.get(ch);
                    System.out.println("{" + cur + "} --" + ch + "--> {" + nxt + "}");
                    cur = nxt;
                }
            }
            if (dead) continue;
            if (dfa.finals.contains(cur)) System.out.println("Accepted ✅ (reached final {" + cur + "})");
            else System.out.println("Rejected ❌ ({" + cur + "} is not final)");
        }
    }
}
