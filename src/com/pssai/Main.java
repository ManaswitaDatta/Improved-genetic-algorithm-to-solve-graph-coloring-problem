package com.pssai;

import java.io.*;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {


        /*Population population = new Population();
        Algorithm.initializePopulation(population,graph);
        String result = Algorithm.evolve(population, graph);*/

        //run 10 times for given input file

       /* String[] files = new String[20];
        files[0] = "anna.col";
        files[1] = "david.col";
        files[2] = "games120.col";
        files[3] = "huck.col";
        files[4] = "jean.col";
        files[5] = "miles250.col";
        files[6] = "myciel3.col";
        files[7] = "myciel4.col";
        files[8] = "myciel5.col";
        files[9] = "myciel6.col";
        files[10] = "myciel7.col";
        files[11] = "queen5_5.col";
        files[12] = "queen6_6.col";
        files[13] = "queen7_7.col";
        files[14] = "queen8_8.col";
        files[15] = "queen9_9.col";
        files[16] = "queen10_10.col";
        files[17] = "queen11_11.col";
        files[18] = "queen12_12.col";
        files[19] = "queen13_13.col";
        */
    	//138,0,1,239,0,0,0
    	/*args = new String[7];
    	args[0] = "myciel3.col"; //filename 
    	args[1] = "1000";  //no of iteration
    	args[2] = /*"25000"; "10";//population size
    	args[3] = "0.4";//pc
    	args[4] = "0.2";//pm 
    	args[5] = "2";//range divisor
    	args[6] = "4";//optimal range of colours*/
    	
        File file = new File("DIMACS/"+ args[0] );
        FileReader fileReader;
        BufferedReader bufferedReader;
        Graph graph = new Graph();
        ArrayList<Edge> edges = new ArrayList<>();

        //Reading input
        try {
            fileReader = new FileReader(file);
            bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if(line.startsWith("p")){
                    String[] parts = line.split(" ");
                    graph.numberOfVertices = Integer.parseInt(parts[2]);
                }
                if(line.startsWith("e")){
                    String[] parts = line.split(" ");
                    edges.add(new Edge(Integer.parseInt(parts[1]), Integer.parseInt(parts[2])));
                }
            }
            fileReader.close();
            graph.edges = edges;

        } catch (IOException e) {
            e.printStackTrace();
        }

        //Setting parameters
        Algorithm.numberOfIterations = Integer.parseInt(args[1]);
        Algorithm.populationSize     = Integer.parseInt(args[2]);
        Algorithm.pc                 = Double.parseDouble(args[3]);
        Algorithm.pm                 = Double.parseDouble(args[4]);
        Algorithm.rangeDivisor       = Integer.parseInt((args[5]));
        if (args.length == 7) {
            Algorithm.optimalNumberOfColors = Integer.parseInt((args[6]));
        }

        try {
            PrintWriter writer = new PrintWriter("output_" + args[0] + ".csv", "UTF-8");
            Population population = new Population();
            Algorithm.initializePopulation(population, graph);
            String result = Algorithm.evolve(population, graph);
            writer.println("generation,colors,conflicts,sequence");
            writer.println(result);
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //For printing all files
        /*for(String s : files) {
            new File("results/"+s).mkdirs();
            File file = new File("DIMACS/"+ s );
            FileReader fileReader;
            BufferedReader bufferedReader;
            Graph graph = new Graph();
            ArrayList<Edge> edges = new ArrayList<>();

            //Reading input
            try {
                fileReader = new FileReader(file);
                bufferedReader = new BufferedReader(fileReader);
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    if(line.startsWith("p")){
                        String[] parts = line.split(" ");
                        graph.numberOfVertices = Integer.parseInt(parts[2]);
                    }
                    if(line.startsWith("e")){
                        String[] parts = line.split(" ");
                        edges.add(new Edge(Integer.parseInt(parts[1]), Integer.parseInt(parts[2])));
                    }
                }
                fileReader.close();
                graph.edges = edges;

            } catch (IOException e) {
                e.printStackTrace();
            }

            //Setting parameters
            Algorithm.numberOfIterations = Integer.parseInt(args[1]);
            Algorithm.populationSize     = Integer.parseInt(args[2]);
            Algorithm.pc                 = Double.parseDouble(args[3]);
            Algorithm.pm                 = Double.parseDouble(args[4]);
            Algorithm.rangeDivisor       = Integer.parseInt((args[5]));

            //Evolving population

            for (int i = 0; i < 10; i++) {
                try {
                    PrintWriter writer = new PrintWriter("results/"+s+"/"+"output_" + i + "_" + s + ".csv", "UTF-8");
                    Population population = new Population();
                    Algorithm.initializePopulation(population, graph);
                    String result = Algorithm.evolve(population, graph);
                    writer.println("generation,colors,conflicts");
                    writer.println(result);
                    writer.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
            */
    }
}
