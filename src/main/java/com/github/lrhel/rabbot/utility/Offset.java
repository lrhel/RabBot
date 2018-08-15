package com.github.lrhel.rabbot.utility;

public class Offset {
    private int offset;

    public Offset(){
        this.offset = 0;
    }

    public int getOffset() {
        return offset;
    }

    public void plusOffset(int limit) {
        this.offset += limit;
    }

    public void minusOffset(int limit) {
        this.offset -= limit;
    }
}
