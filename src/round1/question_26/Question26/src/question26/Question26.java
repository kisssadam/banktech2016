/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package question26;

/**
 *
 * @author Czuczi
 */
public class Question26 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Double a = Double.NaN;
        Double b = Double.NaN;
        Double c = -0.0d;
        Double d = 1.0d;
        Double e = 0d;
        Double f = null;
        Double g = null;
        
        System.out.println(a==b);
        System.out.println(f==g);
        System.out.println(a.equals(b));
        System.out.println(c.equals(e));
        System.out.println(d/a/b);
        System.out.println(d/c*-1);
        System.out.println(e/c);
        System.out.println(e/g);
    }

}
