/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graph;

import java.util.ArrayList;
import java.util.PriorityQueue;

/**
 * Class represented undirected Graphs
 * Main data structure is array of priority Queue PriorityQueue<Edge>[]
 * Implemented only methods necessary for building Graph and
 * Minimum Spanning Tree (MST) 
 * MST is represented by ArrayList<PrimElement>
 * Howevere MST could be built as GraphUndirected
 */
public class GraphUndirected {
    //In Array vertex each vertex contains references 
    //to the list of its neighbors
    private PriorityQueue<Edge>[] vertex;
    //The mark array to keep track of visited vertices
    private String[] vertexVisit;
    //The auxiliary array to keep information about vertices names
    //to compare the MST with the original Graph
    private int[] vertexName;
    
    //Parameter verticesAmount points to the number of
    //vertices in Graph
    public GraphUndirected(int verticesAmount) {
        vertex = new PriorityQueue[verticesAmount];
        vertexVisit = new String[verticesAmount];
        vertexName = new int[verticesAmount];
        //Initialisation the references to the neighbors queues
        for (int i=0; i<vertex.length; i++) {
            vertex[i] = new PriorityQueue();            
        }
    }
    // to create empty Graph
    public GraphUndirected(){}
    
    /*Inner class represents the edge for the vertex
    *with the index of neighbor vertex in the array vertex and its weight
    *Comparable implementation is required for building PriorityQueue 
    */
    private class Edge implements Comparable<Edge>{
        private int neighborIndex;
        private int weight;
        
        private Edge(int neighborIndex, int weight) {
            this.neighborIndex = neighborIndex;
            this.weight = weight;
        }
        
        @Override
        public int compareTo(Edge edge) {
            if (weight < edge.weight) return -1;
            else if (weight > edge.weight) return 1;
            else return 0;
        }
    }
    
    /*
    * Method to build adjacent list of the vertex with name headName
    * new Edge with name neighborName and weight is added
    * if index out of range false is returned
    * Method requires correct vertex and vertexName arrays
    */
    public boolean setEdge (int headName, int neighborName, int weight) {
        int headIndex = this.getVertexIndex(headName);
        int neighborIndex = this.getVertexIndex(neighborName);
        if (headIndex < 0 && headIndex > this.vertex.length && neighborIndex < 0
                && neighborIndex > this.vertex.length) return false;
        Edge edgeHead = new Edge(neighborIndex, weight);
        Edge edgeNeighbor = new Edge(headIndex, weight);
        this.vertex[headIndex].add(edgeHead);
        this.vertex[neighborIndex].add(edgeNeighbor);
        return true;
    }
           
    //Next 2 method to coordinate arrays vertexName with
    //array vertex
    
    /*
    * return vertex name according to the given index
    * if not found return -1
    */
    public int getVertexName(int vertexIndex) {
        if (vertexIndex<0 && vertexIndex>vertexName.length) return -1;
        return vertexName[vertexIndex];
    }
    /*
    * return vertex index according to the given name
    * if not found return -1
    */
    public int getVertexIndex(int name) {
        for (int i=0; i<vertexName.length; i++) {
            if (vertexName[i] == name) return i;
        }
        return -1;
    }
    
    /*
    * Method to build Graph Undirected from 
    * array of arrays (1 vertexName, 2 vertexName, weight)
    * Method requires full array with all edges
    */
    public static GraphUndirected buildGraph(int[][] data) {
        ArrayList<Integer> newVertexList = new ArrayList();
        for (int i=0; i<data.length; i++){
            if (!newVertexList.contains(data[i][0])){
                newVertexList.add(data[i][0]);
            }
            if (!newVertexList.contains(data[i][1])){
                newVertexList.add(data[i][1]);
            }
        }
        GraphUndirected newGraph = new GraphUndirected(newVertexList.size());
        
        for (int i=0; i<newVertexList.size(); i++) newGraph.vertexName[i]=newVertexList.get(i);
        for (int i=0; i<data.length; i++) newGraph.setEdge(data[i][0], data[i][1], data[i][2]);
        
        return newGraph;
    }
    
