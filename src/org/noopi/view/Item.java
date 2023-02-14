package org.noopi.view;

public enum Item {
    NEW("Nouveau fichier"),
    OPEN("Ouvrir"),
    SAVE("Enregistrer"),
    SAVE_AS("Enregistrer sous");
    
    private String label;

    Item(String lb) {
        label = lb;
    }
    
    String label() {
        return label;
    }
}
