package m1TSP2026;

import java.util.List;

public class Edge implements Comparable<Edge> {
    private Node from;
    private Node to;
    private Integer weight;

    public Node from() {
        return this.from;
    }

    public Node to() {
        return this.to;
    }

    public Edge(Node from, Node to) {
        this.from = from;
        this.to = to;
        this.weight = null;
    }

    public Edge(Node from, Node to, int weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    public Edge(int fromId, int toId, int weight, Graph graph) {
        this.from = Graph.getNodebyId(fromId, graph);
        this.to = Graph.getNodebyId(toId, graph);
        this.weight = weight;
    }

    public Edge(int fromId, int toId, Graph graph) {
        this.from = Graph.getNodebyId(fromId, graph);
        this.to = Graph.getNodebyId(toId, graph);
        this.weight = null;
    }

    public boolean isWeighted() {
        return this.weight != null;
    }

    public Integer getWeight() {
        return this.weight;
    }

    public Edge getSymetric() {
        return new Edge(this.to(), this.from(), this.weight);
    }

    public boolean isSelfLoop() {
        return this.from().equals(this.to());
    }

    public boolean isMultiEdge() {
        int counter = 0;
        List<Edge> allEdges = this.from.getGraph().getAllEdges();
        for (Edge e : allEdges) {
            if (e.from().equals(this.from()) && e.to().equals(this.to())) {
                counter++;
            }
        }
        if (counter > 1) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int compareTo(Edge other) {
        if (this.from.getId() != other.from.getId()) {
            return Integer.compare(this.from.getId(), other.from.getId());
        }
        if (this.to.getId() != other.to.getId()) {
            return Integer.compare(this.to.getId(), other.to.getId());
        }
        if (this.weight != null && other.weight != null) {
            return Integer.compare(this.weight, other.weight);
        }
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        Edge other = (Edge) obj;
        return this.from.equals(other.from) && this.to.equals(other.to);
    }

    @Override
    public int hashCode() {
        return this.from.getId() * 1000 + this.to.getId();
    }

    @Override
    public String toString() {
        return this.from.getId() + "->" + this.to.getId();
    }
}
