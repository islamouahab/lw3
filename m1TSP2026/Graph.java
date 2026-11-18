package m1TSP2026;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.io.File;
import java.util.Scanner;
import java.io.PrintWriter;

public class Graph {
    public Map<Node, List<Edge>> adjEdList;

    public void GraphBuilder(int []sa){
        adjEdList = new HashMap<>();
        int id = 1;
        List<Integer> nodes = new ArrayList<>();
        Node pred = new Node(1,this,1+"");
        addNode(pred);
        nodes.add(1);
        for (int i = 0; i < sa.length; i++){
            int n = sa[i];
            if(n == 0){
                if (i < sa.length - 1) {
                    pred = getNodebyId(++id, this);
                    if (!nodes.contains(id)) {
                        nodes.add(id);
                    }
                }
            }
            else{
              Node succ;
              if(!nodes.contains(n)){
                 succ = new Node(n,this,n+"");
                 addNode(succ);
                 nodes.add(n);
              }
              else{
                 succ = getNodebyId(n, this);
              }
              Edge edge = new Edge(pred, succ);
              if (adjEdList.containsKey(pred)){
                adjEdList.get(pred).add(edge);
              } else {
                List<Edge> edges = new ArrayList<>();
                edges.add(edge);
                adjEdList.put(pred, edges);
              }
            }
        }
    }

