**Project Structure**

- **Repository root**: ไฟล์สคริปต์รันและเอกสารโครงการ
   - [run.sh](run.sh) / `run.bat`: สคริปต์รันบน macOS/Linux และ Windows

- **Source trees**:
   - `src/` : โค้ดหลักของเกม
      - [src/Main.java](src/Main.java) : จุดเริ่มต้นของแอป (`main`) — สร้าง `GameModel` และ `GameFrame`
      - [src/GameModel.java](src/GameModel.java) : ตรรกะเกมและสถานะกระดาน (enum `GameMode`, `playerLevel` และเมธอดอัปเดตสถานะ)
      - [src/GameFrame.java](src/GameFrame.java) : หน้าต่างหลักของ GUI — จัดวาง `GamePanel` และเมนู
      - [src/GamePanel.java](src/GamePanel.java) : พาเนลหลักที่รวมองค์ประกอบของเกมเข้าด้วยกัน
      - [src/BoardPanel.java](src/BoardPanel.java) : พาเนลที่รับผิดชอบการแสดงกระดานไฟ (board)
      - [src/BulbView.java](src/BulbView.java) : วิดเจ็ต/คอมโพเนนต์แต่ละหลอดไฟ (สถานะ on/off)
      - [src/LeverView.java](src/LeverView.java) : มุมมอง/คอนโทรลสำหรับสลับสถานะ
      - [src/BackgroundPanel.java](src/BackgroundPanel.java) : พาเนลพื้นหลังสำหรับธีม/กราฟิก
      - [src/MenuPanel.java](src/MenuPanel.java) : เมนูสำหรับรีเซ็ต/ตั้งค่า/เลือกเลเวล
      - [src/Theme.java](src/Theme.java) : คอนฟิกสีหรือธีมของ UI
      - [src/assets/] : โฟลเดอร์เก็บไฟล์สื่อ (รูป ไอคอน ฯลฯ)

   - `src/gui/` : โค้ด GUI ที่แยกเป็นโมดูล/แพ็กเกจย่อย (UI helpers / alternative implementations)
      - [src/gui/LightsOutGUI.java](src/gui/LightsOutGUI.java) : คลาส GUI หลักในโฟลเดอร์ `gui`
      - [src/gui/BackgroundPanel.java](src/gui/BackgroundPanel.java) : เวอร์ชันของพาเนลพื้นหลังภายในแพ็กเกจ `gui`
      - [src/gui/ColorTheme.java](src/gui/ColorTheme.java) : โค้ดธีมสีสำหรับชุด GUI

- **build/**: ผลลัพธ์การคอมไพล์ (ไบนารี / class files)

**Key classes & responsibilities**
- **`Main`**: สร้างและเชื่อมต่อ `GameModel` กับหน้าต่าง (`GameFrame`) และเริ่ม UI thread
- **`GameModel`**: เก็บสถานะเกม (กระดาน, เลเวล, โหมด) พร้อมเมธอดสำหรับสลับสถานะและตรวจสอบชนะ
- **`GameFrame`**: จัดการหน้าต่างหลักและ layout — ผูก event ระหว่าง view กับ model
- **`GamePanel` / `BoardPanel`**: รับผิดชอบการจัดวางคอมโพเนนต์เกม ย่อยเป็น grid ของ `BulbView`
- **`BulbView`**: แสดงสถานะหลอดไฟเดี่ยว (click handler) และอัปเดตเมื่อ model เปลี่ยน
- **`LeverView`**: คอนโทรลสลับ/ปุ่มพิเศษ (หากมี)
- **`Theme` / `ColorTheme`**: ศูนย์กลางกำหนดสีและลุคของ UI เพื่อให้เปลี่ยนธีมได้ง่าย

**How to build & run (local / quick)**
- macOS / Linux (ใช้ `run.sh`):

```bash
./run.sh
```

- Windows (ใช้ `run.bat`):

```powershell
run.bat
```

- หรือ complie และ run แบบ manual ด้วย `javac` / `java` (จาก root ของโปรเจค):

```bash
javac -d build -sourcepath src $(find src -name "*.java")
java -cp build src.Main
```
