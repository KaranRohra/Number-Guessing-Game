import javax.swing.*;

public class StartGame extends JFrame {
    JLabel statement, inputLabel, suggestion, prevGuess, move, borderLine, heading;
    JTextField inputNumber;
    JButton guess, restart;
    JLabel[] prevGames;
    StringBuilder prevString;
    int count, randomNumber;
    boolean isWon;

    public StartGame() {
        GameDatabase.initializeDatabase();
        GameDatabase.deleteFromDatabase();
        statement = new JLabel("<html>We have selected a random number between 1 and 100." +
                "<br/> See if you can guess it in 5 turns or fewer." +
                "<br/> We'll tell you if your guess was high or low.</html>");
        inputLabel = new JLabel("Enter a guess:");
        inputNumber = new JTextField();
        guess = new JButton("Guess");
        suggestion = new JLabel();
        prevGuess = new JLabel();
        prevString = new StringBuilder("Prev Guess: ");
        move = new JLabel();
        borderLine = new JLabel();
        prevGames = new JLabel[5];
        heading = new JLabel();
        restart = new JButton("Restart");

        for (int i = 0; i < 5; i++) {
            prevGames[i] = new JLabel();
            add(prevGames[i]);
        }
        displayRecentGame();

        String dash = "";
        for (int i = 0; i <= 43; i++) {
            dash = dash.concat("-");
        }
        //&emsp; represent 4 space in html
        borderLine.setText("<html>" + dash + "<br/>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;" +
                "RECENT GAMES<br/>" + dash + "</html>");
        heading.setText("<html> &emsp;&emsp;Statement &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp; Moves<br/>" + dash + "</html>");

        guess.addActionListener(e -> {
            GameDatabase.initializeDatabase();
            if (isWon) {
                suggestion.setText("<html>Game Over !!! <br/> You Won</html>");
                move.setText("<html>You have taken " + count + " move</html>");
                inputNumber.setEditable(false);
                return;
            } else if (count == 5) {
                suggestion.setText("<html>Game Over !!! <br/> You Lose</html>");
                inputNumber.setEditable(false);
                return;
            }
            try {
                int n = Integer.parseInt(inputNumber.getText());
                prevString.append(n).append(" ");
                if (n == randomNumber) {
                    suggestion.setText("<html>Game Over !!! <br/> You Won</html>");
                    move.setText("<html>You have taken " + count + " move</html>");
                    inputNumber.setText("Number is "+randomNumber);
                    inputNumber.setEditable(false);
                    isWon = true;

                    GameDatabase.insertIntoDatabase(1, count);
                    GameDatabase.deleteFromDatabase();
                    displayRecentGame();

                    GameDatabase.closeDatabase();
                    return;
                } else if (n < randomNumber) {
                    suggestion.setText("Number is low");
                } else {
                    suggestion.setText("Number is high");
                }
                count++;
                move.setText("You have " + (5 - count) + " moves left");
                if (count == 5) {
                    suggestion.setText("<html>Game Over !!! <br/> You Lose</html>");
                    inputNumber.setText("Number is "+randomNumber);
                    inputNumber.setEditable(false);
                    GameDatabase.insertIntoDatabase(0, count);
                    GameDatabase.deleteFromDatabase();
                    displayRecentGame();

                    GameDatabase.closeDatabase();
                    return;
                }
            } catch (Exception ea) {
                suggestion.setText("Invalid Input");
            }
            prevGuess.setText(prevString.toString());
            inputNumber.setText("");
            GameDatabase.closeDatabase();
        });

        restart.addActionListener(e -> {
            inputNumber.setEditable(true);
            inputNumber.setText("");
            prevGuess.setText("");
            prevString = new StringBuilder("Prev Guess: ");
            suggestion.setText("");
            move.setText("");
            count = 0;
            isWon = false;
            randomNumber=getRandomNumber();
        });
        randomNumber=getRandomNumber();

        move.setBounds(20, 160, 400, 30);
        prevGuess.setBounds(20, 140, 400, 30);
        suggestion.setBounds(20, 190, 300, 30);
        guess.setBounds(90, 120, 100, 30);
        restart.setBounds(200, 120, 100, 30);
        inputNumber.setBounds(120, 80, 200, 30);
        inputLabel.setBounds(20, 70, 100, 50);
        statement.setBounds(20, 20, 350, 50);
        borderLine.setBounds(20, 220, 400, 50);
        heading.setBounds(20, 260, 400, 50);

        add(restart);
        add(heading);
        add(borderLine);
        add(move);
        add(prevGuess);
        add(suggestion);
        add(guess);
        add(inputNumber);
        add(statement);
        add(inputLabel);

        setLayout(null);
        setVisible(true);
        setLocation(530, 150);
        setSize(400, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void displayRecentGame() {
        int count = GameDatabase.fetchData(prevGames);
        for (int i = 0, y = 300; i < 5; i++) {
            if (count-- >= 0)
                continue;
            prevGames[i].setBounds(20, y, 400, 50);
            y += 30;
        }
    }

    public int getRandomNumber(){
        int randomNumber = (int) (Math.random() * 100 + 1);
        System.out.println("Generated Random Number = "+randomNumber);
        return randomNumber;
    }

    public static void main(String[] args) {
        new StartGame();
    }
}
