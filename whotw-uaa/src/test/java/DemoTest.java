import org.junit.Test;

/**
 * @author whotw
 * @description DemoTest
 * @date 2021/5/3 7:41
 */
public class DemoTest {
    public static void main(String[] args) {
//        Long toChangByte = 4611686018427387904L;
        Long toChangByte = 4611686018427387904L;
        String result = Long.toBinaryString(toChangByte);
        System.out.println(result);
        spiltLongBinary(8, result);
        Long a=1L;
        Long b=3L;
        System.out.println(a^b);

    }
    @Test
    public void testDecimalConvertBinary() {
        Long toChangByte = Long.MAX_VALUE;
        String result = Long.toBinaryString(toChangByte);
        System.out.println(result);
        spiltLongBinary(8, result);
    }

    /**
     * 拆分二进制
     *
     * @param perLength 每段截取的长度
     * @param toSpilt   要拆分的字符
     */
    public static void spiltLongBinary(int perLength, String toSpilt) {
        int length = toSpilt.length();
        String out;
        do {
            out = toSpilt.substring(length - perLength - 1, length - 1);
            System.out.println(out);
            toSpilt = toSpilt.substring(0, length - perLength);
            length = toSpilt.length();
        } while (length > perLength);
        System.out.println(toSpilt);
    }
}
