package com.github.lrhel.rabbot.utility;

public class ExtendedBoolean {
    boolean bool;

    public ExtendedBoolean(boolean value) {
        this.set(value);
    }

    public void set(boolean bool) {
        this.bool = bool;
    }

    public boolean isNot() {
        return !this.bool;
    }

    public boolean is() {
        return this.bool;
    }
}
