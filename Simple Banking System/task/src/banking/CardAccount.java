package banking;

import java.util.Random;

public class CardAccount {
    private Random random = new Random();
    private StringBuilder cardNumber = new StringBuilder("400000");
    private String cardPin;
    private double cardBalance;

    public CardAccount() {
        cardPin = String.format("%04d", random.nextInt(10000));

        cardNumber = generateCard(cardNumber);

        while (SQLiteDatabase.isCardExists(cardNumber.toString())) { //checks for card duplicates in the database
            cardNumber = generateCard(cardNumber);
        }

        cardBalance = 0;
    }

    public StringBuilder generateCard(StringBuilder cardNumber) {
        for (int i = 0; i < 9; i++) {
            int randomDigit = random.nextInt(10);
            cardNumber.append(randomDigit);
        }
        return cardNumber.append(generateCheckSumByLuhnAlgorithm(cardNumber)); //adds the last digit as CheckSum generated by Luhn's algorithm
    }

    public static int generateCheckSumByLuhnAlgorithm(StringBuilder cardNumber) {
        int sum = 0;

        //calculate checkSum (16th digit) by
        // 1) Multiply odd digits by 2;
        // 2) Subtract 9 to numbers over 9;
        // 3) Find _Sum_ of all numbers;
        // 4) _Sum_ + checkSum should be dividable by 10;
        for (int i = 0; i < cardNumber.length(); i++) {
            int x = cardNumber.charAt(i) - '0';
            if (x < 0 || x > 9) {
                return -1;
            }
            if (i % 2 == 0) {
                x *= 2;
                if (x > 9) {
                    x -= 9;
                }
            }
            sum += x;
        }
        return (10 - sum % 10) % 10;
    }

    public String getCardNumber() {
        return cardNumber.toString();
    }

    public String getCardPin() {
        return cardPin;
    }
}