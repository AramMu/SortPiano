/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package piano;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

/**
 *
 * @author Aram
 */
public class SortChangeAction implements ActionListener {
    
    public SortChangeAction(SortEnum sortType, NotesPanel notesPanel, MainWindow mainWindow) {
        this.sortType = sortType;
        this.notesPanel = notesPanel;
        this.mainWindow = mainWindow;
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        notesPanel.setSorting(sortType);
        mainWindow.clearSelection();
        ((JButton)ae.getSource()).setBackground(Color.green);
    }
    
    private final SortEnum sortType;
    private final NotesPanel notesPanel;
    private final MainWindow mainWindow;
}
