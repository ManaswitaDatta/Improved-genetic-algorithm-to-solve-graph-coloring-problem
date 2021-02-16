package com.pssai;

public class Edge {

    int vertex1, vertex2;

    public Edge(int vertex1, int vertex2){
        this.vertex1 = vertex1;
        this.vertex2 = vertex2;
    }

    public boolean equals(Edge other){
        if(this.vertex1 == other.vertex1 && this.vertex2 == other.vertex2)
            return true;
        else
            return false;
    }
}
