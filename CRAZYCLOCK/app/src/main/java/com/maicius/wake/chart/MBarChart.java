package com.maicius.wake.chart;

import java.util.ArrayList;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer.Orientation;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

/**
 * Sales demo bar chart.
 */
public class MBarChart extends AbstractChart {

    /**
     * Returns the chart name.
     *
     * @return the chart name
     */
    public String getName() {
        return "Sales horizontal bar chart";
    }

    /**
     * Returns the chart description.
     *
     * @return the chart description
     */
    public String getDesc() {
        return "The monthly sales for the last 2 years (horizontal bar chart)";
    }

    /**
     * Executes the chart demo.
     *
     * @param context the context
     * @return the built intent
     */
    public Intent execute(Context context) {
        String[] titles = new String[]{"2007", "2008"};
        List<double[]> values = new ArrayList<double[]>();
        values.add(new double[]{5230, 7300, 9240, 10540, 7900, 9200, 12030, 11200, 9500, 10500,
                11600, 13500});
        values.add(new double[]{14230, 12300, 14240, 15244, 15900, 19200, 22030, 21200, 19500, 15500,
                12600, 14000});
        int[] colors = new int[]{Color.CYAN, Color.BLUE};
        XYMultipleSeriesRenderer renderer = buildBarRenderer(colors);
        renderer.setOrientation(Orientation.VERTICAL);
        setChartSettings(renderer, "Monthly sales in the last 2 years", "Month", "Units sold", 0.5,
                12.5, 0, 24000, Color.GRAY, Color.LTGRAY);
        renderer.setXLabels(1);
        renderer.setYLabels(10);
        renderer.addXTextLabel(1, "Jan");
        renderer.addXTextLabel(3, "Mar");
        renderer.addXTextLabel(5, "May");
        renderer.addXTextLabel(7, "Jul");
        renderer.addXTextLabel(10, "Oct");
        renderer.addXTextLabel(12, "Dec");
        int length = renderer.getSeriesRendererCount();
        for (int i = 0; i < length; i++) {
            XYSeriesRenderer seriesRenderer = (XYSeriesRenderer) renderer.getSeriesRendererAt(i);
            seriesRenderer.setDisplayChartValues(true);
        }
        return ChartFactory.getBarChartIntent(context, buildBarDataset(titles, values), renderer,
                Type.DEFAULT);
    }

}
