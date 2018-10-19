package ru.vershinin.game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {

    private static List<String> words;
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(Game.class);
    private static final Random rand = new Random();

    /**
     * Returns a pseudo-random number between min and max, inclusive.
     * The difference between min and max can be at most
     * <code>Integer.MAX_VALUE - 1</code>.
     *
     * @param min Minimum value
     * @param max Maximum value.  Must be greater than min.
     * @return Integer between min and max, inclusive.
     * @see java.util.Random#nextInt(int)
     */
    private static int randInt(int min, int max) {
        return rand.nextInt((max - min) + 1) + min;
    }

    static {
        try {
            fillWords();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void printWords() {
        for (String item: words) {
            log.info(item);
        }
    }

    public static Game getNewGame() {
        return new Game();
    }


    private String secretWord;

    private int attemptNumber;

    {
        setSecretWord();
    }

    private static void fillWords() throws IOException {
        InputStream inputStream = Game.class.getClassLoader().getResourceAsStream("dictionary.txt");
        words = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                words.add(line);
            }
        }
    }

    private void setSecretWord() {

        int index = randInt(0, words.size() - 1);
        secretWord = words.get(index);
    }

    public int getSecretWordLength() {
        return secretWord.length();
    }

    public String getSecretWord() {
        return secretWord;
    }

    public AttempResult attempt(String word) throws WrongLengthExeption {

        if (secretWord.length() != word.length()) throw new WrongLengthExeption("Wrong word lenght");

        int bulls = 0;
        int cows = 0;

        for (int i = 0; i < word.length(); i++) {

            char chA = word.charAt(i);
            char chB = secretWord.charAt(i);

            if (chA == chB) bulls++;
            if (secretWord.indexOf(chA) != -1) cows++;

        }
        boolean result = (secretWord.length() == bulls);

        attemptNumber++;

        return new AttempResult(result, bulls, cows, attemptNumber);
    }
}

