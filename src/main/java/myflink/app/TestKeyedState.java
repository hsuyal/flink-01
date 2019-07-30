package myflink.app;

import myflink.keyedstate.PvUvProcessFunction;
import myflink.model.OptLog;
import myflink.water.TimeLagWaterMarkGen;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.runtime.state.StateBackend;
import org.apache.flink.runtime.state.filesystem.FsStateBackend;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by xumingyang on 2019/7/11.
 */
public class TestKeyedState {
    public static void main(String[] args) throws Exception{
        StreamExecutionEnvironment env=StreamExecutionEnvironment.getExecutionEnvironment();

        env.setParallelism(1);
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);



        env.enableCheckpointing(5000);
        CheckpointConfig checkpointConfig=env.getCheckpointConfig();
        checkpointConfig.setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
        checkpointConfig.setMinPauseBetweenCheckpoints(5000);
        checkpointConfig.setCheckpointTimeout(6000);
        checkpointConfig.setMaxConcurrentCheckpoints(1);
        checkpointConfig.setFailOnCheckpointingErrors(true);
        checkpointConfig.enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);
//        StateBackend backend=new FsStateBackend(


        StateBackend backend = new FsStateBackend(
                "file:///Users/yang/work/cisco/flink/state_bk");

        env.setStateBackend(backend);

        env.setRestartStrategy(RestartStrategies.fixedDelayRestart(
                3, // number of restart attempts
                Time.of(10, TimeUnit.SECONDS).toMilliseconds() // delay
        ));




//        DataStream<Tuple2<Long, Long>> inputStream = env.addSource(new SimpleSourceFunction0()).setParallelism(1);
//
//        System.out.println("**********start***********");
//        inputStream.print();
//        System.out.println("**********end***********");
//
//
//        inputStream.keyBy(0).flatMap(new CountWindowAverage())
//                .print();





//        env.fromElements(Tuple2.of(1L, 3L), Tuple2.of(1L, 5L), Tuple2.of(1L, 7L), Tuple2.of(1L, 4L), Tuple2.of(1L, 2L),
//                Tuple2.of(2L, 3L), Tuple2.of(2L, 5L), Tuple2.of(3L, 7L),Tuple2.of(3L, 6L), Tuple2.of(4L, 5L))
//                .keyBy(0)
//                .flatMap(new CountWindowAverage())
//                .print();


//        inputStream.keyBy(new KeySelector<Tuple2<Double, Double>, Double>() {
//                    @Override
//                    public Double getKey(Tuple2<Double, Double> value) throws Exception {
//                        return value.f0;
//                    }
//                })
//                .flatMap(new CountWindowAverage())
//                .print();


        DataStream<OptLog> inputStream = env.addSource(new SimpleSourceFunction()).uid("keystaete")
                .assignTimestampsAndWatermarks(new TimeLagWaterMarkGen()).setParallelism(1);


        inputStream.print();

        //每个key一个状态，求pageName的uv
        //实时的PV，UV是滚动窗口
        DataStream<Tuple3<String, Long,Long>>  res = inputStream
                .keyBy(new KeySelector<OptLog, String>() {
                    @Override
                    public String getKey(OptLog optLog) throws Exception {
                        return optLog.getPageName();
                    }
                })
                //windowassigner的分类，有了windowassigner才有各种个样的window
                .window(TumblingEventTimeWindows.of(Time.seconds(5)))
                .process(new PvUvProcessFunction()).uid("process");

//                .flatMap(new PageNameUVState())
//                .print();

        res.print();
        env.execute();
    }


    public static final String[] nameArray = new String[] {
            "张三",
            "李四",
            "王五",
            "赵六",
            "钱七",
            "stark"
    };

    public static final String[] pageNameArray = new String[] {
            "首页",
            "课程详情页面",
            "类目页面",
            "我的学习",
            "我的",
            "设置",
            "学习中心",
            "推荐",
            "主类目",
            "搜索",
            "搜索结果",
            "课时",
            "登陆",
            "注册",
            "订单",
            "支付页面",
            "支付成功",
            "H5页面"
    };

    private static class SimpleSourceFunction implements SourceFunction<OptLog> {
        private long num = 0L;
        private volatile boolean isRunning = true;
        @Override
        public void run(SourceContext<OptLog> sourceContext) throws Exception {
            while (isRunning) {

//               int random  = 1;
                int randomNum=(int)(1+Math.random()*(5-1+1));
                int randomPage = (int) (Math.random() * (18 -1 + 1) + 1);
//                sourceContext.collect(OptLog.of(nameArray[randomNum-1],randomNum, new Date().getTime(), pageNameArray[randomPage-1]));
                num++;
                Thread.sleep(1000);
            }
        }
        @Override
        public void cancel() {
            isRunning = false;
        }

    }


    private static class SimpleSourceFunction0 implements SourceFunction<Tuple2<Long, Long>> {
        private long num = 0L;
        private volatile boolean isRunning = true;
        @Override
        public void run(SourceContext<Tuple2<Long, Long>> sourceContext) throws Exception {
            while (isRunning) {
                //new Double(random).longValue();


                Long random = 1L;
                Long page  = 2L;
                Long randomNum= new Double((int)(5+Math.random()*(5-1+1))).longValue();
                Long randomPage = new Double((int)(Math.random() * (18 -1 + 1) + 5)).longValue();
                sourceContext.collect(new Tuple2<Long, Long>(random, page));
                Thread.sleep(100);
            }
        }
        @Override
        public void cancel() {
            isRunning = false;
        }

    }
}
