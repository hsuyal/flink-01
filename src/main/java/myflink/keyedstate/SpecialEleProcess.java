package myflink.keyedstate;

import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.functions.windowing.ProcessAllWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xumingyang on 2019/7/15.
 */
public class SpecialEleProcess extends
        ProcessAllWindowFunction<Tuple3<String, Long, Long>, Tuple3<String, Long,Long>, TimeWindow> {


    @Override
    public void process(Context context, Iterable<Tuple3<String, Long, Long>> elements, Collector<Tuple3<String, Long, Long>> out) throws Exception {

         Map<String,  Long> mapState = new HashMap<>();

        String key = "";
        long resKey = 0;
        long resVal = 0;
        for (Tuple3<String, Long, Long> element : elements) {
            System.out.println("#########TopNFunction  Split topN input is #########"  +  element);
            key = element.f0;
            if(mapState.containsKey(element.f1 + "")) {
                Long val = mapState.get(element.f1 + "");
                mapState.put(element.f1 + "", val + element.f2);
            }else {
                mapState.put(element.f1 + "",  element.f2);
            }
            resVal +=  element.f2;
            resKey +=1;
        }



        String[] keyRes =  key.split("_");
        String tmpKey = keyRes[0] + "_" + keyRes[2];

        System.out.println("#########TopNFunction  Split topN  #########"  +  tmpKey + "_" + resKey + "_" + resVal);
        out.collect(new Tuple3<>(key, resKey, resVal));

    }
}