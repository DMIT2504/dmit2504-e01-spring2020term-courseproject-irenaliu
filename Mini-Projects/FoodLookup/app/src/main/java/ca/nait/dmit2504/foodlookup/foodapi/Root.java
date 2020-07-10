package ca.nait.dmit2504.foodlookup.foodapi;
import java.util.ArrayList;
import java.util.List;
public class Root
{
    private String text;

    private List<Parsed> parsed;

    private List<Hints> hints;

    private Links _links;

    public void setText(String text){
        this.text = text;
    }
    public String getText(){
        return this.text;
    }
    public void setParsed(List<Parsed> parsed){
        this.parsed = parsed;
    }
    public List<Parsed> getParsed(){
        return this.parsed;
    }
    public void setHints(List<Hints> hints){
        this.hints = hints;
    }
    public List<Hints> getHints(){
        return this.hints;
    }
    public void set_links(Links _links){
        this._links = _links;
    }
    public Links get_links(){
        return this._links;
    }
}
