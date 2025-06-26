import org.json.simple.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class WeatherAppGUI extends JFrame {

    private JSONObject weatherData;

    public WeatherAppGUI(){
        super("Weather App");

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(450,650);
        setLocationRelativeTo(null);
        setLayout(null);
        setResizable(false);

        addGUIComponents();
    }

    private void addGUIComponents(){
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds(15, 15, 351, 45);

        JTextField searchTextField = new JTextField();
        searchTextField.setFont(new Font("Dialog", Font.PLAIN, 24));
        searchTextField.setBounds(0, 0, 351, 45);

        JLabel placeholderLabel = new JLabel("Enter City or Country");
        placeholderLabel.setForeground(Color.GRAY);
        placeholderLabel.setFont(new Font("Dialog", Font.ITALIC, 24));
        placeholderLabel.setBounds(10, 0, 351, 45);
        placeholderLabel.setOpaque(false);

        layeredPane.add(searchTextField, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(placeholderLabel, JLayeredPane.PALETTE_LAYER);

        add(layeredPane);

        searchTextField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                placeholderLabel.setVisible(searchTextField.getText().trim().isEmpty());
            }
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                placeholderLabel.setVisible(searchTextField.getText().trim().isEmpty());
            }
            public void changedUpdate(javax.swing.event.DocumentEvent e) {}
        });


        JLabel titleLabel = new JLabel("Weather App by Jenish", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBounds(0,300,450,40);
        add(titleLabel);

        JLabel weatherConditionImage = new JLabel(loadImage("src/asserts/cloudy.png"));
        weatherConditionImage.setBounds(0,125,450,150);
        add(weatherConditionImage);


        JLabel temperaturetext = new JLabel("10 °C");
        temperaturetext.setBounds(0, 270, 450, 40);
        temperaturetext.setHorizontalAlignment(SwingConstants.CENTER);
        temperaturetext.setFont(new Font("Dialog", Font.BOLD, 32)); // Increased font
        add(temperaturetext);

        JLabel weatherConditionDesc = new JLabel("Cloudy");
        weatherConditionDesc.setBounds(0,320,450,40);
        weatherConditionDesc.setFont(new Font("Dialog", Font.PLAIN, 32));
        weatherConditionDesc.setHorizontalAlignment(SwingConstants.CENTER);
        add(weatherConditionDesc);

        JLabel humidityImage = new JLabel(loadImage("src/asserts/humidity.png"));
        humidityImage.setBounds(15,500,74,66);
        add(humidityImage);

        JLabel humidityText = new JLabel("<html><b>Humidity</b> 100%</html>");
        humidityText.setBounds(90,500,85,55);
        humidityText.setFont(new Font("Dialog", Font.PLAIN, 16));
        add(humidityText);

        JLabel windspeedImage = new JLabel(loadImage("src/asserts/windspeed.png"));
        windspeedImage.setBounds(220,500,74,66);
        add(windspeedImage);

        JLabel windspeedText = new JLabel("<html><b>WindSpeed</b> 15km/h</html>");
        windspeedText.setBounds(310,500,85,55);
        windspeedText.setFont(new Font("Dialog", Font.PLAIN, 16));
        add(windspeedText);

        weatherConditionImage.setVisible(false);
        temperaturetext.setVisible(false);
        weatherConditionDesc.setVisible(false);
        humidityImage.setVisible(false);
        humidityText.setVisible(false);
        windspeedImage.setVisible(false);
        windspeedText.setVisible(false);

        JButton searchButton = new JButton(loadImage("src/asserts/search.png"));
        searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        searchButton.setBounds(375,15,47,45);
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userInput = searchTextField.getText();

                if(userInput.replaceAll("\\s", "").length() <= 0){
                    return;
                }

                weatherData = WeatherApp.getWeatherData(userInput);

                if (weatherData == null) {
                    JOptionPane.showMessageDialog(null, "Could not fetch weather data. Please try a different location.");
                    return;
                }


                String weatherCondition = (String) weatherData.get("weather_condition");

                switch(weatherCondition){
                    case "Clear":
                        weatherConditionImage.setIcon(loadImage("src/asserts/clear.png",200,150));
                        break;
                    case "Cloudy":
                        weatherConditionImage.setIcon(loadImage("src/asserts/cloudy.png",200,150));
                        break;
                    case "Rain":
                        weatherConditionImage.setIcon(loadImage("src/asserts/rain.png",200,150));
                        break;
                    case "Snow":
                        weatherConditionImage.setIcon(loadImage("src/asserts/snow.png",200,150));
                        break;
                }

                double temperature = (double) weatherData.get("temperature");
                temperaturetext.setText(temperature + "°C");
                weatherConditionDesc.setText(weatherCondition);

                long humidity = (long) weatherData.get("humidity");
                humidityText.setText("<html><b>Humidity</b> " + humidity + "%</html>");

                double windspeed = (double) weatherData.get("windspeed");
                windspeedText.setText("<html><b>Windspeed</b> " + windspeed + "km/h</html>");

                titleLabel.setVisible(false);
                weatherConditionImage.setVisible(true);
                temperaturetext.setVisible(true);
                weatherConditionDesc.setVisible(true);
                humidityImage.setVisible(true);
                humidityText.setVisible(true);
                windspeedImage.setVisible(true);
                windspeedText.setVisible(true);

            }
        });
        add(searchButton);
    }

    private ImageIcon loadImage(String resoucePath){
        try{
            BufferedImage image = ImageIO.read(new File(resoucePath));
            return new ImageIcon(image);
        }catch(IOException e){
            e.printStackTrace();
        }

        System.out.println("Could not find resource");
        return null;
    }

    private ImageIcon loadImage(String resourcePath, int width, int height) {
        try {
            BufferedImage image = ImageIO.read(new File(resourcePath));
            Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Could not find resource");
        return null;
    }

}
