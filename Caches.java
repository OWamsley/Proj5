import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Random;
public class Caches{

    private int refLength;
    private int uniquePages;
    private int availSlots;
    private int fifoHit = 0;
    private int lruHit = 0;
    private int minHit = 0;
    private int randHit = 0;

    private char[] refString;

    public Caches(){
        gatherInputs();
        fillRefString();
        String print = "\n\nRef Str:";
        for(char i : refString){
            print+= "  " + i;
        }
        System.out.println(print);
        System.out.println("--------------------------------------------------------------");
        fifo();
        System.out.println("--------------------------------------------------------------");
        lru();
        System.out.println("--------------------------------------------------------------");
        min();
        System.out.println("--------------------------------------------------------------");
        rand();
        System.out.println("--------------------------------------------------------------\n");
        System.out.println("Cache Hit Rates: ");
        System.out.printf("FIFO  : %2d of %2d = %3.2f",this.fifoHit, this.refLength, (double)(this.fifoHit /  (double)(this.refLength)) );
        System.out.printf("\nLRU   : %2d of %2d = %3.2f",this.lruHit, this.refLength, (double)(this.lruHit /  (double)(this.refLength)) );
        System.out.printf("\nMIN   : %2d of %2d = %3.2f",this.minHit, this.refLength, (double)(this.minHit /  (double)(this.refLength)) );
        System.out.printf("\nRAND  : %2d of %2d = %3.2f",this.randHit, this.refLength, (double)(this.randHit /  (double)(this.refLength)) );
        int lowHit = 10000000;
        String low = "";
        int highHit = 0;
        String high = "";
        if(this.fifoHit < lowHit){
            //low = " FIFO";
            lowHit = this.fifoHit;
        }
        if(this.fifoHit > highHit){
            //high = " FIFO";
            highHit = this.fifoHit;
        }
        if(this.lruHit < lowHit){
            //low = " LRU";
            lowHit = this.lruHit;
        }
        if(this.lruHit > highHit){
            //high = " LRU";
            highHit = this.lruHit;
        }
        if(this.minHit < lowHit){
            //low = " MIN";
            lowHit = this.minHit;
        }
        if(this.minHit > highHit){
            //high = " MIN";
            highHit = this.minHit;
        }
        if(this.randHit < lowHit){
            //low = " RAND";
            lowHit = this.randHit;
        }
        if(this.randHit > highHit){
            //high = " RAND";
            highHit = this.randHit;
        }
        if(this.fifoHit <= lowHit){
            low += " FIFO";
            lowHit = this.fifoHit;
        }
        if(this.fifoHit >= highHit){
            high += " FIFO";
            highHit = this.fifoHit;

        }
        if(this.lruHit <= lowHit){
            low += " LRU";
            lowHit = this.lruHit;
        }
        if(this.lruHit >= highHit){
            high += " LRU";
            highHit = this.lruHit;

        }
        if(this.minHit <= lowHit){
            low += " MIN";
            lowHit = this.minHit;
        }
        if(this.minHit >= highHit){
            high += " MIN";
            highHit = this.minHit;

        }
        if(this.randHit <= lowHit){
            low += " RAND";
            lowHit = this.randHit;
        }
        if(this.randHit >= highHit){
            high += " RAND";
            highHit = this.randHit;

        }
        System.out.println("\n\nBest:  " + high);
        System.out.println("Worst: " + low);
        
    }

    public char[][] fillEmpty(char[][] arr){
        for(int y = 0; y < arr.length; y++){
            for(int x = 0; x < arr[y].length; x ++){
                arr[x][y] = ' ';
            }
        }

        return arr;
    }

    public void rand(){
        char[] slots = new char[this.availSlots];
        Random random = new Random();
        int nextToReplace = 0;
        int xCount = -1;
        char[][] printArray = new char[this.refLength][this.availSlots];
        for(char ref: this.refString){
            xCount++;
            boolean foundSpot = false; 
            for(int y = 0; y < this.availSlots; y ++){
                //go through each slot to check if there is a match.
                if(slots[y] == ref){
                    //if the slot is already the same as reference character
                    printArray[xCount][y] = '+';
                    this.randHit++;
                    foundSpot = true;
                    break;
                }
            }
            //if no slot matches are found, it must be placed. 
            if(foundSpot == false){
                nextToReplace = random.nextInt(this.availSlots);
                slots[nextToReplace] = ref;
                printArray[xCount][nextToReplace] = ref;
                
            }
        }
        for(int slot = 0; slot < this.availSlots; slot++){
            //row 
            String print = "RAND  " + slot + " : "; 
            for(int i = 0; i < this.refLength; i ++){
                if(printArray[i][slot] == 0){
                    print+= "   ";
                }
                else{
                print += printArray[i][slot] + "  ";
                }
            }
            System.out.println(print);
        }
    }

