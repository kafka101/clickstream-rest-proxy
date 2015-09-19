package io.kafka101.clickstream.rest.proxy.client.dto;

public final class Record<K, V> {
    public final K key;
    public final V value;

    public Record(V value) {
        this(value, null);
    }

    public Record(V value, K key) {
        this.value = value;
        this.key = key;
    }
}