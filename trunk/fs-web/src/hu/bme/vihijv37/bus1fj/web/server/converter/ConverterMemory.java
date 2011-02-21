package hu.bme.vihijv37.bus1fj.web.server.converter;

import java.util.HashMap;

class ConverterMemory {

    private static class Key {
	private final Object key;

	public Key(Object key) {
	    this.key = key;
	}

	@Override
	public boolean equals(Object obj) {
	    if (obj instanceof Key) {
		return ((Key) obj).key == this.key;
	    }
	    return super.equals(obj);
	}

	@Override
	public int hashCode() {
	    return this.key.hashCode();
	}
    }

    private final HashMap<Key, Object> memory;

    public ConverterMemory() {
	this.memory = new HashMap<Key, Object>();
    }

    public Object getConvertedValue(Object object) {
	return this.memory.get(new Key(object));
    }

    public void put(Object object, Object convertedValue) {
	this.memory.put(new Key(object), convertedValue);
    }

}
