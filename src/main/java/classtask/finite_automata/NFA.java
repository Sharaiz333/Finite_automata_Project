/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package classtask.finite_automata;
import java.util.*;

/**
 * NFA container and Thompson-style helpers (specialized for the specific RE).
 *
 * IMPORTANT: This NFA builder is specialized for the Group-7 RE:
 * (g + gg + ggg)* mm   +   hg   +   ggh
 *
 * It intentionally constructs the NFA as:
 *    branch1 = (g+gg+ggg)* · mm
 *    branch2 = hg
 *    branch3 = ggh
 *    full = branch1 + branch2 + branch3
 *
 * This ensures the g-loop applies only to the mm branch.
 */
public class NFA {
    public int start;
    public int accept;
    public ArrayList<NFAState> states = new ArrayList<>();

    // create and return new state id
    private int newState() {
        NFAState s = new NFAState(states.size());
        states.add(s);
        return s.id;
    }

    // Create NFA for single symbol c
    public static NFA symbolNFA(char c) {
        NFA n = new NFA();
        int s0 = n.newState();
        int s1 = n.newState();
        n.states.get(s0).addTrans(c, s1);
        n.start = s0;
        n.accept = s1;
        return n;
    }

    // Concatenate a then b: a·b
    public static NFA concatNFA(NFA a, NFA b) {
        NFA out = new NFA();
        // copy a states
        for (NFAState st : a.states) {
            NFAState copy = new NFAState(out.states.size());
            // copy transitions
            for (Map.Entry<Character, ArrayList<Integer>> e : st.trans.entrySet())
                copy.trans.put(e.getKey(), new ArrayList<>(e.getValue()));
            // copy eps
            copy.eps = new ArrayList<>(st.eps);
            out.states.add(copy);
        }
        int offset = out.states.size();
        // copy b states with offset
        for (NFAState st : b.states) {
            NFAState copy = new NFAState(out.states.size());
            for (Map.Entry<Character, ArrayList<Integer>> e : st.trans.entrySet()) {
                ArrayList<Integer> newList = new ArrayList<>();
                for (int to : e.getValue()) newList.add(to + offset);
                copy.trans.put(e.getKey(), newList);
            }
            for (int to : st.eps) copy.eps.add(to + offset);
            out.states.add(copy);
        }
        // link a.accept -> b.start by epsilon
        out.states.get(a.accept).eps.add(b.start + offset);
        out.start = a.start;
        out.accept = b.accept + offset;
        return out;
    }

    // Union a + b
    public static NFA unionNFA(NFA a, NFA b) {
        NFA out = new NFA();
        int start = out.newState();
        int offsetA = out.states.size();
        // copy a
        for (NFAState st : a.states) {
            NFAState copy = new NFAState(out.states.size());
            for (Map.Entry<Character, ArrayList<Integer>> e : st.trans.entrySet())
                copy.trans.put(e.getKey(), new ArrayList<>(e.getValue()));
            copy.eps = new ArrayList<>(st.eps);
            out.states.add(copy);
        }
        int offsetB = out.states.size();
        // copy b with offset
        for (NFAState st : b.states) {
            NFAState copy = new NFAState(out.states.size());
            for (Map.Entry<Character, ArrayList<Integer>> e : st.trans.entrySet()) {
                ArrayList<Integer> newList = new ArrayList<>();
                for (int to : e.getValue()) newList.add(to + offsetB);
                copy.trans.put(e.getKey(), newList);
            }
            for (int to : st.eps) copy.eps.add(to + offsetB);
            out.states.add(copy);
        }
        int end = out.newState();
        // connect start -> a.start, start -> b.start (eps)
        out.states.get(start).eps.add(a.start + offsetA);
        out.states.get(start).eps.add(b.start + offsetB);
        // connect a.accept -> end, b.accept -> end
        out.states.get(a.accept + offsetA).eps.add(end);
        out.states.get(b.accept + offsetB).eps.add(end);

        out.start = start;
        out.accept = end;
        return out;
    }

    // Kleene star a*
    public static NFA starNFA(NFA a) {
        NFA out = new NFA();
        int start = out.newState();
        int offset = out.states.size();
        // copy a with offset
        for (NFAState st : a.states) {
            NFAState copy = new NFAState(out.states.size());
            for (Map.Entry<Character, ArrayList<Integer>> e : st.trans.entrySet()) {
                ArrayList<Integer> newList = new ArrayList<>();
                for (int to : e.getValue()) newList.add(to + offset);
                copy.trans.put(e.getKey(), newList);
            }
            for (int to : st.eps) copy.eps.add(to + offset);
            out.states.add(copy);
        }
        int end = out.newState();
        // start -> a.start and start -> end
        out.states.get(start).eps.add(a.start + offset);
        out.states.get(start).eps.add(end);
        // a.accept -> a.start and a.accept -> end
        out.states.get(a.accept + offset).eps.add(a.start + offset);
        out.states.get(a.accept + offset).eps.add(end);

        out.start = start;
        out.accept = end;
        return out;
    }

    // Print NFA transitions (including epsilon moves)
    public void print() {
        System.out.println("\n=== NFA Transition Table ===");
        System.out.println("From\tInput\tTo");
        System.out.println("--------------------------");
        for (NFAState st : states) {
            for (Map.Entry<Character, ArrayList<Integer>> e : st.trans.entrySet()) {
                for (int to : e.getValue()) {
                    System.out.println("q" + st.id + "\t" + e.getKey() + "\tq" + to);
                }
            }
            for (int to : st.eps) {
                System.out.println("q" + st.id + "\tε\tq" + to);
            }
        }
        System.out.println("Start: q" + start + "   Accept: q" + accept);
    }
}
