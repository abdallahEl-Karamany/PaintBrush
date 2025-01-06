package pbpck;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Stack;
import javax.imageio.ImageIO;
import javax.swing.*;
/**
 *
 * @author Abdallah El-Karamany & Muhamed Mumtaz
 */
public class Buttons extends JPanel {
    private int x1, x2, y1, y2;
    private Shape shape;
    public static Color current_color = Color.BLACK;
    private boolean _fill, _dott, _erase;
    String flag = "freehand";
    ArrayList<Shape> arr;
    private Stack<Shape> redoStack;
    
    
    private JPanel toolbarPanel;
    private JPanel drawingPanel;
    private JButton colorButton, clr, undo, redo, save;
    private Color[] colors = {
        Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.BLUE,
        Color.CYAN, Color.MAGENTA, Color.PINK, Color.GRAY, Color.BLACK
    };
    private JCheckBox dott, fill;
    private ButtonGroup style;
    private ButtonGroup shapes;
    private JRadioButton rec, lin, ov, pen, erase; 

    // Shapes
    private Oval oval;
    private Rectangle rect;
    private Line line;
    private FreeHand freehand;
    private FreeHand eraser;

    public Buttons() {
        setLayout(new BorderLayout());
        
        // Initialize toolbar
        toolbarPanel = new JPanel();
        toolbarPanel.setBackground(new Color(240, 245, 245));
        toolbarPanel.setPreferredSize(new Dimension(getWidth(), 80));
        
        // Initialize drawing panel
        drawingPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                for(Shape value : arr) {
                    value.drawShape(g2d);
                }
                
                // Draw current shape if exists
                if(rect != null) rect.drawShape(g2d);
                if(line != null) line.drawShape(g2d);
                if(oval != null) oval.drawShape(g2d);
                if(freehand != null) freehand.drawShape(g2d);
                if(eraser != null) eraser.drawShape(g2d);
            }
        };
        drawingPanel.setBackground(Color.WHITE);
       
        arr = new ArrayList<>();
        redoStack = new Stack<>();
        shape = null;
        
        toolbar();
        addDrawingPanelListeners();
        
        // Add panels to main Frame
        add(toolbarPanel, BorderLayout.NORTH);
        add(drawingPanel, BorderLayout.CENTER);
    }
    
    private void toolbar() {
        toolbarPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        
        // Color buttons
        for (final Color color : colors) {
            colorButton = new JButton();
            colorButton.setOpaque(true);
            colorButton.setBackground(color);
            colorButton.setPreferredSize(new Dimension(30, 30));
            colorButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    current_color = color;
                    repaint();
                }
            });
            toolbarPanel.add(colorButton);
        }
        
        // Eraser button
        erase = new JRadioButton("Eraser");
        erase.setToolTipText("eraser");
        erase.setBackground(new Color(240, 245, 245));
        erase.setPreferredSize(new Dimension(80, 30));
        erase.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                flag = "eraser";
                _erase = e.getStateChange() == ItemEvent.SELECTED;
            }
        });
        
        // Clear button
        clr = new JButton("Clear");
        clr.setToolTipText("clear");
        clr.setPreferredSize(new Dimension(70, 30));
        clr.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                arr.clear();
                drawingPanel.repaint();
            }
        });
        
        // Undo button
        undo = new JButton("Undo");
        undo.setToolTipText("undo");
        undo.setPreferredSize(new Dimension(70, 30));
        undo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!arr.isEmpty()) {
                    redoStack.push(arr.remove(arr.size()-1));
                    drawingPanel.repaint();
                }
            }
        });
        
        // Redo button
        redo = new JButton("Redo");
        redo.setToolTipText("Redo");
        redo.setPreferredSize(new Dimension(70, 30));
        redo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!redoStack.isEmpty()) {
                    arr.add(redoStack.pop());
                    drawingPanel.repaint();
                }
            }
        });
        
        // Save button
        save = new JButton("Save");
        save.setToolTipText("save");
        save.setPreferredSize(new Dimension(70, 30));
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BufferedImage image = new BufferedImage(drawingPanel.getWidth(), 
                                              drawingPanel.getHeight(), 
                                              BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        drawingPanel.paint(g2d);
        g2d.dispose();
        
        try {
            String filePath = "drawing.png";
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save Image");
            fileChooser.setSelectedFile(new File(filePath));
            
            int userSelection = fileChooser.showSaveDialog(null);
            
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                ImageIO.write(image, "png", fileToSave);
                System.out.println("Image saved successfully to: " + 
                                            fileToSave.getAbsolutePath());}
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Failed to save image.");
        }
           }
        });
        
        // Shape selection buttons
        shapes = new ButtonGroup();
        
        rec = new JRadioButton("Rectangle");
        rec.setToolTipText("rectangle");
        rec.setBackground(new Color(240, 245, 245));
        rec.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                flag = "Rectangle";
            }
        });
        
        lin = new JRadioButton("Line");
        lin.setBackground(new Color(240, 245, 245));
        lin.setToolTipText("line");
        lin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                flag = "Line";
            }
        });
        
        ov = new JRadioButton("Oval");
        ov.setBackground(new Color(240, 245, 245));
        ov.setToolTipText("oval");
        ov.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                flag = "Oval";
            }
        });
        
        pen = new JRadioButton("Pencil");
        pen.setBackground(new Color(240, 245, 245));
        pen.setToolTipText("free hand");
        pen.setSelected(true);
        pen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                flag = "freehand";
                if(current_color == Color.WHITE) {
                    current_color = Color.BLACK;
                }
            }
        });
        
        // Add all shape buttons to button group
        shapes.add(rec);
        shapes.add(lin);
        shapes.add(ov);
        shapes.add(pen);
        shapes.add(erase);
        
        // Style checkboxes
        fill = new JCheckBox("FiLL", false);
        fill.setToolTipText("fill");
        fill.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                _fill = e.getStateChange() == ItemEvent.SELECTED;
            }
        });
        
        dott = new JCheckBox("Dotted", false);
        dott.setToolTipText("dashed");
        dott.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                _dott = e.getStateChange() == ItemEvent.SELECTED;
            }
        });
        style=new ButtonGroup();
        style.add(dott);
        style.add(fill);
        // Add all components to toolbar
        toolbarPanel.add(erase);
        toolbarPanel.add(clr);
        toolbarPanel.add(undo);
        toolbarPanel.add(redo);
        toolbarPanel.add(save);
        toolbarPanel.add(rec);
        toolbarPanel.add(lin);
        toolbarPanel.add(ov);
        toolbarPanel.add(pen);
        toolbarPanel.add(dott);
        toolbarPanel.add(fill);
    }
    
    private void addDrawingPanelListeners() {
        drawingPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                x1 = e.getX();
                y1 = e.getY();
                
                switch(flag) {
                    case "Rectangle":
                        rect = new Rectangle(x1, y1, x1, y1, _dott, _fill, current_color, false);
                        break;
                    case "Line":
                        line = new Line(x1, y1, x1, y1, _dott, _fill, current_color, false);
                        break;
                    case "Oval":
                        oval = new Oval(x1, y1, x1, y1, _dott, _fill, current_color, false);
                        break;
                    case "freehand":
                        freehand = new FreeHand(x1, y1, x1, y1, _dott, _fill, current_color, false);
                        break;
                    case "eraser":
                        eraser = new FreeHand(x1, y1, x1, y1, false, false, Color.WHITE, true);
                        break;
                }
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                x2 = e.getX();
                y2 = e.getY();
                
                switch(flag) {
                    case "Rectangle":
                        rect.setX2(x2);
                        rect.setY2(y2);
                        arr.add(rect);
                        rect = null;
                        break;
                    case "Line":
                        line.setX2(x2);
                        line.setY2(y2);
                        arr.add(line);
                        line = null;
                        break;
                    case "Oval":
                        oval.setX2(x2);
                        oval.setY2(y2);
                        arr.add(oval);
                        oval = null;
                        break;
                    case "freehand":
                        freehand.setX2(x2);
                        freehand.setY2(y2);
                        arr.add(freehand);
                        freehand = null;
                        break;
                    case "eraser":
                        eraser.setX2(x2);
                        eraser.setY2(y2);
                        arr.add(eraser);
                        eraser = null;
                        break;
                }
                drawingPanel.repaint();
            }
        });
        
        drawingPanel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                x2 = e.getX();
                y2 = e.getY();
                
                switch(flag) {
                    case "Rectangle":
                        rect.setX2(x2);
                        rect.setY2(y2);
                        break;
                    case "Line":
                        line.setX2(x2);
                        line.setY2(y2);
                        break;
                    case "Oval":
                        oval.setX2(x2);
                        oval.setY2(y2);
                        break;
                    case "freehand":
                        freehand.setX2(x2);
                        freehand.setY2(y2);
                        break;
                    case "eraser":
                        eraser.setX2(x2);
                        eraser.setY2(y2);
                        break;
                }
                drawingPanel.repaint();
            }
        });
    }
    
    
}
