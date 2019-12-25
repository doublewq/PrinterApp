package com.will.bluetoothprinterdemo.utils;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.github.promeg.pinyinhelper.Pinyin;
import com.will.bluetoothprinterdemo.vo.Order;
import com.will.bluetoothprinterdemo.vo.Product;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.util.List;

/**
 * 蓝牙打印工具类
 */
public class PrintUtil {

    private OutputStreamWriter mWriter = null;
    private OutputStream mOutputStream = null;

    public final static int WIDTH_PIXEL = 576;//80mm热敏打印纸 // 58mm热敏打印纸的点密度384;
    public final static int IMAGE_SIZE = 320;

    /**
     * 初始化Pos实例
     *
     * @param encoding 编码
     * @throws IOException
     */
    public PrintUtil(OutputStream outputStream, String encoding) throws IOException {
        mWriter = new OutputStreamWriter(outputStream, encoding);
        mOutputStream = outputStream;
        initPrinter();
    }

    public void print(byte[] bs) throws IOException {
        mOutputStream.write(bs);
    }

    public void printRawBytes(byte[] bytes) throws IOException {
        mOutputStream.write(bytes);
        mOutputStream.flush();
    }

    /**
     * 初始化打印机
     *
     * @throws IOException
     */
    public void initPrinter() throws IOException {
        mWriter.write(0x1B);
        mWriter.write(0x40);
        mWriter.flush();
    }

    /**
     * 打印换行
     *
     * @return length 需要打印的空行数
     * @throws IOException
     */
    public void printLine(int lineNum) throws IOException {
        for (int i = 0; i < lineNum; i++) {
            mWriter.write("\n");
        }
        mWriter.flush();
    }

    /**
     * 打印换行(只换一行)
     *
     * @throws IOException
     */
    public void printLine() throws IOException {
        printLine(1);
    }

    /**
     * 打印空白(一个Tab的位置，约4个汉字)
     *
     * @param length 需要打印空白的长度,
     * @throws IOException
     */
    public void printTabSpace(int length) throws IOException {
        for (int i = 0; i < length; i++) {
            mWriter.write("\t");
        }
        mWriter.flush();
    }

    /**
     * 绝对打印位置
     *
     * @return
     * @throws IOException
     */
    public byte[] setLocation(int offset) throws IOException {
        byte[] bs = new byte[4];
        bs[0] = 0x1B;
        bs[1] = 0x24;
        bs[2] = (byte) (offset % 256);
        bs[3] = (byte) (offset / 256);
        return bs;
    }

    public byte[] getGbk(String stText) throws IOException {
        byte[] returnText = stText.getBytes("GBK"); // 必须放在try内才可以
        return returnText;
    }

    private int getStringPixLength(String str) {
        int pixLength = 0;
        char c;
        for (int i = 0; i < str.length(); i++) {
            c = str.charAt(i);
            if (Pinyin.isChinese(c)) {
                pixLength += 24;
            } else {
                pixLength += 12;
            }
        }
        return pixLength;
    }

    public int getOffset(String str) {
        return WIDTH_PIXEL - getStringPixLength(str);
    }

    /**
     * 打印文字
     *
     * @param text
     * @throws IOException
     */
    public void printText(String text) throws IOException {
        mWriter.write(text);
        mWriter.flush();
    }

    /**
     * 对齐0:左对齐，1：居中，2：右对齐
     */
    public void printAlignment(int alignment) throws IOException {
        mWriter.write(0x1b);
        mWriter.write(0x61);
        mWriter.write(alignment);
    }

    public void printLargeText(String text) throws IOException {

        mWriter.write(0x1b);
        mWriter.write(0x21);
        mWriter.write(48);

        mWriter.write(text);

        mWriter.write(0x1b);
        mWriter.write(0x21);
        mWriter.write(0);

        mWriter.flush();
    }

    public void printTwoColumn(String title, String content) throws IOException {
        int iNum = 0;
        byte[] byteBuffer = new byte[100];
        byte[] tmp;

        tmp = getGbk(title);
        System.arraycopy(tmp, 0, byteBuffer, iNum, tmp.length);
        iNum += tmp.length;

        tmp = setLocation(getOffset(content));
        System.arraycopy(tmp, 0, byteBuffer, iNum, tmp.length);
        iNum += tmp.length;

        tmp = getGbk(content);
        System.arraycopy(tmp, 0, byteBuffer, iNum, tmp.length);

        print(byteBuffer);
    }

