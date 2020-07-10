package ca.nait.dmit2504.foodlookup.foodapi;
import java.util.ArrayList;

public class Measures
{
    private String uri;

    private String label;

    private ArrayList<Qualified> qualified;

    public void setUri(String uri){
        this.uri = uri;
    }
    public String getUri(){
        return this.uri;
    }
    public void setLabel(String label){
        this.label = label;
    }
    public String getLabel(){
        return this.label;
    }
    public void setQualified(ArrayList<Qualified> qualified){
        this.qualified = qualified;
    }
    public ArrayList<Qualified> getQualified(){
        return this.qualified;
    }
}
