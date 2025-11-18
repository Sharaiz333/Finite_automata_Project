/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package classtask.finite_automata;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Simple NFA state container.
 */
public class NFAState {
    public int id;
    public HashMap<Character, ArrayList<Integer>> trans = new HashMap<>();
    public ArrayList<Integer> eps = new ArrayList<>();

    public NFAState(int id) {
        this.id = id;
    }

    public void addTrans(char c, int to) {
        trans.computeIfAbsent(c, k -> new ArrayList<>()).add(to);
    }

    public void addEps(int to) {
        eps.add(to);
    }

    @Override
    public String toString() {
        return "q" + id;
    }
}
