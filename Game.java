import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.IntFunction;

public class Game {
    // 未翻开：■
    // 已翻开为空：null/none or "" ？
    // 已翻开不为空：1 2 3 4 5 6 7 8

    private int width;
    private int height;
    private int mineNum;

    private Boolean isGameOver;// 布尔类型默认初始值为false，这里沿用

    Character[][] arr;
    int[][] mineArr;

    int foundMinesCount = 0;

    Map<Integer, Integer> hashMap;

    public Game() {
    }

    public Game(int width, int height, int mineNum) {
        this.width = width;
        this.height = height;
        this.mineNum = mineNum;
    }

    public Game(int width, int height, int mineNum, boolean isGameOver) {
        this.width = width;
        this.height = height;
        this.mineNum = mineNum;
        this.isGameOver = isGameOver;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getMineNum() {
        return mineNum;
    }

    public void setMineNum(int mineNum) {
        this.mineNum = mineNum;
    }

    public Boolean getGameOver() {
        return isGameOver;
    }

    public void setGameOver(Boolean gameOver) {
        isGameOver = gameOver;
    }

    public void initBoard() {
        arr = new Character[height][width];
        Arrays.setAll(arr, new IntFunction<Character[]>() {
            @Override
            public Character[] apply(int i) {
                Arrays.setAll(arr[i], new IntFunction<Character>() {
                    @Override
                    public Character apply(int j) {
                        return '■';
                    }
                });
                return arr[i];
            }
        });
    }

    public void displayBoard() {

        for (int i = -1; i < height; i++) {// 行row
//            System.out.print(i+1 + "  ");
            System.out.printf("%2d", i+1);
            for (int j = 0; j < width; j++) {// 列col
                if (i == -1) {
                    System.out.printf("%3d", j+1);
                } else {
                    System.out.printf("%3s", arr[i][j]);
                }
            }
            System.out.println();
        }
    }

    /**
     * 检查当前游戏状态（基于当前操作后）
     * ps：下标不能是大众便于直观计数的1下标（不含0）
     *
     * @param row 符合计算机规则的行下标（含0）
     * @param col 符合计算机规则的列下标（含0）
     * @return true表示当前坐标有雷
     */
    public Boolean checkStat(int row, int col) {// 发一个坐标或者坐标数组过来

//        if (mineArr[row][col] == 1) {}
//        return mineArr[row][col] == 1 ? true : false;
        return mineArr[row][col] == 1;
    }

    public void initMineArr() {
        mineArr = new int[this.height][this.width];

        int ind = 0;
        while (ind < this.mineNum) {
            int i = new Random().nextInt(this.height);
            int j = new Random().nextInt(this.width);
            mineArr[i][j] = 1;// 1表示有地雷
//            hashMap.put(i, j);// key重复了
            ++ind;
        }

        // todo 生成的位置坐标可能重复
    }

    public void displayMineArr() {

        for (int i = -1; i < this.height; i++) {// 行row
//            System.out.print(i+1 + "  ");
            System.out.printf("%2d", i+1);
            for (int j = 0; j < this.width; j++) {// 列col
                if (i == -1) {
                    System.out.printf("%3d", j+1);
                } else {
                    System.out.printf("%3s", mineArr[i][j]);
                }
            }
            System.out.println();
        }
    }

    public boolean checkWin() {
        return foundMinesCount == mineNum;
    }

    public void confirmed() {
        /*hashMap = new HashMap<>();
        hashMap.forEach(new BiConsumer<Integer, Integer>() {
            @Override
            public void accept(Integer row, Integer col) {
                for (int i = row - 1; i <= row + 1; i++) {
                    for (int j = col - 1; j <= col + 1; j++) {
                        if (i >= 0 && i < height && j >= 0 && j < width) {
                            if (arr[i][j] == '■' && mineArr[i][j] != 1) {
                                return;
                            }
                        }
                    }
                }
                foundMinesCount++;
            }
        });*/

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (mineArr[i][j] == 1) {
                    for (int row = i - 1; row <= i + 1; row++) {
                        for (int col = j - 1; col <= j + 1; col++) {
                            if (row >= 0 && row < height && col >= 0 && col < width) {
                                if (arr[row][col] == '■' && mineArr[row][col] != 1) {
                                    return;
                                }
                            }
                        }
                    }
                    foundMinesCount++;
                }
            }
        }

    }

    public void reveal(int row, int col) {
        // 检查
        if (row < 0 || row > this.height || col < 0 || col > this.width) {
            System.out.println("error input...");
        } else {
            Boolean nowStat = checkStat(row, col);
            if (!nowStat) {
                // 计算当前坐标周围的地雷个数
                // 如果周围没有雷，那么更新（翻开）当前坐标为空，并且一同更新周围一圈为空
                // 如果周围有雷，那么只更新（只翻开）当前坐标所显示代表周围地雷数量的数字
//                int res = calculate(row, col);
//                calculate(row, col) > 0 ? arr[row][col] = (char) res : leaveBlank(row, col);// 这里写的不对
                int res = calculate(row, col);
//                if (res > 0) arr[row][col] = (char) res;// 估计得+32
                if (res > 0) arr[row][col] = Character.forDigit(res, 10);
                else leaveBlank(row, col);

            } else {
                isGameOver = true;// 更新变量
            }
        }

//        displayBoard();
    }

    public int calculate(int row, int col) {
        int mineN = 0;
        // 判断当前坐标四周是否与地图边界所重合
        // 麻烦的做法：挨个分类讨论所处位置，比如位于四角、紧贴边界非四角、非四角非紧贴边界
        // 不分类讨论，直接全部计算，然后判断每个位置坐标是否在边界坐标内
        // 1,2

//        int[][] temp = new int[3][3];
        for (int i = row - 1; i <= row + 1; i++) {// 重复计算自己的坐标无所谓，因为走到这里就表明当前坐标没雷
            for (int j = col - 1; j <= col + 1; j++) {
                if ((i > 0 && i < this.height && j > 0 && j < this.width) && checkStat(i, j)) mineN++;
            }
        }

        return mineN;
    }

    public void leaveBlank(int row, int col) {
        for (int i = row - 1; i <= row + 1; i++) {
            for (int j = col - 1; j <= col + 1; j++) {
                if (i >= 0 && i < this.height && j >= 0 && j < this.width) {
                    if (arr[i][j] == '■') {
                        arr[i][j] = ' ';
                    }
                }
            }
        }
    }

    @Override
    public String toString() {
        return "Game{" +
                "width=" + width +
                ", height=" + height +
                ", mineNum=" + mineNum +
                '}';
    }
}