    public void printThreeColumn(String left, String middle, String right) throws IOException {
        int iNum = 0;
        byte[] byteBuffer = new byte[200];
        byte[] tmp = new byte[0];

        System.arraycopy(tmp, 0, byteBuffer, iNum, tmp.length);
        iNum += tmp.length;

        tmp = getGbk(left);
        System.arraycopy(tmp, 0, byteBuffer, iNum, tmp.length);
        iNum += tmp.length;

        int pixLength = getStringPixLength(left) % WIDTH_PIXEL;
        if (pixLength > WIDTH_PIXEL / 2 || pixLength == 0) {
            middle = "\n\t\t" + middle;
        }

        tmp = setLocation(192);
        System.arraycopy(tmp, 0, byteBuffer, iNum, tmp.length);
        iNum += tmp.length;

        tmp = getGbk(middle);
        System.arraycopy(tmp, 0, byteBuffer, iNum, tmp.length);
        iNum += tmp.length;

        tmp = setLocation(getOffset(right));
        System.arraycopy(tmp, 0, byteBuffer, iNum, tmp.length);
        iNum += tmp.length;

        tmp = getGbk(right);
        System.arraycopy(tmp, 0, byteBuffer, iNum, tmp.length);

        print(byteBuffer);
    }

    public void printFiveColumn(String left, String mid_lef, String middle, String mid_rig, String right) throws IOException {
        int iNum = 0;
        byte[] byteBuffer = new byte[300];
        byte[] tmp = new byte[0];

        System.arraycopy(tmp, 0, byteBuffer, iNum, tmp.length);
        iNum += tmp.length;

        tmp = getGbk(left);
        System.arraycopy(tmp, 0, byteBuffer, iNum, tmp.length);
        iNum += tmp.length;

        int pixLength = getStringPixLength(left) % WIDTH_PIXEL;
        if (pixLength > WIDTH_PIXEL / 2 || pixLength == 0) {
            mid_lef = "\n\t\t" + mid_lef;
        }

        tmp = setLocation(200);
        System.arraycopy(tmp, 0, byteBuffer, iNum, tmp.length);
        iNum += tmp.length;

        // 设置中间靠左的数据
        tmp = getGbk(mid_lef);
        System.arraycopy(tmp, 0, byteBuffer, iNum, tmp.length);
        iNum += tmp.length;

        tmp = setLocation(200+94);
        System.arraycopy(tmp, 0, byteBuffer, iNum, tmp.length);
        iNum += tmp.length;

        // 设置中间的数据
        tmp = getGbk(middle);
        System.arraycopy(tmp, 0, byteBuffer, iNum, tmp.length);
        iNum += tmp.length;

        tmp = setLocation(200+94+94);
        System.arraycopy(tmp, 0, byteBuffer, iNum, tmp.length);
        iNum += tmp.length;

        // 设置中间靠右的数据
        tmp = getGbk(mid_rig);
        System.arraycopy(tmp, 0, byteBuffer, iNum, tmp.length);
        iNum += tmp.length;

        tmp = setLocation(200+94+94+94);
        System.arraycopy(tmp, 0, byteBuffer, iNum, tmp.length);
        iNum += tmp.length;

        // 设置最右边的数据
        tmp = getGbk(right);
        System.arraycopy(tmp, 0, byteBuffer, iNum, tmp.length);

        print(byteBuffer);
    }

    public void printDashLine() throws IOException {
        printText("--------------------------------");
    }

    public void printBitmap(Bitmap bmp) throws IOException {
        bmp = compressPic(bmp);
        byte[] bmpByteArray = draw2PxPoint(bmp);
        printRawBytes(bmpByteArray);
    }

