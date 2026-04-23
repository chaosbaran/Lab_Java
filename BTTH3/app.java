import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class app extends JFrame {

    private DetailPanel leftDetailPanel;
    private JPanel rightListPanel;
    private List<Product> productList;

    public app() {
        setTitle("Giày Store");
        setSize(1200, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);

        // Khởi tạo dữ liệu mẫu
        initData();

        // Cài đặt Layout chính
        setLayout(new BorderLayout(15, 0));
        getRootPane().setBorder(new EmptyBorder(20, 20, 20, 20));

        // Khởi tạo Panel bên trái
        leftDetailPanel = new DetailPanel();
        leftDetailPanel.setPreferredSize(new Dimension(380, 0)); // Rộng hơn một chút
        add(leftDetailPanel, BorderLayout.WEST);

        // Khởi tạo Panel bên phải
        rightListPanel = new JPanel();
        rightListPanel.setLayout(new GridLayout(0, 4, 10, 10)); 
        rightListPanel.setBackground(Color.WHITE);

        for (Product p : productList) {
            rightListPanel.add(createProductCard(p));
        }

        JScrollPane scrollPane = new JScrollPane(rightListPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        // Hiển thị sản phẩm đầu tiên lên bên trái lúc mới mở app
        if (!productList.isEmpty()) {
            leftDetailPanel.setProduct(productList.get(0));
        }
    }

    // Khởi tạo dữ liệu
    private void initData() {
        productList = new ArrayList<>();
        productList.add(new Product("4DFWD PULSE SHOES", "$160.00", "Adidas", "This product is excluded from all promotional discounts and offers.", "img1.png"));
        productList.add(new Product("FORUM MID SHOES", "$100.00", "Adidas", "This product is excluded from all promotional discounts and offers.", "img2.png"));
        productList.add(new Product("SUPERNOVA SHOES", "$150.00", "Adidas", "This product is excluded from all promotional discounts and offers.", "img3.png"));
        productList.add(new Product("Adidas", "$120.00", "Adidas", "This product is excluded from all promotional discounts and offers.", "img4.png"));
        productList.add(new Product("Adidas", "$160.00", "Adidas", "This product is excluded from all promotional discounts and offers.", "img5.png"));
        productList.add(new Product("4DFWD PULSE SHOES", "$160.00", "Adidas", "This product is excluded from all promotional discounts and offers.", "img6.png"));
        productList.add(new Product("4DFWD PULSE SHOES", "$160.00", "Adidas", "This product is excluded from all promotional discounts and offers.", "img1.png"));
        productList.add(new Product("FORUM MID SHOES", "$100.00", "Adidas", "This product is excluded from all promotional discounts and offers.", "img2.png"));
    }

    // Tạo Card cho mỗi sản phẩm ở danh sách bên phải
    private JPanel createProductCard(Product p) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(new Color(245, 245, 245));
        
        // Viền mặc định
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(230, 230, 230), 1, true),
                new EmptyBorder(10, 10, 10, 10) 
        ));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Tiêu đề
        String shortTitle = p.title.length() > 16 ? p.title.substring(0, 16) + "..." : p.title;
        JLabel title = new JLabel(shortTitle);
        title.setFont(new Font("Arial", Font.BOLD, 15));
        title.setForeground(new Color(60, 60, 60));

        // Mô tả phụ
        String shortDesc = p.description.length() > 25 ? p.description.substring(0, 25) + "..." : p.description;
        JLabel desc = new JLabel(shortDesc);
        desc.setFont(new Font("Arial", Font.BOLD, 12)); 
        desc.setForeground(new Color(170, 170, 170));

        // Hình ảnh thumbnail
        JLabel imageLabel = new JLabel();
        imageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        imageLabel.setIcon(resizeImageKeepAspectRatio(p.imagePath, 190, 130)); 

        // Khung chứa Hãng và Giá ở dưới cùng
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(245, 245, 245));
        bottomPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        
        JLabel brand = new JLabel(p.brand);
        brand.setFont(new Font("Arial", Font.PLAIN, 12));
        brand.setForeground(new Color(100, 100, 100));

        JLabel price = new JLabel(p.price);
        price.setFont(new Font("Arial", Font.BOLD, 18));
        price.setForeground(new Color(80, 80, 80));

        bottomPanel.add(brand, BorderLayout.WEST);
        bottomPanel.add(price, BorderLayout.EAST);

        // Canh trái cho tất cả các thành phần
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        desc.setAlignmentX(Component.LEFT_ALIGNMENT);
        bottomPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Thêm vào card
        card.add(title);
        card.add(Box.createVerticalStrut(3));
        card.add(desc);
        card.add(Box.createVerticalStrut(10));
        card.add(imageLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(bottomPanel);

        // Sự kiện Hover và Click
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(new Color(100, 150, 255), 1, true), 
                        new EmptyBorder(10, 10, 10, 10)
                ));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(new Color(230, 230, 230), 1, true), 
                        new EmptyBorder(10, 10, 10, 10)
                ));
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                leftDetailPanel.setProduct(p);
            }
        });

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(Color.WHITE);
        wrapper.add(card, BorderLayout.NORTH);

        return wrapper;
    }

    // Hàm tiện ích để resize ảnh cho vừa khung
    private ImageIcon resizeImageKeepAspectRatio(String imagePath, int boxWidth, int boxHeight) {
        BufferedImage canvas = new BufferedImage(boxWidth, boxHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = canvas.createGraphics();
        
        try {
            File imgFile = new File(imagePath);
            if (imgFile.exists()) {
                BufferedImage originalImage = ImageIO.read(imgFile);
                int imgWidth = originalImage.getWidth();
                int imgHeight = originalImage.getHeight();

                // Tính toán tỷ lệ để thu phóng
                double ratio = Math.min((double) boxWidth / imgWidth, (double) boxHeight / imgHeight);
                int newWidth = Math.max(1, (int) (imgWidth * ratio));
                int newHeight = Math.max(1, (int) (imgHeight * ratio));

                Image resizedImg = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
                
                // Canh giữa ảnh trong khung Box
                int x = (boxWidth - newWidth) / 2;
                int y = (boxHeight - newHeight) / 2;
                g2.drawImage(resizedImg, x, y, null);
            } else {
                g2.setColor(new Color(245, 245, 245));
                g2.fillRect(0, 0, boxWidth, boxHeight);
            }
        } catch (Exception ex) {
        g2.setColor(new Color(245, 245, 245));
            g2.fillRect(0, 0, boxWidth, boxHeight);
        }
        g2.dispose();
        return new ImageIcon(canvas);
    }

    // Lớp panel bên trái (fade-in)
    class DetailPanel extends JPanel {
        private JLabel imgLabel;
        private JLabel titleLabel;
        private JLabel priceLabel;
        private JLabel brandLabel;
        private JLabel descLabel;

        private float alpha = 1.0f; // Độ mờ (0.0 là vô hình, 1.0 là rõ hoàn toàn)
        private Timer fadeTimer;

        public DetailPanel() {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBackground(Color.WHITE);
            setOpaque(true);
            setBorder(new EmptyBorder(10, 10, 10, 20));

            imgLabel = new JLabel();
            imgLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            titleLabel = new JLabel();
            titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
            titleLabel.setForeground(Color.DARK_GRAY);

            priceLabel = new JLabel();
            priceLabel.setFont(new Font("Arial", Font.BOLD, 20));
            priceLabel.setForeground(Color.DARK_GRAY);

            brandLabel = new JLabel();
            brandLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            brandLabel.setForeground(Color.GRAY);

            descLabel = new JLabel();
            descLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            descLabel.setForeground(Color.GRAY);

            add(Box.createVerticalStrut(20));
            add(imgLabel);
            add(Box.createVerticalStrut(30));
            
            JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);// Thêm một đường kẻ ngang nhỏ
            separator.setMaximumSize(new Dimension(400, 1));
            separator.setAlignmentX(Component.LEFT_ALIGNMENT);
            add(separator);
            add(Box.createVerticalStrut(20));
            
            add(titleLabel);
            add(Box.createVerticalStrut(10));
            add(priceLabel);
            add(Box.createVerticalStrut(10));
            add(brandLabel);
            add(Box.createVerticalStrut(10));
            add(descLabel);

            // Cài đặt Timer cho hiệu ứng chuyển đổi
            fadeTimer = new Timer(20, e -> {
                alpha += 0.08f; // Tăng dần độ mờ
                if (alpha >= 1.0f) {
                    alpha = 1.0f;
                    fadeTimer.stop();
                }
                repaint();
            });
        }

        // Hàm được gọi khi click vào item bên phải
        public void setProduct(Product p) {
            imgLabel.setIcon(resizeImageKeepAspectRatio(p.imagePath, 360, 250));
            titleLabel.setText(p.title);
            priceLabel.setText(p.price);
            brandLabel.setText(p.brand);
            descLabel.setText("<html><p style='width: 320px; line-height: 1.5;'>" + p.description + "</p></html>");

            // Kích hoạt hiệu ứng Fade-in
            alpha = 0.0f; 
            if (fadeTimer.isRunning()) {
                fadeTimer.stop();
            }
            fadeTimer.start();
        }

        // Vẽ offscreen trước, sau đó mới in
        @Override
        public void paint(Graphics g) {
            if (alpha >= 1.0f) {
                super.paint(g);
                return;
            }

            // Vẽ offscreen
            BufferedImage offscreen = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = offscreen.createGraphics();
            g2.setColor(Color.WHITE);
            g2.fillRect(0, 0, getWidth(), getHeight());
            super.paint(g2); 
            g2.dispose();

            // In ra
            Graphics2D gScreen = (Graphics2D) g.create();
            gScreen.setColor(Color.WHITE);
            gScreen.fillRect(0, 0, getWidth(), getHeight()); // Xóa sạch rác nền cũ
            gScreen.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            gScreen.drawImage(offscreen, 0, 0, null);
            gScreen.dispose();
        }
    }

    class Product {
        String title, price, brand, description, imagePath;
        public Product(String title, String price, String brand, String description, String imagePath) {
            this.title = title; this.price = price; this.brand = brand;
            this.description = description; this.imagePath = imagePath;
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {}

        SwingUtilities.invokeLater(() -> {
            new app().setVisible(true);
        });
    }
}