    /*
    * Method to build Graph Undirected from 
    * array PrimElements(vertexName from, vertexName to, weight)
    */
    public static GraphUndirected buildGraph(ArrayList<PrimElement> pe) {
        ArrayList<Integer> newVertexList = new ArrayList();
        for (int i=0; i<pe.size(); i++){
            if (!newVertexList.contains(pe.get(i).from)){
                newVertexList.add(pe.get(i).from);
            }
            if (!newVertexList.contains(pe.get(i).to)){
                newVertexList.add(pe.get(i).to);
            }
        }
        GraphUndirected newGraph = new GraphUndirected(newVertexList.size());
        
        for (int i=0; i<newVertexList.size(); i++) newGraph.vertexName[i]=newVertexList.get(i);
        for (int i=0; i<pe.size(); i++) newGraph.setEdge(pe.get(i).from, pe.get(i).to, pe.get(i).weight);
        
        return newGraph;
    }
    /*
    *Inner class PrimElement is almost identical to Edge except for
    * the 1st property: vertex from
    * "from" and "to" are indices from vertex when Prim building MST
    * Method convertMST converts "from" and "to" into vertexNames
    * to correlate the MST and labelled Graph
    * Comparable implementation is required for building PriorityQueue 
    */
    private class PrimElement implements Comparable<PrimElement> {
        int from, to, weight;
        
        private PrimElement(int from, Edge edge) {
            this.from = from;
            to = edge.neighborIndex;
            weight = edge.weight;            
        }        

        @Override
        public int compareTo(PrimElement pe) {
            if (weight < pe.weight) return -1;
            else if (weight > pe.weight) return 1;
            else return 0;
        }
    }
    
    /* Method Prim represents Prim algorithm to build MST
    * @param the root vertex 
    * return ArrayList<PrimElement> that represent the structure of MST
    */
    public ArrayList<PrimElement> Prim(int sourceIndex) {
        //Array to represent the structure of MST
        ArrayList<PrimElement> MST = new ArrayList();
        //Cloning the basic structure of this graph for working
        //because PriorityQueue method poll() returns the head of the 
        //PriorityQueue which is the least element and remove it from
        //the PriorityQueue to make the next least element as the head
        PriorityQueue<Edge>[] vertexClone = new PriorityQueue[vertex.length];
        for (int i=0; i<vertexClone.length; i++) vertexClone[i] = new PriorityQueue();
        for (int i=0; i<vertexClone.length; i++) {
            for (Edge edge: vertex[i])vertexClone[i].add(edge);
        }
        //Creating the PriorityQueue to input the next smallest adjacent 
        //PrimElement and to pick up from it the least one
        PriorityQueue<PrimElement> queue = new PriorityQueue();
        //PrimElement "pe" to insert it in the queue
        //PrimElement "peCandidate" to eject ("poll") it from th queue
        PrimElement pe, peCandidate;        
        
        //There's no path from source, MST is empty
        if (vertexClone[sourceIndex].isEmpty()) return MST;
        
        //Adding the 1st element into queue to start with and mark the source
        //"Visited" in order not to choose the edge to that vertex
        pe = new PrimElement(sourceIndex, vertexClone[sourceIndex].poll());
        queue.add(pe);
        vertexVisit[sourceIndex] = "Visited";
        
        //Building loop until the queue become empty
        while (!queue.isEmpty()) {
            //ejecting the least element from queue to check
            peCandidate = queue.poll();
            //if it is unvisited, it is added to MST
            if (vertexVisit[peCandidate.to] != "Visited") {
                MST.add(peCandidate);
                //and is marked "visited"
                vertexVisit[peCandidate.to] = "Visited";
                //adding the least edge from the new vertex to the queue with check to avoid null edge
                if (!vertexClone[peCandidate.to].isEmpty()) {
                    pe = new PrimElement(peCandidate.to, vertexClone[peCandidate.to].poll());
                    queue.add(pe);                    
                }
            }
            //adding the next least edge from the vertex which edge was polled and 
            //removed from the queue with check to avoid null edge
            if (!vertexClone[peCandidate.from].isEmpty()) {
                pe = new PrimElement(peCandidate.from, vertexClone[peCandidate.from].poll());
                queue.add(pe);
            }
        }
        //converting PrimElements from indices to names
        convertMST(MST);
        //It is ready
        return MST;               
    }
    /*
    * method to convert PrimElements from indices to names
    */
    public void convertMST(ArrayList<PrimElement> MST){
        for (int i=0; i<MST.size(); i++) {
            MST.get(i).from = getVertexName(MST.get(i).from);
            MST.get(i).to = getVertexName(MST.get(i).to);
        }
    }
    
