package com.example.demo;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.w3c.dom.Text;

import javax.xml.transform.Result;
import java.io.IOException;
import java.util.ResourceBundle;

public class HelloController {
    public Connection con = HelloApplication.con;

    public static String currEmail;

    @FXML private TextField email;
    @FXML private PasswordField password;
    @FXML private TextField name;
    @FXML private TextField addr;
    @FXML private Label contRating;
    @FXML private TextField productName;
    @FXML private TextField productTypes;
    @FXML private RadioButton isTool;
    @FXML private TextField productUnits;
    @FXML private ComboBox<String> prods;

    @FXML private TableView recipeTable;
    @FXML private TableColumn<Product, String> tableProdName;
    @FXML private TableColumn<Product, String> tableProdAmount;
    @FXML private TextField prodAmt;

    @FXML
    public void initialize() {
        if (contRating != null) {
            ResultSet rs = null;
            try {
                rs = HelloApplication.executeSelect("SELECT ROUND(AVG(rating),1) FROM Reviews WHERE contributor='"
                        + currEmail + "';");
                rs.next();
                contRating.setText("Average Rating: " + rs.getString(1) + "/5.0");
            } catch (SQLException e) {
                System.out.println(e);
            }
        }
        if (prods != null) {
            try {
                ResultSet rs = HelloApplication.executeSelect("SELECT pname FROM Products;");
                while (rs.next()) {
                    prods.getItems().add(rs.getString(1));
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    @FXML
    protected void handleLogoff(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Login");
        Scene scene2 = new Scene(fxmlLoader.load(), 600, 400);
        stage.setScene(scene2);
        stage.show();
        ((Node) event.getSource()).getScene().getWindow().hide();
    }

    //Login Page
    @FXML
    protected void handleCreateAccount(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("newAccount.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Create Account");
        Scene scene2 = new Scene(fxmlLoader.load(), 600, 400);
        stage.setScene(scene2);
        stage.show();
        ((Node) event.getSource()).getScene().getWindow().hide();
    }

    @FXML
    protected void handleLogin(MouseEvent event) throws IOException {
        try{
            ResultSet rs = HelloApplication.executeSelect("SELECT email FROM Users WHERE email='" + email.getText() + "' AND pin='" + password.getText() + "'");
            if (rs.next()) {
                currEmail = email.getText();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("HomeSelection.fxml"));
                Stage stage = new Stage();
                stage.setTitle("Home");
                Scene scene2 = new Scene(fxmlLoader.load(), 600, 400);
                stage.setScene(scene2);
                stage.show();
                ((Node) event.getSource()).getScene().getWindow().hide();
            }
        }catch(Exception e){
            System.out.println(e);
        }
    }

    //Create Account
    @FXML
    protected void handleNewAccountLogin(MouseEvent event) throws IOException {
        try {
            HelloApplication.executeInsert("INSERT INTO Users VALUES ('" + email.getText() + "','" + name.getText()
                    + "','" + password.getText() + "',0,'W Peachtree St', 'ATL', 'US', 1)");
            currEmail = email.getText();
            HelloApplication.executeInsert("UPDATE Users SET flag=1 WHERE email='" + currEmail + "'");
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("HomeSelection.fxml"));
            Stage stage = new Stage();
            stage.setTitle("New Home");
            Scene scene2 = new Scene(fxmlLoader.load(), 600, 400);
            stage.setScene(scene2);
            stage.show();
            ((Node) event.getSource()).getScene().getWindow().hide();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
    @FXML
    protected void handleBackToLogin(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Create Account");
        Scene scene2 = new Scene(fxmlLoader.load(), 600, 400);
        stage.setScene(scene2);
        stage.show();
        ((Node) event.getSource()).getScene().getWindow().hide();
    }

    //Select Homepage
    @FXML
    protected void handleContLogin(MouseEvent event) throws IOException {
        try {
            HelloApplication.executeInsert("UPDATE Users SET flag=2 WHERE email='" + currEmail + "';");
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ContributorHome.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Home");
            Scene scene2 = new Scene(fxmlLoader.load(), 600, 400);
            stage.setScene(scene2);
            stage.show();
            ((Node) event.getSource()).getScene().getWindow().hide();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    @FXML
    protected void handleChefLogin(MouseEvent event) throws IOException {
        try {
            HelloApplication.executeInsert("UPDATE Users SET flag=3 WHERE email='" + currEmail + "'");
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ChefHome.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Chef Home");
            Scene scene2 = new Scene(fxmlLoader.load(), 600, 400);
            stage.setScene(scene2);
            stage.show();
            ((Node) event.getSource()).getScene().getWindow().hide();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    //Contributor Home
    @FXML
    protected void handleContBack(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("HomeSelection.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Home Selection");
        Scene scene2 = new Scene(fxmlLoader.load(), 600, 400);
        stage.setScene(scene2);
        stage.show();
        ((Node) event.getSource()).getScene().getWindow().hide();
    }

    @FXML
    protected void handleContWrite(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("WriteRecipe.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Write Recipe");
        Scene scene2 = new Scene(fxmlLoader.load(), 600, 400);
        stage.setScene(scene2);
        stage.show();
        ((Node) event.getSource()).getScene().getWindow().hide();
    }

    @FXML
    protected void handleBrowseRecipes(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("BrowseRecipesCont.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Browse Recipes");
        Scene scene2 = new Scene(fxmlLoader.load(), 600, 400);
        stage.setScene(scene2);
        stage.show();
        ((Node) event.getSource()).getScene().getWindow().hide();
    }

    @FXML
    protected void handleContSeeRecipes(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MyRecipes.fxml"));
        Stage stage = new Stage();
        stage.setTitle("See Recipe");
        Scene scene2 = new Scene(fxmlLoader.load(), 600, 400);
        stage.setScene(scene2);
        stage.show();
        ((Node) event.getSource()).getScene().getWindow().hide();
    }

    //write recipe and my recipes and browse recipes
    @FXML
    protected void handleBackToContHome (MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ContributorHome.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Cont Home");
        Scene scene2 = new Scene(fxmlLoader.load(), 600, 400);
        stage.setScene(scene2);
        stage.show();
        ((Node) event.getSource()).getScene().getWindow().hide();
    }

    //Chef Home
    @FXML
    protected void handleSeeProducts(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MyProducts.fxml"));
        Stage stage = new Stage();
        stage.setTitle("My Products");
        Scene scene2 = new Scene(fxmlLoader.load(), 600, 400);
        stage.setScene(scene2);
        stage.show();
        ((Node) event.getSource()).getScene().getWindow().hide();
    }

    @FXML
    protected void handleBrowseRecipesChef (MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("BrowseRecipesHC.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Chef Browse Recipe");
        Scene scene2 = new Scene(fxmlLoader.load(), 600, 400);
        stage.setScene(scene2);
        stage.show();
        ((Node) event.getSource()).getScene().getWindow().hide();
    }

    @FXML
    protected void handleGroceryRuns(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("GroceryRuns.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Grocery Runs");
        Scene scene2 = new Scene(fxmlLoader.load(), 600, 400);
        stage.setScene(scene2);
        stage.show();
        ((Node) event.getSource()).getScene().getWindow().hide();
    }

    //my products and browse recipes chef and grocery
    @FXML
    protected void handleBackToChefHome(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ChefHome.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Chef Home");
        Scene scene2 = new Scene(fxmlLoader.load(), 600, 400);
        stage.setScene(scene2);
        stage.show();
        ((Node) event.getSource()).getScene().getWindow().hide();
    }

    //grocery runs
    @FXML
    protected void handleNewGroceryRun(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("GroceryRun.fxml"));
        Stage stage = new Stage();
        stage.setTitle("New Run");
        Scene scene2 = new Scene(fxmlLoader.load(), 600, 400);
        stage.setScene(scene2);
        stage.show();
        ((Node) event.getSource()).getScene().getWindow().hide();
    }
    //grocery run
    @FXML
    protected void handleBackToGroceryRuns(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("GroceryRuns.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Grocery Runs");
        Scene scene2 = new Scene(fxmlLoader.load(), 600, 400);
        stage.setScene(scene2);
        stage.show();
        ((Node) event.getSource()).getScene().getWindow().hide();
    }

    @FXML
    protected void handleGoToAddProduct(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AddProduct.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Add Product");
        Scene scene2 = new Scene(fxmlLoader.load(), 600, 400);
        stage.setScene(scene2);
        stage.show();
        ((Node) event.getSource()).getScene().getWindow().hide();
    }

    @FXML
    protected void handleAddProduct(MouseEvent event) {
        try {
            ResultSet rs = HelloApplication.executeSelect("SELECT * FROM Products WHERE pname='" + productName.getText() + "';");
            if (!rs.next()) {
                int temp;
                if (isTool.isSelected()) {
                    temp = 0;
                } else {
                    temp = 1;
                }
                HelloApplication.executeInsert("INSERT INTO Products VALUES ('" + productName.getText() + "', '" + temp + "', '" + productUnits.getText() + "');");
                if (!isTool.isSelected()) {
                    String[] split = productTypes.getText().split("[,]", 0);
                    for (String str : split) {
                        if (!HelloApplication.executeSelect("SELECT * FROM I_types WHERE itype='" + str + "';").next()) {
                            HelloApplication.executeInsert("INSERT INTO I_types VALUES ('" + str + "');");
                        }
                        HelloApplication.executeInsert("INSERT INTO Ing_Types VALUES ('" + productName.getText() + "', '" + str + "');");
                    }

                }
            } else {
                System.out.println("Product already exists!!");
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    public class Product {
        private String name;
        private Integer amount;

        public Product(String name, int amount) {
            this.name = name;
            this.amount = amount;
        }

        public String getName() {
            return name;
        }

        public Integer getAmount() {
            return amount;
        }
    }

    @FXML
    protected void addProdToRecipe(MouseEvent event) {
        tableProdName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableProdAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        recipeTable.getItems().addAll(new Product(prods.getValue(), Integer.parseInt(prodAmt.getText())));
    }
}