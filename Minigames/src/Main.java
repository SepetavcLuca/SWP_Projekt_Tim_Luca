import Models.*;
import Repl.*;

import java.sql.SQLException;
import java.util.Scanner;
public class Main {


    static String userPw;
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";

    static Scanner reader= new Scanner(System.in);
    // Tic Tac Toe
    static char[][] tictactoe = new char[3][3];


    public static void main(String[] args) {
        System.out.println("Seas.");

        User u = new User();


        boolean login = true;
        int choice;
        do {
            System.out.printf("\n\n\n\n\n%50s\n", ANSI_RED +"Anmeldung/Regristration" + ANSI_RESET);
            System.out.printf("%43s", "=========================\n");
            do {
                System.out.println("\n0 ... Anmeldung");
                System.out.println("1 ... Regristration");
                System.out.print("Ihre Wahl: ");
                String input = reader.nextLine();

                choice = checkIfInputIsRightInt(input, 1,0);
            }while(choice == -1);

            switch (choice) {
                case 0:
                    login(u);
                    break;
                case 1:
                    register(u);
                    break;
                default:
                    System.out.println("Sie haben eine falsche Taste gedrückt");
                    login = false;
            }
        } while (!login);

        TicTacToe();
    }

    // Anmeldung
    public static boolean login (User u){
        ReposyDB rep = null;
        try {
            rep = new Reposy();
            rep.open();
            System.out.println("\n\nAnmeldung:");
            System.out.println("==========");
            System.out.print("Benutzername: ");
            String username = reader.next();
            System.out.print("Passwort: ");
            String password = reader.next();
            userPw = password;

            if (rep.login(username, password)) {
                System.out.println("Sie sind eingelogt.");
                u.setUsername(username);
                u.setPassword(password);
                return true;
            }
            else{
                System.out.println("Der Benutzername oder das Passwort war falsch.\n" +
                        "Bitte versuchen sie es nocheinmal.");
                return login(u);
            }

        }
        catch (ClassNotFoundException e) {

            // System.out.println("MySQL-Treiber konnte nicht geladen werden!");

        }
        catch (SQLException e) {
            //System.out.println("Datenbankfehler!");
        }
        finally {
            try {
                rep.close();
            } catch (SQLException e) {
                //System.out.println("DB-Verbindung konnte nicht beendet werden");
            }

        }
        return false;
    }
    public static void register(User u) {
        ReposyDB rep = null;
        try {
            rep = new Reposy();
            rep.open();
            System.out.println("\n\nRegristration:");
            System.out.print("==============");
            if (rep.register(generateNewUser(u))) {
                System.out.println("Sie wurden erfolgreich regristriert");
            }
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL-Treiber konnte nicht geladen werden!");

        } catch (SQLException e) {
            System.out.println("Datenbankfehler!");
        }

        finally{
            try {
                rep.close();
            } catch (SQLException e) {
                // System.out.println("DB-Verbindung konnte nicht beendet werden");
            }

        }
    }
    public static User generateNewUser (User u) {
        String un;
        do {
            System.out.print("\nGeben sie ihren Benutzername ein: ");
            un = reader.next();
        }while(checkIfUsernameAlreadyExists(un));
        u.setUsername(un);
        System.out.print("Geben sie ihr Passwort ein: ");
        u.setPassword(reader.next());
        return u;
    }
    public static boolean checkIfUsernameAlreadyExists(String un){
        ReposyDB rep = null;
        try {
            rep = new Reposy();
            rep.open();

            if (rep.checkIfUserExists(un)){
                System.out.println("Der Benutzername ist leider schon vergeben.");
                return true;
            }
        }
        catch (ClassNotFoundException e) {
            //System.out.println("MySQL-Treiber konnte nicht geladen werden!");

        }
        catch (SQLException e) {
            //System.out.println("Datenbankfehler!");
        }
        finally {
            try {
                rep.close();
            }
            catch(SQLException e){
                // System.out.println("DB-Verbindung konnte nicht beendet werden");
            }
        }
        return false;
    }
    public static int checkIfInputIsRightInt(String input, int highestPosibilty, int lowestPosibilty){
        reader = new Scanner(System.in);
        try {
            int result = Integer.parseInt(input);
            if(result <= highestPosibilty && result >=lowestPosibilty){
                return result;
            }
            else{
                System.out.println("Sie haben eine zu große/kleine Zahl eingegeben.\n" +
                        "Bitte versuchen sie es erneut.\n");
            }
        } catch (NumberFormatException e) {
            System.out.println("Sie haben eine falsche Zahl/Zeichen eingegben.\n" +
                    "Bitte versuchen sie es erneut.\n");
            return -1;
        }
        return -1;
    }


