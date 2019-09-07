package myflink.app;

import myflink.keyedstate.PvUvProcessFunction;
import myflink.keyedstate.PvUvProcessFunctionof1Day;
import myflink.model.OptLog;
import myflink.water.WaterMaskExt;
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
import org.apache.flink.streaming.api.windowing.triggers.ContinuousEventTimeTrigger;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;
/**
 * Created by xumingyang on 2019/9/7.
 */
public class TestUV {
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


        DataStream<OptLog> normal = env.addSource(new SimpleSourceFunction())
                .assignTimestampsAndWatermarks(new WaterMaskExt()).setParallelism(1);


        /**
         * .window(TumblingProcessingTimeWindows.of(Time.days(1), Time.hours(-8)))
         .trigger(ContinuousProcessingTimeTrigger.of(Time.seconds(10)))
         .evictor(TimeEvictor.of(Time.seconds(0), true))
         */



        DataStream<Tuple3<String, Long,Long>>  resStream01 = normal
                .keyBy(new KeySelector<OptLog, String>() {
                    @Override
                    public String getKey(OptLog optLog) throws Exception {
                        return  optLog.getPageName();
                    }
                })
                //windowassigner的分类，有了windowassigner才有各种个样的window,topN是滑动窗口
                .window(TumblingEventTimeWindows.of(Time.days(1)))
                .trigger(ContinuousEventTimeTrigger.of(Time.seconds(5)))
                .process(new PvUvProcessFunctionof1Day());


        //每个key一个状态，滚动窗口
        DataStream<Tuple3<String, Long,Long>>  resStream02 = normal
                .keyBy(new KeySelector<OptLog, String>() {
                    @Override
                    public String getKey(OptLog optLog) throws Exception {
                        return  optLog.getPageName();
                    }
                })
                //windowassigner的分类，有了windowassigner才有各种个样的window,topN是滑动窗口
                .timeWindow(Time.seconds(5))
                .process(new PvUvProcessFunction());

        resStream01.print();
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
                long time = -1L;
                if(num <20) {
                    Date date = new Date();//获取当前时间    
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    calendar.add(Calendar.DATE, -3);//当前时间减去一天 
                    time = calendar.getTime().getTime();
                }else if(num < 40 && num > 20) {
                    Date date = new Date();//获取当前时间    
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    calendar.add(Calendar.DATE, -2);//当前时间减去一天 
                    time = calendar.getTime().getTime();
                }else {
                    Date date = new Date();//获取当前时间    
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    calendar.add(Calendar.DATE, -1);//当前时间减去一天 
                    time = calendar.getTime().getTime();
                }
                sourceContext.collect(OptLog.of(nameArray[randomNum-1],randomNum, time,
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
                        pageNameArray[randomPage-1],
                        pageNameArray[randomPage-1]
                ));
                num++;
                Thread.sleep(500);
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
