package com.p11.practica11;

import javafx.application.Application;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class P11_Torrecillas extends Application {

    private int state = 0;
    static Label etiOperando1 = new Label("");
    static Label etiOperador = new Label("");
    static Label etiOperando2 = new Label("");

    static RadioButton suma = new RadioButton("Suma");
    static RadioButton resta = new RadioButton("Resta");
    static RadioButton producto = new RadioButton("Producto");
    static RadioButton division = new RadioButton("División");
    static ToggleGroup radioGroup = new ToggleGroup();

    static Button resultadoButton = new Button("Resultado");


    private EventHandler<MouseEvent> numberHandler = e -> {
        Button b = (Button) e.getSource();
/*      Esto seria la forma de atrapar el Infinity con código. Ya que no es una excepcion y la clase Double la contempla.

        if (etiOperando1.getText().equalsIgnoreCase("Infinity") || etiOperando1.getText().equalsIgnoreCase("-Infinity")) {
            etiOperando1.setText("");
            state = 0;
            new ArithmeticException("Error prueba");
        }

 */
        if (state == 0 || state == 1) {
            if (etiOperando1.getText().equals("ERROR")) {
                etiOperando1.setText("");
            }
            state =1;
            etiOperando1.setText(etiOperando1.getText() + b.getText());
        } else if ((state == 2 && radioGroup.getSelectedToggle() !=null && radioGroup.getSelectedToggle().isSelected()) || state == 3 ) {
            state =3;
            radioGroup.getSelectedToggle().setSelected(false);
            etiOperando2.setText(etiOperando2.getText() + b.getText());
        }
    };


    private EventHandler<MouseEvent> resetHandler = e -> {
        state = 0;
        etiOperando1.setText("");
        etiOperador.setText("");
        etiOperando2.setText("");

        if (radioGroup.getSelectedToggle() != null && radioGroup.getSelectedToggle().isSelected()) {
            radioGroup.getSelectedToggle().setSelected(false);
        }
    };


    private EventHandler<ActionEvent> operatorHandler = e -> {
        if (state == 0 || state == 3) {
            if (radioGroup.getSelectedToggle() != null && radioGroup.getSelectedToggle().isSelected()) {
                radioGroup.getSelectedToggle().setSelected(false);
            }
        }



        if (state == 1 || state == 2) {
            state = 2;
            if (suma.isSelected()) {
                etiOperador.setText("+");
            } else if (resta.isSelected()) {
                etiOperador.setText("-");
            } else if (producto.isSelected()) {
                etiOperador.setText("*");
            } else if (division.isSelected()) {
                etiOperador.setText("/");
            }
        }
    };

    private EventHandler<MouseEvent> calculateResult = e -> {
        if (state == 3) {

            double result = 0;
            try {
                if (etiOperador.getText().equals("+")) {
                    result = Double.parseDouble(etiOperando1.getText()) + Double.parseDouble(etiOperando2.getText());
                } else if (etiOperador.getText().equals("-")) {
                    result = Double.parseDouble(etiOperando1.getText()) - Double.parseDouble(etiOperando2.getText());
                } else if (etiOperador.getText().equals("*")) {
                    result = Double.parseDouble(etiOperando1.getText()) * Double.parseDouble(etiOperando2.getText());
                } else if (etiOperador.getText().equals("/")) {
                    if (etiOperando2.getText().equals("0")) {
                        result = Double.parseDouble(etiOperando1.getText()) / Double.parseDouble(etiOperando2.getText());
                        throw new InfinityException("");
                    }
                }

                etiOperando1.setText(Double.toString(result));
                etiOperador.setText("");
                etiOperando2.setText("");

                state = 2;

                /* Para forma manual.

                if (etiOperando1.getText().equalsIgnoreCase("Infinity")) {
                    state = 0;
                }

                 */

            } catch (ArithmeticException exception) {
                // Esto pasa cuando la calculadora es con INT.
                etiOperando1.setText("ERROR");
                etiOperador.setText("");
                etiOperando2.setText("");
                state = 0;
            } catch (InfinityException ex) {
                //Exception creado para atrapar el punto flotante con un double
                System.out.println("Infinity. Catch InfinityException.");
                etiOperando1.setText("ERROR");
                etiOperador.setText("");
                etiOperando2.setText("");
                state = 0;
            }
            //if (radioGroup.getSelectedToggle().isSelected()) {
            //radioGroup.getSelectedToggle().setSelected(false);
            //}
        }
    };

    @Override
    public void start(Stage stage) throws Exception {
        HBox hBoxTop = new HBox();
        try {
            FileInputStream fis = new FileInputStream("C:\\Users\\chris\\IdeaProjects\\Practica11\\src\\main\\java\\com\\p11\\practica11\\calc.jpg");
            //FileInputStream fis = new FileInputStream("/home/ALU1K/IdeaProjects/Practica11/src/main/java/com/p11/practica11/calc.jpg");
            Image image = new Image(fis);
            ImageView imageView = new ImageView(image);
            hBoxTop = new HBox(imageView);
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
        }

        suma.setToggleGroup(radioGroup);
        resta.setToggleGroup(radioGroup);
        producto.setToggleGroup(radioGroup);
        division.setToggleGroup(radioGroup);
        VBox vBoxLeft = new VBox(suma, resta, producto, division);

        suma.setOnAction(operatorHandler);
        resta.setOnAction(operatorHandler);
        producto.setOnAction(operatorHandler);
        division.setOnAction(operatorHandler);

        Button button0 = new Button("0");
        button0.addEventHandler(MouseEvent.MOUSE_CLICKED, numberHandler);
        Button buttonReset = new Button("Reset");
        buttonReset.addEventHandler(MouseEvent.MOUSE_CLICKED, resetHandler);

        GridPane gridPane = new GridPane();

        for (int i = 0 ; i < 9 ; i++) {
            Button button = new Button(Integer.toString(i+1));
            gridPane.add(button, i % 3, i / 3);

            button.setPrefSize(50, 40);
            button.addEventHandler(MouseEvent.MOUSE_CLICKED, numberHandler);

        }

        gridPane.add(button0, 0,3);
        gridPane.add(buttonReset, 2,3);

        button0.setPrefSize(50, 40);
        buttonReset.setPrefSize(50, 40);

        resultadoButton.addEventHandler(MouseEvent.MOUSE_CLICKED, calculateResult);
        resultadoButton.setPrefSize(80,40);

        VBox vBoxRight = new VBox(resultadoButton);
        vBoxRight.setPrefSize(120, 210);

        etiOperando1.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, FontPosture.REGULAR, 20));
        etiOperador.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, FontPosture.REGULAR, 20));
        etiOperando2.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, FontPosture.REGULAR, 20));
        HBox hBoxBottom = new HBox(etiOperando1, etiOperador, etiOperando2);
        TitledPane titledBottom =new TitledPane("Resultado", hBoxBottom);

        hBoxBottom.setPrefHeight(70);

        BorderPane base = new BorderPane();

        base.setTop(hBoxTop);
        base.setLeft(vBoxLeft);
        base.setCenter(gridPane);
        base.setRight(vBoxRight);
        base.setBottom(titledBottom);

        base.setPadding(new Insets(1));

        BorderPane.setMargin(hBoxTop, new Insets(0));
        BorderPane.setMargin(vBoxLeft, new Insets(0));
        BorderPane.setMargin(gridPane, new Insets(0));
        BorderPane.setMargin(vBoxRight, new Insets(0));
        BorderPane.setMargin(titledBottom, new Insets(0));

        hBoxTop.setPadding(new Insets(20, 0, 20, 0));
        //hBoxTop.setStyle("-fx-background-color: lightpink");

        vBoxLeft.setSpacing(23);
        vBoxLeft.setAlignment(Pos.TOP_LEFT);
        vBoxLeft.setPadding(new Insets(15, 0, 0, 15));
        vBoxLeft.setPrefWidth(100);
        //vBoxLeft.setStyle("-fx-background-color: lightgreen");

        gridPane.setAlignment(Pos.TOP_CENTER);
        gridPane.setPadding(new Insets(15 ,0 , 0 ,0));
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.setPrefSize(150, 150);

        //gridPane.setStyle("-fx-background-color: cornsilk");

        vBoxRight.setAlignment(Pos.BOTTOM_LEFT);
        vBoxRight.setPrefWidth(150);
        vBoxRight.setPadding(new Insets(0, 0, 23, 0));
        //vBoxRight.setStyle("-fx-background-color: lightblue");

        hBoxBottom.setAlignment(Pos.CENTER);
        //hBoxBottom.setStyle("-fx-background-color: red");

        Scene scene = new Scene(base, 450, 450);
        stage.setTitle("Calculadora Christian Torrecillas");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

    }
}


class InfinityException extends Exception {
    public InfinityException (String message) {

    }
}