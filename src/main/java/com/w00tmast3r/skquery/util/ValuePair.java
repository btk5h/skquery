package com.w00tmast3r.skquery.util;

public class ValuePair<V1, V2> {

    private final V1 firstValue;
    private final V2 secondValue;

    public ValuePair(V1 firstValue, V2 secondValue) {
        this.firstValue = firstValue;
        this.secondValue = secondValue;
    }

    public V1 getFirstValue() {
        return firstValue;
    }

    public V2 getSecondValue() {
        return secondValue;
    }
}
