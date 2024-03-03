package FiniteAutomata;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.Multigraph;
import org.jgrapht.graph.builder.GraphTypeBuilder;

import javax.swing.*;
import java.util.List;

public class FiniteAutomatonGraph extends JFrame {
    private Graph<String, DefaultWeightedEdge> graph;

    public FiniteAutomatonGraph() {
        graph = new Multigraph<>(DefaultWeightedEdge.class);
    }

    public void addState(String state) {
        graph.addVertex(state);
    }

    public void addTransition(String sourceState, String targetState, String symbol) {
        graph.addVertex(sourceState);
        graph.addVertex(targetState);

        // Create a unique edge for each transition
        DefaultWeightedEdge edge = graph.addEdge(sourceState, targetState);

        // Store the symbolic weight associated with the edge
    }

    public Graph<String, DefaultWeightedEdge> getGraph() {
        return graph;
    }

    public void drawAutomaton(FiniteAutomaton2 automaton) {
        List<Transition2> transitions = automaton.getDelta();

        for (Transition2 transition : transitions) {
            addState(transition.getFromState());
            addState(transition.getToState());
            addTransition(transition.getFromState(), transition.getToState(), transition.getSymbol()); // Assuming weight is 1 for simplicity
        }
    }

    private Graph<String, DefaultWeightedEdge> createGraph() {
        return GraphTypeBuilder
                .<String, DefaultWeightedEdge>directed()
                .allowingMultipleEdges(true)
                .allowingSelfLoops(true)
                .weighted(true)
                .buildGraph();
    }
}
