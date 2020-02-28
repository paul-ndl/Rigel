package ch.epfl.rigel.math;

public abstract class Interval {

    private final double lowerBound, upperBound;

    /**
     * constructs an interval with the given bounds
     */
    protected Interval (double lowerBound,double upperBound){
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    /**
     * returns the lowerBound
     */
    public double low(){
        return lowerBound;
    }

    /**
     * returns the upperBound
     */
    public double high(){
        return upperBound;
    }

    /**
     * returns the size of the interval
     */
    public double size(){
        return upperBound-lowerBound;
    }

    /**
     * checks if the interval contains v
     */
    public abstract boolean contains(double v);


    /**
     * prevents to use this method
     */
    @Override
    public final boolean equals(Object o){
        throw new UnsupportedOperationException();
    }

    /**
     * prevents to use this method
     */
    @Override
    public final int hashCode(){
        throw new UnsupportedOperationException();
    }

}
