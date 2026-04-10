import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class bai4 {

    public static void main(String[] args) {
        Scanner docDuLieu = new Scanner(System.in);

        // Kiểm tra xem có dữ liệu nhập vào hay không
        if (docDuLieu.hasNextInt()) {
            // Nhập n (số lượng phần tử) và k (tổng mục tiêu)
            int n = docDuLieu.nextInt();
            int k = docDuLieu.nextInt();

            int[] mangSo = new int[n];
            // Nhập các phần tử của mảng
            for (int i = 0; i < n; i++) {
                mangSo[i] = docDuLieu.nextInt();
            }

            // Bảng quy hoạch động dp[i][j]
            int[][] dp = new int[n + 1][k + 1];

            // Khởi tạo bảng DP
            for (int i = 0; i <= n; i++) {
                for (int j = 0; j <= k; j++) {
                    dp[i][j] = -1; // -1 nghĩa là không thể tạo ra tổng j
                }
            }
            // Tổng bằng 0 thì luôn đạt được với 0 phần tử
            for (int i = 0; i <= n; i++) {
                dp[i][0] = 0;
            }

            // Bắt đầu tính toán Quy hoạch động
            for (int i = 1; i <= n; i++) {
                int giaTriHienTai = mangSo[i - 1];
                for (int j = 1; j <= k; j++) {
                    // Mặc định là không chọn phần tử hiện tại
                    dp[i][j] = dp[i - 1][j];

                    // Nếu có thể chọn phần tử hiện tại và nó mang lại dãy con dài hơn
                    if (j >= giaTriHienTai && dp[i - 1][j - giaTriHienTai] != -1) {
                        dp[i][j] = Math.max(dp[i][j], dp[i - 1][j - giaTriHienTai] + 1);
                    }
                }
            }

            // Nếu ô cuối cùng vẫn là -1 thì không có dãy nào có tổng bằng k
            if (dp[n][k] == -1) {
                System.out.println("Không có dãy con nào thoả mãn.");
            } else {
                // Truy vết để tìm ra các phần tử của dãy con
                List<Integer> ketQua = new ArrayList<>();
                int tongConLai = k;

                for (int i = n; i > 0 && tongConLai > 0; i--) {
                    // Nếu giá trị độ dài không đổi khi bỏ qua phần tử i, ta ưu tiên bỏ qua
                    if (dp[i][tongConLai] == dp[i - 1][tongConLai]) {
                        continue;
                    } else {
                        // Bắt buộc phải chọn phần tử này
                        ketQua.add(mangSo[i - 1]);
                        tongConLai -= mangSo[i - 1];
                    }
                }

                // Vì truy vết đi từ cuối lên nên ta cần đảo ngược list lại để in đúng thứ tự
                Collections.reverse(ketQua);

                // In kết quả
                for (int i = 0; i < ketQua.size(); i++) {
                    System.out.print(ketQua.get(i) + (i < ketQua.size() - 1 ? ", " : ""));
                }
                System.out.println();
            }
        }

        docDuLieu.close();
    }
}