package com.pssai;


import java.util.ArrayList;
import java.util.Random;

public class Algorithm {

    static int populationSize;
    static double pc, pm;                   // crossover/mutation probability
    static int g, numberOfIterations;       // current generation number, max iterations ie max generation
    static int rangeDivisor;                // number of vertices divided by this, is the range of initial colors
    static int optimalNumberOfColors = 0;   // optimal number of colors

    static String evolve(Population pop, Graph g){
        StringBuffer buffer = new StringBuffer();
        GeneSequence best = null;
        GeneSequence overallBest = null;
        Random r = new Random();
        int leastColors = Integer.MAX_VALUE;
        int leastConflicts = Integer.MAX_VALUE;

        do{
            buffer.append(Algorithm.g +","+ (best==null?"":finalFitnessOf(best)+ "," + fitnessOf(best,g) + "," +
                   printSequence(best) + "\n"));
            Algorithm.g++;
            GeneSequence[] bestSecondBestWorst = null, offspring = null, mutatedOffspring = null;
            boolean genesCrossed = false, genesMutated = false;
            if(r.nextDouble() > Algorithm.pc){
                bestSecondBestWorst = select(pop, g);
                best = bestSecondBestWorst[0];
                offspring = crossover(bestSecondBestWorst[0], bestSecondBestWorst[1], g);
                genesCrossed = true;
                if(r.nextDouble() > Algorithm.pm){
                    mutatedOffspring = mutate(offspring[0], offspring[1], g);
                    genesMutated = true;
                }
            }
            if(genesCrossed && !genesMutated)
                replacePopulation(offspring[0], offspring[1], bestSecondBestWorst[2], pop, g);
            if(genesCrossed && genesMutated)
                replacePopulation(mutatedOffspring[0], mutatedOffspring[1], bestSecondBestWorst[2], pop, g);

            if(best != null && fitnessOf(best, g) < leastConflicts){
                overallBest = best;
                leastConflicts = fitnessOf(best,g);
                leastColors = finalFitnessOf(best);
            } else if(best != null && fitnessOf(best,g) == leastConflicts && finalFitnessOf(best) < leastColors){
                overallBest = best;
                leastConflicts = fitnessOf(best,g);
                leastColors = finalFitnessOf(best);
            }

            if(Algorithm.g%100 == 0){
                System.out.println("g: "+Algorithm.g + ", " + "f: " + finalFitnessOf(overallBest) + ", " + "c: " + fitnessOf(overallBest,g));
            }
        }while(Algorithm.g <= Algorithm.numberOfIterations);
        buffer.append(Algorithm.g +","+ (overallBest==null?"":finalFitnessOf(overallBest)+ "," +
                fitnessOf(overallBest,g) + "," + printSequence(overallBest))  );
        Algorithm.g = 0;
        return buffer.toString();
    }

    static void initializePopulation(Population pop, Graph g){
       for(int i = 0; i < populationSize; i++){
           pop.geneSequences.add(new GeneSequence(g.numberOfVertices));
       }
    }

    // F(seq) from paper
    static int fitnessOf(GeneSequence seq, Graph g){
        int conflicts = 0;
        //System.out.println("edges "+g.edges.size());
        for(Integer i : seq.getGenes().keySet()){
            for(Integer j : seq.getGenes().keySet()){
                //System.out.println("bin hier");
                if(seq.getGenes().get(i) == seq.getGenes().get(j) &&
                        g.containsEdge(i,j)){
                    conflicts++;
                    //System.out.println("Detected conflict." + conflicts);
                }
            }
        }
        return conflicts;
    }

    // f(seq) from paper
    static int finalFitnessOf(GeneSequence seq){
        ArrayList<Integer> numbers = new ArrayList<>();
        for(Integer i : seq.getGenes().keySet()){
            if(!numbers.contains(seq.getGenes().get(i))){
                numbers.add(seq.getGenes().get(i));
            }
        }
        return numbers.size();
    }

