package myflink.keyedstate;

import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.runtime.state.FunctionInitializationContext;
import org.apache.flink.runtime.state.FunctionSnapshotContext;
import org.apache.flink.util.Collector;

import java.util.*;

/**
 * Created by xumingyang on 2019/7/11.
 * <p>
 * 计算每个页面UV，PV
 * 状态需要保存用户uid，次数，使用托管状态
 */
public class PageKeyedState extends RichFlatMapFunction<TestKeyedState.OptLog, Tuple2<String, Long>> {
//    private transient ListState<Tuple2<String, Long>> res;

private transient ValueState<Tuple2<String, Long>> sum;



    @Override
    public void flatMap(TestKeyedState.OptLog value, Collector<Tuple2<String, Long>> out) throws Exception {
        Tuple2<String, Long> currentSum = sum.value();

        System.out.println("current sum is " + currentSum.toString());

        // update the count
        currentSum.f0 = value.getPageName();

        // add the second field of the input value
        currentSum.f1 = currentSum.f1 +1;

        // update the state
        sum.update(currentSum);

        System.out.println("sum is " + currentSum.toString());


        // if the count reaches 2, emit the average and clear the state
        out.collect(new Tuple2<>(value.getPageName(), currentSum.f1));
//        sum.clear();
    }

    public void flatMap0(TestKeyedState.OptLog value, Collector<List<Tuple2<String, Long>>> out) throws Exception {

        Tuple2<String, Long> currentSum = sum.value();

        // update the count
        currentSum.f0 = value.getPageName();

        // add the second field of the input value
        currentSum.f1 += 1;

        // update the state
        sum.update(currentSum);

        // if the count reaches 2, emit the average and clear the state
        if (currentSum.f1 > 1) {
//            out.collect(new Tuple2<>(value.getPageName(), currentSum.f1));
            sum.clear();
        }




        System.out.println("execute initializeState snapshotState : " + value.toString());

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
    public void snapshotState(FunctionSnapshotContext context) throws Exception {

//        res.clear();

//        System.out.println("execute initializeState snapshotState : " + hashMap.entrySet().iterator().toString());


//        Iterator<Map.Entry<String, Long>> it =  hashMap.entrySet().iterator();


//        while (it.hasNext()) {
//            Map.Entry<String, Long> entry = it.next();
//            System.out.println("execute initializeState snapshotState : " +entry.getKey() +entry.getValue());
//
//            res.add(new Tuple2(entry.getKey(), entry.getValue()));
//        }

//        System.out.println("execute initializeState snapshotState : " + res.get().toString());

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
    public void initializeState(FunctionInitializationContext context) throws Exception {

//        ListStateDescriptor<Tuple2<String,Long>> descriptor=
//                new ListStateDescriptor<>(
//                        "avgState",
//                        TypeInformation.of(new TypeHint<Tuple2<String, Long>>() {})
//                );
//
//
//        System.out.println("execute initializeState ");
//
//        res = context.getOperatorStateStore().getListState(descriptor);
//
//
//
//        //context.isRestored()代表重新启动
//        if(context.isRestored()){
//
//            System.out.println("execute initializeState isRestored ");
//
//            for(Tuple2<String, Long> ele : res.get()){
//                hashMap.put(ele.f0, ele.f1);
//            }
//        }
    }



}
