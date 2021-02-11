package android.androidVNC;

/* renamed from: android.androidVNC.DH */
public class DH {
    private static final int DH_GEN = 2;
    private static final int DH_KEY = 5;
    private static final int DH_MAX_BITS = 31;
    private static final int DH_MOD = 1;
    private static final int DH_PRIV = 3;
    private static final int DH_PUB = 4;
    private static final int DH_RANGE = 100;
    private long gen;
    private long key;
    private long maxNum = 2147483647L;
    private long mod;
    private long priv;
    private long pub;

    public DH() {
    }

    public DH(long generator, long modulus) throws Exception {
        if (generator >= this.maxNum || modulus >= this.maxNum) {
            throw new Exception("Modulus or generator too large.");
        }
        this.gen = generator;
        this.mod = modulus;
    }

    private long rng(long limit) {
        return (long) (Math.random() * ((double) limit));
    }

    private boolean millerRabin(long n, int trials) {
        for (int i = 0; i < trials; i++) {
            if (XpowYmodN(rng(n - 3) + 2, n - 1, n) != 1) {
                return false;
            }
        }
        return true;
    }

    private long generatePrime() {
        long prime;
        do {
            prime = tryToGeneratePrime(rng(this.maxNum));
        } while (prime == 0);
        return prime;
    }

    private long tryToGeneratePrime(long prime) {
        long cnt;
        if ((1 & prime) == 0) {
            prime++;
        }
        long cnt2 = 0;
        while (true) {
            if (millerRabin(prime, 25)) {
                break;
            }
            cnt = cnt2 + 1;
            if (cnt2 >= 100 || prime >= this.maxNum) {
                cnt2 = cnt;
            } else {
                prime += 2;
                if (prime % 3 == 0) {
                    prime += 2;
                    cnt2 = cnt;
                } else {
                    cnt2 = cnt;
                }
            }
        }
//        cnt2 = cnt;
        if (cnt2 >= 100 || prime >= this.maxNum) {
            return 0;
        }
        return prime;
    }

    private long XpowYmodN(long x, long y, long N) {
        long result = 1;
        for (int i = 0; i < 64; i++) {
            result = (result * result) % N;
            if ((Long.MIN_VALUE & y) != 0) {
                result = (result * x) % N;
            }
            y <<= 1;
        }
        return result;
    }

    public void createKeys() {
        this.gen = generatePrime();
        this.mod = generatePrime();
        if (this.gen > this.mod) {
            long swap = this.gen;
            this.gen = this.mod;
            this.mod = swap;
        }
    }

    public long createInterKey() {
        this.priv = rng(this.maxNum);
        long XpowYmodN = XpowYmodN(this.gen, this.priv, this.mod);
        this.pub = XpowYmodN;
        return XpowYmodN;
    }

    public long createEncryptionKey(long interKey) throws Exception {
        if (interKey >= this.maxNum) {
            throw new Exception("interKey too large");
        }
        long XpowYmodN = XpowYmodN(interKey, this.priv, this.mod);
        this.key = XpowYmodN;
        return XpowYmodN;
    }

    public long getValue(int flags) {
        switch (flags) {
            case 1:
                return this.mod;
            case 2:
                return this.gen;
            case 3:
                return this.priv;
            case 4:
                return this.pub;
            case 5:
                return this.key;
            default:
                return 0;
        }
    }

    public int bits(long number) {
        for (int i = 0; i < 64; i++) {
            number /= 2;
            if (number < 2) {
                return i;
            }
        }
        return 0;
    }

    public static byte[] longToBytes(long number) {
        byte[] bytes = new byte[8];
        for (int i = 0; i < 8; i++) {
            bytes[i] = (byte) ((int) (255 & (number >> ((7 - i) * 8))));
        }
        return bytes;
    }

    public static long bytesToLong(byte[] bytes) {
        long result = 0;
        for (int i = 0; i < 8; i++) {
            result = (result << 8) + ((long) bytes[i]);
        }
        return result;
    }
}
