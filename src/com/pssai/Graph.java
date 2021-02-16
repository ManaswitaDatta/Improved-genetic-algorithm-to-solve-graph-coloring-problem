package com.pssai;


import java.util.ArrayList;

public class Graph {

    int numberOfVertices;
    ArrayList<Edge> edges;

    public Graph(){}

    public Graph(ArrayList<Edge> edges, int numberOfVertices){
        this.numberOfVertices = numberOfVertices;
        this.edges = edges;
    }
    public boolean containsEdge(int v1, int v2){
        for(Edge e : edges) {
            if (e.vertex1 == v1 && e.vertex2 == v2) {
                return true;
            }
        }
        return false;
    }

}