    static GeneSequence[] select(Population pop, Graph g){
        GeneSequence[] result = new GeneSequence[3];
        GeneSequence best = null, secondBest = null, worst = null;
        int bestFitness = Integer.MAX_VALUE;
        int worstFitness = Integer.MAX_VALUE;

        for(GeneSequence seq : pop.geneSequences){
            int fitness = Integer.MAX_VALUE;
            if(best == null){
                fitness = fitnessOf(seq,g);
                best = new GeneSequence(seq);
                secondBest = new GeneSequence(seq);
                worst = new GeneSequence(seq);
                bestFitness = fitness;
                worstFitness = fitness;
            }
            if(fitness == Integer.MAX_VALUE)
                fitness = fitnessOf(seq,g);
            if(fitnessOf(seq,g) < bestFitness){
                secondBest = best;
                best = seq;
                bestFitness = fitness;
            } else if (fitnessOf(seq,g) == bestFitness && finalFitnessOf(seq) < finalFitnessOf(best)){
                secondBest = best;
                best = seq;
                bestFitness = fitness;
            }
            if(fitness > worstFitness){
                worstFitness = fitness;
                worst = seq;
            }
        }
        result[0] = best;
        result[1] = secondBest;
        result[2] = worst;
        return result;
    }

    static GeneSequence[] crossover(GeneSequence seq1, GeneSequence seq2, Graph g){
        GeneSequence[] result = new GeneSequence[2];
        GeneSequence offspring1 = new GeneSequence(seq1);
        GeneSequence offspring2 = new GeneSequence(seq2);
        ArrayList<Edge> conflictEdges1 = new ArrayList<>();
        ArrayList<Edge> conflictEdges2 = new ArrayList<>();

        for(Integer i : seq1.getGenes().keySet()){
            for(Integer j : seq1.getGenes().keySet()){
                if(seq1.getGenes().get(i) == seq1.getGenes().get(j) &&
                        g.containsEdge(i,j)){
                    conflictEdges1.add(new Edge(i,j));
                }
            }
        }
        for(Integer i : seq2.getGenes().keySet()){
            for(Integer j : seq2.getGenes().keySet()){
                if(seq2.getGenes().get(i) == seq2.getGenes().get(j) &&
                        g.containsEdge(i,j)){
                    conflictEdges2.add(new Edge(i,j));
                }
            }
        }
        for(Edge e : conflictEdges1){
            offspring1.setColorOfGene(e.vertex2,offspring1.getGenes().get(e.vertex2)+1);
        }
        for(Edge e : conflictEdges2){
            offspring2.setColorOfGene(e.vertex2,offspring2.getGenes().get(e.vertex2)+1);
        }
        result[0] = offspring1;
        result[1] = offspring2;

        return result;
    }

    static GeneSequence[] mutate(GeneSequence seq1, GeneSequence seq2, Graph g){
        GeneSequence[] result = new GeneSequence[2];
        GeneSequence mutated1 = new GeneSequence(seq1);
        GeneSequence mutated2 = new GeneSequence(seq2);
        ArrayList<Edge> conflictEdges1 = new ArrayList<>();
        ArrayList<Edge> conflictEdges2 = new ArrayList<>();

        for(Integer i : seq1.getGenes().keySet()){
            for(Integer j : seq1.getGenes().keySet()){
                if(seq1.getGenes().get(i) == seq1.getGenes().get(j) &&
                        g.containsEdge(i,j)){
                    conflictEdges1.add(new Edge(i,j));
                }
            }
        }
        for(Integer i : seq2.getGenes().keySet()){
            for(Integer j : seq2.getGenes().keySet()){
                if(seq2.getGenes().get(i) == seq2.getGenes().get(j) &&
                        g.containsEdge(i,j)){
                    conflictEdges2.add(new Edge(i,j));
                }
            }
        }
        for(Edge e : conflictEdges1){
            mutated1.setColorOfGene(e.vertex1,mutated1.getGenes().get(e.vertex1)-1);
        }
        for(Edge e : conflictEdges2){
            mutated2.setColorOfGene(e.vertex1,mutated2.getGenes().get(e.vertex1)-1);
        }
        result[0] = mutated1;
        result[1] = mutated2;

        return result;
    }

    static boolean replacePopulation(GeneSequence seq1, GeneSequence seq2, GeneSequence seq3,
                                     Population pop, Graph g){
        int fitnessOfWorst = fitnessOf(seq3, g);
        if(fitnessOf(seq1, g) < fitnessOfWorst){
            pop.geneSequences.remove(seq3);
            pop.geneSequences.add(seq1);
            //System.out.println("replaced something");
        } else if (fitnessOf(seq2, g) < fitnessOfWorst){
            pop.geneSequences.remove(seq3);
            pop.geneSequences.add(seq2);
        }

        return false;
    }

    static String printSequence(GeneSequence seq){
        String output = "";
        for(Integer i : seq.getGenes().keySet()){
            output += "" + seq.getGenes().get(i) + " ";
        }
        return output;
    }


}
