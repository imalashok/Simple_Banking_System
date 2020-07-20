package banking;

import java.util.Scanner;

enum MenuState {
    MAIN, LOGIN
}

public class Main {
    static MenuState menuState = MenuState.MAIN;
    static String userCardNumber;
    static String userCardPin;
    static private String databaseName = "cardAccounts.db";


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        if (args.length == 2) {
            if ("-fileName".equals(args[0])) {
                databaseName = args[1];
            }
        }

        SQLiteDatabase.setDatabaseName(databaseName);
        SQLiteDatabase.createNewDatabase();
        SQLiteDatabase.createCardTable();

        while (true) {
            if (menuState == MenuState.MAIN) {
                Bank.showMainMenu();
                switch (scanner.nextLine()) {
                    case "1":
                        Bank.createAccount();
                        break;
                    case "2":
                        System.out.println("\nEnter your card number:");
                        userCardNumber = scanner.nextLine();
                        System.out.println("Enter your PIN:");
                        userCardPin = scanner.nextLine();
                        if (SQLiteDatabase.checkCardCredentials(userCardNumber, userCardPin)) {
                            System.out.println("\nYou have successfully logged in!");
                            menuState = MenuState.LOGIN;
                        } else {
                            System.out.println("\nWrong card number or PIN!");
                        }
                        //userCardAccount = Bank.logIntoAccount(userCardNumber, userCardPin);
                        //if (userCardAccount == null) {
                        //    System.out.println("\nWrong card number or PIN!");
                        //} else {
                        //    System.out.println("\nYou have successfully logged in!");
                        //    menuState = MenuState.LOGIN;
                        //}
                        break;
                    case "0":
                        System.out.println("\nBye!");
                        System.exit(0);
                    default:
                        System.out.println("\nWrong menu option!");
                        break;
                }
            } else if (menuState == MenuState.LOGIN) {
                Bank.showLoginMenu();
                switch (scanner.nextLine()) {
                    case "1":
                        System.out.println("\nBalance: " + SQLiteDatabase.checkBalance(userCardNumber, userCardPin));
                        break;
                    case "2":
                        System.out.println("\nEnter income:");
                        String inc = scanner.nextLine();
                        try {
                            int income = Integer.parseInt(inc);
                            if (income > 0) {
                                if (SQLiteDatabase.updateBalance(userCardNumber, income)) {
                                    System.out.println("Income was added!");
                                }
                            } else {
                                System.out.println("Wrong income number format. The value should be INTEGER > 0.");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Wrong income number format. The value should be INTEGER > 0.");
                        }
                        break;
                    case "3":
                        int checkSum = -1;
                        int controlCheckSum = -2;
                        System.out.println("\nTransfer");
                        System.out.println("Enter card number:");
                        String transferCardNumber = scanner.nextLine();
                        if (userCardNumber.equals(transferCardNumber)) {
                            System.out.println("You can't transfer money to the same account!");
                            break;
                        } else if (transferCardNumber.length() == 16) {
                            checkSum = transferCardNumber.charAt(transferCardNumber.length() - 1) - '0';
                        }
                        if (checkSum >= 0 && checkSum <= 9) {
                            StringBuilder transferCard = new StringBuilder(transferCardNumber);
                            controlCheckSum = CardAccount.generateCheckSumByLuhnAlgorithm(transferCard.deleteCharAt(transferCardNumber.length() - 1));
                        }
                        if (checkSum == controlCheckSum) {
                            if (SQLiteDatabase.isCardExists(transferCardNumber)) {
                                System.out.println("Enter how much money you want to transfer:");
                                try {
                                    String money = scanner.nextLine();
                                    int moneyTransfer = Integer.parseInt(money);
                                    if (moneyTransfer == 0) {
                                        System.out.println("Wrong transfer number format. The value should be INTEGER > 0.");
                                        break;
                                    } else if (moneyTransfer <= SQLiteDatabase.checkBalance(userCardNumber, userCardPin)) {
                                        if (SQLiteDatabase.updateBalance(userCardNumber, -moneyTransfer) &&
                                                SQLiteDatabase.updateBalance(transferCardNumber, moneyTransfer)) {
                                            System.out.println("Success!");
                                        } else {
                                            System.out.println("Transfer cannot be completed");
                                        }
                                    } else {
                                        System.out.println("Not enough money!");
                                    }
                                } catch (NumberFormatException e) {
                                    System.out.println("Wrong transfer number format. The value should be INTEGER > 0.");
                                }
                            } else {
                                System.out.println("Such a card does not exist.");
                            }
                        } else {
                            System.out.println("Probably you made mistake in the card number. Please try again!");
                        }
                        break;
                    case "4":
                        if (SQLiteDatabase.closeAccount(userCardNumber, userCardPin)) {
                            System.out.println("\nThe account has been closed!");
                            menuState = MenuState.MAIN;
                        } else {
                            System.out.println("\nAccount cannot be deleted.");
                        }
                        break;
                    case "5":
                        System.out.println("\nYou have successfully logged out!");
                        menuState = MenuState.MAIN;
                        break;
                    case "0":
                        System.out.println("\nBye!");
                        System.exit(0);
                    default:
                        System.out.println("\nWrong menu option!");
                        break;
                }
            }
        }
    }
}