    /*************************************************************************
     * 假设一个360*360的图片，分辨率设为24, 共分15行打印 每一行,是一个 360 * 24 的点阵,y轴有24个点,存储在3个byte里面。
     * 即每个byte存储8个像素点信息。因为只有黑白两色，所以对应为1的位是黑色，对应为0的位是白色
     **************************************************************************/
    private byte[] draw2PxPoint(Bitmap bmp) {
        //先设置一个足够大的size，最后在用数组拷贝复制到一个精确大小的byte数组中
        int size = bmp.getWidth() * bmp.getHeight() / 8 + 1000;
        byte[] tmp = new byte[size];
        int k = 0;
        // 设置行距为0
        tmp[k++] = 0x1B;
        tmp[k++] = 0x33;
        tmp[k++] = 0x00;
        // 居中打印
        tmp[k++] = 0x1B;
        tmp[k++] = 0x61;
        tmp[k++] = 1;
        for (int j = 0; j < bmp.getHeight() / 24f; j++) {
            tmp[k++] = 0x1B;
            tmp[k++] = 0x2A;// 0x1B 2A 表示图片打印指令
            tmp[k++] = 33; // m=33时，选择24点密度打印
            tmp[k++] = (byte) (bmp.getWidth() % 256); // nL
            tmp[k++] = (byte) (bmp.getWidth() / 256); // nH
            for (int i = 0; i < bmp.getWidth(); i++) {
                for (int m = 0; m < 3; m++) {
                    for (int n = 0; n < 8; n++) {
                        byte b = px2Byte(i, j * 24 + m * 8 + n, bmp);
                        tmp[k] += tmp[k] + b;
                    }
                    k++;
                }
            }
            tmp[k++] = 10;// 换行
        }
        // 恢复默认行距
        tmp[k++] = 0x1B;
        tmp[k++] = 0x32;

        byte[] result = new byte[k];
        System.arraycopy(tmp, 0, result, 0, k);
        return result;
    }

    /**
     * 图片二值化，黑色是1，白色是0
     *
     * @param x   横坐标
     * @param y   纵坐标
     * @param bit 位图
     * @return
     */
    private byte px2Byte(int x, int y, Bitmap bit) {
        if (x < bit.getWidth() && y < bit.getHeight()) {
            byte b;
            int pixel = bit.getPixel(x, y);
            int red = (pixel & 0x00ff0000) >> 16; // 取高两位
            int green = (pixel & 0x0000ff00) >> 8; // 取中两位
            int blue = pixel & 0x000000ff; // 取低两位
            int gray = RGB2Gray(red, green, blue);
            if (gray < 128) {
                b = 1;
            } else {
                b = 0;
            }
            return b;
        }
        return 0;
    }

    /**
     * 图片灰度的转化
     */
    private int RGB2Gray(int r, int g, int b) {
        int gray = (int) (0.29900 * r + 0.58700 * g + 0.11400 * b); // 灰度转化公式
        return gray;
    }

