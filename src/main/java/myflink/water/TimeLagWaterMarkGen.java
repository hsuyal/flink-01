package myflink.water;

import myflink.model.OptLog;
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks;
import org.apache.flink.streaming.api.watermark.Watermark;

import javax.annotation.Nullable;

/**
 * Created by xumingyang on 2019/7/12.
 *
 * 数据大面积延迟的时候，数据可能会丢或者没有意义
 */
public class TimeLagWaterMarkGen implements AssignerWithPeriodicWatermarks<OptLog> {

    private long maxLagTime =  3500;

    @Nullable
    @Override
    public Watermark getCurrentWatermark() {
        return new Watermark(System.currentTimeMillis() - maxLagTime);
    }

    @Override
    public long extractTimestamp(OptLog element, long previousElementTimestamp) {
        return element.getOpTs();
    }
}
