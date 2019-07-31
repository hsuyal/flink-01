package myflink.keyedstate;

import com.alibaba.fastjson.JSONObject;
import org.apache.flink.api.common.state.MapState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.typeinfo.BasicTypeInfo;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.text.DateFormat;
import java.util.*;

/**
 * Created by Eva on 2019-07-30.
 */
public class kafkaTestProcess extends ProcessWindowFunction<JSONObject, Tuple3<String, Long, Long>, String, TimeWindow> {
    private transient MapState<String,  Long> mapState;

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        MapStateDescriptor<String, Long> uvMapDes =
                new MapStateDescriptor<>(
                        "uvMap",
                        BasicTypeInfo.STRING_TYPE_INFO,
                        TypeInformation.of(new TypeHint<Long>() {}));

        mapState = getRuntimeContext().getMapState(uvMapDes);
    }

    @Override
    public void process(String s, Context context, Iterable<JSONObject> elements, Collector<Tuple3<String, Long, Long>> out) throws Exception {

        System.out.println("***********every input val*************" + context.window().getStart()+ "_" + elements);

//        if(elements != null && elements.iterator() != null) {
//            Iterator<JSONObject> it = elements.iterator();
//            while (it.hasNext()) {
//                JSONObject curLog = it.next();
//                System.out.println("***********every input val*************" + context.window().getStart()+ "_" + curLog.toString());
//                String mapKey = curLog.getJSONObject("report").getString("name");
//                if(mapState.contains(mapKey)) {
//                    Long val = mapState.get(mapKey);
//                    mapState.put(mapKey, val + 1);
//                }else {
//                    mapState.put(mapKey, 1L);
//                }
//            }
//        }
//
//
//        Iterator<java.util.Map.Entry<String, Long>> it = mapState.entries().iterator();
//
//        Long uv = 0L;
//        Long pv = 0L;
//        while (it.hasNext()) {
//            java.util.Map.Entry<String, Long> entry = it.next();
//            uv++;
//            pv += entry.getValue();
//        }
//
//        Date day  = new Date(context.window().getStart());
//
//        DateFormat df1 = DateFormat.getDateInstance();
//        String rowKey =  df1.format(day);
        out.collect(new Tuple3<String, Long, Long>("haha", 23L,24L));
    }
}