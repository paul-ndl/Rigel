package ch.epfl.rigel.math;

import ch.epfl.rigel.Preconditions;
import java.util.Locale;

public final class RightOpenInterval extends Interval {

    /**
     * constructs an interval with the given bounds
     */
    private RightOpenInterval (double low, double high){
        super(low, high);
    }

    /**
     * constructs an interval with the given bounds if the bounds are correct
     * throws exception otherwise
     */
    public static RightOpenInterval of(double low, double high){
        Preconditions.checkArgument(high>low);
        return new RightOpenInterval(low, high);
    }

    /**
     * constructs a symmetric interval with the given size if positive
     * throws exception otherwise
     */
    public static RightOpenInterval symmetric(double size){
        double symmetricBound = size/2;
        Preconditions.checkArgument(size>0);
        return new RightOpenInterval(-symmetricBound,symmetricBound);
    }

    /**
     * checks if the interval contains v
     */
    @Override
    public boolean contains(double v) {
        return (v>=super.low() && v<super.high());
    }

    /**
     * defines the method floorMod with double type in arguments
     */
    private double floorMod(double x, double y){
        return x - y * Math.floor(x/y);
    }

    /**
     * reduces v in the interval
     */
    public double reduce(double v){
        return super.low() + floorMod(v-super.low(), super.size());
    }

    /**
     * returns a string form of the interval
     */
    @Override
    public String toString(){
        return String.format(Locale.ROOT,"[%s,%s[",super.low(),super.high());
    }

}
