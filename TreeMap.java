import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

// Kelas dasar untuk data
class Person {
    private String name;
    private Date dateOfBirth;

    public Person(String name, Date dateOfBirth) {
        this.name = name.trim();
        this.dateOfBirth = dateOfBirth;
    }

    public String getName() {
        return name;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    @Override
    public String toString() {
        SimpleDateFormat displayFormat = new SimpleDateFormat("dd-MM-yyyy");
        return "Nama: " + name + ", Tanggal Lahir: " + displayFormat.format(dateOfBirth);
    }
}

// Kelas yang mewarisi dari Person
class Employee extends Person {
    private String employeeId;

    public Employee(String name, Date dateOfBirth, String employeeId) {
        super(name, dateOfBirth);
        this.employeeId = employeeId.trim();
    }

    public String getEmployeeId() {
        return employeeId;
    }

    @Override
    public String toString() {
        return super.toString() + ", ID Karyawan: " + formatEmployeeId(employeeId);
    }

    // Format ID Karyawan
    private String formatEmployeeId(String id) {
        return "P30" + String.format("%03d", Integer.parseInt(id));
    }
}

// Kelas utama
class Main {
    private static TreeMap<String, Employee> employeeMap = new TreeMap<>();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            // Menampilkan waktu dan tanggal saat ini dengan format yang diinginkan
            Date currentDate = new Date();
            SimpleDateFormat currentFormat = new SimpleDateFormat("E MMM dd yyyy HH:mm:ss");
            String currentDateString = String.format("Tanggal/Waktu Saat Ini: %s", currentFormat.format(currentDate));
            System.out.println("\nSistem Pendataan Karyawan PetaTiga");
            System.out.println(currentDateString);
            System.out.println("\nMenu:");
            System.out.println("1. Tambah Karyawan");
            System.out.println("2. Hapus Karyawan");
            System.out.println("3. Perbarui Karyawan");
            System.out.println("4. Cari Karyawan");
            System.out.println("5. Tampilkan Semua Karyawan");
            System.out.println("6. Keluar");
            System.out.print("Pilih Opsi: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Konsumsi newline

            switch (choice) {
                case 1:
                    addEmployee(scanner);
                    break;
                case 2:
                    removeEmployee(scanner);
                    break;
                case 3:
                    updateEmployee(scanner);
                    break;
                case 4:
                    searchEmployee(scanner);
                    break;
                case 5:
                    displayEmployees();
                    break;
                case 6:
                    System.out.println("Keluar...");
                    break;
                default:
                    System.out.println("Pilihan tidak valid! Silakan coba lagi.");
            }
        } while (choice != 6);

        scanner.close();
    }

    private static void addEmployee(Scanner scanner) {
        System.out.print("Masukkan Nama: ");
        String name = scanner.nextLine();
        // Mengubah nama menjadi format yang diinginkan (setiap awal kata kapital)
        name = capitalize(name);
        
        System.out.print("Masukkan Tanggal Lahir (DD-MM-YYYY): ");
        String dobInput = scanner.nextLine();
        
        try {
            Date dateOfBirth = validateDate(dobInput);
            System.out.print("Masukkan ID Karyawan (3 digit): ");
            String employeeId = scanner.nextLine();

            // Validasi ID Karyawan
            if (!employeeId.matches("\\d{3}")) {
                throw new IllegalArgumentException("ID Karyawan harus 3 digit.");
            }

            Employee employee = new Employee(name, dateOfBirth, employeeId);
            employeeMap.put(employeeId, employee);
            System.out.println("Karyawan berhasil ditambahkan!");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private static String capitalize(String str) {
        String[] words = str.split(" ");
 StringBuilder capitalized = new StringBuilder();
        for (String word : words) {
            if (word.length() > 0) {
                capitalized.append(Character.toUpperCase(word.charAt(0)))
                           .append(word.substring(1).toLowerCase())
                           .append(" ");
            }
        }
        return capitalized.toString().trim();
    }

    private static void removeEmployee(Scanner scanner) {
        System.out.print("Masukkan ID Karyawan untuk dihapus (3 digit): ");
        String employeeId = scanner.nextLine();

        if (employeeMap.remove(employeeId) != null) {
            System.out.println("Karyawan berhasil dihapus!");
        } else {
            System.out.println("ID Karyawan tidak ditemukan!");
        }
    }

    private static void updateEmployee(Scanner scanner) {
        System.out.print("Masukkan ID Karyawan untuk diperbarui (3 digit): ");
        String employeeId = scanner.nextLine();

        Employee employee = employeeMap.get(employeeId);
        if (employee != null) {
            System.out.print("Masukkan Nama baru: ");
            String name = scanner.nextLine();
            // Mengubah nama menjadi format yang diinginkan (setiap awal kata kapital)
            name = capitalize(name);
            
            System.out.print("Masukkan Tanggal Lahir baru (DD-MM-YYYY): ");
            String dobInput = scanner.nextLine();

            try {
                Date dateOfBirth = validateDate(dobInput);
                Employee updatedEmployee = new Employee(name, dateOfBirth, employeeId);
                employeeMap.put(employeeId, updatedEmployee);
                System.out.println("Data Karyawan berhasil diperbarui!");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("ID Karyawan tidak ditemukan!");
        }
    }

    private static void searchEmployee(Scanner scanner) {
        System.out.print("Masukkan ID Karyawan untuk dicari (3 digit): ");
        String employeeId = scanner.nextLine();

        Employee employee = employeeMap.get(employeeId);
        if (employee != null) {
            System.out.println("Karyawan ditemukan: " + employee);
        } else {
            System.out.println("ID Karyawan tidak ditemukan!");
        }
    }

    private static void displayEmployees() {
        if (employeeMap.isEmpty()) {
            System.out.println("Tidak ada karyawan untuk ditampilkan.");
        } else {
            System.out.println("Daftar Karyawan:");
            for (Map.Entry<String, Employee> entry : employeeMap.entrySet()) {
                System.out.println(entry.getValue());
            }
        }
    }

    private static Date validateDate(String dateInput) {
        try {
            String[] parts = dateInput.split("-");
            if (parts.length != 3) {
                throw new IllegalArgumentException("Format tanggal tidak valid. Harap gunakan DD-MM-YYYY.");
            }
            int day = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            int year = Integer.parseInt(parts[2]);

            if (month < 1 || month > 12) {
                throw new IllegalArgumentException("Bulan harus antara 1 dan 12.");
            }

            // Memeriksa hari yang valid dalam bulan
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month - 1, day);
            if (calendar.get(Calendar.YEAR) != year || calendar.get(Calendar.MONTH) + 1 != month || calendar.get(Calendar.DAY_OF_MONTH) != day) {
                throw new IllegalArgumentException("Tanggal tidak valid. Harap periksa lagi.");
            }

            Date date = dateFormat.parse(String.format("%02d-%02d-%04d", day, month, year));
            if (date.after(new Date())) {
                throw new IllegalArgumentException("Tanggal lahir tidak boleh di masa depan.");
            }
            return date;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Format tanggal tidak valid. Harap gunakan DD-MM-YYYY.");
        } catch (ParseException e) {
            throw new IllegalArgumentException("Format tanggal tidak valid. Harap gunakan DD-MM-YYYY.");
        }
    }
}