    /*
    * Method to calculate the whole distance (total cost) of the MST
    */
    public static int getMSTDistance(ArrayList<PrimElement> MST) {
        int distance = 0;
        for (PrimElement pe: MST) distance = distance + pe.weight;
        return distance;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
    
        // Graph initialization
        GraphUndirected testGraph = new GraphUndirected();
        /*int[][] data = {
            {1, 2, 5}, 
            {1, 3, 4}, 
            {2, 3, 2}, 
            {2, 4, 3}, 
            {3, 5, 4}, 
            {4, 7, 6},
            {5, 4, 2},
            {5, 6, 1},
            {6, 7, 8},
            {7, 8, 2}
        };*/
        int[][] data = {
            {1, 2, 4}, 
            {1, 4, 3}, 
            {1, 6, 5}, 
            {2, 3, 3}, 
            {2, 5, 4}, 
            {3, 4, 2},
            {5, 6, 1},
            {5, 4, 3}
        };
        /*int[][] data = {
            {1, 2, 4}, 
            {2, 3, 3}, 
            {2, 4, 4}, 
            {1, 5, 5}, 
            {1, 8, 8}, 
            {5, 6, 1},
            {5, 7, 2},
            {7, 8, 3},
        };
        int[][] data = {
            {6, 5, 1}, 
            {5, 4, 3}, 
            {4, 3, 2}, 
            {4, 1, 3}, 
            {3, 2, 3}, 
            {1, 6, 5},
            {1, 2, 4},
            {5, 2, 4}
        };*/
        testGraph = buildGraph(data);
        for (int i=0; i < testGraph.vertex.length; i++) {
            System.out.printf("Vertex: %d", testGraph.vertexName[i]);
            System.out.println();
            for(Edge edge: testGraph.vertex[i]) {
                System.out.printf("Edge: from %d to %d with weight %d", testGraph.vertexName[i],
                        testGraph.getVertexName(edge.neighborIndex), edge.weight);
                System.out.println();
            }
        }
        
        ArrayList<PrimElement> MST = testGraph.Prim(0);
        System.out.println("MST arraylist description");
        for (PrimElement pe: MST) {
            System.out.printf("MST Edge: from %d to %d with weight %d", pe.from,
                        pe.to, pe.weight);
            System.out.println();
        }
        System.out.printf("MST total cost: %d", getMSTDistance(MST));
        System.out.println();
        
        GraphUndirected GraphMST = buildGraph(MST);
        System.out.println("MST Graph Description");
        for (int i=0; i < GraphMST.vertex.length; i++) {
            System.out.printf("Vertex: %d", GraphMST.vertexName[i]);
            System.out.println();
            for(Edge edge: GraphMST.vertex[i]) {
                System.out.printf("Edge: from %d to %d with weight %d", GraphMST.vertexName[i],
                        GraphMST.getVertexName(edge.neighborIndex), edge.weight);
                System.out.println();
            }
        
        }
        
        
    }
        
}
