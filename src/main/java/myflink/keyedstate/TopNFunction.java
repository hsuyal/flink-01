package myflink.keyedstate;

import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.functions.windowing.ProcessAllWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by xumingyang on 2019/7/12.
 */
public class TopNFunction extends
        ProcessAllWindowFunction<Tuple3<String, Long, Long>, Tuple3<String, Long,Long>, TimeWindow> {
    private int topSize = 10;
    public TopNFunction(int topSize) {
        // TODO Auto-generated constructor stub
        this.topSize = topSize;
    }


    @Override
    public void process(Context context, Iterable<Tuple3<String, Long, Long>> elements, Collector<Tuple3<String, Long, Long>> out) throws Exception {
        TreeMap<Long, Tuple3<String, Long, Long>> treemap = new TreeMap<Long, Tuple3<String, Long, Long>>(new Comparator<Long>() {
            @Override
            public int compare(Long o1, Long o2) {
                return (o1 < o2) ? -1 : 1;
            }
        });


        for (Tuple3<String, Long, Long> element : elements) {
            treemap.put(element.f1, element);
            if (treemap.size() > topSize) { //只保留前面TopN个元素
                treemap.pollLastEntry();
            }
        }

        for (Map.Entry<Long, Tuple3<String, Long, Long>> entry : treemap
                .entrySet()) {
            System.out.println("#########TopNFunction topN  #########"  +  entry.getValue());
            Tuple3<String, Long, Long> tuple3 = entry.getValue();
            String key = tuple3.f0 + context.window().getStart() + "_" + context.window().getEnd();
            out.collect(new Tuple3<>(key, tuple3.f1, tuple3.f2));
        }


    }
}