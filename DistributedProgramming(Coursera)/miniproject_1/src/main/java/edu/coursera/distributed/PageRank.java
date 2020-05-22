package edu.coursera.distributed;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaPairRDD;
import scala.Tuple2;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * A wrapper class for the implementation of a single iteration of the iterative
 * PageRank algorithm.
 */
public final class PageRank {
    private PageRank() {
    }

    public static JavaPairRDD<Integer, Double> sparkPageRank(
            final JavaPairRDD<Integer, Website> sites,
            final JavaPairRDD<Integer, Double> ranks) {
        // new_rank(B) = 0.15 + 0.85 * sum(rank(A) / out_count(A)) for all A linking to B
        JavaPairRDD<Integer, Double> newRanks = sites
                .join(ranks)
                .flatMapToPair(KaVa -> {
                    Website website = KaVa._2()._1(); Double curARank = KaVa._2()._2();

                    Iterator<Integer> outIter = website.edgeIterator();
                    List<Tuple2<Integer, Double>> contribToB = new LinkedList<>();
                    // Calculate contributions
                    while(outIter.hasNext()){
                        final int dstNode = outIter.next();
                        contribToB.add(new Tuple2(dstNode, curARank / (double)website.getNEdges()));
                    }
            return contribToB;
        });
        return newRanks
                .reduceByKey((Double r1, Double r2) -> r1 + r2) // for each (k,r1) and (k,r2) -> (k, r1+r2)
                .mapValues(r -> 0.15 + 0.85 * r); //0.15 + 0.85 * sum(rank(A) / out_count(A))
    }
}
