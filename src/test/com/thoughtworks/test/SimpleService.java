package com.thoughtworks.test;

public class SimpleService {
    private Print printer;

    public SimpleService(Print printer) {
        this.printer = printer;
    }

    public Print getPrinter() {
        return printer;
    }
}
