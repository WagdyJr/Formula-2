package drift;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Drift extends Application {
    
    private static final Font FONT = Font.font("", FontWeight.BOLD, 15);
     private static final Font FONT2 = Font.font("", FontWeight.SEMI_BOLD, 25);
     private static final Font FONT3 = Font.font("", FontWeight.BOLD, 30);

    private VBox menuBox;
    private int currentItem = 0;

    private ScheduledExecutorService bgThread = Executors.newSingleThreadScheduledExecutor();

    private Parent createContent() {
        Pane root = new Pane();
        root.setPrefSize(1100, 800);

        Rectangle bg = new Rectangle(1100, 800);

        ContentFrame frame1 = new ContentFrame(createLeftContent());
        ContentFrame frame2 = new ContentFrame(createMiddleContent());
        ContentFrame frame3 = new ContentFrame(createRightContent());

        HBox hbox = new HBox(15, frame1, frame2, frame3);
        hbox.setTranslateX(123);
        hbox.setTranslateY(50);

        MenuItem itemExit = new MenuItem("Exit");
        itemExit.setOnActivate(() -> System.exit(0));

        menuBox = new VBox(40,
                new MenuItem("Start"),
               
        
                itemExit);
        menuBox.setAlignment(Pos.TOP_CENTER);
        menuBox.setTranslateX(440);
        menuBox.setTranslateY(400);

        Text about = new Text("All stars in one race");
        about.setTranslateX(50);
        about.setTranslateY(700);
        about.setFill(Color.WHITE);
        about.setFont(FONT);
        about.setOpacity(0.3);

        getMenuItem(0).setActive(true);

        root.getChildren().addAll(bg, hbox, menuBox, about);
        return root;
    }

    private Node createLeftContent() {
        final Text inbox = new Text("Try to pass all cars");
        inbox.setFill(Color.WHITE);

        bgThread.scheduleAtFixedRate(() -> {
            Platform.runLater(() -> {
                TranslateTransition tt = new TranslateTransition(Duration.seconds(0.5), inbox);
                tt.setToY(150);

                FadeTransition ft = new FadeTransition(Duration.seconds(0.5), inbox);
                ft.setToValue(0);

                ParallelTransition pt = new ParallelTransition(tt, ft);
                pt.setOnFinished(e -> {
                    inbox.setTranslateY(-150);
                    inbox.setText("Avoid crashing obstacles");

                    TranslateTransition tt2 = new TranslateTransition(Duration.seconds(1), inbox);
                    tt2.setToY(0);

                    FadeTransition ft2 = new FadeTransition(Duration.seconds(1), inbox);
                    ft2.setToValue(1);

                    ParallelTransition pt2 = new ParallelTransition(tt2, ft2);
                    pt2.play();
                });
                pt.play();
            });
        }, 2, 5, TimeUnit.SECONDS);

        return inbox;
    }

    private Node createMiddleContent() {
        String title = "Car Racing";
        HBox letters = new HBox(0);
        letters.setAlignment(Pos.CENTER);
        for (int i = 0; i < title.length(); i++) {
            Text letter = new Text(title.charAt(i) + "");
            letter.setFont(FONT3);
            letter.setFill(Color.WHITE);
            letters.getChildren().add(letter);

            TranslateTransition tt = new TranslateTransition(Duration.seconds(2), letter);
            tt.setDelay(Duration.millis(i * 50));
            tt.setToY(-25);
            tt.setAutoReverse(true);
            tt.setCycleCount(TranslateTransition.INDEFINITE);
            tt.play();
        }

        return letters;
    }

    private Node createRightContent() {
        String title = "Only the strong survive";
        HBox letters = new HBox(0);
        letters.setAlignment(Pos.CENTER);
        for (int i = 0; i < title.length(); i++) {
            Text letter = new Text(title.charAt(i) + "");
            letter.setFont(FONT);
            letter.setFill(Color.WHITE);
            letter.setOpacity(0);
            letters.getChildren().add(letter);

            FadeTransition ft = new FadeTransition(Duration.seconds(2), letter);
            ft.setDelay(Duration.millis(i * 50));
            ft.setToValue(1);
            ft.setAutoReverse(true);
            ft.setCycleCount(TranslateTransition.INDEFINITE);
            ft.play();
        }

        return letters;
    }

    private MenuItem getMenuItem(int index) {
        return (MenuItem)menuBox.getChildren().get(index);
    }

    private static class ContentFrame extends StackPane {
        public ContentFrame(Node content) {
            setAlignment(Pos.CENTER);

            Rectangle frame = new Rectangle(270, 270);
            frame.setArcWidth(25);
            frame.setArcHeight(25);
            frame.setStroke(Color.WHITESMOKE);

            getChildren().addAll(frame, content);
        }
    }

    private static class MenuItem extends HBox {
        private TriCircle c1 = new TriCircle(), c2 = new TriCircle();
        private Text text;
        private Runnable script;

        public MenuItem(String name) {
            super(60);
            setAlignment(Pos.CENTER);

            text = new Text(name);
            text.setFont(FONT2);
            text.setBoundsType(TextBoundsType.VISUAL);
         
            text.setEffect(new GaussianBlur(2));

            getChildren().addAll(c1, text, c2);
            setActive(false);
            setOnActivate(() -> System.out.println(name + " activated"));
        }

        public void setActive(boolean b) {
            c1.setVisible(b);
            c2.setVisible(b);
            text.setFill(b ? Color.ORANGE : Color.GREY);
        }

        public void setOnActivate(Runnable r) {
            script = r;
        }

        public void activate() {
            if (script != null)
                script.run();
        }
    }

    private static class TriCircle extends Parent {
        public TriCircle() {
            Shape shape1 = Shape.subtract(new Circle(5), new Circle(2));
            shape1.setFill(Color.WHITE);

            Shape shape2 = Shape.subtract(new Circle(5), new Circle(2));
            shape2.setFill(Color.WHITE);
            shape2.setTranslateX(5);

            Shape shape3 = Shape.subtract(new Circle(5), new Circle(2));
            shape3.setFill(Color.WHITE);
            shape3.setTranslateX(2.5);
            shape3.setTranslateY(-5);

            getChildren().addAll(shape1, shape2, shape3);

            setEffect(new GaussianBlur(2.5));
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(createContent());
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.UP) {
                if (currentItem > 0) {
                    getMenuItem(currentItem).setActive(false);
                    getMenuItem(--currentItem).setActive(true);
                }
            }

            if (event.getCode() == KeyCode.DOWN) {
                if (currentItem < menuBox.getChildren().size() - 1) {
                    getMenuItem(currentItem).setActive(false);
                    getMenuItem(++currentItem).setActive(true);
                }
            }

            if (event.getCode() == KeyCode.ENTER) {
                if(currentItem==0)
                {
                    
                    Platform.exit();
                    
                    
                   
                }
                if(currentItem==1)
                {
                       
                   
                }
               
                getMenuItem(currentItem).activate();
            }
        });

        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(event -> {
            bgThread.shutdownNow();
        });
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
        new Game("Drift", 1200, 700).start();
    }
    
}
        
 