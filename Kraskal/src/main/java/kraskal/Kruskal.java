package kraskal;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class Kruskal {
    private final List<Edge> edges;
    private final List<Edge> mst;
    private final DisjointSets<String> vertices;

    public Kruskal(Collection<Edge> edges) {
        this.edges = new LinkedList<>(edges);
        Collections.sort(this.edges);

        this.mst = new LinkedList<>();

        Set<String> v = new HashSet<>();
        for (Edge e: edges) {
            v.add(e.getEnd());
            v.add(e.getStart());
        }
        this.vertices = new DisjointSets<>(v);
    }

    public List<Edge> stepForward() {
        if (mst.size() == vertices.size() - 1) return null;
        Edge edge = edges.remove(0);
        String start = edge.getStart(), end = edge.getEnd();
        if (vertices.find(start).equals(vertices.find(end)))
            return getCycledTree(edge);
        mst.add(edge);
        vertices.unite(start, end);
        List<Edge> res = new ArrayList<>();
        res.add(edge);
        return res;
    }

    private List<Edge> getCycledTree(@NotNull Edge bridge) {
        String parent = vertices.find(bridge.getStart());
        List<String> v = vertices.getSet(parent);
        return mst.stream().filter(k -> vertices.find(k.getStart()).equals(parent)).collect(Collectors.toList());
    }

    public boolean isConnected() {
        DisjointSets<String> ds = new DisjointSets<>(vertices.getParents().keySet());
        for (Edge e: edges) ds.unite(e.getStart(), e.getEnd());
        String parent = ds.find(edges.get(0).getStart());
        for (String p: ds.getParents().keySet()) if (!ds.find(p).equals(parent)) return false;
        return true;
    }
}
