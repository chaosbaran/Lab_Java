import java.util.Scanner;

public class bai1 {
    public static double dienTichXapXi(double r, int soLanThu) {
        int diemTrong = 0;

        for (int i = 0; i < soLanThu; i++) {
            double x = (Math.random() * 2 * r) - r;
            double y = (Math.random() * 2 * r) - r;

            // Kiểm tra xem điểm (x, y) có nằm trong hình tròn không
            if (x * x + y * y <= r * r) {
                diemTrong++;
            }
        }

        // Diện tích của hình vuông bao quanh hình tròn là (2r) * (2r) = 4 * r^2
        double dienTichHinhVuong = 4 * r * r;
        double dienTichXapXi = dienTichHinhVuong * ((double) diemTrong / soLanThu);

        return dienTichXapXi;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Bán kính r =  ");
        double r = scanner.nextDouble();
        int soLanThu = 1000000; // Số lần thử (1 triệu điểm để có độ chính xác tốt)

        double area = dienTichXapXi(r, soLanThu);
        
        System.out.println("Số lần thử ngẫu nhiên = " + soLanThu);
        System.out.println("Diện tích xấp xỉ của hình tròn = " + area);
        scanner.close();
    }
}