package com.ked.core.services.impl;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

@Component
public class ChartGenerator {

    public byte[] generatePieChart(Map<String, Double> categoryDistribution) {
        DefaultPieDataset dataset = new DefaultPieDataset();

        for (Map.Entry<String, Double> entry : categoryDistribution.entrySet()) {
            dataset.setValue(entry.getKey(), entry.getValue());
        }

        JFreeChart chart = ChartFactory.createPieChart(
                "Распределение транзакций по категориям",
                dataset,
                true,
                true,
                false
        );

        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setOutlineVisible(false);
        plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(800, 600));

        chart.getPlot().setBackgroundPaint(Color.WHITE);
        chart.getPlot().setOutlineVisible(false);
        ((PiePlot<?>) chart.getPlot()).setLabelFont(new Font("SansSerif", Font.PLAIN, 12));

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ChartUtils.writeChartAsPNG(baos, chart, 800, 600);
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
