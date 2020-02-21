package ch.epfl.rigel.math;

import ch.epfl.rigel.Preconditions;
import java.util.Locale;

public final class RightOpenInterval extends Interval {

    private RightOpenInterval (double low, double high){
        super(low, high);
    }

    public static RightOpenInterval of(double low, double high){
        Preconditions.checkArgument(high>low);
        return new RightOpenInterval(low, high);
    }

    public static RightOpenInterval symmetric(double size){
        double symmetricBound = size/2;
        Preconditions.checkArgument(size>0);
        return new RightOpenInterval(-symmetricBound,symmetricBound);
    }

    @Override
    public boolean contains(double v) {
        return (v>=super.low() && v<super.high());
    }

    private double floorMod(double x, double y){
        return x - y * Math.floor(x/y);
    }

    public double reduce(double v){
        return super.low() + floorMod(v-super.low(), super.size());
    }

    public String toString(){
        return String.format(Locale.ROOT,"[%s,%s[",super.low(),super.high());
    }

}
