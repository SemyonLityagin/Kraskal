package kraskal;

import org.jetbrains.annotations.NotNull;

public class Edge implements Comparable<Edge>{
    private final String start;
    private final String end;
    private final int weight;

    public Edge(String start, String end, int weight) {
        this.start = start;
        this.end = end;
        this.weight = weight;
    }

    @Override
    public int compareTo(@NotNull Edge o) {
        if (this.weight == o.weight) return 0;
        return (this.weight > o.weight) ? 1: -1;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public int getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return start + ' ' + end + ' ' + weight;
    }
}
