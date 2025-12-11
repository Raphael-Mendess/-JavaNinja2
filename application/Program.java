package application;

import db.DB;
import entities.Employee;
import entities.User;
import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class Program {

    private static final String path = "C:\\Users\\Pichau\\email.txt";

    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        Scanner scanner = new Scanner(System.in);

        List<Employee> list = new ArrayList<>();
        Date relogio = new Date();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Exibe horário atual
        System.out.println("\u001B[33mAtual Hour\u001B[0m");
        System.out.println(relogio.toString());
        System.out.println();

        // Email principal
        System.out.print("\u001B[33mEnter your email:\u001B[0m");
        String email = scanner.nextLine();
        salvarTexto("Main Email: " + email, 2);
        abrirArquivo();

        // Saldo da conta
        System.out.print("\u001B[33mEnter the account balance R$:\u001B[0m");
        double balance = scanner.nextDouble();

        // Efeito de contagem
        for (int i = 1; i <= balance; i++) {
            System.out.println(i);
            try {
                Thread.sleep(0, 2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // Registro de usuário
        salvarTexto("Registered user", 1);
        salvarTexto("Email: " + email, 1);
        salvarTexto("Balance account: " + balance, 2);
        abrirArquivo();

        System.out.print("Enter the deposit date (dd/MM/yyyy): ");
        LocalDate birthDate = LocalDate.parse(scanner.next(), fmt);

        User user = new User(email, balance, birthDate);

        // Cadastro de funcionários
        System.out.println();
        System.out.print("\u001B[33mDeseja gerar uma lista com as informaçõs dos funcionarios ? (s/n)\u001B[0m");
        char ch = scanner.next().charAt(0);
        scanner.nextLine();

        if (ch == 's') {
            System.out.print("\u001B[33mDigite o número de funcionários:\u001B[0m ");
            int number = scanner.nextInt();

            for (int i = 0; i < number; i++) {
                System.out.println();
                System.out.println(" | Employee #" + (i + 1) + " |");

                System.out.print("Name: ");
                scanner.nextLine();
                String name = scanner.nextLine();

                salvarTexto("------- // -------", 1);
                salvarTexto("   Employees   ", 1);
                salvarTexto("Name: " + name, 1);

                System.out.print("Salary: ");
                double salary = scanner.nextDouble();
                salvarTexto("Salary: " + salary, 1);

                Employee employee = new Employee(name, salary);

                Connection(name, salary );

                // Animação de atualização
                double novoSaldo = balance - salary;
                if (balance > 0) {
                    for (int j = (int) balance; j >= (int) novoSaldo; j--) {
                        System.out.println(j);
                        try {
                            Thread.sleep(0, 2);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    balance = novoSaldo;

                    salvarTexto("Updated account balance: " + balance, 2);
                    abrirArquivo();
                }
            }
        }
        // Calculadora de investimento
        Calculadora();

        // Abre arquivo no fim
        abrirArquivo();
        scanner.close();

    }  

    // ------------ MÉTODOS -------------
    public static void Calculadora() {
        Scanner sc = new Scanner(System.in);

        System.out.print("Informe o valor investido a cada duas semanas: R$ ");
        double valor = sc.nextDouble();

        double selic = valor * 0.15;

        salvarTexto("Taxa Selic com 15% ao ano: " + selic, 1);
    }

    public static void salvarTexto(String texto, int linhasEmBranco) {
        try {
            File file = new File(path);
            boolean arquivoVazio = file.length() == 0;

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
                bw.write(texto);
                for (int i = 0; i < linhasEmBranco; i++) {
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar: " + e.getMessage());
        }
    }

    public static void abrirArquivo() {
        try {
            Desktop.getDesktop().open(new File(path));
        } catch (Exception e) {
            System.out.println("Não foi possível abrir o arquivo.");
        }
    }          

    public static void Connection(String name, double salary) { 
        Connection conn = null;
        PreparedStatement st = null;

        try {
            conn = DB.getConnection();

            st = conn.prepareStatement(
                "INSERT INTO employees "
                + "(name, salary) "
                + "VALUES "
                + "(?, ?)",
                Statement.RETURN_GENERATED_KEYS);         

            st.setString(1, name);
            st.setDouble(2, salary);    
            
            int rowsAffected = st.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }     
        finally {
            DB.closeStatement(st);
            DB.closeConnection();
        }    
    }    
}
 