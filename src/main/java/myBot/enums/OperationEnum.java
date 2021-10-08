package myBot.enums;

import java.util.Arrays;
import java.util.List;

public enum OperationEnum {
    ADDITION,
    SUBTRACTION,
    MULTIPLICATION,
    DIVISION;

    public static List<OperationEnum> getPlusMinus() {
        return Arrays.asList(ADDITION, SUBTRACTION);
    }

    public static List<OperationEnum> getMultiplicationDivision() {
        return Arrays.asList(MULTIPLICATION, DIVISION);
    }

    public static List<OperationEnum> getAllOperations() {
        return Arrays.asList(values());
    }
}
