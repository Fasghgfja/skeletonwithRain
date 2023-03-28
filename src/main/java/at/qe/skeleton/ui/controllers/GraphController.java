package at.qe.skeleton.ui.controllers;


import at.qe.skeleton.api.services.MeasurementService;
import at.qe.skeleton.model.Measurement;
import at.qe.skeleton.model.SensorStation;
import at.qe.skeleton.services.SensorStationService;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.axes.cartesian.CartesianScales;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearAxes;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearTicks;
import org.primefaces.model.charts.bar.BarChartDataSet;
import org.primefaces.model.charts.bar.BarChartModel;
import org.primefaces.model.charts.bar.BarChartOptions;
import org.primefaces.model.charts.line.LineChartDataSet;
import org.primefaces.model.charts.line.LineChartModel;
import org.primefaces.model.charts.line.LineChartOptions;
import org.primefaces.model.charts.optionconfig.animation.Animation;
import org.primefaces.model.charts.optionconfig.legend.Legend;
import org.primefaces.model.charts.optionconfig.legend.LegendLabel;
import org.primefaces.model.charts.optionconfig.title.Title;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Component
@Scope("view")
public class GraphController implements Serializable {

    /**
     * Different charts that can be used in the application
     * to call them from the frontend just use graphcontroller.fieldname
     */
    private LineChartModel lineModel = new LineChartModel();
    private LineChartModel cartesianLinerModel;
    private BarChartModel barModel = new BarChartModel();

    @Autowired
    MeasurementService measurementService;


    @Autowired
    SensorStationService sensorService;


    /**
     * Attribute to cache the currently displayed sensor station.
     */
    private SensorStation sensorStation;

    /**
     * Attribute to cache the latest Measurements.
     */
    private List<Measurement> latestMeasurements;


    /**
     * Method used in the dashboard to change the Graph displayed based on row selection of the panel beside.
     */
    public void onRowSelect(SelectEvent<SensorStation> event) {
        sensorStation = (SensorStation) event.getObject();
        createLineModel();
        createCartesianLinerModel();
        latestMeasurements = new ArrayList<>(measurementService.getLatestPlantMeasurements(sensorStation));
        if (!latestMeasurements.isEmpty()) {
            createBarModel(latestMeasurements);
        }
    }

    /**
     * Method used in the dashboard to change the Graph displayed based on row selection of the panel beside.
     */
    public void onRowSelectLineChart(SelectEvent<Measurement> event) {
        Measurement measurement = event.getObject();
        sensorStation = measurement.getSensorStation();
        createCartesianLinerModel();
        latestMeasurements = new ArrayList<>(measurementService.getLatestPlantMeasurements(sensorStation));
        if (!latestMeasurements.isEmpty()) {
            createLineModel(latestMeasurements);
        }
    }


    //TODO: hide y axis values for dashboard graph
    public void createBarModel(List<Measurement> measurements) {
        barModel = new BarChartModel();
        ChartData data = new ChartData();

        BarChartDataSet barDataSet = new BarChartDataSet();
        barDataSet.setLabel("Selected Sensor Station Last Measurements");

        //TODO:change this with a query for AirValue GroundValue HumidityValue etc instead of hoping they come out in the correct order
        List<Number> values = new ArrayList<>();
        measurements.forEach(measurement -> {
            if (measurement == null) {
                values.add(0);
            } else {
                values.add(Integer.parseInt(measurement.getValue_s()));
            }
        });

        barDataSet.setData(values);
        List<String> bgColor = new ArrayList<>();
        bgColor.add("rgba(255, 99, 132, 0.2)");
        bgColor.add("rgba(255, 159, 64, 0.2)");
        bgColor.add("rgba(255, 205, 86, 0.2)");
        bgColor.add("rgba(75, 192, 192, 0.2)");
        bgColor.add("rgba(54, 162, 235, 0.2)");
        bgColor.add("rgba(153, 102, 255, 0.2)");
        bgColor.add("rgba(201, 203, 207, 0.2)");
        barDataSet.setBackgroundColor(bgColor);

        List<String> borderColor = new ArrayList<>();
        borderColor.add("rgb(255, 99, 132)");
        borderColor.add("rgb(255, 159, 64)");
        borderColor.add("rgb(255, 205, 86)");
        borderColor.add("rgb(75, 192, 192)");
        borderColor.add("rgb(54, 162, 235)");
        borderColor.add("rgb(153, 102, 255)");
        borderColor.add("rgb(201, 203, 207)");
        barDataSet.setBorderColor(borderColor);
        barDataSet.setBorderWidth(1);

        data.addChartDataSet(barDataSet);

        List<String> labels = new ArrayList<>();
        labels.add("Temperature");
        labels.add("Air Humidity");
        labels.add("Ground Humidity");
        labels.add("Light Intensity");
        labels.add("Air Quality");
        labels.add("Air Pressure");
        data.setLabels(labels);
        barModel.setData(data);

        //Options
        BarChartOptions options = new BarChartOptions();
        CartesianScales cScales = new CartesianScales();
        CartesianLinearAxes linearAxes = new CartesianLinearAxes();
        linearAxes.setOffset(true);
        linearAxes.setBeginAtZero(true);
        CartesianLinearTicks ticks = new CartesianLinearTicks();
        linearAxes.setTicks(ticks);
        cScales.addYAxesData(linearAxes);
        options.setScales(cScales);

        Title title = new Title();
        title.setDisplay(true);
        title.setText("Bar Chart");
        options.setTitle(title);


        Legend legend = new Legend();
        legend.setDisplay(true);
        legend.setPosition("top");
        LegendLabel legendLabels = new LegendLabel();
        legendLabels.setFontStyle("italic");
        legendLabels.setFontColor("#2980B9");
        legendLabels.setFontSize(24);
        legend.setLabels(legendLabels);
        options.setLegend(legend);


        // disable animation
        Animation animation = new Animation();
        animation.setDuration(0);
        options.setAnimation(animation);

        barModel.setOptions(options);
    }

