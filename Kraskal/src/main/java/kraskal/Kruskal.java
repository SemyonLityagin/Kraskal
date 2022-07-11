package kraskal;

import org.jetbrains.annotations.NotNull;

import javax.sound.sampled.AudioFormat;
import java.util.*;
import java.util.stream.Collectors;

public class Kruskal {
    private final List<Edge> edges;
    private List<Edge> mst;
    private final DisjointSets<String> vertices;
    private List<List<Edge>> steps;
    private int i = 0;
    private final StringBuilder log = new StringBuilder();

    public Kruskal(Collection<Edge> edges) {
        this.edges = new LinkedList<>(edges);
        Collections.sort(this.edges);

        Set<String> v = new HashSet<>();
        for (Edge e : edges) {
            v.add(e.getEnd());
            v.add(e.getStart());
        }
        this.vertices = new DisjointSets<>(v);
    }

    public boolean isConnected() {
        DisjointSets<String> ds = new DisjointSets<>(vertices.getParents().keySet());
        for (Edge e : edges) ds.unite(e.getStart(), e.getEnd());
        String parent = ds.find(edges.get(0).getStart());
        for (String p : ds.getParents().keySet()) if (!ds.find(p).equals(parent)) return false;
        return true;
    }

    public void run() {
        this.mst = new LinkedList<>();
        steps = new ArrayList<>();
        while (!(mst.size() == vertices.size() - 1)) {
            Edge edge = edges.remove(0);
            String start = edge.getStart(), end = edge.getEnd();
            if (vertices.find(start).equals(vertices.find(end))) {
                steps.add(getCycle(edge));
                continue;
            }
            mst.add(edge);
            vertices.unite(start, end);
            List<Edge> res = new ArrayList<>();
            res.add(edge);
            steps.add(res);
        }
    }

    public List<Edge> stepForward() {
        if (i == 0) log.append("Hачало работы алгоритма;\n");
        if (i < steps.size()) {
            List<Edge> res = steps.get(i++);
            if (res.size() == 1) {
                Edge e = res.get(0);
                log.append(String.format("Выбрано ребро %s-%s c весом %o\n", e.getStart(), e.getEnd(), e.getWeight()));
            } else {
                String cycle = cycleToString(res);
                Edge bridge = res.get(res.size() - 1);
                log.append(String.format("Ребро %s-%s не выбрано, т.к. образует цикл %s\n",
                        bridge.getStart(), bridge.getEnd(), cycle));
            }
            return res;
        }
        log.append("Конец работы алгоритма;\n");
        return null;
    }

    public List<Edge> stepBack() {
        if (i > 0) {
            List<Edge> res = steps.get(--i);
            if (res.size() == 1) {
                Edge e = res.get(0);
                log.append(String.format("Ребро %s-%s с весом %o удалено из остова\n", e.getStart(), e.getEnd(), e.getWeight()));
            } else {
                String cycle = cycleToString(res);
                Edge bridge = res.get(res.size() - 1);
                log.append(String.format("Ребро, %s-%s образующее цикл %s удалено из рассмотренных\n",
                        bridge.getStart(), bridge.getEnd(), cycle));
            }
            return res;
        }
        log.append("Шаг назад невозможен;\n");
        return null;
    }


    private String cycleToString(List<Edge> res) {
        StringBuilder cycle = new StringBuilder();
        cycle.append(res.get(0).getStart());
        cycle.append("-");
        for (int i = 0; i < res.size() - 1; i++) {
            cycle.append(res.get(i).getEnd());
            cycle.append("-");
        }
        cycle.append(res.get(0).getStart());
        return cycle.toString();
    }

    public List<List<Edge>> getSteps() {
        return steps;
    }

    public String getLog() {
        return log.toString();
    }

    private List<Edge> DFS(Map<String, Map<String, Integer>> graph, String start, String end, Set<String> visited, List<Edge> path) {
        if (start.equals(end)) {
            return path;
        }
        visited.add(start);
        for (Map.Entry<String, Integer> next : graph.get(start).entrySet()) {
            String v = next.getKey();
            if (!visited.contains(v)) {
                List<Edge> newPath = new ArrayList<>(path);
                newPath.add(new Edge(start, v, next.getValue()));
                List<Edge> res = DFS(graph, v, end, visited, newPath);
                if (res != null) return res;
            }
        }
        return null;
    }

    private List<Edge> getCycle(Edge bridge) {
        Map<String, Map<String, Integer>> graph = fromEdgesToGraph(getCycledTree(bridge));
        List<Edge> res = DFS(graph, bridge.getStart(), bridge.getEnd(), new HashSet<>(), new ArrayList<>());
        res.add(bridge);
        return res;
    }

    private List<Edge> getCycledTree(Edge bridge) {
        String parent = vertices.find(bridge.getStart());
        return mst.stream().filter(k -> vertices.find(k.getStart()).equals(parent)).collect(Collectors.toList());
    }

    private Map<String, Map<String, Integer>> fromEdgesToGraph(List<Edge> edges) {
        Map<String, Map<String, Integer>> graph = new HashMap<>();
        for (Edge e : edges) {
            Map<String, Integer> start = graph.computeIfAbsent(e.getStart(), k -> new HashMap<>());
            Map<String, Integer> end = graph.computeIfAbsent(e.getEnd(), k -> new HashMap<>());
            start.put(e.getEnd(), e.getWeight());
            end.put(e.getStart(), e.getWeight());
        }
        return graph;
    }
}
