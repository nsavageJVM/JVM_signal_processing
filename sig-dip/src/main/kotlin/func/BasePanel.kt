package func

/**
 * Created by ubu on 20.04.17.
 */

import javafx.application.Platform
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.geometry.Insets
import javafx.scene.chart.LineChart
import javafx.scene.chart.NumberAxis
import javafx.scene.chart.XYChart
import javafx.scene.layout.VBox

class MainWindowController {

    @FXML
    var vboxRaw = VBox()
    @FXML
    var vboxProc = VBox()


    fun initialize() {

        Entrypoint.processData("/ecg1.csv")
        vboxRaw.children.addAll(createChart(Entrypoint.rawData()))
        vboxProc.children.addAll(createChart(Entrypoint.processData()))
    }

    @FXML
	private fun file_exit(event: ActionEvent) {
        Platform.exit()
        System.exit(0)
	}


    private fun createChart(data: DoubleArray): LineChart<Number, Number>  {
        val xAxis =   NumberAxis()
        val yAxis =   NumberAxis()

        xAxis.setForceZeroInRange(false)
        xAxis.setAnimated(false)
        yAxis.setScaleX(0.0)
        yAxis.setForceZeroInRange(false)

        val chart = LineChart(xAxis, yAxis)
        chart.setLegendVisible(false);
        chart.setAlternativeColumnFillVisible(false);
        chart.setAlternativeRowFillVisible(false);
        chart.setPadding(Insets(10.0, 0.0, 0.0, 0.0))
        chart.setVerticalZeroLineVisible(false)
        chart.setHorizontalZeroLineVisible(false)
        chart.setVerticalGridLinesVisible(false)
        chart.getYAxis().setTickLabelsVisible(false)
        chart.getYAxis().setOpacity(0.0)

        val dataSeries = XYChart.Series<Number,Number>()
        val dataAsList = data.asList().subList(100, 1000)
        var x: Int = 0
        dataAsList.forEach({ d ->
            dataSeries.data.add(XYChart.Data<Number, Number>(x, d))
            x++
        } )

        chart.getData().add( dataSeries)

        return chart
    }


}