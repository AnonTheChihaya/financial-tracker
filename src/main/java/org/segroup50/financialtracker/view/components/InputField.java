package org.segroup50.financialtracker.view.components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * A reusable input field component with label.
 * Can be used for both regular text input and password input.
 *
 * @author ZHANG Shuo
 * @version 1.2
 * @since 2025-04-08
 */
public class InputField extends JPanel {
    private final JLabel label;
    private final JTextField inputField;
    private String placeholder;
    private Color placeholderColor = Color.GRAY;
    private Color normalColor;
    private boolean readOnly = false;

    /**
     * Constructs a new InputField component.
     *
     * @param labelText The text to display as the field label
     * @param isPassword Whether this field should be a password field (true) or regular text field (false)
     */
    public InputField(String labelText, boolean isPassword) {
        this(labelText, isPassword, null);
    }

    /**
     * Constructs a new InputField component with placeholder.
     *
     * @param labelText The text to display as the field label
     * @param isPassword Whether this field should be a password field (true) or regular text field (false)
     * @param placeholder The placeholder text to display when the field is empty
     */
    public InputField(String labelText, boolean isPassword, String placeholder) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setAlignmentX(Component.LEFT_ALIGNMENT);

        // Create and configure label
        label = new JLabel(labelText);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setBorder(new EmptyBorder(0, 0, 5, 0)); // Bottom margin
        add(label);

        // Create input field based on type
        inputField = isPassword ? new JPasswordField() : new JTextField();
        inputField.setAlignmentX(Component.LEFT_ALIGNMENT);
        inputField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35)); // Set height to 35
        inputField.setPreferredSize(new Dimension(inputField.getPreferredSize().width, 35));


        // Save normal text color
        normalColor = inputField.getForeground();

        // Set up placeholder if provided
        if (placeholder != null) {
            this.placeholder = placeholder;
            inputField.setText(placeholder);
            inputField.setForeground(placeholderColor);

            inputField.addFocusListener(new FocusListener() {
                @Override
                public void focusGained(FocusEvent e) {
                    if (!readOnly && inputField.getText().equals(InputField.this.placeholder)) {
                        inputField.setText("");
                        inputField.setForeground(normalColor);
                        if (isPassword) {
                            ((JPasswordField) inputField).setEchoChar('*');
                        }
                    }
                }

                @Override
                public void focusLost(FocusEvent e) {
                    if (!readOnly && inputField.getText().isEmpty()) {
                        inputField.setText(InputField.this.placeholder);
                        inputField.setForeground(placeholderColor);
                        if (isPassword) {
                            ((JPasswordField) inputField).setEchoChar((char) 0);
                        }
                    }
                }
            });

            // Special handling for password fields to show placeholder
            if (isPassword) {
                ((JPasswordField) inputField).setEchoChar((char) 0);
            }
        }

        add(inputField);
    }

    /**
     * Sets whether the field is read-only (not editable).
     *
     * @param readOnly true to make the field read-only, false to make it editable
     */
    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
        inputField.setEditable(!readOnly);
        inputField.setFocusable(!readOnly);
    }

    /**
     * Checks if the field is read-only.
     *
     * @return true if the field is read-only, false otherwise
     */
    public boolean isReadOnly() {
        return readOnly;
    }

    /**
     * Gets the text from the input field.
     * Returns empty string if the current text is the placeholder.
     *
     * @return The text entered in the field (excluding placeholder)
     */
    public String getText() {
        if (placeholder != null && inputField.getText().equals(placeholder)) {
            return "";
        }
        return inputField.getText();
    }

    /**
     * Sets the text of the input field.
     * If setting to empty and there's a placeholder, shows the placeholder.
     *
     * @param text The text to set
     */
    public void setText(String text) {
        if (text == null || text.isEmpty()) {
            if (placeholder != null) {
                inputField.setText(placeholder);
                inputField.setForeground(placeholderColor);
                if (inputField instanceof JPasswordField) {
                    ((JPasswordField) inputField).setEchoChar((char) 0);
                }
            } else {
                inputField.setText("");
            }
        } else {
            inputField.setText(text);
            inputField.setForeground(normalColor);
            if (inputField instanceof JPasswordField) {
                ((JPasswordField) inputField).setEchoChar('*');
            }
        }
    }

    /**
     * Gets the underlying input field component.
     *
     * @return The JTextField or JPasswordField instance
     */
    public JTextField getInputField() {
        return inputField;
    }

    /**
     * Gets the label component.
     *
     * @return The JLabel instance
     */
    public JLabel getLabel() {
        return label;
    }

    /**
     * Sets the placeholder text for this input field.
     *
     * @param placeholder The placeholder text to display when the field is empty
     */
    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        if (inputField.getText().isEmpty()) {
            setText("");
        }
    }

    /**
     * Gets the current placeholder text.
     *
     * @return The placeholder text, or null if not set
     */
    public String getPlaceholder() {
        return placeholder;
    }

    /**
     * Sets the color for the placeholder text.
     *
     * @param color The color to use for placeholder text
     */
    public void setPlaceholderColor(Color color) {
        this.placeholderColor = color;
        if (placeholder != null && inputField.getText().equals(placeholder)) {
            inputField.setForeground(placeholderColor);
        }
    }

    /**
     * Gets the current placeholder color.
     *
     * @return The current placeholder color
     */
    public Color getPlaceholderColor() {
        return placeholderColor;
    }
}
