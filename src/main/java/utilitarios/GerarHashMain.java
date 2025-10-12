package utilitarios;

public class GerarHashMain {
    public static void main(String[] args) {
        String senha = "joao1808";  // MUDE PARA SUA SENHA
        String hash = SenhaUtil.gerarHash(senha);

        System.out.println("========================================");
        System.out.println("Senha: " + senha);
        System.out.println("Hash gerado:");
        System.out.println(hash);
        System.out.println("========================================");
        System.out.println("Copie esse hash para inserir no banco de dados");
        System.out.println("========================================");
    }
}