    public void min(){
        char[] slots = new char[this.availSlots];
        int xCount = -1;
        char[][] printArray = new char[this.refLength][this.availSlots];
        for(char ref: this.refString){
            xCount++;
            boolean foundSpot = false; 
            for(int y = 0; y < this.availSlots; y ++){
                //go through each slot to check if there is a match.
                if(slots[y] == ref){
                    //if the slot is already the same as reference character
                    printArray[xCount][y] = '+';
                    this.minHit++;
                    foundSpot = true;
                    break;
                }
            }
            //if no slot matches are found, it must be placed. 
            if(foundSpot == false){
                boolean placed = false;
                for(int slot = 0; slot < this.availSlots; slot++){
                    //check for empty slot
                    if(slots[slot] == 0){
                        //found empty slot, place it
                        slots[slot] = ref;
                        printArray[xCount][slot] = ref;
                        placed = true;
                        break;
                    }
                }
                if(placed == false){
                    //no empty slots, check if there is a slot with a character that won't be in the ref string again
                    for(int slot = 0; slot < this.availSlots; slot++){
                        boolean correctSlot = true;
                        for(int remaining = xCount; remaining < this.refLength; remaining++){
                            if(slots[slot] == refString[remaining]){
                                //found that char, slot is not valid. 
                                correctSlot = false;
                            }
                        }
                        if(correctSlot){
                            slots[slot] = ref;
                            printArray[xCount][slot] = ref;
                            placed = true;
                            break;
                        }
                    }
                }
                if(placed == false){
                    //no empty slots, and no slots where the character will not show again. Find the slot with a char that reappears furthest in future. 
                    int bestSlot = 0;
                    int furthestFuture = -1;
                    for(int slot = 0; slot < this.availSlots; slot++){
                        for(int remaining = xCount; remaining < this.refLength; remaining++){
                            if(slots[slot] == refString[remaining]){
                                //found that char, slot is not valid. 
                                if(remaining > furthestFuture){
                                    furthestFuture = remaining;
                                    bestSlot = slot;
                                }
                            }
                        }
                    }
                    //place in the best slot
                    slots[bestSlot] = ref;
                    printArray[xCount][bestSlot] = ref;
                }
            }
        }
        for(int slot = 0; slot < this.availSlots; slot++){
            //row 
            String print = "MIN   " + slot + " : "; 
            for(int i = 0; i < this.refLength; i ++){
                if(printArray[i][slot] == 0){
                    print+= "   ";
                }
                else{
                print += printArray[i][slot] + "  ";
                }
            }
            System.out.println(print);
        }
    }

    public void lru(){
        ArrayList<Integer> lastUsedSlot = new ArrayList<>();
        for(int i = 0; i < this.availSlots; i++){
            lastUsedSlot.add(i);
        }
        char[] slots = new char[this.availSlots];
        int xCount = -1;
        int yCount = 0;
        int nextToReplace = 0;
        char[][] printArray = new char[this.refLength][this.availSlots];
        for(char ref: this.refString){
            xCount++;
            boolean foundSpot = false; 
            for(int y = 0; y < this.availSlots; y ++){
                //go through each slot to check if there is a match.
                if(slots[y] == ref){
                    //if the slot is already the same as reference character
                    printArray[xCount][y] = '+';
                    this.lruHit++;
                    foundSpot = true;
                    //move slot used to back of queue. 
                    lastUsedSlot.remove(Integer.valueOf(y));
                    lastUsedSlot.add(y);
                    break;
                }
            }
            //if no slot matches are found, it must be placed. 
            if(foundSpot == false){
                int replaceSlot = lastUsedSlot.get(0);
                slots[replaceSlot] = ref;
                printArray[xCount][replaceSlot] = ref;
                //choosing what slot will be overriden next. 
                lastUsedSlot.remove(Integer.valueOf(replaceSlot));
                lastUsedSlot.add(replaceSlot);
            }
        }
        for(int slot = 0; slot < this.availSlots; slot++){
            //row 
            String print = "LRU   " + slot + " : "; 
            for(int i = 0; i < this.refLength; i ++){
                if(printArray[i][slot] == 0){
                    print+= "   ";
                }
                else{
                print += printArray[i][slot] + "  ";
                }
            }
            System.out.println(print);
        }
    }

    public void fifo(){
        char[] slots = new char[this.availSlots];
        int xCount = -1;
        int yCount = 0;
        int nextToReplace = 0;
        char[][] printArray = new char[this.refLength][this.availSlots];
        for(char ref: this.refString){
            xCount++;
            boolean foundSpot = false; 
            for(int y = 0; y < this.availSlots; y ++){
                //go through each slot to check if there is a match.
                if(slots[y] == ref){
                    //if the slot is already the same as reference character
                    printArray[xCount][y] = '+';
                    this.fifoHit++;
                    foundSpot = true;
                    break;
                }
            }
            //if no slot matches are found, it must be placed. 
            if(foundSpot == false){
                slots[nextToReplace] = ref;
                printArray[xCount][nextToReplace] = ref;
                if((nextToReplace + 1) == this.availSlots){
                    nextToReplace = 0;
                }
                else{
                    nextToReplace++;
                }
            }
        }
        for(int slot = 0; slot < this.availSlots; slot++){
            //row 
            String print = "FIFO  " + slot + " : "; 
            for(int i = 0; i < this.refLength; i ++){
                if(printArray[i][slot] == 0){
                    print+= "   ";
                }
                else{
                print += printArray[i][slot] + "  ";
                }
            }
            System.out.println(print);
        }
    }


    public void fillRefString(){
        char[] letters = new char[15];
        letters[0] = 'A';
        letters[1] = 'B';
        letters[2] = 'C';
        letters[3] = 'D';
        letters[4] = 'E';
        letters[5] = 'F';
        letters[6] = 'G';
        letters[7] = 'H';
        letters[8] = 'I';
        letters[9] = 'J';
        letters[10] = 'K';
        letters[11] = 'L';
        letters[12] = 'M';
        letters[13] = 'N';
        letters[14] = 'O';
        
        this.refString = new char[this.refLength];
        Random random = new Random();
        int key;
        for(int j = 0; j < this.refLength; j++){
            key = random.nextInt(this.uniquePages);
            refString[j] = letters[key];
        }
        

    }

    public void gatherInputs(){
        Scanner input = new Scanner(System.in);
        System.out.println("Enter the page reference pattern length: " );
        this.refLength = input.nextInt();
        System.out.println("Enter how many unique pages from 2 - 15: ");
        this.uniquePages = input.nextInt();
        System.out.println("Enter how many slots available: ");
        this.availSlots = input.nextInt();
    }
}