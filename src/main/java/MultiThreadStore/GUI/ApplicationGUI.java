package MultiThreadStore.GUI;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import MultiThreadStore.Business.SelectionPolicy;
import MultiThreadStore.Business.SimulationManager;

public class ApplicationGUI extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Main Window");

        VBox mainLayout = new VBox(10);
        mainLayout.setStyle("-fx-padding: 20;");
        mainLayout.setAlignment(Pos.CENTER);
        Button openStore = new Button("Open Store");

        Label timeLimitLabel = new Label("Time Limit:");
        TextField timeLimitField = new TextField();
        timeLimitField.setMaxWidth(150);

        Label maxProcessingTimeLabel = new Label("Max Processing Time:");
        TextField maxProcessingTimeField = new TextField();
        maxProcessingTimeField.setMaxWidth(150);

        Label minProcessingTimeLabel = new Label("Min Processing Time:");
        TextField minProcessingTimeField = new TextField();
        minProcessingTimeField.setMaxWidth(150);

        Label maxArrivalTimeLabel = new Label("Max Arrival Time:");
        TextField maxArrivalTimeField = new TextField();
        maxArrivalTimeField.setMaxWidth(150);

        Label minArrivalTimeLabel = new Label("Min Arrival Time:");
        TextField minArrivalTimeField = new TextField();
        minArrivalTimeField.setMaxWidth(150);

        Label numberOfQueuesLabel = new Label("Number of Queues:");
        TextField numberOfQueuesField = new TextField();
        numberOfQueuesField.setMaxWidth(150);

        Label numberOfClientsLabel = new Label("Number of Clients:");
        TextField numberOfClientsField = new TextField();
        numberOfClientsField.setMaxWidth(150);

        Label togglePolicyLabel = new Label("Strategy: Shortest Time");
        Button togglePolicyButton = new Button("Toggle Strategy");

        mainLayout.getChildren().addAll(
                numberOfClientsLabel, numberOfClientsField,
                numberOfQueuesLabel, numberOfQueuesField,
                timeLimitLabel, timeLimitField,
                maxArrivalTimeLabel, maxArrivalTimeField,
                minArrivalTimeLabel, minArrivalTimeField,
                maxProcessingTimeLabel,maxProcessingTimeField ,
                minProcessingTimeLabel, minProcessingTimeField,

                togglePolicyLabel, togglePolicyButton,openStore);
        Scene mainScene = new Scene(mainLayout, 500, 700);

        primaryStage.setScene(mainScene);
        primaryStage.show();

        openStore.setOnAction(e -> {
            try{
                int timeLimit = Integer.parseInt(timeLimitField.getText());
                int maxProcessingTime = Integer.parseInt(maxProcessingTimeField.getText());
                int minProcessingTime = Integer.parseInt(minProcessingTimeField.getText());
                int numberOfQueues = Integer.parseInt(numberOfQueuesField.getText());
                int numberOfClients = Integer.parseInt(numberOfClientsField.getText());
                int minArrivalTime = Integer.parseInt(maxArrivalTimeField.getText());
                int maxArrivalTime = Integer.parseInt(minArrivalTimeField.getText());
                SelectionPolicy policy;
                if(togglePolicyLabel.getText().equals("Strategy: Shortest Time")){
                    policy = SelectionPolicy.SHORTEST_TIME;
                }else{
                        policy = SelectionPolicy.SHORTEST_QUEUE;
                }
                openListWindow(timeLimit, maxProcessingTime, minProcessingTime, maxArrivalTime, minArrivalTime, numberOfQueues, numberOfClients, policy);
            }catch (NumberFormatException ex){
                showError("Invalid input", "Use numbers in the inputs!");
            }
        });
        togglePolicyButton.setOnAction(e -> {toggleStrategyAction(togglePolicyLabel);});

    }

    private void openListWindow(int timeLimit, int maxProcessingTime, int minProcessingTime, int maxArrivalTime, int minArrivalTime, int numberOfQueues, int numberOfClients, SelectionPolicy policy) {
        Stage listStage = new Stage();
        listStage.setTitle("List Window");
        ListView<String> listView = new ListView<>();
        //listView.setItems(FXCollections.observableArrayList("Item 1", "Item 2", "Item 3"));
        VBox listLayout = new VBox(10, listView);
        Label peakHour = new Label("Peak hour: ");
        Label averageWaitingTime = new Label("Average Waiting Time: ");
        Label averageServiceTime = new Label("Average Service Time: ");
        listLayout.getChildren().addAll(peakHour, averageWaitingTime, averageServiceTime);
        listLayout.setStyle("-fx-padding: 20;");
        Scene listScene = new Scene(listLayout, 1000, 700);
        listStage.setScene(listScene);
        listStage.show();

        SimulationManager gen = new SimulationManager(timeLimit, maxProcessingTime, minProcessingTime,maxArrivalTime, minArrivalTime, numberOfQueues, numberOfClients, policy);
        Thread t = new Thread(gen);
        t.start();

        javafx.animation.Timeline timeline = new javafx.animation.Timeline(
                new javafx.animation.KeyFrame(javafx.util.Duration.seconds(1),e->{
                    listView.setItems(gen.getOutputData());
                    peakHour.setText("Peak hour: " +gen.getStatistics().getPeakHour());
                    if(gen.getFinishFlag()){
                        averageServiceTime.setText("Average Service Time: " +gen.getStatistics().getAverageServiceTime());
                        averageWaitingTime.setText("Average Waiting Time: " +gen.getStatistics().getAverageWaitingTime());
                    }
                })
        );
        timeline.setCycleCount(javafx.animation.Animation.INDEFINITE);
        timeline.play();


    }
    private void toggleStrategyAction(Label togglePolicyLabel) {
        if(togglePolicyLabel.getText().equals("Strategy: Shortest Time")) {
            togglePolicyLabel.setText("Strategy: Shortest Queue");
        }
        else{
            if(togglePolicyLabel.getText().equals("Strategy: Shortest Queue")) {
                togglePolicyLabel.setText("Strategy: Shortest Time");
            }
        }
    }

    private void showError(String title, String message) {
        Alert error = new Alert(Alert.AlertType.ERROR);
        error.setTitle(title);
        error.setContentText(message);
        error.showAndWait();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
