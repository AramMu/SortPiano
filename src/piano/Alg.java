/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package piano;

import java.util.Random;

/**
 *
 * @author Aram
 */
public class Alg {
    private static int abs (int x) {
        return x >= 0 ? x : -x;
    }
    
    public static int[] generateRandom (int count) {
        Random rand = new Random();
        int[] res = new int[count];
        for (int i = 0; i < res.length; ++i) {
            res[i] = abs (rand.nextInt())%140+10;
        }
        return res;
    }
}
