package com.example.task23_ya;

/**
 * @author darya
 * @version 1.0
 * @since 10/05/2026
 * Model class representing an expense item
 */
public class Expense {
    private String keyID;
    private String description;
    private double amount;
    private String category;
    private String date;

    /**
     * Default constructor for Expense.
     * <p>
     *
     */
    public Expense() {
    }

    /**
     * Constructor for Expense with all fields.
     * <p>
     *
     * @param keyID Unique identifier for the expense
     * @param description Description of the expense
     * @param amount Amount of the expense
     * @param category Category of the expense
     * @param date Date of the expense
     */
    public Expense(String keyID, String description, double amount, String category, String date) {
        this.keyID = keyID;
        this.description = description;
        this.amount = amount;
        this.category = category;
        this.date = date;
    }

    /**
     * Gets the key ID of the expense.
     * <p>
     *
     * @return The key ID
     */
    public String getKeyID() { return keyID; }

    /**
     * Sets the key ID of the expense.
     * <p>
     *
     * @param keyID The key ID to set
     */
    public void setKeyID(String keyID) { this.keyID = keyID; }

    /**
     * Gets the description of the expense.
     * <p>
     *
     * @return The description
     */
    public String getDescription() { return description; }

    /**
     * Sets the description of the expense.
     * <p>
     *
     * @param description The description to set
     */
    public void setDescription(String description) { this.description = description; }

    /**
     * Gets the amount of the expense.
     * <p>
     *
     * @return The amount
     */
    public double getAmount() { return amount; }

    /**
     * Sets the amount of the expense.
     * <p>
     *
     * @param amount The amount to set
     */
    public void setAmount(double amount) { this.amount = amount; }

    /**
     * Gets the category of the expense.
     * <p>
     *
     * @return The category
     */
    public String getCategory() { return category; }

    /**
     * Sets the category of the expense.
     * <p>
     *
     * @param category The category to set
     */
    public void setCategory(String category) { this.category = category; }

    /**
     * Gets the date of the expense.
     * <p>
     *
     * @return The date
     */
    public String getDate() { return date; }

    /**
     * Sets the date of the expense.
     * <p>
     *
     * @param date The date to set
     */
    public void setDate(String date) { this.date = date; }
}