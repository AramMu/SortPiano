package piano;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

final class NotesPanel extends JPanel {
    
    public NotesPanel() {
        super();
        sorter = new Sorter(this);
        //numbers = new int[32];
    }
    
    void setSorting(SortEnum sortType) {
        this.sortType = sortType;
    }
    void setDescending (boolean bln) {
        sorter.setDescending(bln);
    }
    boolean getDescending() {
        return sorter.getDescending();
    }
    public void setNumberArray (int[] num) {
        numbers = num;
        pressed = new boolean [num.length];
        repaint();
    }
    public int[] getNumberArray () {
        return numbers;
    }
    
    public void setNumberArraySize (int size) {
        stopPlay();
        numbers = Alg.generateRandom(size);
        pressed = new boolean[size];
        repaint();
    }
    
    public void setNumber (int ind, int val) {
        numbers[ind] = val;
        repaint();
    }
    
    public void swap (int a, int b) {
        repaint();
    }
    
    public void setPressed (int ind, boolean bln) {
        pressed[ind] = bln;
        repaint();
    }

    void pausePlay () {
       //playThread.interrupt();
        sorter.setPause(true);
    }
    
    void resumePlay () {
        sorter.setPause(false);
    }
    
    void stopPlay () {
        if(playThread != null) {
            if (playThread.isAlive()) {
                sorter.setPause(false);
            }
            playThread.interrupt();
        }
    }
    
    void randomize () {
        if (playThread != null && playThread.isAlive()) stopPlay();
        numbers = Alg.generateRandom(numbers.length);
        repaint();
    }

    void doClick(int ind) {
        setPressed(ind, true);
        Player.play (ind+39-numbers.length/2);
    }
    
    void unClick (int ind) {
        setPressed (ind, false);
    }

    void updateNum(int a, int x) {
        setNumber (a, x);
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        int width = this.getWidth();
        int height = this.getHeight();
        int scaleX = width / numbers.length;
        double scaleY = height*1.0 / maxValue;
        //g.setColor (Color.white);
        //g.fillRect(0, 0, width, height);
            
        for (int i = 0; i < numbers.length; ++i) {
            if (pressed[i]) {
                g.setColor (Color.red);
            } else {
                g.setColor(Color.green);
            }
            g.fillRect((int) (scaleX*i), (int) (height-scaleY*numbers[i]), (int) scaleX, (int) (scaleY*numbers[i]-0.01));
        }
    }

    void startPlay() {
        if (playThread != null && playThread.isAlive()) return;
        playThread = new Thread() {
            @Override
            public void run () {
                try {
                    sorter.startSort (sortType);
                } catch (InterruptedException ex) {
                    //Logger.getLogger(NotesPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        playThread.start();
        isPlaying = true;
    }
    void reverse() {
        int n = numbers.length;
        for (int i = 0; i < n/2; ++i) {
            int tmp = numbers[i];
            numbers[i] = numbers[n-i-1];
            numbers[n-i-1] = tmp;
        }
        repaint();
    }

    void setSpeed(int value) {
        sorter.setTimer(value);
    }
    
    int [] numbers;
    int maxValue = 150;
    boolean[] pressed;
    SortEnum sortType = SortEnum.BUBBLE_SORT;
    private final Sorter sorter;
    private Thread playThread;
    private boolean isPlaying;
    
}