package util;

import org.jetbrains.annotations.NotNull;

public class Triple<L, M, R>{
    private L l;
    private R r;
    private M m;

    public Triple(L l, M m, R r){
        super();
        this.m = m;
        this.l = l;
        this.r = r;
    }

    public L getLeft() {
        return this.l;
    }

    public M getMiddle() {
        return this.m;
    }

    public R getRight() {
        return this.r;
    }

    @Override
    public String toString() {
        return "(" + l + ","+ m + r + ")";
    }
}
