package myflink.keyedstate;

import myflink.model.OptLog;
import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;
import org.apache.flink.api.common.state.MapState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.typeinfo.BasicTypeInfo;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by xumingyang on 2019/7/12.
 */
public class PvUvProcessFunction  extends ProcessWindowFunction<OptLog, Tuple3<String, Long, Long>, String, TimeWindow> {

    private transient ListState<Tuple2<String, Long>> uvState;

    private transient MapState<String,  Long> mapState;

    static private List<Tuple2<String, Long>> getListState(java.util.Map<String, Long> map, String day) {
        List<Tuple2<String, Long>> list = new ArrayList<>();
        Iterator<java.util.Map.Entry<String, Long>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            java.util.Map.Entry<String, Long> entry = it.next();
            String curDay = entry.getKey().split("_")[0];
            if(curDay.equalsIgnoreCase(day)) {
                list.add(new Tuple2(entry.getKey(), entry.getValue()));
            }
        }

        return list;
    }


    @Override
    public void process(String s, Context context, Iterable<OptLog> elements, Collector<Tuple3<String, Long, Long>> out)
            throws Exception {

        System.out.println("*****************PvUvProcessFunction window start*********key is: " + s +
                "    window size is :" + context.window().getStart() + "-------" + context.window().getEnd());
        java.util.Map<String, Long> map  = new java.util.HashMap<>() ;


        Date day  = new Date(context.window().getStart());
        DateFormat df1 = DateFormat.getDateInstance();
        String rowKey =  df1.format(day);


        if(uvState.get() != null && uvState.get().iterator() != null) {
            Iterator<Tuple2<String, Long>> it = uvState.get().iterator();

            while (it.hasNext()) {
                Tuple2<String, Long> obj = it.next();
                map.put(obj.f0, obj.f1);
            }
        }

        List<Tuple2<String, Long>> tmp_start = getListState(map, rowKey);
        System.out.println("********PvUvProcessFunction该窗口上一个保存的状态值是：********" + tmp_start.toString());


        if(elements != null && elements.iterator() != null) {
            Iterator<OptLog> it = elements.iterator();
            while (it.hasNext()) {
                OptLog curLog = it.next();
                System.out.println("***********PvUvProcessFunction every input val*************" + curLog.toString());

                String mapKey = rowKey + "_" + curLog.getUserName();
                if(map.containsKey(mapKey)) {
                    Long val = map.get(mapKey);
                    map.put(mapKey, val + 1);
                }else {
                    map.put(mapKey, 1L);
                }
            }
        }

        List<Tuple2<String, Long>> tmp = getListState(map, rowKey);
        Long pvCounts = 0L;
        if(tmp != null && tmp.size() != 0) {
            for(int i = 0; i < tmp.size(); i++) {
                pvCounts += tmp.get(i).f1;
            }
        }

        System.out.println("PvUvProcessFunction update current sum is " + tmp.toString());


        uvState.update(tmp);

        long uvCounts = new Integer(tmp.size()).longValue();
        out.collect(new Tuple3<>(rowKey+ "_" + s, uvCounts, pvCounts));




        /***************mapState*********************/

//        if(elements != null && elements.iterator() != null) {
//            Iterator<OptLog> it = elements.iterator();
//            while (it.hasNext()) {
//                OptLog curLog = it.next();
//                System.out.println("***********every input val*************" + curLog.toString());
//                String mapKey = curLog.getUserName();
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
//        System.out.println("update000000 current sum is " + tmp.toString());
//
//        out.collect(new Tuple3<>(rowKey+ "_map_" + s, uv, pv));

    }

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        ListStateDescriptor<Tuple2<String, Long>> listStateDescriptor=
                new ListStateDescriptor<>("uvProcess", TypeInformation.of(new TypeHint<Tuple2<
                        String, Long>>() {}));
        uvState = getRuntimeContext().getListState(listStateDescriptor);


        MapStateDescriptor<String, Long> uvMapDes =
                new MapStateDescriptor<>(
                        "uvMap",
                        BasicTypeInfo.STRING_TYPE_INFO,
                        TypeInformation.of(new TypeHint<Long>() {}));

        mapState = getRuntimeContext().getMapState(uvMapDes);
    }
}