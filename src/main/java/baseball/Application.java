package baseball;

import camp.nextstep.edu.missionutils.Console;
import camp.nextstep.edu.missionutils.Randoms;

import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.regex.Pattern;

public class Application {
    private static final int DIGITS = 3;
    private static final int RESTART = 1;
    private static final int QUIT = 2;
    private static final int MIN_DIGIT = 1;
    private static final int MAX_DIGIT = 9;
    private static final String START_MESSAGE = "숫자 야구 게임을 시작합니다.";
    private static final String INPUT_MESSAGE = "숫자를 입력해주세요 : ";
    private static final String GAME_OVER_MESSAGE = String.format("%d개의 숫자를 모두 맞히셨습니다! 게임 종료", DIGITS);
    private static final String RESTART_MESSAGE = String.format("게임을 새로 시작하려면 %d, 종료하려면 %d를 입력하세요.", RESTART, QUIT);

    private static final String GUESSED_NUMBER_REGEX = String.format("[%d-%d]{%d}", MIN_DIGIT, MAX_DIGIT, DIGITS);
    private static final String RESTART_NUMBER_REGEX = String.format("[%d%d]", RESTART, QUIT);


    public static void main(String[] args) {
        System.out.println(START_MESSAGE);
        playGame();
    }

    public static void playGame() {
        List<Integer> targetNumber = createTargetNumber();

        while (true) {
            System.out.print(INPUT_MESSAGE);

            String input = Console.readLine();
            validateGuessedNumber(input);
            List<Integer> guessedNumber = parseGuessedNumber(input);

            int numberOfStrike = calculateStrike(targetNumber, guessedNumber);
            int numberOfBall = calculateBall(targetNumber, guessedNumber);

            printResult(numberOfStrike, numberOfBall);

            if (numberOfStrike == DIGITS) {
                System.out.println(GAME_OVER_MESSAGE);
                break;
            }
        }
        System.out.println(RESTART_MESSAGE);

        String input = Console.readLine();
        validateRestartNumber(input);
        int restartNumber = Integer.parseInt(input);

        if (restartNumber == RESTART) {
            playGame();
        }
    }

    public static List<Integer> createTargetNumber() {
        ArrayList<Integer> targetNumber = new ArrayList<>();
        while (targetNumber.size() < DIGITS) {
            int digit = Randoms.pickNumberInRange(MIN_DIGIT, MAX_DIGIT);

            if (!targetNumber.contains(digit)) {
                targetNumber.add(digit);
            }
        }
        return targetNumber;
    }

    public static void validateGuessedNumber(String input) {
        if (!Pattern.matches(GUESSED_NUMBER_REGEX, input)) {
            throw new IllegalArgumentException();
        }
        HashSet<Character> hashSet = new HashSet<>();
        for (int i = 0; i < DIGITS; i++) {
            if (hashSet.contains(input.charAt(i))) {
                throw new IllegalArgumentException();
            }
            hashSet.add(input.charAt(i));
        }
    }

    public static List<Integer> parseGuessedNumber(String input) {
        ArrayList<Integer> guessedNumber = new ArrayList<>();
        for (int i = 0; i < DIGITS; i++) {
            guessedNumber.add(input.charAt(i) - '0');
        }
        return guessedNumber;
    }

    public static int calculateStrike(List<Integer> targetNumber, List<Integer> guessedNumber) {
        int numberOfStrike = 0;
        for (int i = 0; i < DIGITS; i++) {
            if (targetNumber.get(i).equals(guessedNumber.get(i))) {
                numberOfStrike++;
            }
        }
        return numberOfStrike;
    }

    public static int calculateBall(List<Integer> targetNumber, List<Integer> guessedNumber) {
        int numberOfBall = 0;
        if (targetNumber.get(0).equals(guessedNumber.get(1)) || targetNumber.get(0).equals(guessedNumber.get(2))) {
            numberOfBall++;
        }
        if (targetNumber.get(1).equals(guessedNumber.get(2)) || targetNumber.get(1).equals(guessedNumber.get(0))) {
            numberOfBall++;
        }
        if (targetNumber.get(2).equals(guessedNumber.get(0)) || targetNumber.get(2).equals(guessedNumber.get(1))) {
            numberOfBall++;
        }
        return numberOfBall;
    }

    public static void printResult(int numberOfStrike, int numberOfBall) {
        if (numberOfStrike == 0 && numberOfBall == 0) {
            System.out.println("낫싱");
            return;
        }
        if (numberOfStrike == 0) {
            System.out.println(numberOfBall + "볼");
            return;
        }
        if (numberOfBall == 0) {
            System.out.println(numberOfStrike + "스트라이크");
            return;
        }
        System.out.println(numberOfBall + "볼 " + numberOfStrike + "스트라이크");
    }

    public static void validateRestartNumber(String input) {
        if (!Pattern.matches(RESTART_NUMBER_REGEX, input)) {
            throw new IllegalArgumentException();
        }
    }
}