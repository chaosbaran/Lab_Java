import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

// Lớp đại diện cho một trạm phát sóng (Điểm tọa độ 2D)
class Diem implements Comparable<Diem> {
    int x, y;

    public Diem(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // Sắp xếp ưu tiên trục x trước, nếu x bằng nhau thì xét trục y
    @Override
    public int compareTo(Diem khac) {
        if (this.x == khac.x) {
            return Integer.compare(this.y, khac.y);
        }
        return Integer.compare(this.x, khac.x);
    }
}

public class bai3 {

    // Hàm tính tích có hướng của 2 vector OA và OB
    public static long tichCoHuong(Diem O, Diem A, Diem B) {
        return (long) (A.x - O.x) * (B.y - O.y) - (long) (A.y - O.y) * (B.x - O.x);
    }

    // Hàm tìm Bao Lồi bằng thuật toán Monotone Chain
    public static List<Diem> timBaoLoi(List<Diem> danhSachDiem) {
        int soLuongTram = danhSachDiem.size();
        
        // Nếu số trạm ít hơn hoặc bằng 3, tất cả đều là trạm cảnh báo
        if (soLuongTram <= 3) return new ArrayList<>(danhSachDiem);

        // 1. Sắp xếp các điểm từ trái sang phải
        Collections.sort(danhSachDiem);

        List<Diem> baoLoi = new ArrayList<>();

        // 2. Xây dựng nửa đường bao phía dưới
        for (Diem diem : danhSachDiem) {
            // Nếu tạo thành góc rẽ phải thì loại bỏ điểm trước đó
            while (baoLoi.size() >= 2 && tichCoHuong(baoLoi.get(baoLoi.size() - 2), baoLoi.get(baoLoi.size() - 1), diem) <= 0) {
                baoLoi.remove(baoLoi.size() - 1);
            }
            baoLoi.add(diem);
        }

        // 3. Xây dựng nửa đường bao phía trên
        int kichThuocBaoDuoi = baoLoi.size();
        for (int i = soLuongTram - 2; i >= 0; i--) {
            Diem diem = danhSachDiem.get(i);
            // Tương tự, nếu tạo thành góc rẽ phải thì loại bỏ
            while (baoLoi.size() > kichThuocBaoDuoi && tichCoHuong(baoLoi.get(baoLoi.size() - 2), baoLoi.get(baoLoi.size() - 1), diem) <= 0) {
                baoLoi.remove(baoLoi.size() - 1);
            }
            baoLoi.add(diem);
        }

        // Loại bỏ điểm cuối cùng vì nó bị trùng với điểm xuất phát
        baoLoi.remove(baoLoi.size() - 1);

        return baoLoi;
    }

    public static void main(String[] args) {
        Scanner docDuLieu = new Scanner(System.in);

        // Đọc tổng số lượng trạm từ bàn phím
        if (docDuLieu.hasNextInt()) {
            int tongSoTram = docDuLieu.nextInt();
            List<Diem> cacTram = new ArrayList<>();

            // Đọc tọa độ x, y của từng trạm
            for (int i = 0; i < tongSoTram; i++) {
                int x = docDuLieu.nextInt();
                int y = docDuLieu.nextInt();
                cacTram.add(new Diem(x, y));
            }

            // Gọi hàm xử lý để tìm ra các trạm nằm ở lớp ngoài cùng
            List<Diem> cacTramCanhBao = timBaoLoi(cacTram);

            // In kết quả
            System.out.println("Các trạm cảnh báo:");
            System.out.println(cacTramCanhBao.get(0).x + " " + cacTramCanhBao.get(0).y);
            for (int i = cacTramCanhBao.size() - 1; i > 0; i--) {
                System.out.println(cacTramCanhBao.get(i).x + " " + cacTramCanhBao.get(i).y);
            }
        }

        docDuLieu.close();
    }
}