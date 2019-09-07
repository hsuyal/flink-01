package myflink.app.state;

import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.api.common.state.ListState;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.runtime.state.FunctionInitializationContext;
import org.apache.flink.runtime.state.FunctionSnapshotContext;
import org.apache.flink.streaming.api.checkpoint.CheckpointedFunction;
import org.apache.flink.util.Collector;
import scala.Tuple2;

/**
 * Created by xumingyang on 2019/9/7.
 */
public class CountWithOperatorState extends RichFlatMapFunction<Long, Tuple2<Long, String>> implements CheckpointedFunction{
    private ListState<Long> listState ;

    @Override
    public void flatMap(Long value, Collector<Tuple2<Long, String>> out) throws Exception {

    }

    @Override
    public void snapshotState(FunctionSnapshotContext context) throws Exception {

    }

    @Override
    public void initializeState(FunctionInitializationContext context) throws Exception {

    }

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
    }
}
