import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class BookStoreGUI extends JFrame {

  private static final int WINDOW_WIDTH = 800;
  private static final int WINDOW_LENGTH = 600;

  private JPanel mainPanel, booksPanel, cartPanel, searchPanel, titlePanel;
  private JLabel titleLabel, logoLabel, booksLabel, cartLabel;
  private JList<String> booksList;
  private JList<String> cartList;
  private JButton addButton, removeButton, checkoutButton, searchButton;
  private JTextField searchField;
  private BookInfo bookInfo;
  private String[] bookNames;
  private double[] prices;
  private DefaultListModel<String> cartModel;
  private int selectedIndex;
  private double total, subTotal;

  public BookStoreGUI() throws IOException {
    setTitle("ASTU E_BOOK");
    setLayout(new BorderLayout());
    setSize(WINDOW_WIDTH, WINDOW_LENGTH);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    bookInfo = new BookInfo();
    bookNames = bookInfo.getBookNames();
    prices = bookInfo.getBookPrices();

    buildMainPanel();
    add(mainPanel);

    setVisible(true);
  }

  private void buildMainPanel() {
    mainPanel = new JPanel(new GridBagLayout());
    mainPanel.setBackground(new Color(240, 248, 255));

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.BOTH;
    gbc.insets = new Insets(10, 10, 10, 10);

    buildTitlePanel();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 2;
    gbc.weightx = 1.0;
    gbc.weighty = 0.1;
    mainPanel.add(titlePanel, gbc);

    buildBooksPanel();
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.gridwidth = 1;
    gbc.weightx = 0.5;
    gbc.weighty = 0.8;
    mainPanel.add(booksPanel, gbc);

    buildCartPanel();
    gbc.gridx = 1;
    gbc.gridy = 1;
    mainPanel.add(cartPanel, gbc);

    buildSearchPanel();
    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.gridwidth = 2;
    gbc.weightx = 1.0;
    gbc.weighty = 0.1;
    mainPanel.add(searchPanel, gbc);
  }

  private void buildTitlePanel() {
    titlePanel= new JPanel(new BorderLayout());
    titlePanel.setBackground(new Color(70, 130, 180));

    titleLabel = new JLabel("ASTU E-BOOK STORE");
    titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
    titleLabel.setForeground(Color.WHITE);
    titleLabel.setHorizontalAlignment(JLabel.CENTER);
    titlePanel.add(titleLabel, BorderLayout.CENTER);
  }

  private void buildBooksPanel() {
    booksPanel = new JPanel(new BorderLayout());
    booksPanel.setBackground(Color.WHITE);

    booksLabel = new JLabel("Available Books");
    booksLabel.setFont(new Font("Arial", Font.BOLD, 18));
    booksLabel.setHorizontalAlignment(JLabel.CENTER);
    booksPanel.add(booksLabel, BorderLayout.NORTH);

    DefaultListModel<String> booksModel = new DefaultListModel<>();
    for (String book : bookNames) {
      booksModel.addElement(book);
    }

    booksList = new JList<>(booksModel);
    booksList.setFont(new Font("Arial", Font.PLAIN, 14));
    booksList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    booksList.addListSelectionListener(new ListSelectionListener() {
      @Override
      public void valueChanged(ListSelectionEvent e) {
        selectedIndex = booksList.getSelectedIndex();
      }
    });

    JScrollPane scrollPane = new JScrollPane(booksList);
    booksPanel.add(scrollPane, BorderLayout.CENTER);

    addButton = new JButton("Add to Cart");
    addButton.setFont(new Font("Arial", Font.PLAIN, 14));
    addButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        addToCart(selectedIndex);
      }
    });

    JPanel buttonPanel = new JPanel(new FlowLayout());
    buttonPanel.add(addButton);
    booksPanel.add(buttonPanel, BorderLayout.SOUTH);
  }

  private void buildCartPanel() {
    cartPanel = new JPanel(new BorderLayout());
    cartPanel.setBackground(Color.WHITE);

    cartLabel = new JLabel("Cart");
    cartLabel.setFont(new Font("Arial", Font.BOLD, 18));
    cartLabel.setHorizontalAlignment(JLabel.CENTER);
    cartPanel.add(cartLabel, BorderLayout.NORTH);

    cartModel = new DefaultListModel<>();
    cartList = new JList<>(cartModel);
    cartList.setFont(new Font("Arial", Font.PLAIN, 14));

    JScrollPane scrollPane = new JScrollPane(cartList);
    cartPanel.add(scrollPane, BorderLayout.CENTER);

    removeButton = new JButton("Remove from Cart");
    removeButton.setFont(new Font("Arial", Font.PLAIN, 14));
    removeButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        removeFromCart(cartList.getSelectedIndex());
      }
    });

    checkoutButton = new JButton("Checkout");
    checkoutButton.setFont(new Font("Arial", Font.PLAIN, 14));
    checkoutButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        checkout();
      }
    });

    JPanel buttonPanel = new JPanel(new GridLayout(2, 1));
    buttonPanel.add(removeButton);
    buttonPanel.add(checkoutButton);
    cartPanel.add(buttonPanel, BorderLayout.SOUTH);
  }

  private void buildSearchPanel() {
    searchPanel = new JPanel(new FlowLayout());
    searchPanel.setBackground(new Color(240, 248, 255));

    searchField = new JTextField(20);
    searchField.setFont(new Font("Arial", Font.PLAIN, 14));

    searchButton = new JButton("Search");
    searchButton.setFont(new Font("Arial", Font.PLAIN, 14));
    searchButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        searchBooks(searchField.getText());
      }


		private void searchBooks(String searchQuery) {
			DefaultListModel<String> booksModel = (DefaultListModel<String>) booksList.getModel();
			booksModel.clear();
		
			for (String book : bookNames) {
				if (book.toLowerCase().contains(searchQuery.toLowerCase())) {
					booksModel.addElement(book);
				}
			}
		
			// Automatically select the searched book if found
			if (booksModel.size() > 0) {
				booksList.setSelectedIndex(0);
				selectedIndex = 0;
			}
		}
	
    });

    searchPanel.add(searchField);
    searchPanel.add(searchButton);
  }

  private void addToCart(int index) {
    if (index >= 0 && index < bookNames.length) {
      String selectedBook = bookNames[index];
      cartModel.addElement(selectedBook);
      subTotal += prices[index];
      updateCartTotal();
    }
  }

  private void removeFromCart(int index) {
    if (index >= 0 && index < cartModel.size()) {
      String removedBook = cartModel.elementAt(index);
      cartModel.remove(index);
      subTotal -= prices[getIndexInBooksArray(removedBook)];
      updateCartTotal();
    }
  }

  private void checkout() {
    if (cartModel.isEmpty()) {
      JOptionPane.showMessageDialog(this, "Your cart is empty.",
          "Empty Cart", JOptionPane.INFORMATION_MESSAGE);
    } else {
      DecimalFormat df = new DecimalFormat("#.##");
      JOptionPane.showMessageDialog(this, "Subtotal: $" + df.format(subTotal) + "\n"
          + "Total: $" + df.format(total), "Checkout", JOptionPane.INFORMATION_MESSAGE);
    }
  }

  private int getIndexInBooksArray(String book) {
    for (int i = 0; i < bookNames.length; i++) {
      if (bookNames[i].equals(book)) {
        return i;
      }
    }
    return -1;
  }

  private void updateCartTotal() {
    total = subTotal;
    DecimalFormat df = new DecimalFormat("#.##");
    cartLabel.setText("Cart (Total: $" + df.format(total) + ")");
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        try {
          new BookStoreGUI();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    });
  }
}