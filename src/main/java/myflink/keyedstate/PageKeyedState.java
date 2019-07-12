package myflink.keyedstate;

import myflink.app.TestKeyedState;
import myflink.model.OptLog;
import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.runtime.state.FunctionInitializationContext;
import org.apache.flink.runtime.state.FunctionSnapshotContext;
import org.apache.flink.streaming.api.checkpoint.CheckpointedFunction;
import org.apache.flink.util.Collector;

import java.util.*;

/**
 * Created by xumingyang on 2019/7/11.
 * <p>
 * 计算每个页面UV，PV
 * 状态需要保存用户uid，次数，使用托管状态
 */
public class PageKeyedState extends RichFlatMapFunction<OptLog, List<Tuple3<String,String, Long>>>
implements CheckpointedFunction {
    //pageName,  username, vcount
    private transient ListState<Tuple3<String,String, Long>> uvAndPv;

    private transient ValueState<Tuple2<String, Long>> sum;



//    @Override
//    public void flatMap(OptLog value, Collector<Tuple2<String, Long>> out) throws Exception {
//        Tuple2<String, Long> currentSum = sum.value();
//
//        System.out.println("current sum is " + currentSum.toString());
//
//        // update the count
//        currentSum.f0 = value.getPageName();
//
//        // add the second field of the input value
//        currentSum.f1 = currentSum.f1 +1;
//
//        // update the state
//        sum.update(currentSum);
//
//        System.out.println("sum is " + currentSum.toString());
//
//
//        // if the count reaches 2, emit the average and clear the state
//        out.collect(new Tuple2<>(value.getPageName(), currentSum.f1));
////        sum.clear();
//    }

//    @Override
//    public void flatMap(OptLog value, Collector<List<Tuple2<String, Long>>> out) throws Exception {
//
//    }

    private Map<String, Long> hashmap = new HashMap<>();

    @Override
    public void flatMap(OptLog value, Collector<List<Tuple3<String, String, Long>>> out) throws Exception {

        System.out.println("execute initializeState snapshotState : " + value.toString());

        String curName = value.getPageName();
        String curUser = value.getUserName();

        // add the second field of the input value

        if(hashmap.containsKey(curUser)) {
            long nums = hashmap.get(curUser);
            hashmap.put(curUser, nums + 1);
        }else {
            hashmap.put(curUser, 1L);
        }

        List<Tuple3<String,String, Long>> list = new ArrayList<>();


        // 更新托管状态
        if(hashmap != null && hashmap.entrySet() != null) {
            Iterator<Map.Entry<String, Long>>it = hashmap.entrySet().iterator();
            if(it != null) {
                while (it.hasNext()) {
                    Map.Entry<String, Long> entry = it.next();
                    list.add(new Tuple3<>(curName, entry.getKey(), entry.getValue()));
                }

            }
        }

//        uvAndPv.update(list);
        out.collect(list);
        uvAndPv.clear();


//
//        List<Tuple2<String, Long>> list = new ArrayList<>();
//        HashMap<String, Long> hashMap = new HashMap<>();
//        // access the state value
//        if(res != null && res.get() != null) {
//
//            Iterator<Tuple2<String, Long>> it = res.get().iterator();
//
//            if(it != null) {
//                while (it.hasNext()) {
//                    Tuple2<String, Long> tuple = it.next();
//                    hashMap.put(tuple.f0, tuple.f1);
//                    list.add(tuple);
//
//                }
//            }
//        }
//
//        hashMap.put(new Tuple2<>())




//        if(hashMap.containsKey(value.getUserName())) {
//            Long val = hashMap.getOrDefault(value.getUserName(), 0L);
//            hashMap.put(value.getUserName(), val + 1);
//
//            //从原始状态中修改为托管状态，抛到下游
//            out.collect(getListState(hashMap));
//            hashMap.clear();
//
//        }else {
//            hashMap.put(value.getUserName(), 1L);
//        }
    }

    @Override
    public void open(Configuration parameters) throws Exception {



        //context.isRestored()代表重新启动
//        if(context.isRestored()){
//            for(Long element:checkPointCountList.get()){
//                listBufferElements.add(element);
//            }
//        }
        /**
         * 注意这里仅仅用了状态，但是没有利用状态来容错
         */
//        super.open(parameters);
//
//        ListStateDescriptor<Tuple2<String, Long>> descriptor =
//                new ListStateDescriptor<>(
//                        "avgState",
//                        TypeInformation.of(new TypeHint<Tuple2<String, Long>>() {
//                        })
//                );
//
//        System.out.println("execute initializeState ");
//        res = getRuntimeContext().getListState(descriptor);


        hashmap = new HashMap<>();
        ValueStateDescriptor<Tuple2<String, Long>> descriptor =
                new ValueStateDescriptor<>(
                        "average", // the state name
                        TypeInformation.of(new TypeHint<Tuple2<String, Long>>() {}), // type information
                        Tuple2.of("默认", 0L)); // default value of the state, if nothing was set
        sum = getRuntimeContext().getState(descriptor);
    }

    /**
     * 更新托管状态
     *
     * @param context
     * @throws Exception
     */
    @Override
    public void snapshotState(FunctionSnapshotContext context) throws Exception {
        uvAndPv.clear();

        System.out.println("0000execute initializeState snapshotState : " + hashmap.entrySet().iterator().toString());


        Iterator<Map.Entry<String, Long>> it =  hashmap.entrySet().iterator();


        while (it.hasNext()) {
            Map.Entry<String, Long> entry = it.next();
            System.out.println("execute initializeState snapshotState : " +entry.getKey() +entry.getValue());

            uvAndPv.add(new Tuple3("",entry.getKey(), entry.getValue()));
        }

        System.out.println("1111execute initializeState snapshotState : " + uvAndPv.get().toString());

    }

    private List<Tuple2<String, Long>> getListState(HashMap<String, Long> map) {
        List<Tuple2<String, Long>> list = new ArrayList<>();
        Iterator<Map.Entry<String, Long>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Long> entry = it.next();
            list.add(new Tuple2(entry.getKey(), entry.getValue()));
        }

        return list;
    }


    /**
     * 更新原始状态
     *
     * @param context
     * @throws Exception
     */
    @Override
    public void initializeState(FunctionInitializationContext context) throws Exception {


        ListStateDescriptor<Tuple3<String, String, Long>> listStateDescriptor=
                new ListStateDescriptor<>("checkPointCountList",TypeInformation.of(new TypeHint<Tuple3<String,
                        String, Long>>() {}));

        uvAndPv = context.getOperatorStateStore().getListState(listStateDescriptor);
//        //context.isRestored()代表重新启动
        if(context.isRestored()){
            System.out.println("execute initializeState isRestored ");
            for(Tuple3<String,String, Long> ele : uvAndPv.get()){
                hashmap.put(ele.f1, ele.f2);
            }
        }
    }



}
