package javadev.core.util;

/*
 * @author sulin
 * 创建日期 2005-10-21
 */

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

public class BarChart3DHelper{
   
    public CategoryDataset createDataset(List list)
    {
        DefaultCategoryDataset defaultcategorydataset = new DefaultCategoryDataset();
        Iterator it = list.iterator();
        while(it.hasNext())
        {
            for(int i = 0;i<list.size();i++)
            {
                Map map = (Map)it.next();
            	defaultcategorydataset.addValue(Integer.parseInt(String.valueOf( map.get("count"))), String.valueOf(map.get("series")),  String.valueOf(map.get("category")));
            }
        }
        
        return defaultcategorydataset;
    }
    public JFreeChart createChart(CategoryDataset categorydataset,String title,String series,String category)
    {
        JFreeChart jfreechart = ChartFactory.createBarChart3D(title, //图形标题名称
                											series,//domain 轴 Lable 
                                                                         
                											category, //range 轴 Lable
                                                                       
                                                            categorydataset, //  dataset
                                                            PlotOrientation.VERTICAL, //垂直显示
                                                            true, // legend?
                                                            true,  // tooltips?
                                                            false); //URLs?
        jfreechart.setBackgroundPaint(Color.white);   //设定背景色为白色
        CategoryPlot categoryplot = jfreechart.getCategoryPlot(); //获得 plot：CategoryPlot！！
        categoryplot.setBackgroundPaint(Color.lightGray); //设定图表数据显示部分背景色
        categoryplot.setDomainGridlinePaint(Color.white); //横坐标网格线白色
        categoryplot.setDomainGridlinesVisible(true); //可见
        categoryplot.setRangeGridlinePaint(Color.white); //纵坐标网格线白色
        //下面两行使纵坐标的最小单位格为整数
        NumberAxis numberaxis = (NumberAxis)categoryplot.getRangeAxis();
        numberaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        BarRenderer barrenderer = (BarRenderer)categoryplot.getRenderer(); //获得renderer 注意这里是下嗍造型
                                                                             //到BarRenderer！！
        
        barrenderer.setDrawBarOutline(false); // Bar的外轮廓线不画
        barrenderer.setMaximumBarWidth(0.04);
        GradientPaint gradientpaint = new GradientPaint(0.0F, 0.0F, Color.blue, 
        0.0F, 0.0F, new Color(0, 0, 64));   //设定特定颜色
        GradientPaint gradientpaint1 = new GradientPaint(0.0F, 0.0F, Color.green, 
        0.0F, 0.0F, new Color(0, 64, 0));
        GradientPaint gradientpaint2 = new GradientPaint(0.0F, 0.0F, Color.red,
        0.0F, 0.0F, new Color(64, 0, 0));
        barrenderer.setSeriesPaint(0, gradientpaint); //给series1 Bar设定上面定义的颜色
        barrenderer.setSeriesPaint(1, gradientpaint1); //给series2 Bar 设定上面定义的颜色
        barrenderer.setSeriesPaint(2, gradientpaint2); //给series3 Bar 设定上面定义的颜色
        CategoryAxis categoryaxis = categoryplot.getDomainAxis();  //横轴上的 Lable 倾斜度
        categoryaxis.setCategoryLabelPositions(CategoryLabelPositions.DOWN_45);        
        return jfreechart;
    }
//    public String execute() throws Exception{
//        CategoryDataset categorydataset=createDataset(list);
//        chart = createChart(categorydataset,title,series,category);
//        return SUCCESS;
//    }
//    public JFreeChart getChart() { 
//        return chart; 
//     }
}
