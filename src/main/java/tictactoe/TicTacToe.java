package tictactoe;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class TicTacToe {
    private static final char SIGN_X = 'x'; // переменная
    private static final char SIGN_O = 'o'; // переменная
    private static final char SIGN_EMPTY = '.'; // переменная
    private static final String FILE_RESULT_GAME = "src/main/resources/xml/GameResult.xml";

    private Player[] players = new Player[2];
    private ArrayList<Step> steps = new ArrayList<>();
    private char[][] table; //двумерный символьный массив, игровое поле
    private final Scanner scanner = new Scanner(System.in);
    private ArrayList<Player> playerWinList = new ArrayList<>();

    /*
     * Конструктор игры
     * Создание игроков и запуск игрового процесса
     * */
    public TicTacToe() {
        for (int i = 1; i <= 2; i++) {
            System.out.println("Введите имя Player №" + i);
            if (i == 1) {
                players[0] = createPlayer((long) i, SIGN_X);
            }
            if (i == 2) {
                players[1] = createPlayer((long) i, SIGN_O);
            }

        }
        game(players);
    }

    /**
     * Создание игроков
     *
     * @param id     присваивается id игрока
     * @param symbol присваивается символ которым ходит игрок
     * @return возвращает экземпляр игрока
     */
    private Player createPlayer(Long id, char symbol) {
        Player player = new Player();
        String playerName;

        player.setId(id);

        while (true) {
            playerName = scanner.nextLine();

            if (playerName.isBlank()) {
                System.out.println("Ваше имя некорректно. Строка не должна быть пустой");
                System.out.print("Player №" + id + ", повторите ввод имени ");
            } else {
                player.setName(playerName);
                break;
            }
        }

        player.setSymbol(symbol);

        return player;
    }

    /**
     * Game логика
     *
     * @param players принимает массив игроков
     */

    private void game(Player[] players) {
        table = new char[3][3];
        int x = 1;
        int numStep = 0;
        initTable(); // инициализация таблицы
        Step step;
        Player playerWin = null;

        //Цикл игрового процесса до тех пор пока кто-нидудь не выйграет или ничья
        for (int i = 0; i < 2; i++) {
            numStep++;
            step = new Step();
            step.setNumStep(numStep);
            step.setPlayerId(players[i].getId());

            System.out.println("Player " + players[i].getName() + " сделайте ход");

            step.setDot(turnHuman(players[i].getSymbol())); // ход игрока
            steps.add(step);

            printTable();
            // проверка: если победа
            if (checkWin(players[i].getSymbol())) {
                playerWin = players[i];
            }

            if (playerWin == null && !isTableFull()) {
                if (i == 1) {
                    i = -1;
                } else continue;
            } else if (isTableFull()) {
                System.out.println("Ничья");
                playerWinList.add(null);
                break;
            } else {
                System.out.println(players[i].getName() + " выиграл!");
                playerWinList.add(playerWin);
                break;
            }
        }

        while (true) {
            System.out.print("Желаете сыграть снова? (введите 0 = ДА, 1 = НЕТ): ");
            x = scanner.nextInt();

            if (x == 0) {
                game(players);
            } else if (x == 1) {
                System.out.println("Конец игры.");
                setResultGameXml(players, steps, playerWinList);
                break;
            } else System.out.println("Введеное значение некорректно, повторите ввод");
        }

    }

    private void initTable() { //метод обеспечивает начальную инициализацию игровой таблицы,
        // заполняя её ячейки «пустыми» символами
        for (int row = 0; row < 3; row++)
            for (int col = 0; col < 3; col++)
                table[row][col] = SIGN_EMPTY;
    }

    private void printTable() { // метод, отображающий текущее состояние игровой таблицы
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++)
                System.out.print(table[row][col] + " ");
            System.out.println();
        }
    }

    private String turnHuman(char dot) { // метод, который позволяет пользователю сделать ход
        int x, y;

        do {
            System.out.print("введите координаты X от 1 до 3: ");
            x = scanner.nextInt() - 1;

            System.out.print("введите координаты Y от 1 до 3: ");
            y = scanner.nextInt() - 1;

            //Проверяем правильнойсть ячейки и не занята ли она
            if (isCellValid(x, y)) {
                if (table[y][x] == SIGN_X || table[y][x] == SIGN_O) {
                    System.out.println("Ячейка занята. Введите другие координаты!");
                } else break;
            }
        }
        while (true);

        table[y][x] = dot;
        return x + " " + y;
    }

    private boolean isCellValid(int x, int y) { // метод проверки валидности ячейки
        if (x < 0 || y < 0 || x >= 3 || y >= 3) {
            System.out.println("Числа не из диапазона от 1 до 3! Повторите ввод!");
            return false;
        }
        return true;
    }

    private boolean checkWin(char dot) { // метод проверки победы по горизонтали, вертикали и
        // диагонали
        for (int i = 0; i < 3; i++) {
            if ((table[i][0] == dot && table[i][1] == dot &&
                    table[i][2] == dot) || (table[0][i] == dot && table[1][i] == dot && table[2][i] == dot))
                return true;
            if ((table[0][0] == dot && table[1][1] == dot &&
                    table[2][2] == dot) || (table[2][0] == dot && table[1][1] == dot && table[0][2] == dot))
                return true;
        }
        return false;
    }

    private boolean isTableFull() { // метод проверки на ничью
        for (int row = 0; row < 3; row++) {// циклом  проходим по всем ячейкам игровой таблицы и,
            // если они все заняты, возвращаем true
            for (int col = 0; col < 3; col++) {
                if (table[row][col] == SIGN_EMPTY)
                    return false;
            }
        }// если хотя бы одна ячейка свободна, возвращаем false
        return true; // ничья
    }

    /**
     * Запись результатов игры в xml
     *
     * @param players       массив игроков
     * @param stepPlayer    лист шагов
     * @param playerWinList лист победителей
     */
    private void setResultGameXml(Player[] players, ArrayList<Step> stepPlayer,
                                  ArrayList<Player> playerWinList) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;

        try {
            builder = factory.newDocumentBuilder();

            Document doc = builder.newDocument();
            Element rootElement = doc.createElement("GamePlay");

            doc.appendChild(rootElement);
            rootElement.appendChild(getPlayer(doc, players[0]));
            rootElement.appendChild(getPlayer(doc, players[1]));

            for (Player i : playerWinList) {
                Element elementGame = doc.createElement("Game");
                Element gameResult = doc.createElement("GameResult");
                if (i != null) {
                    rootElement.appendChild(getStepInGame(doc, elementGame, stepPlayer));
                    gameResult.appendChild(getPlayer(doc, i));
                } else {
                    rootElement.appendChild(getStepInGame(doc, elementGame, stepPlayer));
                    gameResult.appendChild(doc.createTextNode("Tie"));
                }
                rootElement.appendChild(gameResult);
            }

            TransformerFactory transformFactory = TransformerFactory.newInstance();
            Transformer transformer = transformFactory.newTransformer();

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            DOMSource domSource = new DOMSource(doc);

            StreamResult console = new StreamResult(System.out);
            StreamResult file = new StreamResult(new File(FILE_RESULT_GAME));

            transformer.transform(domSource, console);
            transformer.transform(domSource, file);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Node getPlayer(Document doc, Player playerInfo) {
        Element player = doc.createElement("Player");
        player.setAttribute("id", String.valueOf(playerInfo.getId()));
        player.setAttribute("name", playerInfo.getName());
        player.setAttribute("symbol", String.valueOf(playerInfo.getSymbol()));

        return player;
    }

    private static Node getStepInGame(Document doc, Element elementGame,
                                      ArrayList<Step> stepPlayer) {
        for (Step i : stepPlayer) {
            Element elementStep = doc.createElement("Step");
            elementStep.setAttribute("num", i.getNumStep().toString());
            elementStep.setAttribute("playerId", i.getPlayerId().toString());
            elementStep.appendChild(doc.createTextNode(i.getDot()));
            elementGame.appendChild(elementStep);

        }
        return elementGame;
    }

}
