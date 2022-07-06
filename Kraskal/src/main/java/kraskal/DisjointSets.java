package kraskal;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class DisjointSets<T> {
    private final Map<T, T> parents = new HashMap<>();

    public DisjointSets(@NotNull Collection<T> arr) {
        for (T x: arr) parents.put(x, x);
    }

    public T find(T x) {
        T parent = parents.get(x);
        if (parent.equals(x)) return x;
        return find(parent);
    }

    public Map<T, T> getParents() {
        return parents;
    }

    public void unite(T a, T b) {
        parents.put(find(a), find(b));
    }

    public List<T> getSet(T parent) {
        return parents.keySet().stream().filter(k -> find(k).equals(parent)).collect(Collectors.toList());
    }

    public int size() {
        return parents.size();
    }
}
