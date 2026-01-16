package com.ocosur.ocosystem.livechicken.expense;

public enum ExpenseCategoryCode {

    WATER(ExpenseType.GENERIC),
    ELECTRICITY(ExpenseType.GENERIC),
    RENT(ExpenseType.GENERIC),

    FOOD(ExpenseType.FOOD),
    FUEL(ExpenseType.FUEL),
    VEHICLE(ExpenseType.VEHICLE),
    OTHER(ExpenseType.GENERIC);

    private final ExpenseType type;

    ExpenseCategoryCode(ExpenseType type) {
        this.type = type;
    }

    public ExpenseType getType() {
        return type;
    }
}
