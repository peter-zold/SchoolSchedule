package schedule.roomarrangement;

import java.io.Serializable;
import java.util.HashMap;

public class CustomHashMapForDisplayingRoomArrangement<K, V, T, S, O, P> extends HashMap<K, CustomHashMapForDisplayingRoomArrangement.Entry<V, T, S, O, P>> implements Serializable {

    private static final long serialVersionUID = 1L;

    private static volatile CustomHashMapForDisplayingRoomArrangement<?, ?, ?, ?, ?, ?> instance;

    private CustomHashMapForDisplayingRoomArrangement() {
        super();
    }

    public static <K, V, T, S, O, P> CustomHashMapForDisplayingRoomArrangement<K, V, T, S, O, P> getInstance() {
        if (instance == null) {
            instance = new CustomHashMapForDisplayingRoomArrangement<>();
        }
        return (CustomHashMapForDisplayingRoomArrangement<K, V, T, S, O, P>) instance;
    }

    public CustomHashMapForDisplayingRoomArrangement(int initialCapacity) {
        super(initialCapacity);
    }

    public static class Entry<V, T, S, O, P> implements Serializable {
        private static final long serialVersionUID = 1L;

        private final V value1;
        private final T value2;
        private final S value3;
        private final O value4;
        private final P value5;

        public Entry(V value1, T value2, S value3, O value4, P value5) {
            this.value1 = value1;
            this.value2 = value2;
            this.value3 = value3;
            this.value4 = value4;
            this.value5 = value5;
        }

        public V getValue1() {
            return value1;
        }

        public T getValue2() {
            return value2;
        }

        public S getValue3() {
            return value3;
        }

        public O getValue4() {
            return value4;
        }

        public P getValue5() {
            return value5;
        }
    }

    public void put(K key, V value1, T value2, S value3, O value4, P value5) {
        Entry<V, T, S, O, P> entry = new Entry<>(value1, value2, value3, value4, value5);
        put(key, entry);
    }

    public V get1(K key) {
        Entry<V, T, S, O, P> entry = get(key);
        return entry != null ? entry.getValue1() : null;
    }

    public T get2(K key) {
        Entry<V, T, S, O, P> entry = get(key);
        return entry != null ? entry.getValue2() : null;
    }

    public S get3(K key) {
        Entry<V, T, S, O, P> entry = get(key);
        return entry != null ? entry.getValue3() : null;
    }

    public O get4(K key) {
        Entry<V, T, S, O, P> entry = get(key);
        return entry != null ? entry.getValue4() : null;
    }

        public P get5(K key) {
            Entry<V, T, S, O, P> entry = get(key);
            return entry != null ? entry.getValue5() : null;
        }

        public Entry<V, T, S, O, P> getHash(Object key) {
            return super.get(key);
        }

        public String getAllElementsAtOnce(K key) {
            Entry<V, T, S, O, P> entry = get(key);
            if (entry != null) {
                return entry.getValue1().toString() + ", " + entry.getValue2().toString() + ", "
                        + entry.getValue3().toString() + ", " + entry.getValue4().toString()
                        + ", " + entry.getValue5().toString();
            }
            return null;
        }

        public Integer getInteger(K key) {
            Entry<V, T, S, O, P> entry = get(key);
            if (entry != null) {
                Object value1 = entry.getValue1();
                if (value1 instanceof Integer) {
                    return (Integer) value1;
                }
            }
            return null;
        }

        public Double getDouble(K key) {
            Entry<V, T, S, O, P> entry = get(key);
            if (entry != null) {
                Object value1 = entry.getValue1();
                if (value1 instanceof Double) {
                    return (Double) value1;
                }
            }
            return null;
        }

        public Boolean getBoolean(K key) {
            Entry<V, T, S, O, P> entry = get(key);
            if (entry != null) {
                Object value1 = entry.getValue1();
                if (value1 instanceof Boolean) {
                    return (Boolean) value1;
                }
            }
            return null;
        }

    public String getString(K key) {
        Entry<V, T, S, O, P> entry = get(key);
        if (entry != null) {
            String value1 = String.valueOf(entry.getValue1());
            String value2 = String.valueOf(entry.getValue2());
            String value3 = String.valueOf(entry.getValue3());
            String value4 = String.valueOf(entry.getValue4());
            String value5 = String.valueOf(entry.getValue5());
            return value1 + ", " + value2 + ", " + value3 + ", " + value4 + ", " + value5;
        }
        return null;
    }
}