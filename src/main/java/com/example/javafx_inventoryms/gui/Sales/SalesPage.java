package com.example.javafx_inventoryms.gui.Sales;

import com.example.javafx_inventoryms.objects.Sale;
import javafx.geometry.Insets;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
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
        Label titleLabel = new Label("Sales Management");
        titleLabel.getStyleClass().add("form-title");
        HBox summaryBox = createSummaryCards();
        HBox mainContent = new HBox(20);
        VBox formPanel = createFormPanel();
        VBox dataPanel = createDataPanel();
        HBox.setHgrow(formPanel, Priority.NEVER);
        HBox.setHgrow(dataPanel, Priority.ALWAYS);
        mainContent.getChildren().addAll(formPanel, dataPanel);
        content.getChildren().addAll(titleLabel, summaryBox, mainContent);
    }

    public HBox createSummaryCards(){

    }
    public VBox createFormPanel(){

    }
    public VBox createDataPanel(){

    }

    public void loadSalesData(){

    }
}