    public void fromDotFileBuilder(String filename, String extension){
        this.adjEdList = new HashMap<>();
        String fullPath = filename + extension;
        File file = new File(fullPath);
        if (!file.exists()) {
            file = new File("dotGraphsTestPW2/" + fullPath);
        }
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty() || line.startsWith("#") || line.startsWith("//") || line.startsWith("rankdir")) {
                    continue;
                }
                if (line.contains("->") || line.contains("--")) {
                    String op = "--";
                    if (line.contains("->")) {
                        op = "->";
                    }
                    Integer weight = null;
                    if (line.contains("[")) {
                        int start = line.indexOf("[");
                        int end = line.indexOf("]");
                        String attr = line.substring(start + 1, end);
                        line = line.substring(0, start).trim();
                        String[] attrs = attr.split(",");
                        for (String a : attrs) {
                            String[] kv = a.trim().split("=");
                            if (kv[0].trim().equals("len") || kv[0].trim().equals("label")) {
                                weight = Integer.parseInt(kv[1].trim());
                            }
                        }
                    }
                    String[] parts = line.split(op);
                    int fromId = Integer.parseInt(parts[0].trim());
                    int toId = Integer.parseInt(parts[1].trim());
                    if (weight != null) {
                        addEdge(fromId, toId, weight);
                    } else {
                        addEdge(fromId, toId);
                    }
                } else {
                    if (!line.contains("{") && !line.contains("}")) {
                        int id = Integer.parseInt(line.trim());
                        addNode(id);
                    }
                }
            }
            scanner.close();
        } catch (Exception e) {
        }
    }

    public int[] toSuccessorArray(){
        int[] sa = new int[0];
        List<Node> nodes = getAllNodes();
        Collections.sort(nodes);
        for (Node node : nodes) {
            List<Edge> edges = this.adjEdList.get(node);
            List<Edge> sortedEdges = new ArrayList<>(edges);
            Collections.sort(sortedEdges);
            for (Edge edge : sortedEdges) {
                sa = Arrays.copyOf(sa, sa.length + 1);
                sa[sa.length - 1] = edge.to().getId();
            }
            sa = Arrays.copyOf(sa, sa.length + 1);
            sa[sa.length - 1] = 0;
        }
        return sa;
    }

    public int[][] toAdjMatrix(){
        int size = this.adjEdList.size();
        int[][] matrix = new int[size][size];
        for (Node node : this.adjEdList.keySet()) {
            List<Edge> edges = this.adjEdList.get(node);
            for (Edge edge : edges) {
                int fromIndex = node.getId() - 1;
                int toIndex = edge.to().getId() - 1;
                matrix[fromIndex][toIndex] = matrix[fromIndex][toIndex] + 1;
            }
        }
        return matrix;
    }

    public Graph getReverse(){
        Graph reverseGraph = new Graph();
        for (Node node : this.adjEdList.keySet()) {
            List<Edge> edges = this.adjEdList.get(node);
            for (Edge edge : edges) {
                Node fromNode = edge.from();
                Node toNode = edge.to();
                Edge reverseEdge = new Edge(toNode, fromNode);
                if (reverseGraph.adjEdList.containsKey(toNode)) {
                    reverseGraph.adjEdList.get(toNode).add(reverseEdge);
                } else {
                    List<Edge> newEdges = new ArrayList<>();
                    newEdges.add(reverseEdge);
                    reverseGraph.adjEdList.put(toNode, newEdges);
                }
            }
        }
        return reverseGraph;
    }

    public Graph getTransitiveClosure(){
        Graph tc = this.toSimpleGraph();
        for (Node u : tc.getAllNodes()) {

            List<Node> predecessors = new ArrayList<>();
            for (Node p : tc.getAllNodes()) {
                List<Edge> edgesFromP = tc.adjEdList.get(p);
                if (edgesFromP == null) {
                    continue;
                }
                for (Edge edge : edgesFromP) {
                    if (edge.to().equals(u)) {
                        predecessors.add(p);
                        break;
                    }
                }
            }

            List<Node> successors = new ArrayList<>();
            List<Edge> edgesFromU = tc.adjEdList.get(u);
            if (edgesFromU != null) {
                for (Edge edge : edgesFromU) {
                    successors.add(edge.to());
                }
            }

            for (Node p : predecessors) {
                for (Node s : successors) {
                    boolean edgeExists = false;
                    List<Edge> edgesFromPred = tc.adjEdList.get(p);
                    if (edgesFromPred != null) {
                        for (Edge edge : edgesFromPred) {
                            if (edge.to().equals(s)) {
                                edgeExists = true;
                                break;
                            }
                        }
                    }
                    if (!edgeExists && !p.equals(s)) {
                        tc.addEdge(p, s);
                    }
                }
            }
        }
        return tc;
    }

    public Graph(int... sa){
        GraphBuilder(sa);
    }

    public Graph(String filename){
        fromDotFileBuilder(filename, ".gv");
    }

    public Graph(String filename, String extension){
        fromDotFileBuilder(filename, extension);
    }

    public static Graph fromDotFile(String filename){
        return new Graph(filename, ".gv");
    }

    public static Graph fromDotFile(String filename, String extension){
        return new Graph(filename, extension);
    }

    public String toDotString(){
        String s = "digraph {\n\trankdir=LR\n";
        List<Node> nodes = getAllNodes();
        Collections.sort(nodes);
        for (Node u : nodes) {
            List<Edge> edges = this.adjEdList.get(u);
            if (!edges.isEmpty()) {
                List<Edge> sortedEdges = new ArrayList<>(edges);
                Collections.sort(sortedEdges);
                for (Edge e : sortedEdges) {
                    if (e.isWeighted()) {
                        s = s + "\t" + u.getId() + " -> " + e.to().getId() + " [label=" + e.getWeight() + ", len=" + e.getWeight() + "]\n";
                    } else {
                        s = s + "\t" + u.getId() + " -> " + e.to().getId() + "\n";
                    }
                }
            } else {
                if (inDegree(u) == 0) {
                    s = s + "\t" + u.getId() + "\n";
                }
            }
        }
        s = s + "}\n";
        return s;
    }

    public void toDotFile(String fileName){
        toDotFile(fileName, ".gv");
    }

    public void toDotFile(String fileName, String extension){
        try {
            PrintWriter writer = new PrintWriter(new File(fileName + extension));
            writer.print(toDotString());
            writer.close();
        } catch (Exception e) {
        }
    }

    public int nbNodes(){
        return this.adjEdList.size();
    }

    public boolean usesNode(Node n){
        return this.adjEdList.containsKey(n);
    }

    public boolean usesNode(int id){
        return getNode(id) != null;
    }

    public boolean holdsNode(Node n){
        return this.adjEdList.containsKey(n) && n.getGraph() == this;
    }

    public Node getNode(int id){
        for (Node node : this.adjEdList.keySet()) {
            if (node.getId() == id) {
                return node;
            }
        }
        return null;
    }

    public boolean addNode(Node n){
        if (this.adjEdList.containsKey(n)) {
            return false; 
        }
        this.adjEdList.put(n, new ArrayList<>()); 
        return true;
    }

    public boolean addNode(int id){
        Node n = new Node(id, this, id + "");
        return addNode(n);
    }

    public boolean removeNode(Node n){
        if (!this.adjEdList.containsKey(n)) {
            return false; 
        }
        this.adjEdList.remove(n); 
        for (List<Edge> edges : this.adjEdList.values()) {
            for (int i = edges.size() - 1; i >= 0; i--) {
                if (edges.get(i).to().equals(n)) {
                    edges.remove(i);
                }
            }
        }
        return true;
    }

    public boolean removeNode(int id){
        return removeNode(getNode(id));
    }

    public List<Node> getAllNodes(){
        List<Node> nodes = new ArrayList<>();
        nodes = new ArrayList<>(this.adjEdList.keySet());
        return nodes;
    }

    public int largestNodeId(){
        int maxId = 0;
        for (Node node : this.adjEdList.keySet()) {
            if (node.getId() > maxId) {
                maxId = node.getId();
            }
        }
        return maxId;
    }

    public int smallestNodeId(){
        int minId = Integer.MAX_VALUE;
        for (Node node : this.adjEdList.keySet()) {
            if (node.getId() < minId) {
                minId = node.getId();
            }
        }
        return minId;
    }

    public List<Node> getSuccessors(Node n){
        List<Node> successors = new ArrayList<>();
        List<Edge> edges = this.adjEdList.get(n);
        for (Edge edge : edges) {
            if (!successors.contains(edge.to())) {
                successors.add(edge.to());
            }
        }
        return successors;
    }

    public List<Node> getSuccessors(int id){
        return getSuccessors(getNode(id));
    }

    public List<Node> getSuccessorsMulti(Node n){
        List<Node> successors = new ArrayList<>();
        List<Edge> edges = this.adjEdList.get(n);
        for (Edge edge : edges) {
            successors.add(edge.to());
        }
        return successors;
    }

    public List<Node> getSuccessorsMulti(int id){
        return getSuccessorsMulti(getNode(id));
    }

    public boolean adjacent(Node u, Node v){
        List<Edge> edges = this.adjEdList.get(u);
        for (Edge edge : edges) {
            if (edge.to().equals(v)) {
                return true;
            }
        }
        return false;
    }

    public boolean adjacent(int uId, int vId){
        return adjacent(getNode(uId), getNode(vId));
    }

    public int inDegree(Node n){
        return n.inDegree();
    }

    public int inDegree(int id){
        return inDegree(getNode(id));
    }

    public int outDegree(Node n){
        return n.outDegree();
    }

    public int outDegree(int id){
        return outDegree(getNode(id));
    }

    public int degree(Node n){
        return n.degree();
    }

    public int degree(int id){
        return degree(getNode(id));
    }

    public int nbEdges(){
        int count = 0;
        for (List<Edge> edges : this.adjEdList.values()) {
            for (Edge edge : edges) {
                count++;
            }
        }
        return count;
    }

    public boolean existsEdge(Node u, Node v){
        if (u == null || v == null) {
            return false;
        }
        List<Edge> edges = this.adjEdList.get(u);
        for (Edge edge : edges) {
            if (edge.to().equals(v)) {
                return true;
            }
        }
        return false;
    }

    public boolean existsEdge(int uId, int vId){
        return existsEdge(getNode(uId), getNode(vId));
    }

    public boolean existsEdge(Edge edge){
        return existsEdge(edge.from(), edge.to());
    }

    public boolean isMultiEdge(Node u, Node v){
        List<Edge> edges = this.adjEdList.get(u);
        int count = 0;
        for (Edge edge : edges) {
            if (edge.to().equals(v)) {
                count++;
            }
        }
        if (count > 1) {
            return true;
        }
        return false;
    }

    public boolean isMultiEdge(int uId, int vId){
        return isMultiEdge(getNode(uId), getNode(vId));
    }

    public void addEdge(Node from, Node to){
        Edge edge = new Edge(from, to);
        if (this.adjEdList.containsKey(from)) {
            this.adjEdList.get(from).add(edge);
        } else {
            List<Edge> edges = new ArrayList<>();
            edges.add(edge);
            this.adjEdList.put(from, edges);
        }
        if (!this.adjEdList.containsKey(to)) {
            this.adjEdList.put(to, new ArrayList<>());
        }
    }

    public void addEdge(Node from, Node to, int weight){
        Edge edge = new Edge(from, to, weight);
        if (this.adjEdList.containsKey(from)) {
            this.adjEdList.get(from).add(edge);
        } else {
            List<Edge> edges = new ArrayList<>();
            edges.add(edge);
            this.adjEdList.put(from, edges);
        }
        if (!this.adjEdList.containsKey(to)) {
            this.adjEdList.put(to, new ArrayList<>());
        }
    }

    public void addEdge(Edge edge){
        if (edge.isWeighted()) {
            addEdge(edge.from(), edge.to(), edge.getWeight());
        } else {
            addEdge(edge.from(), edge.to());
        }
    }

    public void addEdge(int fromId, int toId){
        Node from = getNodebyId(fromId, this);
        Node to = getNodebyId(toId, this);
        addEdge(from, to);
    }

    public void addEdge(int fromId, int toId, int weight){
        Node from = getNodebyId(fromId, this);
        Node to = getNodebyId(toId, this);
        addEdge(from, to, weight);
    }

    public boolean removeEdge(Node from, Node to){
        if (from == null || to == null) {
            return false;
        }
        List<Edge> edges = this.adjEdList.get(from);
        for (Edge edge : edges) {
            if (edge.to().equals(to)) {
                edges.remove(edge);
                return true;
            }
        }
        return false;
    }

    public boolean removeEdge(int fromId, int toId){
        return removeEdge(getNode(fromId), getNode(toId));
    }

    public boolean removeEdge(Edge edge){
        return removeEdge(edge.from(), edge.to());
    }

    public List<Edge> getOutEdges(Node n){
        return n.getOutEdges();
    }

    public List<Edge> getOutEdges(int id){
        return getOutEdges(getNode(id));
    }

    public List<Edge> getInEdges(Node n){
        return n.getInEdges();
    }

    public List<Edge> getInEdges(int id){
        return getInEdges(getNode(id));
    }

    public List<Edge> getIncidentEdges(Node n){
        List<Edge> incidentEdges = new ArrayList<>();
        incidentEdges.addAll(n.getInEdges());
        incidentEdges.addAll(n.getOutEdges());
        return incidentEdges;
    }

    public List<Edge> getIncidentEdges(int id){
        return getIncidentEdges(getNode(id));
    }

    public List<Edge> getEdges(Node u, Node v){
        List<Edge> result = new ArrayList<>();
        List<Edge> edges = this.adjEdList.get(u);
        for (Edge edge : edges) {
            if (edge.to().equals(v)) {
                result.add(edge);
            }
        }
        return result;
    }

    public List<Edge> getEdges(int uId, int vId){
        return getEdges(getNode(uId), getNode(vId));
    }

    public List<Edge> getAllEdges(){
        List<Edge> allEdges = new ArrayList<>();
        for (List<Edge> edges : this.adjEdList.values()) {
            allEdges.addAll(edges);
        }
        return allEdges;
    }

    public static Node getNodebyId(int id, Graph graph) {
        for (Node node : graph.adjEdList.keySet()) {
            if (node.getId() == id) {
                return node;
            }
        }
        Node n = new Node(id, graph, id + "");
        graph.addNode(n);
        return n;
    }

    public boolean isMultiGraph(){
        List<Node> nodes = new ArrayList<>(this.adjEdList.keySet());
        Map<Edge, Integer> edgeCount = new HashMap<>();
        for (Node node : nodes) {
            List<Edge> edges = this.adjEdList.get(node);
            for (Edge edge : edges) {
                if (!edgeCount.containsKey(edge)) {
                    edgeCount.put(edge, 1);
                } else {
                    edgeCount.put(edge, edgeCount.get(edge) + 1);
                    if (edgeCount.get(edge) > 1) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isSimpleGraph(){
        return !isMultiGraph();
    }

    public boolean hasSelfLoops(){
        List<Node> nodes = new ArrayList<>(this.adjEdList.keySet());
        for (Node node : nodes) {
            List<Edge> edges = this.adjEdList.get(node);
            for (Edge edge : edges) {
                if (edge.from().equals(edge.to())) {
                    return true;
                }
            }
        }
        return false;
    }

    public Graph toSimpleGraph(){
        Graph simpleGraph = new Graph();
        for (Node node : this.adjEdList.keySet()) {
            simpleGraph.adjEdList.put(node, new ArrayList<>());
        }
        Map<Edge, Integer> edgeCount = new HashMap<>();
        for (Node node : this.adjEdList.keySet()) {
            List<Edge> edges = this.adjEdList.get(node);
            for (Edge edge : edges) {
                if (!edgeCount.containsKey(edge)) {
                    edgeCount.put(edge, 1);
                } else {
                    edgeCount.put(edge, edgeCount.get(edge) + 1);
                }
                if (!edge.from().equals(edge.to()) && edgeCount.get(edge) == 1) {
                    simpleGraph.addEdge(edge.from(), edge.to());
                }
            }
        }
        return simpleGraph;
    }

    public Graph copy(){
        Graph copyGraph = new Graph();
        for (Node node : this.adjEdList.keySet()) {
            List<Edge> edges = this.adjEdList.get(node);
            for (Edge edge : edges) {
                copyGraph.addEdge(edge.from(), edge.to());
            }
        }
        return copyGraph;
    }

    public void dfsUtil(Node node, List<Node> visited, List<Node> dfsList) {
        Deque<Node> stack = new ArrayDeque<>();
        stack.push(node);
        while (!stack.isEmpty()) {
            Node currentNode = stack.pop();
            if (!visited.contains(currentNode)) {
                List<Node> successors = getSuccessors(currentNode);
                Collections.sort(successors);
                dfsList.add(currentNode);
                visited.add(currentNode);
                for (int i = successors.size() - 1; i >= 0; i--) {
                    Node successor = successors.get(i);
                    if (!visited.contains(successor)) {
                        stack.push(successor);
                    }
                }
            }
        }
    }

    public List<Node> getDFS(){
        List<Node> dfsList = new ArrayList<>();
        List<Node> visited = new ArrayList<>();
        List<Node> nodes = getAllNodes();
        Collections.sort(nodes);
        for (Node node : nodes) {
            if (!visited.contains(node)) {
                dfsUtil(node, visited, dfsList);
            }
        }
        return dfsList;
    }

    public List<Node> getDFS(Node u){
        List<Node> dfsList = new ArrayList<>();
        List<Node> visited = new ArrayList<>();
        dfsUtil(u, visited, dfsList);
        for (Node node : this.adjEdList.keySet()) {
            if (!visited.contains(node)) {
                dfsUtil(node, visited, dfsList);
            }
        }
        return dfsList;
    }

    public List<Node> getDFS(int id){
        return getDFS(getNode(id));
    }

    public void bfsUtil(Node node, List<Node> visited, List<Node> bfsList) {
        Deque<Node> queue = new ArrayDeque<>();
        queue.add(node);
        visited.add(node);
        while (!queue.isEmpty()) {
            Node currentNode = queue.poll();
            bfsList.add(currentNode);
            List<Node> successors = getSuccessors(currentNode);
            Collections.sort(successors);
            for (Node successor : successors) {
                if (!visited.contains(successor)) {
                    visited.add(successor);
                    queue.add(successor);
                }
            }
        }
    }

    public List<Node> getBFS(){
        List<Node> bfsList = new ArrayList<>();
        List<Node> visited = new ArrayList<>();
        List<Node> nodes = getAllNodes();
        Collections.sort(nodes);
        for (Node node : nodes) {
            if (!visited.contains(node)) {
                bfsUtil(node, visited, bfsList);
            }
        }
        return bfsList;
    }

    public List<Node> getBFS(Node u){
        List<Node> bfsList = new ArrayList<>();
        List<Node> visited = new ArrayList<>();
        bfsUtil(u, visited, bfsList);
        for (Node node : this.adjEdList.keySet()) {
            if (!visited.contains(node)) {
                bfsUtil(node, visited, bfsList);
            }
        }
        return bfsList;
    }

    public List<Node> getBFS(int id){
        return getBFS(getNode(id));
    }

    static int time = 0;

    public void dfsWithVisitInfoUtil(Node u, List<Node> dfsList, Map<Node, NodeVisitInfo> nodeVisit, Map<Edge, EdgeVisitType> edgeVisit){
        time++;
        nodeVisit.get(u).discovery = time;
        nodeVisit.get(u).color = NodeColor.GRAY;
        dfsList.add(u);
        List<Edge> edges = this.adjEdList.get(u);
        for (Edge edge : edges) {
            Node v = edge.to();
            if (nodeVisit.get(v).color == NodeColor.WHITE) {
                edgeVisit.put(edge, EdgeVisitType.TREE);
                nodeVisit.get(v).predecessor = u;
                dfsWithVisitInfoUtil(v, dfsList, nodeVisit, edgeVisit);
            } else if (nodeVisit.get(v).color == NodeColor.GRAY) {
                edgeVisit.put(edge, EdgeVisitType.BACKWARD);
            } else {
                if (nodeVisit.get(u).discovery < nodeVisit.get(v).discovery) {
                    edgeVisit.put(edge, EdgeVisitType.FORWARD);
                } else {
                    edgeVisit.put(edge, EdgeVisitType.CROSS);
                }
            }
        }
        nodeVisit.get(u).color = NodeColor.BLACK;
        time++;
        nodeVisit.get(u).finished = time;
    }

    public List<Node> getDFSWithVisitInfo(Map<Node, NodeVisitInfo> nodeVisit, Map<Edge, EdgeVisitType> edgeVisit){
        Node u = getNode(smallestNodeId());
        return getDFSWithVisitInfo(u, nodeVisit, edgeVisit);
    }

    public List<Node> getDFSWithVisitInfo(Node u, Map<Node, NodeVisitInfo> nodeVisit, Map<Edge, EdgeVisitType> edgeVisit){
        time = 0;
        List<Node> dfsList = new ArrayList<>();
        for (Node node : this.adjEdList.keySet()) {
            nodeVisit.put(node, new NodeVisitInfo());
        }
        dfsWithVisitInfoUtil(u, dfsList, nodeVisit, edgeVisit);
        for (Node node : this.adjEdList.keySet()) {
            if (nodeVisit.get(node).color == NodeColor.WHITE) {
                dfsWithVisitInfoUtil(node, dfsList, nodeVisit, edgeVisit);
            }
        }
        return dfsList;
    }
}
