package MaJIangSuanFa;

import java.util.*;

public class MahjongWinChecker {

    // 检查是否胡牌：4*3 + 2 模式
    public boolean checkWin(int[] tiles) {
        // 复制并排序手牌
        int[] sortedTiles = Arrays.copyOf(tiles, tiles.length);
        Arrays.sort(sortedTiles);

        // 检查牌数是否为14张
        if (sortedTiles.length != 14) {
            return false;
        }

        // 尝试所有可能的对子作为将牌
        for (int i = 0; i < sortedTiles.length - 1; i++) {
            if (sortedTiles[i] == sortedTiles[i + 1]) {
                // 复制数组并移除对子
                List<Integer> remainingTiles = new ArrayList<>();
                for (int j = 0; j < sortedTiles.length; j++) {
                    if (j != i && j != i + 1) {
                        remainingTiles.add(sortedTiles[j]);
                    }
                }

                // 检查剩余牌能否组成4组刻子或顺子
                if (canFormGroups(remainingTiles)) {
                    return true;
                }
            }
        }

        return false;
    }

    // 检查剩余牌能否组成4组刻子或顺子
    private boolean canFormGroups(List<Integer> tiles) {
        if (tiles.isEmpty()) {
            return true;
        }

        // 获取当前最小的牌
        int minTile = tiles.get(0);

        // 尝试组成刻子
        if (tiles.size() >= 3 &&
                tiles.get(1) == minTile &&
                tiles.get(2) == minTile) {

            // 移除刻子
            List<Integer> newTiles = new ArrayList<>(tiles);
            newTiles.remove(0);
            newTiles.remove(0);
            newTiles.remove(0);

            // 递归检查剩余牌
            if (canFormGroups(newTiles)) {
                return true;
            }
        }

        // 尝试组成顺子
        if (tiles.contains(minTile + 1) && tiles.contains(minTile + 2)) {
            // 移除顺子
            List<Integer> newTiles = new ArrayList<>(tiles);
            newTiles.remove(Integer.valueOf(minTile));
            newTiles.remove(Integer.valueOf(minTile + 1));
            newTiles.remove(Integer.valueOf(minTile + 2));

            // 递归检查剩余牌
            if (canFormGroups(newTiles)) {
                return true;
            }
        }

        return false;
    }

    public static void main(String[] args) {
        MahjongWinChecker checker = new MahjongWinChecker();

        // 测试用例：11123456778899 (胡牌)
        int[] winTiles = {1, 1, 1, 2, 3, 4, 5, 6, 7, 7, 8, 8, 9, 9};
        System.out.println(checker.checkWin(winTiles)); // 输出true

        // 测试用例：12345678912345 (不胡牌)
        int[] loseTiles = {1, 2, 3, 4, 5, 6, 7, 8, 9, 1, 2, 3, 4, 5};
        System.out.println(checker.checkWin(loseTiles)); // 输出false
    }
}