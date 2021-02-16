package com.pssai;

import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class GeneSequence {

    private HashMap<Integer, Integer> genes;


    public GeneSequence(int size){
        genes = new HashMap<Integer,Integer>();
        for(int i = 0; i < size; i++){
            int randomNum = 0;
            if(Algorithm.optimalNumberOfColors == 0)
                randomNum = ThreadLocalRandom.current().nextInt(0,size/Algorithm.rangeDivisor);
            else {
                randomNum = ThreadLocalRandom.current().nextInt(0, Algorithm.optimalNumberOfColors/2);
            }

            genes.put(i,randomNum);
        }
    }
    public GeneSequence(GeneSequence seq){
        genes = seq.genes;

    }
   /* public int getColorOfGene(int index){
        return genes.get(index);
    }
    */
    public void setColorOfGene(int index, int value){
        genes.put(index, value);
    }
    public HashMap<Integer, Integer> getGenes(){
        return genes;
    }
}
