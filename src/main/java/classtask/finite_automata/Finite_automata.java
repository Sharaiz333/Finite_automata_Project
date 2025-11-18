/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package classtask.finite_automata;
import java.util.*;

/**
 * Main program for Group 7 project (RE-specific).
 * Builds NFA (with g-loop only on mm branch), converts to DFA, minimizes, prints and simulates.
 */
public class Finite_automata{
    public static void main(String[] args) {
        System.out.println("=========================================");
        System.out.println(" Group 7 TOA: RE -> NFA -> DFA -> Minimize");
        System.out.println(" RE: (g + gg + ggg)* mm + hg + ggh  (g-loop ONLY before mm)");
        System.out.println("=========================================");

        // alphabet
        ArrayList<Character> alphabet = new ArrayList<>(Arrays.asList('g','h','m'));

        // Build fragments:
        NFA g = NFA.symbolNFA('g');
        NFA gg = NFA.concatNFA(NFA.symbolNFA('g'), NFA.symbolNFA('g'));
        NFA ggg = NFA.concatNFA(gg, NFA.symbolNFA('g'));

        // inside = (g + gg + ggg)
        NFA inside = NFA.unionNFA(NFA.unionNFA(g, gg), ggg);
        // loop = (inside)*
        NFA loop = NFA.starNFA(inside);

        // mm branch (concatenate two m's)
        NFA mm = NFA.concatNFA(NFA.symbolNFA('m'), NFA.symbolNFA('m'));
        // branch1 = loop · mm  (this ensures only mm gets leading g-loop)
        NFA branch1 = NFA.concatNFA(loop, mm);

        // hg branch (exact)
        NFA hg = NFA.concatNFA(NFA.symbolNFA('h'), NFA.symbolNFA('g'));
        // ggh branch (exact)
        NFA ggh = NFA.concatNFA(gg, NFA.symbolNFA('h'));

        // full = branch1 + hg + ggh  <-- IMPORTANT: loop concatenated only with mm
        NFA full = NFA.unionNFA(NFA.unionNFA(branch1, hg), ggh);

        // Print NFA
        full.print();

        // Convert NFA -> DFA
        DFAConverter.DFAResult dfaRes = DFAConverter.nfaToDfa(full, alphabet);
        DFAConverter.printDFA(dfaRes.dfa, alphabet, dfaRes.idToSet, "DFA Transition Table (before minimization)");

        // Minimization
        DFAConverter.DFA mdfa = DFAConverter.minimizeDFA(dfaRes.dfa, alphabet);

        // For printing minimized mapping we provide simple placeholders (minimized states are classes,
        // exact mapping to NFA subsets can be constructed by tracking classes; for simplicity we show minimized transitions).
        ArrayList<HashSet<Integer>> miniMap = new ArrayList<>();
        for (int i = 0; i < mdfa.numStates; i++) {
            HashSet<Integer> h = new HashSet<>();
            h.add(i);
            miniMap.add(h);
        }
        DFAConverter.printDFA(mdfa, alphabet, miniMap, "Minimized DFA Transition Table");

        // Interactive simulation on minimized DFA
        DFAConverter.simulateDFAInteractive(mdfa, alphabet);

        System.out.println("\nProgram finished.");
    }
}
