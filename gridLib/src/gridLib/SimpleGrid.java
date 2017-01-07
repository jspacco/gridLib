package gridLib;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class SimpleGrid extends JFrame
{
    
    private int numRows;
    private int numCols;
    private Color[][] cells;
    protected final int MARGIN_SIZE = 5;
    protected final int DOUBLE_MARGIN_SIZE=MARGIN_SIZE*2;
    protected int squareSize=25;
    protected JPanel canvas;
    //protected boolean visible=true;

    private static final long serialVersionUID = 1L;
    
    public SimpleGrid(int size) {
        this(size, size, true);
    }
    
    public SimpleGrid(int size, boolean visible) {
        this(size, size, visible);
    }
    
    public SimpleGrid(int numRows, int numCols) {
        this(numRows, numCols, true);
    }
    
    public SimpleGrid(int numRows, int numCols, boolean visible) {
        super("SimpleGrid");
        this.numRows=numRows;
        this.numCols=numCols;
        this.cells=new Color[numRows][numCols];
        for(int i = 0; i < numRows; i++) {
            for(int j = 0; j < numCols; j++) {
                cells[i][j]=Color.WHITE;
            }
        }
        if (!visible) {
            // Don't create the canvas or make the GUI visible
            // This is useful for testing with JUnit
            return;
        }
        final JFrame outerFrame=this;
        createMenus();
        canvas=new JPanel() {
            private JFrame frame=outerFrame;
            private static final long serialVersionUID = 1L;

            @Override
            public void paint(Graphics graphics) {
                Graphics2D g=(Graphics2D)graphics;
                
                int offset=MARGIN_SIZE;
                
                for(int r = 0; r < numRows; r++) {
                    for(int c = 0; c < numCols; c++) {
                        // first color the rectangle white
                        //g.setColor(Color.WHITE);
                        g.setColor(cells[r][c]);
                        g.fillRect(c * squareSize + offset, 
                                r * squareSize + offset, 
                                squareSize, 
                                squareSize);
                        g.setColor(Color.BLACK);
                        g.drawLine(offset+c*squareSize, 
                                offset+r*squareSize, 
                                offset+(c+1)*squareSize, 
                                offset+r*squareSize);
                        g.drawLine(offset+c*squareSize, 
                                offset+r*squareSize, 
                                offset+c*squareSize, 
                                offset+(r+1)*squareSize);
                        g.drawLine(offset+(c+1)*squareSize, 
                                offset+r*squareSize, 
                                offset+(c+1)*squareSize, 
                                offset+(r+1)*squareSize);
                        g.drawLine(offset+c*squareSize, 
                                offset+(r+1)*squareSize, 
                                offset+(c+1)*squareSize, 
                                offset+(r+1)*squareSize);
                    }
                }
                //frame.setPreferredSize(new Dimension(numRows*squareSize + MARGIN_SIZE, numCols*squareSize + MARGIN_SIZE));
                setPreferredSize(new Dimension(numCols*squareSize + 2*MARGIN_SIZE, numRows*squareSize + 2*MARGIN_SIZE));
                frame.pack();
            }
            
        };

        //this.setSize(numCols * squareSize + 2*MARGIN_SIZE, numRows * squareSize + 2*MARGIN_SIZE);
        //this.setPreferredSize(new Dimension(numCols * squareSize + 2*MARGIN_SIZE, numRows * squareSize + 2*MARGIN_SIZE));

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //setSize((numCols + DOUBLE_MARGIN_SIZE) * squareSize, (numRows + DOUBLE_MARGIN_SIZE) * squareSize);
        
        //setContentPane(canvas);
        this.getContentPane().add(canvas, BorderLayout.CENTER);
        this.setResizable(true);
        
        this.pack();
        
        this.setLocation(100,100);
        this.setVisible(true);
        this.toFront();
        
        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
    }
    
    /**
     * FIXME this doesn't resize properly
     * 
     * @param squareSize
     */
    public void setSquareSize(int squareSize) {
        this.squareSize=squareSize;
        //this.setPreferredSize(new Dimension(numCols * squareSize + 2*MARGIN_SIZE, numRows * squareSize + 2*MARGIN_SIZE));
        //this.pack();
        repaint();
    }
    public int getNumRows() {
        return numRows;
    }
    public int getNumCols() {
        return numCols;
    }
    public Color getColor(int row, int col) {
        boundsCheck(row, col);
        return cells[row][col];
    }
    public void setColor(int row, int col, Color color) {
        boundsCheck(row, col);
        cells[row][col]=color;
        repaint();
    }
    protected void boundsCheck(int row, int col)
    {
        if(row < 0 || row >= getNumRows())
            throw new IndexOutOfBoundsException(
                    (new StringBuilder("row ")).append(row).append(" is out of bounds (min row value is 0, max row value is ").
                    append(getNumRows()-1).append(")").toString());
        if(col < 0 || col >= getNumCols())
            throw new IndexOutOfBoundsException(
                    (new StringBuilder("col ")).append(col).append(" is out of bounds (min col value is 0, max col value is ").
                    append(getNumCols()-1).append(")").toString());
    }
    private static BufferedImage getScreenShot(Component component) {
        BufferedImage image = new BufferedImage(
                component.getWidth(),
                component.getHeight(),
                BufferedImage.TYPE_INT_RGB);
        // call the Component's paint method, using
        // the Graphics object of the image.
        component.paint( image.getGraphics() );
        return image;
    }
    
    protected void createMenus() {
        JMenuBar menuBar=new JMenuBar();

        JMenu menu=new JMenu("File");
        menuBar.add(menu);

        // save option
        JMenuItem save=new JMenuItem("Save");
        menu.add(save);
        final JFrame frame=this;
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Save");
                BufferedImage img = getScreenShot(frame.getContentPane());

                //Create a file chooser
                final JFileChooser fc = new JFileChooser();
                //In response to a button click:
                int returnVal = fc.showSaveDialog(frame);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    try {
                        // write the image as a PNG
                        ImageIO.write(
                                img,
                                "png",
                                fc.getSelectedFile());
                    } catch(Exception ex) {
                        JOptionPane.showMessageDialog(frame,
                                "Unable to save file to "+fc.getSelectedFile().getName(),
                                "error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // quit option
        JMenuItem quit=new JMenuItem("Quit");
        menu.add(quit);
        quit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                System.exit(0);
            }
        });
        frame.setJMenuBar(menuBar);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.deepHashCode(cells);
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SimpleGrid other = (SimpleGrid) obj;
        if (!Arrays.deepEquals(cells, other.cells))
            return false;
        return true;
    }
    
    
}
