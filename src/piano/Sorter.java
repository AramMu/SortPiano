/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package piano;

import static java.lang.Math.abs;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Aram
 */
public class Sorter {
    private final NotesPanel notesPanel;
    private int[] arr;
    int preva=-1, prevb=-1;
    private int timer=80;
    private boolean isPause=false;
    private boolean isDescending = false;
    
    Sorter (NotesPanel notesPanel) {
        this.notesPanel = notesPanel;
    }
    
    void setTimer (int timer) {
        this.timer = timer;
    }
    int getTimer () {
        return timer;
    }

    void setDescending (boolean bln) {
        isDescending = bln;
    }
    boolean getDescending() {
        return isDescending;
    }
    
    
    public synchronized void setPause (boolean bln) {
        isPause = bln;
        if (!isPause) {
            notify();
        }
    }
    
    void fixup () {
        int n = notesPanel.getNumberArray().length;
        if(preva>=0 && preva<=n) notesPanel.unClick(preva);
        if(prevb>=0 && prevb<=n) notesPanel.unClick(prevb);
        preva = prevb = -1;
    }
    
    private synchronized boolean compareLess (int a, int b) throws InterruptedException {
        if (Thread.interrupted()) {
            fixup();
            throw new InterruptedException();
        }
        
        while (isPause) {
            try {
                wait();
            } catch (InterruptedException ex) {
                fixup();
                throw ex;
                //Logger.getLogger(Sorter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        try {
            Thread.sleep (timer);
        } catch (InterruptedException ex) {
            fixup();
            throw ex;
        }
        if (preva>=0 && prevb>=0) {
            notesPanel.unClick(preva);
            notesPanel.unClick(prevb);
        }
        notesPanel.doClick(a);
        notesPanel.doClick(b);
        preva = a;
        prevb = b;
        if (isDescending) return arr[a] > arr[b];
        return arr[a] < arr[b];
    }
    /*
    private boolean compareLessEqual(int a, int b) throws InterruptedException {
        return !compareLess (b, a);
    }
    */
    private void swap (int a, int b) {
        int tmp = arr[a];
        arr[a] = arr[b];
        arr[b] = tmp;
        notesPanel.updateNum (a,arr[a]);
        notesPanel.updateNum (b,arr[b]);
    }
    
    private void update (int ind, int val) {
        notesPanel.updateNum (ind, val);
    }
    
    public void insertSort () throws InterruptedException {
        arr = notesPanel.getNumberArray();
        for (int i = 1; i < arr.length; ++i) {
            int j = i;
            while (j >= 1 && compareLess (j, j-1)) {
                swap (j, j-1);
                j--;
            }
        }
        fixup ();
    }
    
    private int partition (int[] arr, int b, int e) throws InterruptedException {
        int ind = b;
        for (int i = b; i < e; ++i) {
            if (!compareLess (e-1, i)) {
                swap (i, ind);
                ind++;
            }
        }
        boolean flag = true;
        for (int i = b; i < e-1; ++i) {
            if (arr[i+1] < arr[i]) {
                flag = false;
            }
        }
        if (flag) return -1;
        return ind-1;
    }
    
    private void quickSort (int b, int e) throws InterruptedException {
        if (e-b <= 1) {
            return;
        }
        int p = partition (arr, b, e);
        if (p < 0) return;
        quickSort (b, p);
        quickSort (p+1, e);
    }
    
    public void quickSort () throws InterruptedException {
        arr = notesPanel.getNumberArray();
        quickSort (0, arr.length);
        fixup ();
    }

    private void mergeSort (int[] arr, int b, int e) throws InterruptedException {
        if (e-b <= 1) {
            return;
        }
        int m = (b+e)/2;
        mergeSort (arr, b, m);
        mergeSort (arr, m, e);
        int l=b,r=m;
        int[] tmp = new int[e-b];
        for (int i = 0; i < tmp.length; ++i) {
            if (l < m && (r >= e || compareLess (l, r))){
                tmp[i] = arr[l];
                l++;
            } else {
                tmp[i] = arr[r];
                r++;
            }
        }
        for (int i = 0; i < tmp.length; ++i) {
            arr[b+i] = tmp[i];
            update (b+i, arr[b+i]);
        }
    }
    
    public void mergeSort () throws InterruptedException {
        arr = notesPanel.getNumberArray();
        mergeSort (arr, 0, arr.length);
        fixup ();
    }
    
    public void bubbleSort () throws InterruptedException {
        arr = notesPanel.getNumberArray();
        for (int i = 0; i < arr.length-1; ++i) {
            boolean isSorted = true;
            for (int j = 0; j < arr.length-1-i; ++j) {
                if (compareLess (j+1, j)) {
                    swap (j, j+1);
                    isSorted = false;
                }
            }
            if (isSorted) {
                break;
            }
        }
        fixup ();
    }
    
    public void cocktailSort () throws InterruptedException {
        arr = notesPanel.getNumberArray();
        for (int i = 0; i < arr.length-1; ++i) {
            boolean isSorted = true;
            if (i%2==0) {
                for (int j = i/2; j < arr.length-1-i/2; ++j) {
                    if (compareLess (j+1, j)) {
                        swap (j, j+1);
                        isSorted = false;
                    }
                }
            } else {
                for (int j = arr.length - 2 - (i-1)/2; j >= 1+i/2; --j) {
                    if (compareLess (j, j-1)) {
                        swap (j, j-1);
                        isSorted = false;
                    }
                }
            }
            if (isSorted) {
                break;
            }
        }
        fixup ();
    }
    
    private void heapify (int k, int n) throws InterruptedException {
        while (2*k+1 < n) {
            if (compareLess (k, 2*k+1)) {
                if (2*k+2 < n && compareLess (2*k+1, 2*k+2)) {
                    swap (k, 2*k+2);
                    k = 2*k+2;
                } else {
                    swap (k, 2*k+1);
                    k = 2*k+1;
                }
            } else if (2*k+2 < n && compareLess (k, 2*k+2)) {
                swap (k, 2*k+2);
                k = 2*k+2;
            } else {
                break;
            }
        }
    }
    
    public void heapSort () throws InterruptedException {
        arr = notesPanel.getNumberArray();
        int n = arr.length;
        for (int i = (n-1)/2; i >= 0; --i) {
            heapify (i, n);
        }
        for (int i = n-1; i >= 1; --i) {
            swap (i, 0);
            n--;
            heapify (0, n);
        }
        fixup ();
    }
    
    private int binSearch (int st, int b, int e) throws InterruptedException {
        if (e-b <= 1) {
            if (compareLess (st, b)) {
                return b;
            } else {
                return b+1;
            }
        }
        int m = (b+e)/2;
        if (arr[st] == arr[m]) {
            return m+1;
        }
        if (compareLess (st,m)) {
            return binSearch (st, b, m);
        } else {
            return binSearch (st, m, e);
        }
    } 
    
    public void treeSort () throws InterruptedException {
        arr = notesPanel.getNumberArray();
        int n = arr.length;
        for (int i = 1; i < n; ++i) {
            int ind = binSearch (i, 0, i);
            //System.out.println("ind = " + ind);
            int tmp = arr[i];
            for (int j = i-1; j >= ind; --j) {
                arr[j+1] = arr[j];
                update (j+1, arr[j+1]);
            }
            arr[ind] = tmp;
            update (ind, arr[ind]);
        }
        fixup ();
    }
    
    public void selectSort () throws InterruptedException {
        arr= notesPanel.getNumberArray();
        int n = arr.length;
        for (int i = 0; i < n-1; ++i) {
            int ind = i;
            for (int j = i+1; j < n; ++j) {
                if (compareLess (j, ind)) {
                    ind = j;
                }
            }
            swap (i, ind);
        }
        fixup ();
    }
    
    public void bogoSort () throws InterruptedException {
        arr = notesPanel.getNumberArray();
        int n = arr.length;
        if (n <= 1) return;
        boolean isSorted = false;
        Random rand = new Random();
        while (!isSorted) {
            isSorted=true;
            int a = abs (rand.nextInt())%n;
            int b = abs (rand.nextInt())%(n-1);
            if (b >= a) b++;
            if (a > b) {
                int tmp = a;
                a = b;
                b = tmp;
            }
            if (compareLess (b,a)) {
                swap (a,b);
            }
            for (int i = 0; i < n-1; ++i) {
                if (arr[i] > arr[i+1]) {
                    isSorted=false;
                }
            }
        }
        fixup ();
    }
    
    public void simpleSort () throws InterruptedException {
        arr = notesPanel.getNumberArray();
        for (int i = 0; i < arr.length; ++i) {
            for (int j = i+1; j < arr.length; ++j) {
                if (compareLess (j, i)) {
                    swap (j,i);
                }
            }
        }
        fixup();
    }
    
    public void specSort () throws InterruptedException {
        arr = notesPanel.getNumberArray();
        int n = arr.length;
        if (compareLess (1,0)) {
            swap (0,1);
        }
        swap (1, n-1);
        for (int i = 1; i < n/2; ++i) {
            if (compareLess (i,i-1)) {
                swap (i,i-1);
                int j = i-1;
                while (j-1>=0 && compareLess(j,j-1)) {
                    swap (j,j-1);
                    j--;
                }
                swap (i, n-i-1);
            } else {
                swap (i,n-1-i);
                int j = n-1-i;
                while (j+1<n && compareLess (j+1,j)) {
                    swap (j+1,j);
                    j++;
                }
            }
            
            
            if (compareLess (i,i-1)) {
                swap (i,i-1);
                int j = i-1;
                while (j-1>=0 && compareLess(j,j-1)) {
                    swap (j,j-1);
                    j--;
                }
                //swap (i, n-i-1);
            } else {
                swap (i,n-2-i);
                int j = n-2-i;
                while (j+1<n && compareLess (j+1,j)) {
                    swap (j+1,j);
                    j++;
                }
                swap (n-2-i, i);
            }
        }
        if (n%2==1) {
            int i = n/2;
            if (compareLess (i, i-1)) {
                swap(i,i-1);
                i--;
                while (i-1>=0 && compareLess(i,i-1)) {
                    swap (i,i-1);
                    i--;
                }
            } else {
                //swap (i,i+1);
                //i++;
                while (i+1<n && compareLess(i+1,i)) {
                    swap(i,i+1);
                    i++;
                }
            }
        }
        fixup();
    }
    
    
     public void stackSort () throws InterruptedException {
        arr = notesPanel.getNumberArray();
        int n = arr.length;
        int count=0;
        while (true) {
            /*
            System.out.println("Arr");
            for (int i = 0; i < n; ++i) {
                System.out.print(arr[i] + " ");
            }
            System.out.println("");
*/
            int[] val = new int[n];
            int st=count-1;
            int total=1;
            ArrayList <ArrayList <Integer> > stack = new ArrayList <> ();
            boolean prev=false;
            boolean threw = false;
            for (int i = count; i < n; ++i) {
                boolean flag=false;
                if (!threw) count++;
                while (st >= 0 && compareLess(i, st)) {
                    if (val[st] >= stack.size()) {
                        stack.add(new ArrayList<>());
                    }
                    stack.get(val[st]).add(arr[st]);
                    arr[st] = arr[i];
                    if (st+1<count) count = st+1;
                    st--;
                    flag = true;
                    prev = true;
                    threw = true;
                }     
                arr[st+1] = arr[i];
                if (prev && !flag) {
                    total++;
                    prev=false;
                }
                val[st+1] = total-1;
                st++;
            }
            //System.out.println("count " + count);
            if (st == n-1 || count >= n) break;
            for (int i = 0; i < stack.size(); ++i) {
                //System.out.println("stack " + i);
                for (int j = stack.get(i).size()-1; j >= 0 ; --j) {
                    st++;
                    arr[st] = stack.get(i).get(j);
                    //System.out.print(arr[st] + " ");
                }
                //System.out.println("");
            }
            for (int i = 0; i < n; ++i) {
                update(i, arr[i]);
            }
        }
        fixup ();
    }
    

    void startSort(SortEnum sortType) throws InterruptedException {
        //System.out.println("starting to play type " + sortType);
        switch (sortType) {
            case BUBBLE_SORT:
                bubbleSort();
                break;
            case INSERT_SORT:
                insertSort();
                break;
            case SELECTION_SORT:
                selectSort();
                break;
            case COCKTAIL_SORT:
                cocktailSort();
                break;
            case MERGE_SORT:
                mergeSort();
                break;
            case QUICK_SORT:
                quickSort();
                break;
            case TREE_SORT:
                treeSort();
                break;
            case HEAP_SORT:
                heapSort();
                break;
            case BOGO_SORT:
                bogoSort();
                break;
            case SIMPLE_SORT:
                simpleSort();
                break;
            case SPEC_SORT:
                specSort();
                break;
            case STACK_SORT:
                stackSort();
                break;
            default:
                System.out.println("Error in sorter switch default value entered");
        }
        //System.out.println("finished to play type " + sortType);
    }
}

