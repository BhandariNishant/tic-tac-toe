package androidsamples.java.tictactoe;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class UserRecord {
    private int singlewin;
    private int singleloss;
    private int singledraw;
    private int doublewin;
    private int doubleloss;
    private int doubledraw;

    public UserRecord() {
        // Default constructor required for calls to DataSnapshot.getValue(UserRecord.class)
        singlewin = 0;
        singleloss = 0;
        singledraw = 0;
        doublewin = 0;
        doubleloss = 0;
        doubledraw = 0;
    }

    public UserRecord(int singlewin, int singleloss, int singledraw, int doublewin, int doubleloss, int doubledraw) {
        this.singlewin = singlewin;
        this.singleloss = singleloss;
        this.singledraw = singledraw;
        this.doublewin = doublewin;
        this.doubleloss = doubleloss;
        this.doubledraw = doubledraw;
    }

    public int getSinglewin() {
        return singlewin;
    }

    public int getSingleloss() {
        return singleloss;
    }

    public int getSingledraw() {
        return singledraw;
    }

    public int getDoublewin() {
        return doublewin;
    }

    public int getDoubleloss() {
        return doubleloss;
    }

    public int getDoubledraw() {
        return doubledraw;
    }

    public void incrementSinglewin() {
        this.singlewin++;
    }

    public void incrementSingleloss() {
        this.singleloss++;
    }

    public void incrementSingledraw() {
        this.singledraw++;
    }

    public void incrementDoublewin() {
        this.doublewin++;
    }

    public void incrementDoubleloss() {
        this.doubleloss++;
    }

    public void incrementDoubledraw() {
        this.doubledraw++;
    }

}
