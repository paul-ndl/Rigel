package ch.epfl.rigel.math;

import ch.epfl.rigel.Preconditions;

import java.util.Locale;

public final class ClosedInterval extends Interval {

    /**
     * constructs an interval with the given bounds
     */
    private ClosedInterval (double low, double high){
        super(low, high);
    }


    /**
     * constructs an interval with the given bounds if the bounds are correct
     * throws exception otherwise
     */
    public static ClosedInterval of(double low, double high){
        Preconditions.checkArgument(high>low);
        return new ClosedInterval(low, high);
    }

    /**
     * constructs a symmetric interval with the given size if positive
     * throws exception otherwise
     */
    public static ClosedInterval symmetric(double size){
        double symmetricBound = size/2;
        Preconditions.checkArgument(size>0);
        return new ClosedInterval(-symmetricBound,symmetricBound);
    }

    /**
     * checks if the interval contains v
     */
    @Override
    public boolean contains(double v) {
        return (v>=super.low() && v<=super.high());
    }

    /**
     * clips v in the interval
     */
    public double clip (double v){
        if (v<super.low()){
            return super.low();
        } else if (v>super.high()){
            return super.high();
        } else {
            return v;
        }
    }

    /**
     * returns a string form of the interval
     */
    public String toString(){
        return String.format(Locale.ROOT,"[%s,%s]",super.low(),super.high());
    }
}
