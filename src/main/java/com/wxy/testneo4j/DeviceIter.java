package com.wxy.testneo4j;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

public class DeviceIter<Device> implements Iterable<Device>  {
    @Override
    public Iterator iterator() {
        return new MyIterator();
    }

    private class MyIterator implements Iterator<Device>{
        @Override
        public boolean hasNext() {
            return false;
        }
        @Override
        public Device next() {
            return null;
        }
    }
}
