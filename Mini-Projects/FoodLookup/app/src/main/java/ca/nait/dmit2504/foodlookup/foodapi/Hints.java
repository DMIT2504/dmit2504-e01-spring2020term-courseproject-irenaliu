package ca.nait.dmit2504.foodlookup.foodapi;
import java.util.ArrayList;
import java.util.List;
public class Hints
{
    private Food food;

    private List<Measures> measures;

    public void setFood(Food food){
        this.food = food;
    }
    public Food getFood(){
        return this.food;
    }
    public void setMeasures(List<Measures> measures){
        this.measures = measures;
    }
    public List<Measures> getMeasures(){
        return this.measures;
    }
}
