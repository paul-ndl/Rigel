package ch.epfl.rigel.math;

import ch.epfl.rigel.Preconditions;

import java.util.Locale;

public final class ClosedInterval extends Interval {

    private ClosedInterval (double low, double high){
        super(low, high);
    }


    public static ClosedInterval of(double low, double high){
        Preconditions.checkArgument(high>low);
        return new ClosedInterval(low, high);
    }

    public static ClosedInterval symmetric(double size){
        double symmetricBound = size/2;
        Preconditions.checkArgument(size>0);
        return new ClosedInterval(-symmetricBound,symmetricBound);
    }

    public boolean contains(double v) {
        return (v>=super.low() && v<=super.high());
    }

    public double clip (double v){
        if (v<super.low()){
            return super.low();
        } else if (v>super.high()){
            return super.high();
        } else {
            return v;
        }
    }

    public String toString(){
        return String.format(Locale.ROOT,"[%s,%s]",super.low(),super.high());
    }
}
