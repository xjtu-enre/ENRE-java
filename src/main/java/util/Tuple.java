package util;

public class Tuple<L,R>  {

    private L l;
    private R r;

    public Tuple(L l, R r){
        super();
        this.l = l;
        this.r = r;
    }

    public L getL() {
        return this.l;
    }

    public R getR() {
        return this.r;
    }

    @Override
    public String toString() {
        return "(" + l + "," + r + ")";
    }
}
