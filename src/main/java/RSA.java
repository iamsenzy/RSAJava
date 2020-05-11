import java.math.BigInteger;
import java.util.Random;

public class RSA {

    static Random rand = new Random();


    public static BigInteger getRandom(BigInteger min, BigInteger max){

        BigInteger result;
        do {
            result = new BigInteger(max.bitLength(), rand);
        }while (result.compareTo(min) < 0 || result.compareTo(max) > 0);
        return result;
    }

    public static boolean millerRabin(BigInteger n, int count ){
        if (n.compareTo(BigInteger.ONE) == 0)
            return false;

        if (n.compareTo(BigInteger.valueOf(3)) < 0)
            return true;
        if(count >= n.intValue()){
            count = n.intValue() - 1;
        }
        for (int i = 0; i < count; i++) {
            BigInteger val = getRandom(BigInteger.TWO,n.subtract(BigInteger.ONE));
            if( power(val,n.subtract(BigInteger.ONE),n).compareTo(BigInteger.ONE)!=0){
                return false;
            }
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

    public static BigInteger getRandomPrim(){
        BigInteger result;
        while (true){
            //result = getRandom(new BigInteger(1023,rand),new BigInteger(1024, rand));
            result = getRandom(BigInteger.ONE,new BigInteger("100"));
            if(millerRabin(result, 3)){
                return result;
            }
        }
    }


    public static void main(String[] args) {
        System.out.println(getRandomPrim()  );
    }
}
