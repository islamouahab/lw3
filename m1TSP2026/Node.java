package m1TSP2026;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Node implements Comparable<Node> {
    private int id;
    private String name;
    private Graph graph;

    public Node(int id, Graph graph, String name) {
        this.id = id;
        this.graph = graph;
        this.name = name;
    }
    public Node(int id) {
        this.id = id;
    }
    public Node(String name) {
        this.name = name;
    }

    public int getId() {
        return this.id;
    }

    public Graph getGraph() {
        return this.graph;
    }

    public String getName() {
        return this.name;
    }

    public List<Node> getSuccessors() {
        List<Node> nodes = new ArrayList<Node>();
        List<Edge> edges = this.graph.adjEdList.get(this);
        for (Edge edge : edges) {
            if (!nodes.contains(edge.to())) {
                nodes.add(edge.to());
            }
        }
        return nodes;
    }

    public List<Node> getSuccessorsMulti() {
        List<Node> nodes = new ArrayList<Node>();
        List<Edge> edges = this.graph.adjEdList.get(this);
        for (Edge edge : edges) {
            nodes.add(edge.to());
        }
        return nodes;
    }

    public boolean adjacent(Node u) {
        List<Edge> edges = graph.adjEdList.get(this);
        for (Edge edge : edges) {
            if (edge.to().equals(u)) {
                return true;
            }
        }
        return false;
    }

    public boolean adjacent(int uId) {
        return adjacent(this.graph.getNode(uId));
    }

    public int degree() {
        return this.inDegree() + this.outDegree();
    }

    public int inDegree() {
        int i = 0;
        Collection<List<Edge>> AllEdges = graph.adjEdList.values();
        for (List<Edge> edges : AllEdges) {
            for (Edge e : edges) {
                if (e.to().equals(this)) {
                    i++;
                }
            }
        }
        return i;
    }

    public int outDegree() {
        int i = 0;
        List<Edge> edges = graph.adjEdList.get(this);
        for (Edge edge : edges) {
            if (edge.from().equals(this)) {
                i++;
            }
        }
        return i;
    }

    public List<Edge> getOutEdges() {
        List<Edge> AllEdges = graph.adjEdList.get(this);
        List<Edge> OutEdges = new ArrayList<Edge>();
        for (Edge edge : AllEdges) {
            if (edge.from().equals(this)) {
                OutEdges.add(edge);
            }
        }
        return OutEdges;
    }

    public List<Edge> getInEdges() {
        Collection<List<Edge>> AllEdges = graph.adjEdList.values();
        List<Edge> InEdges = new ArrayList<Edge>();
        for (List<Edge> edges : AllEdges) {
            for (Edge e : edges) {
                if (e.to().equals(this)) {
                    InEdges.add(e);
                }
            }
        }
        return InEdges;
    }

    public List<Edge> getIncidentEdges() {
        List<Edge> incidentEdges = new ArrayList<Edge>();
        incidentEdges.addAll(this.getInEdges());
        incidentEdges.addAll(this.getOutEdges());
        return incidentEdges;
    }

    public List<Edge> getEdgesTo(Node u) {
        List<Edge> edges = this.graph.adjEdList.get(this);
        List<Edge> EdgesToU = new ArrayList<Edge>();
        for (Edge e : edges) {
            if (e.to().equals(u)) {
                EdgesToU.add(e);
            }
        }
        return EdgesToU;
    }

    public boolean equals(Object obj) {
        Node other = (Node) obj;
        return this.id == other.id;
    }

    @Override
    public int hashCode() {
        return this.id;
    }

    @Override
    public int compareTo(Node other) {
        return Integer.compare(this.id, other.id);
    }

    @Override
    public String toString() {
        return "" + this.id;
    }
}
