import javax.swing.*;
import java.awt.*;
import java.io.*;

public class ToDo extends JFrame {
    private DefaultListModel<String> taskListModel;
    private JList<String> taskList;
    private JTextField taskInputField;
    private JButton addButton, deleteButton, saveButton, loadButton, markDoneButton;

    private static final String FILE_NAME = "tasks.txt";

    public ToDo() {
        // Frame setup
        setTitle("Advanced To-Do List");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Task list model + JList
        taskListModel = new DefaultListModel<>();
        taskList = new JList<>(taskListModel);
        taskList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(taskList);

        // Input field
        taskInputField = new JTextField();

        // Buttons
        addButton = new JButton("Add");
        deleteButton = new JButton("Delete");
        markDoneButton = new JButton("Mark Done");
        saveButton = new JButton("Save");
        loadButton = new JButton("Load");

        // Panel for input and buttons
        JPanel inputPanel = new JPanel(new BorderLayout(5, 5));
        inputPanel.add(taskInputField, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 5, 5, 5));
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(markDoneButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(loadButton);

        inputPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add components to frame
        add(scrollPane, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);

        // Listeners
        addButton.addActionListener(e -> addTask());
        deleteButton.addActionListener(e -> deleteTask());
        markDoneButton.addActionListener(e -> markTaskDone());
        saveButton.addActionListener(e -> saveTasks());
        loadButton.addActionListener(e -> loadTasks());

        // Allow pressing Enter in text field to add task
        taskInputField.addActionListener(e -> addTask());

        // Load tasks at startup
        loadTasks();
    }

    private void addTask() {
        String task = taskInputField.getText().trim();
        System.out.println("DEBUG: Add button clicked. Input = '" + task + "'");

        if (!task.isEmpty()) {
            taskListModel.addElement(task);
            taskInputField.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Enter a task!");
        }
    }

    private void deleteTask() {
        int index = taskList.getSelectedIndex();
        if (index != -1) {
            System.out.println("DEBUG: Deleting task at index " + index);
            taskListModel.remove(index);
        } else {
            JOptionPane.showMessageDialog(this, "Select a task to delete!");
        }
    }

    private void markTaskDone() {
        int index = taskList.getSelectedIndex();
        if (index != -1) {
            String task = taskListModel.get(index);
            if (!task.startsWith("[Done]")) {
                taskListModel.set(index, "[Done] " + task);
                System.out.println("DEBUG: Marked task as done: " + task);
            } else {
                JOptionPane.showMessageDialog(this, "Task already marked as done!");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Select a task to mark done!");
        }
    }

    private void saveTasks() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (int i = 0; i < taskListModel.size(); i++) {
                writer.write(taskListModel.get(i));
                writer.newLine();
            }
            JOptionPane.showMessageDialog(this, "Tasks saved successfully!");
            System.out.println("DEBUG: Tasks saved to " + FILE_NAME);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving tasks!");
        }
    }

    private void loadTasks() {
        taskListModel.clear();
        File file = new File(FILE_NAME);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    taskListModel.addElement(line);
                }
                System.out.println("DEBUG: Tasks loaded from " + FILE_NAME);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error loading tasks!");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ToDo app = new ToDo();   // ✅ Class name matches file name
            app.setVisible(true);
        });
    }
}
