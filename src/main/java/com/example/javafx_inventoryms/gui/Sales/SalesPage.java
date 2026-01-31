package com.example.javafx_inventoryms.gui.Sales;

import com.example.javafx_inventoryms.objects.Sale;
import javafx.geometry.Insets;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class SalesPage extends ScrollPane{
    private TableView<Sale> salesTable;
    private TextField grossRevenueField;
    private TextField cogsField;
    private TextField operatingExpenserField;
    private ComboBox<String> paymentMethodCombo;
    private TextField invoiceNumberField;
    private TextField employeeIdField;
    private Label dailySalesLabel;
    private Label weeklySalesLabel;
    private Label monthlySalesLabel;
    private LineChart<String, Number> salesTrendChart;
    private BarChart<String, Number> paymentMethodChart;
    private VBox content;

    public SalesPage(){
        content = new VBox(20);
        content.getStyleClass().add("vbox-container");
        content.setPadding(new Insets(20));

        initComponents();
        loadSalesData();

        setContent(content);
        setFitToWidth(true);
        setHbarPolicy(ScrollBarPolicy.NEVER);
        setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
    }

    public void initComponents(){

    }

    public void loadSalesData(){

    }
}