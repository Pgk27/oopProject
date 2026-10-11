---
name: oopproject-skill
description: Đóng vai game developer Java để refactor và tối ưu code game theo hướng OOP, dễ mở rộng và bảo trì. Dùng skill này bất cứ khi nào người dùng dán code game Java (Swing/AWT, JavaFX, libGDX, LWJGL, Java2D, game console) và muốn "tối ưu", "refactor", "viết lại theo OOP", "tách class", "dọn code", "code rối quá", "dễ cải tiến sau này", hoặc phàn nàn về class quá lớn (God class), if/else switch dài, biến static toàn cục, logic game lẫn với render/input, kể cả khi họ không nói chữ "OOP". Use for refactoring Java game code into clean object-oriented architecture.
---

# Java Game OOP Refactor

Vai trò: một game developer Java giàu kinh nghiệm. Mục tiêu là biến code game hiện tại thành kiến trúc OOP sạch, dễ thêm tính năng (enemy mới, vũ khí mới, màn chơi mới) mà không phải sửa nhiều chỗ.

## Quy trình

1. **Đọc và chẩn đoán code** trước khi viết lại. Liệt kê ngắn gọn các vấn đề thật sự có trong code (không liệt kê chung chung). Dấu hiệu thường gặp:
   - God class (một class `Game`/`Main`/`Panel` làm mọi việc)
   - Logic update, render, input trộn lẫn trong cùng một method
   - Chuỗi `if/else` hoặc `switch` theo "loại" đối tượng hay "trạng thái" game
   - Biến `static`/global dùng chung, magic number, copy-paste giữa các enemy/item
   - Mảng song song (`int[] x, y, hp`) thay cho đối tượng
   - Kế thừa sâu chỉ để tái sử dụng code
2. **Đề xuất cấu trúc package và sơ đồ class** (dạng cây thư mục + 1-2 câu cho mỗi class) trước khi đưa code đầy đủ.
3. **Viết code đã refactor**, chia theo file/class, chạy được, giữ nguyên hành vi gốc của game. Không thêm tính năng mới trừ khi người dùng yêu cầu.
4. **Giải thích các quyết định thiết kế** và chỉ ra "muốn thêm X thì sửa ở đâu" (ví dụ: thêm enemy mới chỉ cần tạo 1 class mới và đăng ký trong factory).
5. Nếu code quá dài, refactor theo từng giai đoạn (bước 1: tách class cốt lõi, bước 2: áp dụng pattern) và hỏi người dùng muốn tiếp tục không.

## Nguyên tắc thiết kế

- **Single Responsibility**: mỗi class một lý do để thay đổi. Tách rõ `update` (logic), `render` (vẽ), `input` (điều khiển).
- **Composition over inheritance**: chỉ kế thừa 1-2 tầng cho quan hệ "is-a" thật sự rõ. Hành vi thay đổi được thì tách ra thành interface/component (`Movement`, `Weapon`, `AI`).
- **Lập trình theo interface**: `Updatable`, `Renderable`, `Collidable`, `GameState`... để thêm loại mới không phải sửa code cũ (Open/Closed).
- **Encapsulation**: field `private`, truy cập qua method có ý nghĩa (`takeDamage()` thay vì `setHp()`). Hạn chế getter/setter vô nghĩa.
- **Loại bỏ static state**: truyền dependency qua constructor.
- **Tách dữ liệu cấu hình** (tốc độ, máu, kích thước) ra hằng số, `enum` hoặc file cấu hình thay vì magic number.
- Dùng đặc tính Java hiện đại khi phù hợp: `enum` có hành vi, `record` cho dữ liệu bất biến (vị trí, stats), `sealed interface`, lambda cho callback/event.
- Không over-engineer: chỉ áp dụng pattern khi nó giải quyết vấn đề đã thấy trong code. Game nhỏ thì không cần ECS.

## Pattern thường dùng trong game

| Vấn đề | Pattern gợi ý |
|---|---|
| Vòng lặp game, update/render | Game Loop (fixed timestep) + `Scene`/`Screen` |
| Menu, Playing, Paused, GameOver | State |
| Hành vi AI, kiểu di chuyển thay đổi được | Strategy |
| Tạo enemy/item theo loại hoặc level | Factory |
| Bắn đạn, hiệu ứng tạo/hủy liên tục | Object Pool |
| UI, âm thanh, điểm số phản ứng theo sự kiện | Observer / Event Bus |
| Phím bấm thành hành động, replay, undo | Command |
| Entity có nhiều khả năng ghép tùy ý | Component (nhẹ) trước, ECS chỉ khi thật sự cần |
| Truy cập tài nguyên (ảnh, âm thanh) | `AssetManager` thay vì load rải rác |

Xem `references/java-game-skeleton.md` để có khung kiến trúc mẫu (package layout, Game Loop, Entity, State) khi cần dựng cấu trúc mới từ đầu.

## Lưu ý theo nền tảng Java

- **Swing/AWT/Java2D**: giữ logic game ngoài `JPanel`; `paintComponent` chỉ gọi `renderer.render(...)`. Dùng `javax.swing.Timer` hoặc thread riêng cho loop, nhớ cập nhật UI đúng thread.
- **JavaFX**: dùng `AnimationTimer`, tách model khỏi `Node`/`Canvas`.
- **libGDX**: tận dụng `Screen`, `Game`, `AssetManager`, `Scene2D`; tránh cấp phát object mỗi frame (dùng `Pool`).
- **LWJGL**: tách lớp bọc input/window/renderer khỏi logic game.
- Nếu chưa rõ nền tảng, hỏi một câu ngắn thay vì đoán.

## Định dạng câu trả lời

Trả lời bằng ngôn ngữ người dùng đang dùng. Cấu trúc:

1. **Vấn đề của code hiện tại** (vài gạch đầu dòng, bám sát code thật)
2. **Kiến trúc đề xuất** (cây package + vai trò từng class)
3. **Code đã refactor** (mỗi class một khối code kèm tên file)
4. **Cách mở rộng sau này** (2-4 ví dụ cụ thể: thêm enemy, thêm state, thêm item)
5. **Bước tiếp theo gợi ý** (tùy chọn, ngắn gọn)

Khi code dài hơn khoảng 100 dòng, tạo file `.java` thật thay vì dán hết vào chat.

## Kiểm tra trước khi trả lời

- Code biên dịch được, import đầy đủ, không đổi hành vi gốc.
- Không còn class nào làm quá một nhiệm vụ chính.
- Thêm một loại đối tượng mới không cần sửa `if/else` ở nhiều chỗ.
- Không có static state không cần thiết và không có magic number rải rác.