import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Calculator extends JFrame {
    private final JTextField display = new JTextField();
    private double firstOperand = 0;
    private String operator = "";
    private boolean startNewNumber = true;

    public Calculator() {
        setTitle("Calculator");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(320, 420);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(6,6));

        display.setFont(new Font("SansSerif", Font.BOLD, 28));
        display.setHorizontalAlignment(SwingConstants.RIGHT);
        display.setEditable(false);
        display.setText("0");
        add(display, BorderLayout.NORTH);

        JPanel buttons = new JPanel(new GridLayout(5, 4, 6, 6));
        String[] labels = {
            "C", "±", "%", "/",
            "7", "8", "9", "*",
            "4", "5", "6", "-",
            "1", "2", "3", "+",
            "0", ".", "⌫", "="
        };

        for (String t : labels) {
            JButton b = new JButton(t);
            b.setFont(new Font("SansSerif", Font.PLAIN, 20));
            b.addActionListener(this::onButtonPressed);
            buttons.add(b);
        }

        add(buttons, BorderLayout.CENTER);
        setVisible(true);
    }

    private void onButtonPressed(ActionEvent e) {
        String cmd = ((JButton)e.getSource()).getText();

        if ("0123456789".contains(cmd)) {
            if (startNewNumber) {
                display.setText(cmd);
                startNewNumber = false;
            } else {
                if (!(display.getText().equals("0") && cmd.equals("0")))
                    display.setText(display.getText() + cmd);
            }
            return;
        }

        switch (cmd) {
            case ".":
                if (startNewNumber) {
                    display.setText("0.");
                    startNewNumber = false;
                } else if (!display.getText().contains(".")) {
                    display.setText(display.getText() + ".");
                }
                break;

            case "C":
                display.setText("0");
                firstOperand = 0;
                operator = "";
                startNewNumber = true;
                break;

            case "⌫":
                if (!startNewNumber) {
                    String s = display.getText();
                    if (s.length() > 1) display.setText(s.substring(0, s.length() - 1));
                    else { display.setText("0"); startNewNumber = true; }
                }
                break;

            case "±":
                if (!display.getText().equals("0")) {
                    if (display.getText().startsWith("-")) display.setText(display.getText().substring(1));
                    else display.setText("-" + display.getText());
                }
                break;

            case "%":
                try {
                    double val = Double.parseDouble(display.getText());
                    val = val / 100.0;
                    display.setText(trimDouble(val));
                    startNewNumber = true;
                } catch (NumberFormatException ex) {}
                break;

            case "+":
            case "-":
            case "*":
            case "/":
                applyOperator(cmd);
                break;

            case "=":
                calculateResult();
                operator = "";
                break;
        }
    }

    private void applyOperator(String op) {
        try {
            if (!operator.isEmpty() && !startNewNumber) {
                calculateResult();
            } else {
                firstOperand = Double.parseDouble(display.getText());
            }
            operator = op;
            startNewNumber = true;
        } catch (NumberFormatException ex) {}
    }

    private void calculateResult() {
        try {
            double second = Double.parseDouble(display.getText());
            double result = 0;
            switch (operator) {
                case "+": result = firstOperand + second; break;
                case "-": result = firstOperand - second; break;
                case "*": result = firstOperand * second; break;
                case "/":
                    if (second == 0) {
                        display.setText("Error");
                        startNewNumber = true;
                        return;
                    }
                    result = firstOperand / second; break;
                case "": result = second; break;
            }
            display.setText(trimDouble(result));
            firstOperand = result;
            startNewNumber = true;
        } catch (NumberFormatException ex) {}
    }

    private String trimDouble(double d) {
        if (Double.isNaN(d) || Double.isInfinite(d)) return "Error";
        if (d == (long)d) return String.format("%d", (long)d);
        else return String.format("%s", d);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Calculator::new);
    }
}