    // TicTacToe
    public static void TicTacToe(){

        GameStatus gameStatus = GameStatus.SpielNochNichtFertig;
        char player = 'x';
        int row, column;

        feldBelegen();

        do {
            tictactoeAnzeigen();
            row = eingabe("Zeile [1-3]: ");
            column = eingabe("Spalte [1-3]: ");
            // nur wenn das gewählte Feld frei ist wird es belegt
            if(tictactoe[row][column] == '-'){
                tictactoe[row][column] = player;

                //überprüfen, ob das Spiel fertig ist
                gameStatus = checkIfGameIsOver(player);

                // falls aktuelle x am zug war, wird der Spieler nach 'o' gewechsel
                // kurze Schreibweise für if ... else
                // es wird überprüft ob der player = 'x' ist,
                // wenn true (?) wird zu 'o' gewechselt
                // wenn false dann wir zu 'x' gewechselt (:)
                player = player == 'x' ? 'o' : 'x';

            }

            else {
                System.out.println("Das Feld war belegt. Bitte erneut eingeben");
            }
        }while(gameStatus == GameStatus.SpielNochNichtFertig);

        tictactoeAnzeigen();
        showGameStatus(gameStatus);
    }
    public static void feldBelegen(){
        // bei einem 2dim. Array benötigten wir mormalerweise 2 Schleifen
        // alle Zeilen durchlaufen
        for(int i = 0; i<3; i++) {
            // alle Spalten durchlaufen
            for (int j = 0; j < 3; j++) {
                // Startwert jedem Feld zuweisen
                tictactoe[i][j] = '-';
            }
        }
    }
    public static void tictactoeAnzeigen(){
        for(int i = 0; i<3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.printf("%5c", tictactoe[i][j]);
            }
            System.out.println();
        }
    }
    public static int eingabe(String text){
        int value;

        do {
            System.out.print(text);
            value = reader.nextInt();
        }while((value<1) || (value>3));
        // da Arrays immer bei 0 beginnen, müssen wir bei der Eingabe
        //      1 abziehen (der Benutzer gibt z.B. 1 ein ->
        //      wir benötigen dann 0)
        return value-1;
    }
    public static GameStatus checkIfGameIsOver(char player){
        GameStatus playerStatus = player == 'x' ? GameStatus.Spieler1HatGewonnen : GameStatus.Spieler2HatGewonnen;
        // 3 gleiche Zeichen in einer der 3 Zeilen
        for(int i=0; i<3;i++){
            if( checkIfRowFull(i, player)) {
                return playerStatus;
            }
        }
        // 3 gleiche zeichen in einer der 3 Spalten
        for(int i=0; i<3;i++){
            if( checkIfRowFull(i, player)) {
                return playerStatus;
            }
        }
        // 3 Zeichen in einer Diagonale
        if(checkIfDiagFull(player)){
            return playerStatus;
        }

        // alle Felder sind belegt
        if(checkIfAllFieldsAreOccupied()){
            return GameStatus.KeinerHatGewonnen;
        }
        return GameStatus.SpielNochNichtFertig;
    }
    public static boolean checkIfRowFull(int row, char player) {
        if ((tictactoe[row][0] == player) && (tictactoe[row][1] == player) && (tictactoe[row][2] == player)) {
            return true;
        }
        return false;
    }
    public static boolean checkIfColumnFull(int col, char player) {
        if ((tictactoe[0][col] == player) && (tictactoe[1][col] == player) && (tictactoe[2][col] == player)) {
            return true;
        }
        return false;
    }
    public static boolean checkIfDiagFull(char player) {
        // Diag. von li. oben nach re. unten
        if ((tictactoe[0][0] == player) && (tictactoe[1][1] == player) && (tictactoe[3][3] == player)){
            return true;
        }
        // diag. von re. oben nach li. unten
        if ((tictactoe[0][2] == player) && (tictactoe[1][1] == player) && (tictactoe[2][0] == player)){
            return true;
        }
        return false;
    }
    public static boolean checkIfAllFieldsAreOccupied(){
        // zumindest ein Feld ist noch frei
        for(int row=0; row<3; row++){
            for(int col=0; col<3; col++){
                if(tictactoe[row][col] == '-'){
                    return false;
                }
            }
        }
        return true;

    }
    public static void showGameStatus(GameStatus gameStatus){
        if(gameStatus == GameStatus.Spieler1HatGewonnen){
            System.out.println("Spieler 1 hat gewonnen!");
        }
        else if(gameStatus == GameStatus.Spieler2HatGewonnen){
            System.out.println("Spieler 2 hat gewonnen!");
        }
        else if (gameStatus == GameStatus.KeinerHatGewonnen){
            System.out.println("Keiner hat gewonnen!");

        }
    }



}