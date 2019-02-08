import java.util.ArrayList;
/**
 * Write a description of class tester here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class tester {
    public static int count = 0;
    public static void main(String args[]) {
        ArrayList<Integer> thing = new ArrayList<Integer>();
        thing.add(1);
        thing.add(2);
        thing.add(1, 3);
        System.out.println(thing);
    }
}