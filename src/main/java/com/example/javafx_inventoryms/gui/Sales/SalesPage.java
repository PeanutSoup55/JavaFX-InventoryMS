package com.example.javafx_inventoryms.gui.Sales;

import com.example.javafx_inventoryms.objects.Sale;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
        HBox summaryBox = new HBox(15);
        summaryBox.setAlignment(Pos.CENTER);
        VBox dailyCard = createSummaryCard("Today's Sales", "$0.00", "daily-sales");
        dailySalesLabel = (Label) ((VBox) dailyCard.getChildren().get(0)).getChildren().get(1);
        VBox weeklyCard = createSummaryCard("This Week", "$0.00", "weekly-sales");
        weeklySalesLabel = (Label) ((VBox) weeklyCard.getChildren().get(0)).getChildren().get(1);
        VBox monthlyCard = createSummaryCard("This Month", "$0.00", "monthly-sales");
        monthlySalesLabel = (Label) ((VBox) monthlyCard.getChildren().get(0)).getChildren().get(1);
        HBox.setHgrow(dailyCard, Priority.ALWAYS);
        HBox.setHgrow(weeklyCard, Priority.ALWAYS);
        HBox.setHgrow(monthlyCard, Priority.ALWAYS);
        summaryBox.getChildren().addAll(dailyCard, weeklyCard, monthlyCard);
        return summaryBox;
    }
    public VBox createSummaryCard(String title, String value, String id){
        VBox card = new VBox(10);
        card.getStyleClass().add("form-panel");
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(20));
        card.setId(id);

        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("form-label");
        titleLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #64748b;");

        Label valueLabel = new Label(value);
        valueLabel.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: #10b981;");

        VBox content = new VBox(5, titleLabel, valueLabel);
        content.setAlignment(Pos.CENTER);

        card.getChildren().add(content);

        return card;
    }
    public VBox createFormPanel(){

    }
    public VBox createDataPanel(){

    }

    public void loadSalesData(){

    }
}