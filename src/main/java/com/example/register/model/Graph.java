package com.example.register.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Graph {
    private int size;
    private int[][] links;
    private String[] nodes;

    public Graph(int n){
        this.size = n;
        this.links = new int[n][n];
        for(int i=0;i<links.length;i++){
            for(int j=0;j<links[0].length;j++){
                links[i][j] = 0;
            }
        }
        nodes = new String[n];
    }

    public void setNode(int n, String name){
        this.nodes[n] = name;
    }

    public void addLink(int i, int j){
        this.links[i][j] = this.links[i][j]+1;
        this.links[j][i] = this.links[j][i]+1;
    }

    @Override
    public String toString() {

        return '{' +
                "\"size\":" + Integer.toString(size) +
                ", \"links\":" + ArraystoString(links) +
                ", \"nodes\":" + my2String() +
                '}';
    }

    public int[][] getLinks() {
        return links;
    }

    public String my2String(){
        StringBuilder b = new StringBuilder();
        b.append("[");
        for(int i=0;i<this.size;i++){
            b.append("\""+nodes[i].toString()+"\"");
            if(i<this.size-1){
                b.append(",");
            }
        }
        b.append("]");
        return b.toString();
    }

    public String ArraystoString(int [][] arr) {

        if (arr == null)
            return "null";

        int iMax = arr.length - 1;
        if (iMax == -1)
            return "[]";

        StringBuilder b = new StringBuilder();
        b.append("[");
        for (int i = 0; i <= iMax; i++) {
            if (arr[i].length == 0) {
                b.append("[]");
                continue;
            }else if (arr[i] == null) {
                b.append("[null]");
                continue;
            }
            b.append("[");
            for (int j = 0; j < arr[i].length; j++) {
                b.append("" + arr[i][j]);
                if (j == arr[i].length - 1) {
                    b.append("]");
                    continue;
                }
                b.append(",");
            }
            if(i<=iMax-1){
                b.append(",");
            }
        }
        b.append("]");
        return b.toString();
    }
}
