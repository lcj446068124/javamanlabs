package sun.spring.scheduler.batch.processor;

/**
 * Created by root on 2016/3/8.
 */
public class Tuple<K,V> {

    private K key;
    private V value;

    public Tuple(){}

    public Tuple(K key, V value){
        this.key = key;
        this.value = value;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }
}
