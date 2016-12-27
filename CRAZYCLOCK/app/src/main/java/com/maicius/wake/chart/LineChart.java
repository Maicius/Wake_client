package com.maicius.wake.chart;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.maicius.wake.InterChange.GetUpHistory;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.chart.TimeChart;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import static android.R.attr.format;

public class LineChart extends AbstractChart {

    @Override
    public String getName() {
        //暂时不用
        return "";
    }

    @Override
    public String getDesc() {
        //暂时不用
        return "";
    }

    @Override
    public Intent execute(Context context) {

        //创建intent
        Intent intent = ChartFactory.getTimeChartIntent(context, mGetDateDataset(), mGetRenderer(), null, "起床历史");
        return intent;
    }

    //渲染器设置并返回
    private XYMultipleSeriesRenderer mGetRenderer() {
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        renderer.setAxisTitleTextSize(24);
        renderer.setChartTitleTextSize(32);
        renderer.setLabelsTextSize(18);
        renderer.setLegendTextSize(24);
        renderer.setPointSize(6f);
        //in this order: top, left, bottom, right
        renderer.setMargins(new int[]{20, 30, 15, 0});

        XYSeriesRenderer r = new XYSeriesRenderer();

        r.setPointStyle(PointStyle.CIRCLE);
        r.setColor(Color.GREEN);
        r.setFillPoints(true);

        renderer.addSeriesRenderer(r);
        renderer.setAxesColor(Color.DKGRAY);
        renderer.setLabelsColor(Color.LTGRAY);
        renderer.setChartTitle("起床历史时间折线图");
        renderer.setXTitle("起床日期");
        renderer.setYTitle("起床时间");
        renderer.setZoomButtonsVisible(true);
        renderer.setYAxisMin(4.0);
        renderer.setYAxisMax(12.0);
        renderer.setXLabels(5);
        renderer.setYLabels(8);
        renderer.setShowGrid(true);

        int length = renderer.getSeriesRendererCount();
        for (int i = 0; i < length; i++) {
            SimpleSeriesRenderer seriesRenderer = renderer.getSeriesRendererAt(i);
            seriesRenderer.setDisplayChartValues(false);
        }
        return renderer;
    }

    //数据集设置并返回
    private XYMultipleSeriesDataset mGetDateDataset() {
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        TimeSeries series = new TimeSeries("起床时间变化 ");

        ArrayList<String> mTimes = GetUpHistory.times;
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat fmt2 = new SimpleDateFormat("HH:mm");
        try {
            for (int i = 0; i < mTimes.size(); ++i) {
                String tmp = mTimes.get(i);
                StringTokenizer st = new StringTokenizer(tmp, " ");
                String date = st.nextToken();
                String time = st.nextToken();

                StringTokenizer st2 = new StringTokenizer(time, ":");
                int hour =Integer.parseInt(st2.nextToken());
                int minute = Integer.parseInt(st2.nextToken());
                int second = Integer.parseInt(st2.nextToken());
                double tm = hour + minute/60.0 + second/3600.0;
                series.add(fmt.parse(date), tm);

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        dataset.addSeries(series);

        return dataset;
    }
}
