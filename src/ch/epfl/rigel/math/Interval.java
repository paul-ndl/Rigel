package ch.epfl.rigel.math;

public abstract class Interval {

    private final double lowerBound, upperBound;

    protected Interval (double lowerBound,double upperBound){
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    public double low(){
        return lowerBound;
    }

    public double high(){
        return upperBound;
    }

    public double size(){
        return upperBound-lowerBound;
    }

    public abstract boolean contains(double v);


    public final boolean equals(Object o){
        throw new UnsupportedOperationException();
    }

    public final int hashCode(){
        throw new UnsupportedOperationException();
    }

}
