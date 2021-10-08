package myBot.calculation;

import myBot.enums.OperationEnum;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Calculator {
    Random rnd = new Random();

    private int getRandomIntBetWeenRange(int min, int max) {
        return (int) (Math.random() * ((max - min) + 1)) + min;
    }

    public String getMultiplicationTask(int first, int second) {
        return rnd.nextBoolean() ?
                String.format("%s * %s = ", first, second) : String.format("%s * %s = ", second, first);
    }

    private void addTaskToSet(Set<String> tasks, OperationEnum operation, int min, int max) {
        int first;
        int second;

        switch (operation) {
            case MULTIPLICATION:
                first = getRandomIntBetWeenRange(min, max);
                second = getRandomIntBetWeenRange(2, Math.max(max, 10));
                tasks.add(getMultiplicationTask(first, second));
                break;
            case DIVISION:
                first = getRandomIntBetWeenRange(min, max);
                second = getRandomIntBetWeenRange(2, Math.max(max, 10));
                int multiplicationResult = first * second;
                tasks.add(String.format("%s : %s = ", multiplicationResult, first));
                break;
            case SUBTRACTION:
                first = getRandomIntBetWeenRange(min, max);
                second = getRandomIntBetWeenRange(min, max);
                int subtractionResult = first - second;
                tasks.add(String.format("%s - %s = ", first, second));
                break;
            case ADDITION:
                first = getRandomIntBetWeenRange(min, max);
                second = getRandomIntBetWeenRange(min, max);
                int additionResult = first + second;
                if (additionResult >= min && additionResult <= max)
                    tasks.add(String.format("%s + %s = ", first, second));
                break;
        }
    }

    Set<String> getTaskSet(OperationEnum operation, int min, int max, int count) {
        Set<String> tasks = new HashSet<>();
        while (tasks.size() < count) {
            addTaskToSet(tasks, operation, min, max);
        }
        return tasks;
    }
}
