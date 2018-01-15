package junit;

import static org.junit.Assert.*;

import java.awt.Color;

import org.junit.Test;

import gridLib.SimpleGrid;

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
    @Test
    public void test12by24() {
        SimpleGrid g=new SimpleGrid(12, 24);
        g.setColor(0,0,Color.BLUE);
    }
    public static void main(String[] args) {
        //TestSimpleGrid g=new TestSimpleGrid();
        //g.test12by24();
        //g.test2();
        SimpleGrid g=new SimpleGrid(10, 15);
        g.setColor(-1, 0, Color.BLUE);
    }

}
