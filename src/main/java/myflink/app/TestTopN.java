package myflink.app;

import myflink.keyedstate.PvUvProcessFunction;
import myflink.keyedstate.SpecialEleProcess;
import myflink.keyedstate.TopNFunction;
import myflink.model.OptLog;
import myflink.water.WaterMaskExt;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.runtime.state.StateBackend;
import org.apache.flink.runtime.state.filesystem.FsStateBackend;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.collector.selector.OutputSelector;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SplitStream;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by xumingyang on 2019/7/12.
 */
public class TestTopN {
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



        SplitStream<OptLog> splitStream = env.addSource(new SimpleSourceFunction())
                .assignTimestampsAndWatermarks(new WaterMaskExt()).setParallelism(1)
                .split(new OutputSelector<OptLog>() {
                    @Override
                    public Iterable<String> select(OptLog value) {
                        List<String> output = new ArrayList<String>();

                        if ("首页".equalsIgnoreCase(value.getPageName())) {
                            output.add("special");
                        }
                        else {
                            output.add("normal");
                        }
                        return output;
                    }
                });


        DataStream<OptLog> normal = splitStream.select("normal");

        DataStream<OptLog> special = splitStream.select("special");

//        DataStream<OptLog> all = splitStream.select("even","odd");

//        inputStream.print();

        //每个key一个状态，求pageName的uv


        //每个key一个状态，求pageName的uv
//        DataStream<Tuple3<String, Long,Long>>  specailRes = special
//                .keyBy(new KeySelector<OptLog, String>() {
//                    @Override
//                    public String getKey(OptLog optLog) throws Exception {
//                        return  (int) (Math.random() * (100 -1 + 1) + 1) + "_" + optLog.getPageName();
//                    }
//                })
//                //windowassigner的分类，有了windowassigner才有各种个样的window,topN是滑动窗口
//                .timeWindow((Time.minutes(30)),Time.seconds(5))
//                .process(new PvUvProcessFunction());
//
//        specailRes.print();

        DataStream<Tuple3<String, Long,Long>>  normalRes = normal
                .keyBy(new KeySelector<OptLog, String>() {
                    @Override
                    public String getKey(OptLog optLog) throws Exception {
                        return  optLog.getPageName();
                    }
                })
                //windowassigner的分类，有了windowassigner才有各种个样的window,topN是滑动窗口
                .timeWindow((Time.minutes(5)),Time.minutes(5))
                .process(new PvUvProcessFunction());

        normalRes.print();


//        DataStream<Tuple3<String, Long,Long>>  specialTmp = specailRes
//                .windowAll(TumblingProcessingTimeWindows.of(Time.seconds(20)))
//                .process(new SpecialEleProcess());
//
//        specailRes.print();

//        System.out.println("************start topN ******************");
//
//        /***************topN代码*********************/
        DataStream<Tuple3<String, Long,Long>>
                topNRes = normalRes
                .windowAll(TumblingProcessingTimeWindows.of(Time.minutes(5)))
                //所有key元素进入一个20s长的窗口（选20秒是因为上游窗口每20s计算一轮数据，topN窗口一次计算只统计一个窗口时间内的变化）
                .process(new TopNFunction(20));//计算该窗口TopN



        topNRes.map(new MapFunction<Tuple3<String,Long,Long>, Object>() {
            @Override
            public Object map(Tuple3<String, Long, Long> value) {
                System.out.println("topN result datastream is :"  + value);

                return value;
            }
        });
//
        topNRes.print();
////        res.print();
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
            "课程详情页面"
    };

    private static class SimpleSourceFunction implements SourceFunction<OptLog> {
        private long num = 0L;
        private volatile boolean isRunning = true;
        @Override
        public void run(SourceContext<OptLog> sourceContext) throws Exception {
            while (isRunning) {
                int randomNum=(int)(1+Math.random()*(5-1+1));
                int randomPage = (int) (Math.random() * (2 -1 + 1) + 1);
                int randomSeq = (int) (Math.random() * (100000 -1 + 1) + 1);
                sourceContext.collect(OptLog.of(nameArray[randomNum-1]+randomSeq,randomNum, new Date().getTime(),
                        "首页",
                        pageNameArray[randomPage-1],
                        pageNameArray[randomPage-1],
                        pageNameArray[randomPage-1],
                        pageNameArray[randomPage-1],
                        pageNameArray[randomPage-1],
                        pageNameArray[randomPage-1],
                        pageNameArray[randomPage-1],
                        pageNameArray[randomPage-1],
                        pageNameArray[randomPage-1],
                        pageNameArray[randomPage-1],
                        pageNameArray[randomPage-1],
                        pageNameArray[randomPage-1],
                        pageNameArray[randomPage-1],
                        pageNameArray[randomPage-1],
                        pageNameArray[randomPage-1]
                ));
                num++;
                Thread.sleep(1);
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
                Thread.sleep(0);
            }
        }
        @Override
        public void cancel() {
            isRunning = false;
        }

    }
}
