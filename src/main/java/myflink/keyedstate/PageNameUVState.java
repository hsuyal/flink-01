package myflink.keyedstate;

import myflink.app.TestKeyedState;
import myflink.model.OptLog;
import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.util.Collector;

import java.util.*;

/**
 * Created by xumingyang on 2019/7/11.
 */
public class PageNameUVState extends RichFlatMapFunction<OptLog, Tuple3<String, Long, Long>> {
    //pageName,  username, vcount
    private transient ListState<Tuple2<String, Long>> uvList;

    @Override
    public void flatMap(OptLog value, Collector<Tuple3<String, Long, Long>> out) throws Exception {
        Iterable<Tuple2<String, Long>> curUvList = uvList.get();
        System.out.println("current sum is " + curUvList.toString());


        Map<String, Long> map  = new HashMap<>() ;
        Iterator<Tuple2<String, Long>> it = curUvList.iterator();

        while (it.hasNext()) {
            Tuple2<String, Long> v  =  it.next();
            map.put(v.f0, v.f1);
        }

        if(map.containsKey(value.getUserName())) {
           long count =  map.get(value.getUserName()) + 1;
           map.put(value.getUserName(), count);
        }else {
            map.put(value.getUserName(), 1L);
        }



        List<Tuple2<String, Long>> tmp = getListState(map);

        Long pvCounts = 0L;
        for(int i = 0; i < tmp.size(); i++) {
                pvCounts += tmp.get(i).f1;
        }

        uvList.update(tmp);
        System.out.println("update current sum is " + tmp.toString());


        // if the count reaches 2, emit the average and clear the state
        out.collect(new Tuple3<>(value.getPageName(), new Integer(tmp.size()).longValue(), pvCounts));
//        sum.clear();

    }


    private List<Tuple2<String, Long>> getListState(Map<String, Long> map) {
        List<Tuple2<String, Long>> list = new ArrayList<>();
        Iterator<Map.Entry<String, Long>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Long> entry = it.next();
            list.add(new Tuple2(entry.getKey(), entry.getValue()));
        }

        return list;
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        /**
         * 注意这里仅仅用了状态，但是没有利用状态来容错
         */
//        hashmap = new HashMap<>();
        ListStateDescriptor<Tuple2<String, Long>> listStateDescriptor=
                new ListStateDescriptor<>("uv",
                        TypeInformation.of(new TypeHint<Tuple2<String, Long>>() {}));
        uvList = getRuntimeContext().getListState(listStateDescriptor);
    }

}