    /**
     * 对图片进行压缩（去除透明度）
     *
     * @param bitmapOrg
     */
    private Bitmap compressPic(Bitmap bitmapOrg) {
        // 获取这个图片的宽和高
        int width = bitmapOrg.getWidth();
        int height = bitmapOrg.getHeight();
        // 定义预转换成的图片的宽度和高度
        int newWidth = IMAGE_SIZE;
        int newHeight = IMAGE_SIZE;
        Bitmap targetBmp = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);
        Canvas targetCanvas = new Canvas(targetBmp);
        targetCanvas.drawColor(0xffffffff);
        targetCanvas.drawBitmap(bitmapOrg, new Rect(0, 0, width, height), new Rect(0, 0, newWidth, newHeight), null);
        return targetBmp;
    }

    public static void printOrder(BluetoothSocket bluetoothSocket, Bitmap bitmap, Order order, List<Product> productList){
        try {
            PrintUtil pUtil = new PrintUtil(bluetoothSocket.getOutputStream(), "GBK");
            // 店铺名 居中 放大
            pUtil.printAlignment(1);
            pUtil.printLargeText("温梦家纺");
            pUtil.printLine();
            pUtil.printAlignment(0);
            pUtil.printLine();

            // 打印头部信息
            pUtil.printTwoColumn("客户:", order.getConsumerName());
            pUtil.printLine();
            pUtil.printTwoColumn("订单号:",order.getOrderID());
            pUtil.printLine();
            pUtil.printTwoColumn("下单时间:",order.getTime());
            pUtil.printLine();

            // 分隔线
            pUtil.printDashLine();
            pUtil.printLine();

            //打印商品列表的头
            pUtil.printText("商品");
            pUtil.printTabSpace(2);
            pUtil.printText("颜色");
            pUtil.printTabSpace(1);
            pUtil.printText("数量");
            pUtil.printTabSpace(1);
            pUtil.printText("单价");
            pUtil.printTabSpace(1);
            pUtil.printText("小计");
            pUtil.printLine();

            for (int i = 0; i < productList.size(); i++) {
                Product product = productList.get(i);
                // 小计 小数点保留两位
                double sumPricedoub = (Integer.valueOf(product.getNumbers()) * Double.valueOf(product.getPrice()));
                DecimalFormat formPrice = new DecimalFormat("#.00");
                String sumPrice = formPrice.format(sumPricedoub);
                pUtil.printFiveColumn(product.getName(),product.getColor(),product.getNumbers()+"",product.getPrice()+"",sumPrice);
            }

            //打印中间的订单数量和金额信息
            pUtil.printDashLine();
            pUtil.printLine();

            pUtil.printTwoColumn("销售总数量:",order.getProductNum()+"");
            pUtil.printLine();
            pUtil.printTwoColumn("销售额总计:",order.getSalary()+"");
            pUtil.printLine();

            //打印中间的金额信息
            pUtil.printDashLine();
            pUtil.printLine();

            pUtil.printTwoColumn("应付金额:",order.getSalary()+"");
            pUtil.printLine();
            pUtil.printTwoColumn("实收金额:",order.getHasPay()+"");
            pUtil.printLine();
            pUtil.printTwoColumn("欠款：",(order.getSalary()-order.getHasPay())+"");
            pUtil.printLine();

            //打印最下面的信息
            pUtil.printDashLine();
            pUtil.printLine();

            pUtil.printTwoColumn("销售地址：","西安市锦绣家纺城西区39-40号");
            pUtil.printLine();
            pUtil.printTwoColumn("电话：","029-81038039");
            pUtil.printLine();
            pUtil.printTwoColumn("手机/微信：","13389288598");
            pUtil.printLine();
            pUtil.printTwoColumn("经营：","高中档被子、羽绒被、充绒被、夏凉被、枕芯、保健枕、荞麦养生枕。各种规格白毛、羊羔毛、水晶绒床垫。夏季大量生产销售麻将凉枕。");
            pUtil.printLine();

            //
            pUtil.printDashLine();
            pUtil.printBitmap(bitmap);
            pUtil.printLine(4);

        }catch (IOException e) {

        }
    }

    public static void printTest(BluetoothSocket bluetoothSocket, Bitmap bitmap) {

        try {
            PrintUtil pUtil = new PrintUtil(bluetoothSocket.getOutputStream(), "GBK");
            // 店铺名 居中 放大
            pUtil.printAlignment(1);
            pUtil.printLargeText("解忧杂货店");
            pUtil.printLine();
            pUtil.printAlignment(0);
            pUtil.printLine();

            pUtil.printTwoColumn("时间:", "2017-05-09 15:50:41");
            pUtil.printLine();

            pUtil.printTwoColumn("订单号:", System.currentTimeMillis() + "");
            pUtil.printLine();

            pUtil.printTwoColumn("付款人:", "VitaminChen");
            pUtil.printLine();

            // 分隔线
            pUtil.printDashLine();
            pUtil.printLine();

            //打印商品列表
            pUtil.printText("商品");
            pUtil.printTabSpace(2);
            pUtil.printText("数量");
            pUtil.printTabSpace(1);
            pUtil.printText("    单价");
            pUtil.printLine();

            pUtil.printThreeColumn("iphone6", "1", "4999.00");
            pUtil.printThreeColumn("测试一个超长名字的产品看看打印出来会怎么样哈哈哈哈哈哈", "1", "4999.00");

            pUtil.printDashLine();
            pUtil.printLine();

            pUtil.printTwoColumn("订单金额:", "9998.00");
            pUtil.printLine();

            pUtil.printTwoColumn("实收金额:", "10000.00");
            pUtil.printLine();

            pUtil.printTwoColumn("找零:", "2.00");
            pUtil.printLine();

            pUtil.printDashLine();

            pUtil.printBitmap(bitmap);

            pUtil.printLine(4);

        } catch (IOException e) {

        }
    }
}