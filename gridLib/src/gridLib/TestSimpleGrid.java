package gridLib;

import static org.junit.Assert.*;

import java.awt.Color;

import org.junit.Test;

public class TestSimpleGrid
{

    @Test
    public void test() {
        SimpleGrid g=new SimpleGrid(10,20,false);
        SimpleGrid g2=new SimpleGrid(10,20,false);

        g.setColor(0,0,Color.BLUE);
        g2.setColor(0,0,Color.BLUE);
        System.out.println(g.getColor(0,0));
        System.out.println(g.getColor(0,1));
        assertEquals(g, g2);
    }
    
    @Test
    public void test2() {
        SimpleGrid g=new SimpleGrid(10, 20);
        g.setColor(0,0,Color.BLUE);
        g.setSquareSize(50);
    }
    public static void main(String[] args) {
        SimpleGrid g=new SimpleGrid(10, 20);
        g.setColor(0,0,Color.BLUE);
        g.setSquareSize(50);
    }

}
