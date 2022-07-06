package com.example

import kraskal.Edge
import kraskal.Kruskal
import tornadofx.launch
import java.util.*

fun main() {
    /*val s = Scanner(System.`in`)
    val n = s.nextInt()

    //Составление списка ребёр по входным данным
    val edges = mutableListOf<Edge>()
    for (i in 0..n-1) {
        edges.add(Edge(s.next(), s.next(), s.nextInt()))
    }

    //Передаем список ребер на вход алгоритму
    val alg = Kruskal(edges)
    //Тут хранится результат каждого шага:
    //  1) Лист из одного ребра, если оно не образует цикл и его можно спокойно добавить
    //  2) Лист из несколькиз ребер, которые представляют собой поддерево, в котором образовался цикл
    //  3) null если алгоритм завершён и дальнейшие шаги невозможны
    //Тут хранится результат каждого шага:
    //  1) Лист из одного ребра, если оно не образует цикл и его можно спокойно добавить
    //  2) Лист из несколькиз ребер, которые представляют собой поддерево, в котором образовался цикл
    //  3) null если алгоритм завершён и дальнейшие шаги невозможны
    var x: List<Edge?>? = ArrayList()
    while (x != null) {
        x = alg.stepForward()
        println(x)
    }*/
    launch<MyApp>()
}