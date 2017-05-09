
package com.maicius.wake.chart;

import org.achartengine.ChartFactory;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import java.text.NumberFormat;
import java.util.Random;

/**
 * Budget demo pie chart.
 */
public class MPieChart extends AbstractChart {
    /**
     * Returns the chart name.
     *
     * @return the chart name
     */
    public String getName() {
        return "Budget chart";
    }

    /**
     * Returns the chart description.
     *
     * @return the chart description
     */
    public String getDesc() {
        return "The budget per project for this year (pie chart)";
    }

    /**
     * Executes the chart demo.
     *
     * @param context the context
     * @return the built intent
     */
    public Intent execute(Context context) {
        double[] values = new double[]{0.06, 0.4, 0.3, 0.2, 0.04};
        String[] strs = new String[]{"6点之前", "6点-8点", "8点-10点", "10点-12点", "12点之后"};
        int[] colors = new int[]{Color.BLUE, Color.GREEN, Color.MAGENTA, Color.YELLOW, Color.CYAN};
        CategorySeries series = new CategorySeries("");
        double totalValues = 0;

        DefaultRenderer mRenderer = new DefaultRenderer();
        mRenderer.setChartTitle("起床时间段对比图");
        mRenderer.setZoomButtonsVisible(true);// 显示放大缩小功能按钮
        mRenderer.setStartAngle(180);// 设置为水平开始
        mRenderer.setDisplayValues(true);// 显示数据
        mRenderer.setFitLegend(true);// 设置是否显示图例
        mRenderer.setLegendTextSize(32);// 设置图例字体大小
        mRenderer.setLegendHeight(10);// 设置图例高度

        for (int i = 0; i < values.length; i++)
            totalValues += values[i];
        for (int i = 0; i < values.length; i++) {
            series.add(strs[i], values[i] / totalValues);// 设置种类名称和对应的数值，前面是（key,value）键值对
            SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();
            if (i < colors.length) {
                renderer.setColor(colors[i]);// 设置描绘器的颜色
            } else {
                renderer.setColor(getRandomColor());// 设置描绘器的颜色
            }
            renderer.setChartValuesFormat(NumberFormat.getPercentInstance());// 设置百分比
            mRenderer.setChartTitleTextSize(48);// 设置饼图标题大小
            mRenderer.addSeriesRenderer(renderer);// 将最新的描绘器添加到DefaultRenderer中
        }

        Intent intent = ChartFactory.getPieChartIntent(context,
                series, mRenderer, "起床历史");
        return intent;
    }

    private int getRandomColor() {// 分别产生RBG数值
        Random random = new Random();
        int R = random.nextInt(255);
        int G = random.nextInt(255);
        int B = random.nextInt(255);
        return Color.rgb(R, G, B);
    }
}
