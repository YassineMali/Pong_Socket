
import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class PongPlayer extends JFrame {
    private static final int GAME_WIDTH = 1000;
    private static final int GAME_HEIGHT = (int) (GAME_WIDTH * (0.5555));
    private static final Dimension SCREEN_SIZE = new Dimension(GAME_WIDTH, GAME_HEIGHT);
    JLabel waitingLabel;
    Socket s;
    public PongPlayer() {
        setTitle("Pong Lobby");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(SCREEN_SIZE);
        setResizable(false);

        // Set background image
        ImageIcon backgroundImageIcon = new ImageIcon("pong.jpg");
        Image backgroundImage = resizeImage(backgroundImageIcon.getImage(), GAME_WIDTH, GAME_HEIGHT);
        ImageIcon resizedBackgroundIcon = new ImageIcon(backgroundImage);
        JLabel backgroundLabel = new JLabel(resizedBackgroundIcon);
        backgroundLabel.setBounds(0, 0, GAME_WIDTH, GAME_HEIGHT);

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(SCREEN_SIZE);
        layeredPane.add(backgroundLabel, JLayeredPane.DEFAULT_LAYER);

        JPanel mainPanel = new JPanel();
        mainPanel.setOpaque(false);
        mainPanel.setLayout(null);
        mainPanel.setBounds(0, 0, GAME_WIDTH, GAME_HEIGHT);

        JLabel titleLabel = new JLabel("Pong Game Lobby");
        titleLabel.setBounds(0, 100, GAME_WIDTH, 50);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        mainPanel.add(titleLabel);
        
        waitingLabel = new JLabel("Waiting for Player 2...");
        waitingLabel.setBounds(0, 160, GAME_WIDTH, 30);
        waitingLabel.setHorizontalAlignment(SwingConstants.CENTER);
        waitingLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        waitingLabel.setForeground(Color.WHITE);
        waitingLabel.setVisible(false);
        mainPanel.add(waitingLabel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.setBounds(0, GAME_HEIGHT / 2 - 50, GAME_WIDTH, 120);

        JButton playButton = createButton("Play");
        JButton exitButton = createButton("Exit");

        buttonPanel.add(playButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        buttonPanel.add(exitButton);

        mainPanel.add(buttonPanel);

        layeredPane.add(mainPanel, JLayeredPane.PALETTE_LAYER);
        add(layeredPane);
        pack();
        setLocationRelativeTo(null);
    }

    private JButton createButton(String text) {
        System.out.println("creat");
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFont(new Font("Arial", Font.BOLD, 24));
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(200, 60));
        button.setMaximumSize(button.getPreferredSize());
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setBackground(Color.DARK_GRAY);
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        if(waitingLabel.isVisible()){
            
        }
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (text.equals("Play")) {
                    Thread networkThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if(s==null) {
                                s = new Socket("127.0.0.1", 1234);
                                }
                                
                                waitingLabel.setVisible(true); // Moved here
                                button.setEnabled(false);
                                InputStream in = s.getInputStream();
                                DataInputStream datain = new DataInputStream(in);
        
                                if (datain.readInt() != 0) {
                                    dispose();
                                    new GameFrame(s);
                                }
                            } catch (IOException e1) {
                                dispose();
                                showServerDownPopup();
                            }
                        }
                    });
        
                    networkThread.start();
                } else if (text.equals("Exit")) {
                    // Handle exit button action
                    System.exit(0);
                }
            }
        });

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.GRAY);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.DARK_GRAY);
            }
        });

        return button;
    }

   
private void showServerDownPopup() {
    JPanel panel = new JPanel();
    panel.setLayout(new GridBagLayout());
    panel.setBackground(Color.BLACK);

    JLabel messageLabel = new JLabel("Server is down");
    messageLabel.setFont(new Font("Arial", Font.BOLD, 48));
    messageLabel.setForeground(Color.WHITE);

    JButton closeButton = new JButton("Close");
    closeButton.setFont(new Font("Arial", Font.BOLD, 36));
    closeButton.setForeground(Color.WHITE);
    closeButton.setBackground(Color.DARK_GRAY);
    closeButton.setFocusPainted(false);
    closeButton.setBorder(BorderFactory.createEmptyBorder(20, 60, 20, 60));

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.insets = new Insets(50, 50, 50, 50);
    gbc.anchor = GridBagConstraints.CENTER;
    gbc.fill = GridBagConstraints.NONE;

    panel.add(messageLabel, gbc);

    gbc.gridy = 1;
    gbc.insets = new Insets(0, 50, 50, 50);
    panel.add(closeButton, gbc);

    JDialog dialog = new JDialog(this, "Server Down", true);
    dialog.setContentPane(panel);
    dialog.pack();
    dialog.setSize(dialog.getWidth() * 2, dialog.getHeight() * 2);
    dialog.setLocationRelativeTo(this);
    closeButton.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseEntered(java.awt.event.MouseEvent evt) {
            closeButton.setBackground(Color.GRAY);
        }

        public void mouseExited(java.awt.event.MouseEvent evt) {
            closeButton.setBackground(Color.DARK_GRAY);
        }
    });

    closeButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    });

    dialog.setVisible(true);
}

    private Image resizeImage(Image image, int width, int height) {
        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resizedImage.createGraphics();
        g2d.drawImage(image, 0, 0, width, height, null);
        g2d.dispose();
        return resizedImage;
    }

    public void display() {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                setVisible(true);
                
            }
        });
    }

    public static void main(String[] args) {
        PongPlayer lobbyMenu = new PongPlayer();
        lobbyMenu.display();
    }
}
