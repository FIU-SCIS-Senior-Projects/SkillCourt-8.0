package fiu.com.skillcourt.game;

import java.util.HashMap;

/**
 * Created by e38wam5 on 11/4/2016.
 */

public class Sequences {
    public String name;
    public HashMap<String, String> sequence;
    public Sequences(){}
    public Sequences(String name,HashMap<String, String> sequence)
    {
        this.name=name;
        this.sequence=sequence;
    }
    public HashMap<String, String> Sequence(){return sequence;}
    public String Name(){return name;}

}
