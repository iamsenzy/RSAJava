import java.math.BigInteger;
import java.util.Random;
import java.io.IOException;
import java.util.Scanner;

public class RSA {

    static Random rand = new Random();

    public static BigInteger N;
    public static  BigInteger PHI;
    public static BigInteger E;
    public static BigInteger D;

    public static BigInteger getRandom(BigInteger min, BigInteger max){

        BigInteger result;
        do {
            result = new BigInteger(max.bitLength(),15, rand);
        } while (result.compareTo(min) < 0 || result.compareTo(max) > 0);
        return result;
    }

    public static BigInteger getRandomPrim(){
        BigInteger result;
        while (true){
            result = getRandom(BigInteger.TWO,new BigInteger(1024, 15, new Random()));

            if(millerRabin(result)){
                return result;
            }
        }
    }

    public static Boolean millerRabin(BigInteger num){
        if (num.compareTo(BigInteger.ONE) == 0)
            return false;
        if (num.compareTo(new BigInteger("3")) < 0)
            return true;
        int s = 0;
        BigInteger d = num.subtract(BigInteger.ONE);
        while (d.mod(BigInteger.TWO).equals(BigInteger.ZERO)) {
            s++;
            d = d.divide(BigInteger.TWO);
        }
        for (int i = 0; i < s; i++) {
            BigInteger a = getRandom(BigInteger.TWO, num.subtract(BigInteger.ONE));
            BigInteger x = a.modPow(d, num);
            if (x.equals(BigInteger.ONE) || x.equals(num.subtract(BigInteger.ONE)))
                continue;
            int r = 0;
            for (; r < s; r++) {
                x = x.modPow(BigInteger.TWO, num);
                if (x.equals(BigInteger.ONE))
                    return false;
                if (x.equals(num.subtract(BigInteger.ONE)))
                    break;
            }
            if (r == s)
                return false;
        }
        return true;
    }


    public static BigInteger power(BigInteger a, BigInteger b, BigInteger mod){
        BigInteger result = BigInteger.ONE;
        while (b.compareTo(BigInteger.ZERO)>0){
            if (b.mod(BigInteger.TWO).compareTo(BigInteger.ZERO) == 0){
                b = b.divide(BigInteger.TWO);
                a = a.multiply(a).mod(mod);
            }else{
                b = b.subtract(BigInteger.ONE);
                result = result.multiply(a).mod(mod);

                b = b.divide(BigInteger.TWO);
                a = a.multiply(a).mod(mod);
            }
        }
        return result;
    }

    public static void generateKey(BigInteger p, BigInteger q){
        N = p.multiply(q);
        PHI = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        E = getRandom(BigInteger.ONE,PHI);
        while ( gcd(E,PHI).compareTo(BigInteger.ONE) !=0 ){
            E = getRandom(BigInteger.ONE,PHI);
        }
        D = extendedEuclid(E,PHI);
    }

    public static BigInteger gcd(BigInteger a, BigInteger b){
        if ( b.compareTo(BigInteger.ZERO) == 0){
            return a;
        }
        return gcd(b, a.mod(b));
    }

    public static BigInteger extendedEuclid(BigInteger a, BigInteger b){
        BigInteger d = BigInteger.ZERO;
        BigInteger x1 = BigInteger.ZERO;
        BigInteger x2 = BigInteger.ONE;
        BigInteger y1 = BigInteger.ONE;
        BigInteger temp_phi = b;
        while (a.equals(BigInteger.ZERO) == false){
            BigInteger integer = temp_phi.divide(a);
            BigInteger remainder = temp_phi.subtract(integer.multiply(a));
            temp_phi = a;
            a = remainder;
            BigInteger x = x2.subtract(integer.multiply(x1));
            BigInteger y = d.subtract(integer.multiply(y1));

            x2 = x1;
            x1 = x;
            d = y1;
            y1 = y;
        }
        BigInteger result = null;
        if(temp_phi.equals(BigInteger.ONE)== true){
            result = d.add(b);
        }
        return result;
    }

    public static BigInteger encrypt(BigInteger publicKey, BigInteger N, BigInteger input){
        BigInteger key = publicKey;
        BigInteger n = N;
        return power(input,key,n);
    }

    public static BigInteger decrypt(BigInteger privateKey, BigInteger N, BigInteger encryptedMsg){
        BigInteger key = privateKey;
        BigInteger n = N;
        return power(encryptedMsg,key,n);
    }

    public static void main(String[] args) throws IOException {
        BigInteger p = getRandomPrim();
        BigInteger q = getRandomPrim();
        while (p.compareTo(q) == 0){
            q = getRandomPrim();
        }

        generateKey(p,q);

        System.out.println("p= "+ p +" q= "+q+" e="+E+" d="+D+" N="+N);

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter message");
        String input = scanner.nextLine();
        BigInteger message = new BigInteger(input);

        BigInteger encryptedMsg = encrypt(E,N,message);
        System.out.println("EncryptedMsg:");
        System.out.println(encryptedMsg);

        System.out.println("DecryptedMsg:");
        System.out.println(decrypt(D,N,encryptedMsg));

    }
}
