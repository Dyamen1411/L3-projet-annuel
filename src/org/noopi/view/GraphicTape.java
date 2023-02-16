package org.noopi.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JComponent;

public class GraphicTape extends JComponent {

    // ATTRIBUTS

    //private StdTapeModel tapeModel;

    // Marge horizontale de part et d'autre du composant
    private static final int MARGIN = 20;
    // Largeur préférée du ruban
    private static final int PREFERED_WIDTH = 2 * MARGIN + 800;

    private static final int PREFERED_HEIGHT = 100;
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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int w = getWidth() - 2 * MARGIN;
        int frame = getWidth() / 8;
        int tapeHeight = getHeight() / 2;
        int h = getHeight() / 3;
        int e = MARGIN + getWidth() / 10;
        
        // Barres horizontales du ruban
        g.setColor(Color.RED);
        g.fillRect(MARGIN, h, w, THICK);
        g.fillRect(MARGIN, h + tapeHeight, w, THICK);

        // Barres verticales du ruban
        int barreNb = 9;
        for (int i = 0; i < barreNb; i++) {
            int x = i * frame + MARGIN + e;
            g.fillRect(x, h, 1, tapeHeight);
        }
    }
}
