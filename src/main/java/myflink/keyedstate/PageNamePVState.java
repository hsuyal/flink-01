package myflink.keyedstate;

import myflink.app.TestKeyedState;
import myflink.model.OptLog;
import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.util.Collector;

/**
 * Created by xumingyang on 2019/7/11.
 */
public class PageNamePVState extends RichFlatMapFunction<OptLog, Tuple2<String, Long>>
        {
    //pageName,  username, vcount
    private transient ValueState<Tuple2<String, Long>> sum;

    @Override
    public void flatMap(OptLog value, Collector<Tuple2<String, Long>> out) throws Exception {
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

    @Override
    public void open(Configuration parameters) throws Exception {
        /**
         * 注意这里仅仅用了状态，但是没有利用状态来容错
         */
//        hashmap = new HashMap<>();
        ValueStateDescriptor<Tuple2<String, Long>> descriptor =
                new ValueStateDescriptor<>(
                        "average", // the state name
                        TypeInformation.of(new TypeHint<Tuple2<String, Long>>() {}), // type information
                        Tuple2.of("默认", 0L)); // default value of the state, if nothing was set
        sum = getRuntimeContext().getState(descriptor);
    }

}
