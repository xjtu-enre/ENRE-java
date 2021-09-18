package util;

public class Tuple<L,R>  {

    private L l;
    private R r;

    public Tuple(L relation, R id){
        super();
        this.l = relation;
        this.r = id;
    }

    public L getRelation() {
        return this.l;
    }

    public R getId() {
        return this.r;
    }

    @Override
    public String toString() {
        return "(" + l + "," + r + ")";
    }
}
