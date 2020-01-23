package gridLib;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;

public class SimpleGrid extends JFrame
{
    
    private int numRows;
    private int numCols;
    private Color[][] cells;
    private BufferedImage[] images;
    protected boolean symbolMode=false;
    protected boolean error=false;
    protected Color errorColor;
    protected int errorRow;
    protected int errorCol;
    protected String errorMessage;
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
    
    public void setLetterMode(boolean letterMode) {
        this.symbolMode = letterMode;
    }
    
    private void drawGrid(Graphics2D g) {
        int offset=MARGIN_SIZE;
        
        // has someone tried to set a square that is out of bounds?
        if (error) {
            // TODO: add an arrow if the square is way out of bounds
            g.setColor(errorColor);
            g.fillRect((errorCol+1) * squareSize + offset,
                    (errorRow+1) * squareSize + offset,
                    squareSize,
                    squareSize);
        }

        for (int r = 1; r < numRows + 1; r++) {
            for (int c = 1; c < numCols + 1; c++) {
                g.setColor(cells[r - 1][c - 1]);
                g.fillRect(c * squareSize + offset, r * squareSize + offset, squareSize, squareSize);

                // Draw symbols if desired
                if (symbolMode) {
                    Color col = cells[r - 1][c - 1];
                    BufferedImage img = getColorImage(col);

                    double imgScale = 0.7;
                    int imgMargin = (int) ((1.0 - imgScale) / 2.0 * squareSize);
                    AffineTransform at = new AffineTransform();
                    at.scale(squareSize * imgScale / img.getWidth(), squareSize * imgScale / img.getHeight());
                    g.drawImage(img, new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR),
                            offset + c * squareSize + imgMargin, offset + r * squareSize + imgMargin);
                }
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
        
    }

    private void loadImage(String imageSrc, int position) {
        try {
            images[position] = ImageIO.read(new File("symbols/" + imageSrc).toURI().toURL());
        } catch (IOException e) {
            System.out.println("Image could not be read.");
            e.printStackTrace();
        }
    }

    private BufferedImage getColorImage(Color col) {
        BufferedImage img = null;
        if (col == Color.RED) {
            img = images[0];
        } else if (col == Color.GREEN) {
            img = images[1];
        } else if (col == Color.BLUE) {
            img = images[2];
        } else if (col == Color.BLACK) {
            img = images[3];
        } else if (col == Color.WHITE) {
            img = images[4];
        } else if (col == Color.MAGENTA) {
            img = images[5];
        } else if (col == Color.YELLOW) {
            img = images[6];
        } else if (col == Color.ORANGE) {
            img = images[7];
        } else if (col == Color.CYAN) {
            img = images[8];
        } else if (col == Color.PINK) {
            img = images[9];
        }
        return img;
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

        this.images=new BufferedImage[10];
        for (int i = 1; i < 11; i++) {
            if (i < 10) {
                loadImage("symbol-0" + i + ".png", i - 1);
            } else {
                loadImage("symbol-" + i + ".png", i - 1);
            }
        }

        if (!visible) {
            // Don't create the canvas or make the GUI visible
            // This is useful for testing with JUnit
            return;
        }
        final JFrame outerFrame=this;
        createMenus();
        final int numRows2=this.numRows;
        final int numCols2=this.numCols;
        
        canvas=new JPanel() {
            private JFrame frame=outerFrame;
            private static final long serialVersionUID = 1L;

            @Override
            public void paint(Graphics graphics) {
                Graphics2D g=(Graphics2D)graphics;
                
                drawGrid(g);
                
                //frame.setPreferredSize(new Dimension(numRows*squareSize + MARGIN_SIZE, numCols*squareSize + MARGIN_SIZE));
                setPreferredSize(new Dimension((numCols2+2)*squareSize + 2*MARGIN_SIZE, (numRows2+2)*squareSize + 2*MARGIN_SIZE));
                frame.pack();
            }
            
        };
        
        
        canvas.addMouseListener(new MouseAdapter() {
            private void tooltip(String msg, MouseEvent e) {
                // https://stackoverflow.com/questions/7353021/how-to-show-a-tooltip-on-a-mouse-click
                JComponent component = (JComponent)e.getSource();
                component.setToolTipText(msg);
                MouseEvent phantom = new MouseEvent(
                    component,
                    MouseEvent.MOUSE_MOVED,
                    System.currentTimeMillis(),
                    0,
                    e.getX(),
                    e.getY(),
                    0,
                    false);
                ToolTipManager.sharedInstance().mouseMoved(phantom);
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                Point p=e.getPoint();
                // figure out the grid square
                int col = (p.x - MARGIN_SIZE) / squareSize - 1;
                int row = (p.y - MARGIN_SIZE) / squareSize - 1;
                if (row >= 0 && row < getNumRows() && col >= 0 && col < getNumCols()) {
                    String msg = String.format("row=%d, col=%d\n", row, col);
                    tooltip(msg, e);
                    System.out.printf(msg);
                } else if (error) {
                    if ((errorRow==-1 || errorRow==getNumRows()) && errorCol==col) {
                        System.out.printf(errorMessage+"\n");
                    } else if ((errorCol==-1 || errorCol==getNumCols()) && errorRow==row) {
                        System.out.printf(errorMessage+"\n");
                    }
                    String msg = "<html><p>"+errorMessage.replaceAll("\n","</p><p>")+"</p></html>";
                    tooltip(msg, e);
                }
            }
            
        });

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
                //dispose();
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
        boundsCheck(row, col, color);
        cells[row][col]=color;
        repaint();
    }
    protected void boundsCheck(int row, int col, Color color) {
        // saves the color every time for a potential error color
        // but the color only gets used if boundsCheck() detects an error
        // and sets the error variable to true!
        errorColor=color;
        boundsCheck(row, col);
    }
    protected void boundsCheck(int row, int col)
    {
        if (row >= 0 && row < getNumRows() && col >= 0 && col < getNumCols()) {
            // bounds are OK!
            return;
        }
        error=true;
        
        if (row < 0 && col < 0) {
            // top left
            errorRow=-1;
            errorCol=-1;
        } else if (row < 0 && col >= getNumCols()) {
            // top right
            errorRow=-1;
            errorCol=getNumCols();
        } else if (row < 0) {
            // row out of bounds, col is OK
            errorRow=-1;
            errorCol=col;
        } else if (row >= getNumRows() && col < 0) {
            // bottom left
            errorRow=getNumRows();
            errorCol=-1;
        } else if (row >= getNumRows() && col >= getNumCols()) {
            // bottom right
            errorRow=getNumRows();
            errorCol=getNumCols();
        } else if (row >= getNumRows()) {
            // row out of bounds, col is OK
            errorRow=getNumRows();
            errorCol=col;
        } else if (col < 0) {
            // row is OK at this point
            // left column
            errorRow=row;
            errorCol=-1;
        } else if (col >= getNumCols()) { 
            // right column
            errorRow=row;
            errorCol=getNumCols();
        } 
        // Repaint to draw the current grid with the error indicated
        repaint();
        errorMessage=String.format("(%d, %d) is out of bounds!\n", row, col)+
                String.format("Row must be between %d and %d (you had %d)\n", 0, getNumRows()-1, row)+
                String.format("Col must be between %d and %d (you had %d)", 0, getNumCols()-1, col);
        throw new IndexOutOfBoundsException(errorMessage);
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
    
    public BufferedImage getScreenShot(){
        // This is a hack, but I can't always export the image correctly,
        // especially when running in batch mode.
        int width = (numCols+2)*squareSize + 2*MARGIN_SIZE;
        int height = (numRows+2)*squareSize + 2*MARGIN_SIZE;
        
        // BGR because we don't have an alpha channel
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
        
        Graphics2D g = image.createGraphics();

        // First, paint everything white
        g.setPaint(Color.WHITE);
        g.fillRect(0, 0, image.getWidth(), image.getHeight());
        
        drawGrid(g);
        
        return image;
    }
    
    protected void createMenus() {
        JMenuBar menuBar=new JMenuBar();

        JMenu menu=new JMenu("File");
        menuBar.add(menu);

        // save option
        JMenuItem save=new JMenuItem("Save");
        menu.add(save);
        final SimpleGrid frame=this;
        save.addActionListener(new ActionListener() {
            
            private File currentDir=null;
            
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Save");
                //BufferedImage img = getScreenShot(frame.getContentPane());
                BufferedImage img = frame.getScreenShot();

                //Create a file chooser
                final JFileChooser fc = new JFileChooser(currentDir);
                fc.setSelectedFile(new File("Untitled.png"));
                
                // https://stackoverflow.com/questions/17103171/making-a-jfilechooser-select-the-text-of-the-file-name-but-not-the-extension
                final JTextField textField = getTexField(fc);
                if (textField != null) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            String text = textField.getText();
                            if (text != null) {
                                int index = text.lastIndexOf('.');
                                if (index > -1) {
                                    textField.setSelectionStart(0);
                                    textField.setSelectionEnd(index);
                                }
                            }
                        }
                    });
                }
                
                //In response to a button click:
                int returnVal = fc.showSaveDialog(frame);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    try {
                        // write the image as a PNG
                        ImageIO.write(
                                img,
                                "png",
                                fc.getSelectedFile());
                        currentDir = fc.getSelectedFile().getParentFile();
                    } catch(Exception ex) {
                        JOptionPane.showMessageDialog(frame,
                                "Unable to save file to "+fc.getSelectedFile().getName(),
                                "error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        
            private JTextField getTexField(Container container) {
                for (int i = 0; i < container.getComponentCount(); i++) {
                    Component child = container.getComponent(i);
                    if (child instanceof JTextField) {
                        return (JTextField) child;
                    } else if (child instanceof Container) {
                        JTextField field = getTexField((Container) child);
                        if (field != null) {
                            return field;
                        }
                    }
                }
                return null;
            }
        });
        
        menu.addSeparator();

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
