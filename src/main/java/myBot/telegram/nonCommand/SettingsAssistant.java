package myBot.telegram.nonCommand;

import myBot.exceptions.IllegalSettingsException;

public class SettingsAssistant {

    static int calculateMin(int min, int max) {
        if (min == max) {
            throw new IllegalSettingsException("\uD83D\uDCA9 Совпадение минимального и максимального значений " +
                    "недопустимо");
        }
        return Math.min(min, max);
    }

    static int calculateMax(int min, int max) {
        return Math.max(min, max);
    }

    static int calculateListCount(int listCount) {
        return Math.min(listCount, 10);
    }

    static int calculatePlusMinusUniqueTaskCount(int min, int max) {
        if (max - 2 * min + 1 >= 0) {
            return ((max - 2 * min + 2) * (max - 2 * min + 1)) / 2;
        }
        return 0;
    }

    static int calculateMultiplicationUniqueTaskCount(int min, int max) {
        if (max < 10) {
            return (((max - min + 1) * 10) * 2 - (max - min + 1)) / 2;
        } else {
            return (((max - min + 1) * max) * 2 - (max - min + 1)) / 2;
        }
    }

    static int calculateDivisionUniqueTaskCount(int min, int max) {
        if (max < 10) {
            return (((max - min + 1) * 10) - (max - min + 1)) / 2;
        } else {
            return (((max - min + 1) * max) - (max - min + 1)) / 2;
        }
    }
}
