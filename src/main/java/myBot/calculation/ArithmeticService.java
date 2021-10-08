package myBot.calculation;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import myBot.enums.OperationEnum;
import myBot.fileProcessor.WordFileProcessorImpl;
import myBot.telegram.nonCommand.Settings;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ArithmeticService {
    WordFileProcessorImpl fileProcessor;
    Calculator calculator;

    public FileInputStream getFile(List<OperationEnum> operations, Settings settings) throws IOException {
        List<String> taskList = new ArrayList<>();
        for (int i = 1; i < settings.getListCount(); i++) {
            taskList.addAll(getTaskList(operations, settings));
        }

        if (taskList.isEmpty()) {
            throw new IllegalArgumentException(String.format("По непонятным причинам по заданным настройкам " +
                    "(min = %s, max = %s, listCount = %s) не удалось создать ни одной строки " +
                    "с задасами...(", settings.getMin(), settings.getMax(), settings.getListCount()));
        }
        return fileProcessor.createWordFile(taskList);
    }

    private List<String> getTaskList(List<OperationEnum> operations, Settings settings) {
        int taskCount = getOperationTaskCount(operations.size());
        List<String> taskList = new ArrayList<>();
        for (OperationEnum operationEnum : operations) {
            settings = getFixedSettings(operationEnum, settings);
            fillTaskList(taskList, operationEnum, taskCount, settings.getMin(), settings.getMax(),
                    getActualUniqueTaskCount(operationEnum, settings));
        }
        Collections.shuffle(taskList);
        return taskList;
    }

    private int getOperationTaskCount(int operationsCount) {
        int linesCount = 52;
        switch (operationsCount) {
            case 1:
                return linesCount;
            case 2:
                return linesCount / 2;
            case 3:
                return linesCount / 3;
            case 4:
                return linesCount / 4;
            default:
                throw new IllegalArgumentException(String.format("Недопустимое количество операций для формирования " +
                        "файла с заданиями - %s", operationsCount));
        }
    }

    private Settings getFixedSettings(OperationEnum operation, Settings settings) {
        if (OperationEnum.getPlusMinus().contains(operation) && settings.getPlusMinusUniqueTaskCount() == 0) {
            return new Settings(1, settings.getMax(), settings.getListCount());
        }
        return settings;
    }

    private int getActualUniqueTaskCount(OperationEnum operation, Settings settings) {
        switch (operation) {
            case MULTIPLICATION:
                return settings.getMultiplicationUniqueTaskCount();
            case DIVISION:
                return settings.getDivisionUniqueTaskCount();
            case ADDITION:
            case SUBTRACTION:
                return settings.getPlusMinusUniqueTaskCount();
        }
        return 0;
    }

    private void fillTaskList(List<String> taskList, OperationEnum operation, int taskCount, int min, int max, int uniqueTaskCount) {
        if (taskCount <= uniqueTaskCount) {
            taskList.addAll(calculator.getTaskSet(operation, min, max, taskCount));
        } else {
            taskList.addAll(calculator.getTaskSet(operation, min, max, uniqueTaskCount));
            int remainingNumbers = taskCount - uniqueTaskCount;
            fillTaskList(taskList, operation, remainingNumbers, min, max, uniqueTaskCount);
        }
    }
}
