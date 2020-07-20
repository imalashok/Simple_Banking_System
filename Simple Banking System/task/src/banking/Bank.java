package banking;

public class Bank {

    public static void showMainMenu() {
        System.out.println("\n1. Create an account" +
                "\n2. Log into account" +
                "\n0. Exit");
    }

    public static void showLoginMenu() {
        System.out.println("\n1. Balance" +
                "\n2. Add income" +
                "\n3. Do transfer" +
                "\n4. Close account" +
                "\n5. Log out" +
                "\n0. Exit");
    }

    public static void createAccount() {
        CardAccount cardAccount = new CardAccount();

        System.out.println("\nYour card has been created");
        System.out.println("Your card number:");
        System.out.println(cardAccount.getCardNumber());
        System.out.println("Your card PIN:");
        System.out.println(cardAccount.getCardPin());
        SQLiteDatabase.insertCard(cardAccount.getCardNumber(), cardAccount.getCardPin());
    }
}