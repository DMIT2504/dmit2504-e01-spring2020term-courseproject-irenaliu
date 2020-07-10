package ca.nait.dmit2504.foodlookup.foodapi;
import java.util.ArrayList;

public class Qualified
{
    private ArrayList<Qualifier> qualifiers;

    public void setQualifiers(ArrayList<Qualifier> qualifiers){
        this.qualifiers = qualifiers;
    }
    public ArrayList<Qualifier> getQualifiers(){
        return this.qualifiers;
    }
}
