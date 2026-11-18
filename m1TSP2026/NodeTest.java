/**
 * Very small test helper for inspecting node successors.
 */
package m1TSP2026;
class NodeTest{
    private Node n;

    /**
     * Stores the node to be tested.
     */
    public NodeTest(Node n){
        this.n = n;
    }

    /**
     * Prints the successors of the stored node for quick manual checks.
     */
    public void getSuccessorsTest(){
        System.out.println(n.getSuccessors());
    }
}
