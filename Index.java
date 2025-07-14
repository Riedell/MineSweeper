import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Index {
    // 伪代码示例
    public static void main(String[] args) {
        // 用户输入不考虑0，程序内部传参统一考虑0值，也就是对用户输入的坐标-1处理
        Game game = new Game(15, 10, 15); // 创建一个10x10，15个雷的游戏
//        Game game = new Game(15, 10, 1); // 创建一个10x10，15个雷的游戏
        game.initBoard();// 开始就初始化，且只初始化一次
        game.initMineArr();
        game.displayMineArr();
        Scanner scanner = new Scanner(System.in);

        game.setGameOver(Boolean.FALSE);
//        boolean isGameOver = game.getGameOver();

//        while (!isGameOver) {// 值的更新写到reveal非法内部，导致此处循环内部无法更新。暂时改为下面直接调用获取值
        while (!game.getGameOver()) {
//            game.initBoard();
//            game.initMineArr();
            game.displayBoard(); // 打印当前棋盘
            System.out.print("请输入指令 (例如: open 3 4 或 flag 5 2): ");// action [row] [column] 修改/更新 第几行 第几列
            String line = scanner.nextLine();
            String[] parts = line.split(" "); // 解析输入

            String action = parts[0];
            if ("quit".equals(action) || "q".equals(action)) {
                break;
            }

            try {
                int row = Integer.parseInt(parts[1]) - 1;// 行
                int col = Integer.parseInt(parts[2]) - 1;// 列

                // 根据指令调用游戏逻辑
                switch (action) {
                    case "open":
                    case "o":
                        game.reveal(row, col);
                        break;
//                    case "flag":
//                    case "f":
//                        game.toggleFlag(row, col);
//                        break;
                    default:
                        System.out.println("无效指令！");
                }

                game.confirmed();
                // 检查游戏是否胜利
                if (game.checkWin()) {
                    // ...
                    System.out.println("Congratulate for you!!！");
                    break;
                }

            } catch (Exception e) {
                System.out.println("输入格式错误，请重试！");
            }
        }

        // 游戏结束后显示最终结果
//        game.showAllMines();
//        game.displayBoard();
        System.out.println("游戏结束!");
        scanner.close();

//        game.initBoard();
//        game.displayBoard();
//        game.initMineArr();
//        game.displayMineArr();
    }

}



