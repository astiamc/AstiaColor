package net.strokkur.color.util;

public class Pair<T1, T2> {

    private T1 left;
    private T2 right;

    public static <T1, T2> Pair<T1, T2> get(T1 left, T2 right) {
        return new Pair<>(left, right);
    }

    public Pair(T1 left, T2 right) {
        this.left = left;
        this.right = right;
    }

    public T1 getKey() {
        return left;
    }

    public T2 getValue() {
        return right;
    }

}
