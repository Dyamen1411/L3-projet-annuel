package org.noopi.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;

import javax.swing.JComponent;

import org.noopi.utils.machine.State;
import org.noopi.utils.machine.Symbol;

public class GraphicTape extends JComponent {

    // ATTRIBUTS

    //private StdTapeModel tapeModel;

    // Marge horizontale de part et d'autre du composant
    // private static final int MARGIN = 20;
    // Largeur préférée du ruban
    private static final int PREFERED_WIDTH =  800;

    private static final int PREFERED_HEIGHT = 200;
    // Epaisseur du bord du ruban
    private static final int THICK = 2;

    // CONSTRUCTEUR

    public GraphicTape() {
        
        Dimension preferedSize = new Dimension(PREFERED_WIDTH, PREFERED_HEIGHT);
        setPreferredSize(preferedSize);
    }

    // COMMANDES

    public void shiftTapeRight() {

    }

    public void shiftTapeLeft() {

    }

    public void setSymbol(Symbol s) {
 
    }

    public void setState(State s) {

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int width = PREFERED_WIDTH;
        int height = PREFERED_HEIGHT;
        int tapeHeight = height / 3;
        int h = (height - tapeHeight) / 2;
        
        // Barres horizontales du ruban
        Graphics2D g2D = (Graphics2D) g;
        g2D.setColor(Color.RED);
        g2D.drawLine(0, h, width, h);
        g2D.drawLine(0, h + tapeHeight, width, h + tapeHeight);
        g2D.setStroke(new BasicStroke(THICK));

        // Barres verticales du ruban
        for(int i = width / 80; i <= width; i += 80) {
            g.drawLine(i, h, i, h + tapeHeight);
        }
    }
}

