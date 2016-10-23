/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package question27;

import java.util.function.Predicate;

/**
 *
 * @author Czuczi
 */
public class Question27 {

    private static boolean test(Predicate<Integer> p) {
        return p.test(5);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println(test(i -> i == 5));
//        System.out.println(test(i -> {i == 5;}));
        System.out.println(test((i) -> i == 5));
//        System.out.println(test((int i) -> i == 5);
//        System.out.println(test((int) -> {return i == 5;}));
        System.out.println(test((i) -> {return i == 5;}));
//        System.out.println(test(((i)) -> i == 5));
    }

}
