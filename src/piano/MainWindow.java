package piano;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;

public final class MainWindow {
    
    public MainWindow() {
        Player.init();
        initContent();
        //initContentActions();
        initFrame();
        //notesPanel.setSorting(4);
        //notesPanel.startPlay();
        //Sorter sorter = new Sorter (this);
        //sorter.startSort(2);
        //sorter.specSort();
    }
    public void clearSelection() {
        for (int i = 0; i < sortButtons.length; ++i) {
            sortButtons[i].setBackground(defaultColor);
        }
    }
    public void setSortingButton(SortEnum index) {
        clearSelection();
        sortButtons[index.ordinal()].setBackground(Color.green);
    }

    private void initContent() {
        frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.PAGE_AXIS));
        notesPanel.setLayout(null);
        notesPanel.setSize (800,500);
        notesPanel.setNumberArray(Alg.generateRandom(Integer.parseInt(arraySize.getModel().getValue().toString())));
        sortPanel.setLayout (new GridLayout(2,6));
        sortPanel.setMinimumSize (new Dimension(1100,100));
        sortPanel.setMaximumSize (new Dimension(1100,100));
        frame.getContentPane().add(sortPanel);
        actionPanel.setMinimumSize (new Dimension(800,100));
        actionPanel.setMaximumSize (new Dimension(800,100));

        startButton.addActionListener((ActionEvent ae) -> {
            notesPanel.startPlay();
        });
        
        
        pauseButton.addActionListener((ActionEvent ae) -> {
            notesPanel.pausePlay();
        });
        resumeButton.addActionListener((ActionEvent ae) -> {
            notesPanel.resumePlay();
        });
        stopButton.addActionListener((ActionEvent ae) -> {
            notesPanel.stopPlay();
        });
        randomizeButton.addActionListener((ActionEvent ae) -> {
            notesPanel.randomize();
        });
        reverseButton.addActionListener((ActionEvent ae) -> {
            notesPanel.reverse();
        });
        descendingButton.addActionListener((ActionEvent ae) -> {
            notesPanel.setDescending(!notesPanel.getDescending());
            if (descendingButton.getText().equals ("ascending")) {
                descendingButton.setText ("descending");
            } else {
                descendingButton.setText ("ascending");
            }
        });
        
        speedSlider.addChangeListener((ChangeEvent ce) -> {
            notesPanel.setSpeed (speedSlider.getValue());
        });
        arraySize.setMinimumSize(new Dimension(70,200));
        arraySize.setMaximumSize(new Dimension(70,200));
        arraySize.addChangeListener((ChangeEvent ce) -> {
            notesPanel.setNumberArraySize(Integer.parseInt(arraySize.getModel().getValue().toString()));
        });

        actionPanel.add (startButton);
        actionPanel.add (pauseButton);
        actionPanel.add (resumeButton);
        actionPanel.add (stopButton);
        actionPanel.add (randomizeButton);
        actionPanel.add (descendingButton);
        actionPanel.add (reverseButton);
        frame.getContentPane().add (arraySize);
        frame.getContentPane().add (actionPanel);
        frame.getContentPane().add (speedSlider);
        SortEnum[] sortEnum = SortEnum.values();
        // Note: the last 2 sorting algorithms (specsort and stacksort) are somewhat buggy
        // to disable their usage can just run through sortButtons.length-2 instead
        for (int i = 0; i < sortButtons.length; ++i) {
            sortButtons[i] = new JButton (Constants.ALG_TEXT[i]);
            sortButtons[i].addActionListener(new SortChangeAction(sortEnum[i], notesPanel, this));
            //sortButtons[i].setAlignmentX((float)0.5);
            sortPanel.add(sortButtons[i]);
            //frame.getContentPane().add(sortButtons[i]);
        }
        
        frame.getContentPane().add(notesPanel);
//        defaultColor = sortButtons[0].getBackground();
        notesPanel.setSorting(SortEnum.BUBBLE_SORT);
        setSortingButton(SortEnum.BUBBLE_SORT);
    }

    private void initFrame() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private final JFrame frame = new JFrame("Piano");
    private final NotesPanel notesPanel = new NotesPanel();
    private final JButton [] sortButtons = new JButton[12];
    private final JPanel sortPanel = new JPanel();
    private final JButton startButton = new JButton("start");
    private final JButton stopButton = new JButton("stop");
    private final JButton pauseButton = new JButton("pause");
    private final JButton resumeButton = new JButton("resume");
    private final JButton randomizeButton = new JButton("randomize");
    private final JButton descendingButton = new JButton("ascending");
    private final JButton reverseButton = new JButton("reverse");
    private final JSlider speedSlider = new JSlider (SwingConstants.HORIZONTAL, 0, 300, 80);
    //private final JTextField arraySize = new JTextField ("31");
    private final SpinnerModel spinnerModel = new SpinnerNumberModel (30, 1, 50, 1);
    private final JSpinner arraySize = new JSpinner (spinnerModel);
    private final JPanel actionPanel = new JPanel();
    private final Color defaultColor = new JButton().getBackground();
}
