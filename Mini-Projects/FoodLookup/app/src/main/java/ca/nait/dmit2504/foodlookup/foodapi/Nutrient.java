package ca.nait.dmit2504.foodlookup.foodapi;

public class Nutrient
{
    private int ENERC_KCAL;

    private double PROCNT;

    private double FAT;

    private double CHOCDF;

    private double FIBTG;

    public void setENERC_KCAL(int ENERC_KCAL){
        this.ENERC_KCAL = ENERC_KCAL;
    }
    public int getENERC_KCAL(){
        return this.ENERC_KCAL;
    }
    public void setPROCNT(double PROCNT){
        this.PROCNT = PROCNT;
    }
    public double getPROCNT(){
        return this.PROCNT;
    }
    public void setFAT(double FAT){
        this.FAT = FAT;
    }
    public double getFAT(){
        return this.FAT;
    }
    public void setCHOCDF(double CHOCDF){
        this.CHOCDF = CHOCDF;
    }
    public double getCHOCDF(){
        return this.CHOCDF;
    }
    public void setFIBTG(double FIBTG){
        this.FIBTG = FIBTG;
    }
    public double getFIBTG(){
        return this.FIBTG;
    }
}
