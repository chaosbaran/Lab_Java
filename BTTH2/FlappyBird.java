import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class FlappyBird extends JPanel implements ActionListener, KeyListener {
    // Cửa sổ
    int boardWidth = 360;
    int boardHeight = 640;

    // Hình ảnh
    Image backgroundImg;
    Image birdImg;
    Image topPipeImg;
    Image bottomPipeImg;

    // Đối tượng Bird
    int birdX = boardWidth / 8;
    int birdY = boardHeight / 2;
    int birdWidth = 34;
    int birdHeight = 24;

    class Bird {
        int x = birdX, y = birdY;
        int width = birdWidth, height = birdHeight;
        Image img;
        Bird(Image img) { this.img = img; }
    }
    Bird bird;
    
    // Vật lý
    int velocityY = 0; // Tốc độ rơi
    int gravity = 1;   // Trọng lực

    // Đối tượng pipe
    int pipeX = boardWidth;
    int pipeY = 0;
    int pipeWidth = 64; 
    int pipeHeight = 512;
    int velocityX = -4; // Tốc độ ống di chuyển sang trái

    class Pipe {
        int x = pipeX, y = pipeY;
        int width = pipeWidth, height = pipeHeight;
        Image img;
        boolean passed = false; // Dùng cho tính điểm
        Pipe(Image img) { this.img = img; }
    }
    
    // Game loop
    ArrayList<Pipe> pipes;
    Random random = new Random();
    Timer gameLoop;
    Timer placePipesTimer;

    // Điểm và trang thái game
    boolean gameStarted = false;
    boolean gameOver = false;
    boolean readyToRestart = false;
    double score = 0;

    public FlappyBird() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.blue);
        setFocusable(true);
        addKeyListener(this);

        // Tải hình ảnh
        backgroundImg = new ImageIcon(getClass().getResource("/flappybirdbg.png")).getImage();
        birdImg = new ImageIcon(getClass().getResource("/flappybird.png")).getImage();
        topPipeImg = new ImageIcon(getClass().getResource("/toppipe.png")).getImage();
        bottomPipeImg = new ImageIcon(getClass().getResource("/bottompipe.png")).getImage();

        // Khởi tạo Bird và danh sách pipe
        bird = new Bird(birdImg);
        pipes = new ArrayList<Pipe>();

        // Timer sinh ống nước (độ dài random)
        placePipesTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placePipes();
            }
        });

        // Game loop
        gameLoop = new Timer(1000/60, this); // 60 fps
        gameLoop.start();
    }

    // Hàm sinh pipe random
    public void placePipes() {
        int randomPipeY = (int) (pipeY - pipeHeight/4 - Math.random()*(pipeHeight/2));
        int openingSpace = boardHeight/4; // Khoảng trống giữa 2 ống

        Pipe topPipe = new Pipe(topPipeImg);
        topPipe.y = randomPipeY;
        pipes.add(topPipe);

        Pipe bottomPipe = new Pipe(bottomPipeImg);
        bottomPipe.y = topPipe.y + pipeHeight + openingSpace;
        pipes.add(bottomPipe);
    }

    // Vẽ các thành phần lên màn hình
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        g.drawImage(backgroundImg, 0, 0, boardWidth, boardHeight, null); // Vẽ nền
        g.drawImage(bird.img, bird.x, bird.y, bird.width, bird.height, null); // Vẽ chim

        // Vẽ ống nước
        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);
        }

        // Vẽ điểm số và Game Over
        g.setColor(Color.white);
        // kiểm tra trang thái
        if (!gameStarted) {
            // Chưa bắt đầu
            g.setFont(new Font("Arial", Font.BOLD, 22));
            g.drawString("Nhấn Space/Enter để bắt đầu", 30, boardHeight / 2);
        } else {
            // Đã bắt đầu
            g.setFont(new Font("Arial", Font.PLAIN, 32));
            if (gameOver) {
                if (readyToRestart) {
                    // Hiện chữ Game Over
                    g.drawString("Game Over: " + String.valueOf((int)score), 10, 35);
                    g.setFont(new Font("Arial", Font.PLAIN, 20));
                    g.drawString("Nhấn Space/Enter để chơi lại", 10, 70);
                } else {
                    // Đang trong thời gian delay
                    g.drawString(String.valueOf((int)score), 10, 35);
                }
            } else {
                // Đang chơi bình thường
                g.drawString(String.valueOf((int)score), 10, 35);
            }
        }
    }

    // Xử lý logic mỗi khung hình
    public void move() {
        // Trọng lực chim rơi xuống
        velocityY += gravity;
        bird.y += velocityY;
        bird.y = Math.max(bird.y, 0);

        // Di chuyển ống và kiểm tra va chạm
        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            pipe.x += velocityX;

            // Tính điểm nếu vượt qua ống
            if (!pipe.passed && bird.x > pipe.x + pipe.width) {
                pipe.passed = true;
                score += 0.5; // 0.5 cho ống trên + 0.5 cho ống dưới = 1 điểm
            }

            // Va chạm
            if (collision(bird, pipe)) {
                triggerGameOver();
            }
        }

        // Rơi chạm đất
        if (bird.y > boardHeight) {
            triggerGameOver();
        }
    }

    // Kích hoạt Game Over
    public void triggerGameOver() {
        if (!gameOver) {
            gameOver = true;
            placePipesTimer.stop(); // Dừng sinh ống nước
            
            // Delay 1s
            Timer delayTimer = new Timer(1000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    readyToRestart = true;
                }
            });
            delayTimer.setRepeats(false);
            delayTimer.start();
        }
    }

    // Kiểm tra va chạm
    public boolean collision(Bird a, Pipe b) {
        return a.x < b.x + b.width &&   // Góc trái chim < Góc phải ống
               a.x + a.width > b.x &&   // Góc phải chim > Góc trái ống
               a.y < b.y + b.height &&  // Góc trên chim < Góc dưới ống
               a.y + a.height > b.y;    // Góc dưới chim > Góc trên ống
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Chỉ chạy logic vật lý nếu game đã bắt đầu và chưa chết
        if (gameStarted && !gameOver) {
            move();
        }
        repaint();
    }

    // Nhận phím Space/Enter và Restart game
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!gameStarted) {
                // Lần đầu tiên mở game
                gameStarted = true;
                placePipesTimer.start();
                velocityY = -9;
            } else if (!gameOver) {
                // Đang chơi bình thường
                velocityY = -9;
            } else if (gameOver && readyToRestart) {
                // Đã chết và đợi restart
                bird.y = birdY;
                velocityY = 0;
                pipes.clear();
                score = 0;
                gameOver = false;
                readyToRestart = false;
                placePipesTimer.start();
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Flappy Bird");
        frame.setSize(360, 640); // Kích thước 360x640
        frame.setLocationRelativeTo(null);
        frame.setResizable(false); // Không thể thay đổi kích thước
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        FlappyBird flappyBird = new FlappyBird();
        frame.add(flappyBird);
        frame.pack(); // Ép kích thước vừa với JPannel
        flappyBird.requestFocus();
        frame.setVisible(true);
    }
}