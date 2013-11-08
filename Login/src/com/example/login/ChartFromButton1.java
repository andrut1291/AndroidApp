package com.example.login;
import java.util.Arrays;


import com.androidplot.xy.*;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

/**
*
* This example is based on:
* - multitouch example, by David Buezas (david.buezas at gmail.com) and Michael
* from http://androidplot.com/wiki/A_Simple_XYPlot_with_multi-touch_zooming_and_scrolling
* - AndroidPlot quickstart example http://androidplot.com/wiki/Quickstart
* No license was given with this samples, but I assume that use it for free on BSD-like license ;-)
*
* @author Marcin Lepicki (marcin.lepicki at flex-it.pl)
*
*/
public class ChartFromButton1 extends Activity
{

@Override
public void onCreate(Bundle savedInstanceState)
{
super.onCreate(savedInstanceState);
setContentView(R.layout.chart_button1);

MultitouchPlot multitouchPlot = (MultitouchPlot) findViewById(R.id.stepChartExamplePlot);

// Create two arrays of y-values to plot:
Number[] series1Numbers = {1, 8, 5, 2, 7, 4};
Number[] series2Numbers = {4, 6, 3, 8, 2, 10};

// Turn the above arrays into XYSeries:
XYSeries series1 = new SimpleXYSeries(
Arrays.asList(series1Numbers), // SimpleXYSeries takes a List so turn our array into a List
SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, // Y_VALS_ONLY means use the element index as the x value
"Obwód brzucha"); // Set the display title of the series

// Same as above, for series2
XYSeries series2 = new SimpleXYSeries(Arrays.asList(series2Numbers), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY,
"Series2");

// Create a formatter to use for drawing a series using LineAndPointRenderer:
LineAndPointFormatter series1Format = new LineAndPointFormatter(
Color.rgb(0, 200, 0), // line color
Color.rgb(0, 100, 0), // point color
Color.rgb(150, 190, 150),null); // fill color (optional)

// Add series1 to the xyplot:
multitouchPlot.addSeries(series1, series1Format);

// Same as above, with series2:
multitouchPlot.addSeries(series2, new LineAndPointFormatter(Color.rgb(0, 0, 200), Color.rgb(0, 0, 100),
Color.rgb(150, 150, 190),null));

// Reduce the number of range labels
multitouchPlot.setTicksPerRangeLabel(3);

// By default, AndroidPlot displays developer guides to aid in laying out your plot.
// To get rid of them call disableAllMarkup():
//multitouchPlot.disableAllMarkup();

multitouchPlot.setRangeBoundaries(0, 10, BoundaryMode.FIXED);
multitouchPlot.setDomainBoundaries(0, 2.2, BoundaryMode.FIXED);

}
}