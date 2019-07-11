package myflink.keyedstate;

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
public class CountWindowAverage extends RichFlatMapFunction<Tuple2<Long, Long>, Tuple2<Long, Long>>  {

        /**
         * The ValueState handle. The first field is the count, the second field a running sum.
         */
//        private ValueState<Tuple2<Double, Double>> sum;
//
//        @Override
//        public void flatMap(Tuple2<Double, Double> input, Collector<Tuple2<Double, Double>> out) throws Exception {
//
//            // access the state value
//            Tuple2<Double, Double> currentSum = sum.value();
//
//            System.out.println("first output is " + currentSum.toString());
//
//            // update the count
//            currentSum.f0 += 1;
//
//
//            // add the second field of the input value
//            currentSum.f1 += input.f1;
//
//            // update the state
//            sum.update(currentSum);
//            System.out.println("second output is " + currentSum.toString() + " here ");
//
//            // if the count reaches 2, emit the average and clear the state
//            if (currentSum.f0 >= 2) {
//                out.collect(new Tuple2<>(input.f0, currentSum.f1));
//                sum.clear();
//            }
//        }
//
//        @Override
//        public void open(Configuration config) {
//            ValueStateDescriptor<Tuple2<Double, Double>> descriptor =
//                    new ValueStateDescriptor<>(
//                            "average", // the state name
//                            TypeInformation.of(new TypeHint<Tuple2<Double, Double>>() {}), // type information
//                            Tuple2.of(2D, 2D)); // default value of the state, if nothing was set
//            sum = getRuntimeContext().getState(descriptor);
//        }


    /**
     * The ValueState handle. The first field is the count, the second field a running sum.
     */
    private transient ValueState<Tuple2<Long, Long>> sum;

    @Override
    public void flatMap(Tuple2<Long, Long> input, Collector<Tuple2<Long, Long>> out) throws Exception {

        // access the state value
        Tuple2<Long, Long> currentSum = sum.value();
        System.out.println("currentSum output is " + currentSum.toString() + "current input is: " + input.toString());

        // update the count
        currentSum.f0 += 1;

        // add the second field of the input value
        currentSum.f1 += input.f1;

        // update the state
        sum.update(currentSum);
        System.out.println("sum output is " + sum.value().toString());


        // if the count reaches 2, emit the average and clear the state
        if (currentSum.f0 >= 2) {
            out.collect(new Tuple2<>(input.f0, currentSum.f1 / currentSum.f0));
//            sum.clear();
        }
    }

    @Override
    public void open(Configuration config) {
        ValueStateDescriptor<Tuple2<Long, Long>> descriptor =
                new ValueStateDescriptor<>(
                        "average", // the state name
                        TypeInformation.of(new TypeHint<Tuple2<Long, Long>>() {}), // type information
                        Tuple2.of(0L, 0L)); // default value of the state, if nothing was set
        sum = getRuntimeContext().getState(descriptor);
    }

    }
