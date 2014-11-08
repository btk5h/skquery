package com.w00tmast3r.skquery.elements.events.lang;

public interface Returnable {

    Class<?> getExpectedOutput();

    void setReturn(Object[] returnValue);
}
