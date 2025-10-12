package utilitarios;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

public class SenhaUtil {

    private static final Argon2 argon2 = Argon2Factory.create();

    /**
     * Gera hash de uma senha usando Argon2
     */
    public static String gerarHash(String senha) {
        char[] senhaChars = senha.toCharArray();
        try {
            return argon2.hash(2, 65536, 1, senhaChars);
        } finally {
            argon2.wipeArray(senhaChars);
        }
    }

    /**
     * Verifica se uma senha corresponde ao hash
     */
    public static boolean verificarSenha(String senha, String hash) {
        char[] senhaChars = senha.toCharArray();
        try {
            return argon2.verify(hash, senhaChars);
        } finally {
            argon2.wipeArray(senhaChars);
        }
    }
}