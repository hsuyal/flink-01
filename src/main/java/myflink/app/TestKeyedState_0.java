package myflink.app;

import myflink.keyedstate.CountWithKeyedState;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * Created by xumingyang on 2019/7/11.
 */
public class TestKeyedState_0 {
    public static void main(String[] args) throws Exception{
        StreamExecutionEnvironment env=StreamExecutionEnvironment.getExecutionEnvironment();

        DataStream<Tuple2<Long,Long>> inputStream=env.fromElements(
                Tuple2.of(1L,4L),
                Tuple2.of(2L,3L),
                Tuple2.of(3L,1L),
                Tuple2.of(1L,2L),
                Tuple2.of(3L,2L),
                Tuple2.of(1L,2L),
                Tuple2.of(2L,2L),
                Tuple2.of(2L,9L)

                //1+1+1, 4+2+2
        );

        inputStream
                .keyBy(0)
                .flatMap(new CountWithKeyedState())
                .setParallelism(10)
                .print();

        env.execute();
    }
}