    public void createLineModel(List<Measurement> measurements) {
        lineModel = new LineChartModel();
        ChartData Air_Temperature = new ChartData();
        ChartData Air_Humidity = new ChartData();
        ChartData Ground_Humidity = new ChartData();

        LineChartDataSet dataSet = new LineChartDataSet();
        //TODO:change this with a query for AirValue GroundValue HumidityValue etc instead of hoping they come out in the correct order
        List<Object> values = new ArrayList<>();
        measurements.forEach(measurement -> {
            if (measurement == null) {
                values.add(0);
            } else {
                values.add(Integer.parseInt(measurement.getValue_s()));
            }
        });
        dataSet.setData(values);
        dataSet.setFill(false);
        dataSet.setLabel("Air Temperature");
        dataSet.setBorderColor("rgb(75, 192, 192)");
        dataSet.setTension(0.1);
        Air_Temperature.addChartDataSet(dataSet);


        List<String> labels = new ArrayList<>();
        labels.add("January");
        labels.add("February");
        labels.add("March");
        labels.add("April");
        labels.add("May");
        labels.add("June");
        labels.add("July");
        Air_Temperature.setLabels(labels);

        //Options
        LineChartOptions options = new LineChartOptions();
        Title title = new Title();
        title.setDisplay(true);
        title.setText("Line Chart");
        options.setTitle(title);

        lineModel.setOptions(options);
        lineModel.setData(Air_Temperature);
    }

















    public void createLineModel() {
        lineModel = new LineChartModel();
        ChartData Air_Temperature = new ChartData();


        LineChartDataSet dataSet = new LineChartDataSet();
        List<Object> values = new ArrayList<>();
        values.add(65);
        values.add(59);
        values.add(80);
        values.add(81);
        values.add(56);
        values.add(55);
        values.add(40);
        dataSet.setData(values);
        dataSet.setFill(false);
        dataSet.setLabel("Air Temperature");
        dataSet.setBorderColor("rgb(75, 192, 192)");
        dataSet.setTension(0.1);
        Air_Temperature.addChartDataSet(dataSet);

        List<String> labels = new ArrayList<>();
        labels.add("January");
        labels.add("February");
        labels.add("March");
        labels.add("April");
        labels.add("May");
        labels.add("June");
        labels.add("July");
        Air_Temperature.setLabels(labels);

        //Options
        LineChartOptions options = new LineChartOptions();
        Title title = new Title();
        title.setDisplay(true);
        title.setText("Line Chart");
        options.setTitle(title);

        lineModel.setOptions(options);
        lineModel.setData(Air_Temperature);
    }































    public void createCartesianLinerModel() {
        cartesianLinerModel = new LineChartModel();
        ChartData data = new ChartData();

        LineChartDataSet dataSet = new LineChartDataSet();
        List<Object> values = new ArrayList<>();
        values.add(20);
        values.add(50);
        values.add(100);
        values.add(75);
        values.add(25);
        values.add(0);
        dataSet.setData(values);
        dataSet.setLabel("Left Dataset");
        dataSet.setYaxisID("left-y-axis");
        dataSet.setFill(true);
        dataSet.setTension(0.5);

        LineChartDataSet dataSet2 = new LineChartDataSet();
        List<Object> values2 = new ArrayList<>();
        values2.add(0.1);
        values2.add(0.5);
        values2.add(1.0);
        values2.add(2.0);
        values2.add(1.5);
        values2.add(0);
        dataSet2.setData(values2);
        dataSet2.setLabel("Right Dataset");
        dataSet2.setYaxisID("right-y-axis");
        dataSet2.setFill(true);
        dataSet2.setTension(0.5);

        data.addChartDataSet(dataSet);
        data.addChartDataSet(dataSet2);

        List<String> labels = new ArrayList<>();
        labels.add("Jan");
        labels.add("Feb");
        labels.add("Mar");
        labels.add("Apr");
        labels.add("May");
        labels.add("Jun");
        data.setLabels(labels);
        cartesianLinerModel.setData(data);

        //Options
        LineChartOptions options = new LineChartOptions();
        CartesianScales cScales = new CartesianScales();
        CartesianLinearAxes linearAxes = new CartesianLinearAxes();
        linearAxes.setId("left-y-axis");
        linearAxes.setPosition("left");
        CartesianLinearAxes linearAxes2 = new CartesianLinearAxes();
        linearAxes2.setId("right-y-axis");
        linearAxes2.setPosition("right");

        cScales.addYAxesData(linearAxes);
        cScales.addYAxesData(linearAxes2);
        options.setScales(cScales);

        Title title = new Title();
        title.setDisplay(true);
        title.setText("Cartesian Linear Chart");
        options.setTitle(title);

        cartesianLinerModel.setOptions(options);
    }


}