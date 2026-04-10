import java.util.Scanner;

public class bai2 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Yêu cầu người dùng nhập số lượng điểm ngẫu nhiên
        System.out.print("Nhập số lượng điểm gieo (VD: 1000000 để có độ chính xác tốt): ");
        long soLanThu = scanner.nextLong();

        int diemTrong = 0;

        for (int i = 0; i < soLanThu; i++) {
            double x = (Math.random() * 2) - 1;
            double y = (Math.random() * 2) - 1;

            // Kiểm tra điểm có nằm trong đường tròn đơn vị không (x^2 + y^2 <= 1)
            if (x * x + y * y <= 1) {
                diemTrong++;
            }
        }

        // Tính toán giá trị xấp xỉ của PI
        double piXapXi = 4.0 * diemTrong / soLanThu;
        System.out.println("Giá trị xấp xỉ của PI = " + piXapXi);

        scanner.close();
    }
}