package io.tinypiggy.algorithm;

public class Money {


    private static compose[] composes = new compose[10];
    static {
        composes[0] = new compose(0, 0, 0);
        composes[1] = new compose(0, 1, 3);
        composes[2] = new compose(0, 0, 1);
        composes[3] = new compose(0, 1, 4);
        composes[4] = new compose(0, 0, 2);
        composes[5] = new compose(0, 1, 0);
        composes[6] = new compose(0, 0, 3);
        composes[7] = new compose(0, 1, 1);
        composes[8] = new compose(0, 0, 4);
        composes[9] = new compose(0, 1, 2);
    }

    private final static compose error = new compose(-1, -1, -1);
    private static compose calc(int a, int b, int c, int money){
        if (a < 0 || b < 0 || c < 0 || money < 0){
            return error;
        }
        int remainder = money % 10;
        compose target = composes[remainder];
        b -= target.y;
        c -= target.z;
        if (b < 0 || c < 0){
            return error;
        }
        money -= 5 * target.y + 2 * target.z;
        money /= 10;
        if (a + b / 2 + c / 5 < money){
            return error;
        }

        if (a >= money){
            return new compose(money, target.y, target.z);
        }
        money -= a;
        if ( b / 2 >= money){
            return new compose(a, money * 2 + target.y, target.z);
        }
        money -= b / 2;
        return new compose(a, b + target.y, money * 5 + target.z);
    }

    public static void main(String[] args) {
        System.out.println(calc(2, 15, 42, 127));
    }

    public static class compose {

        compose(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        int x;
        int y;
        int z;

        @Override
        public String toString() {
            return "compose{" +
                    "x=" + x +
                    ", y=" + y +
                    ", z=" + z +
                    '}';
        }
    }
}
