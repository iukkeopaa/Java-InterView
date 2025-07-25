package MaJIangSuanFa;

import java.util.*;

public class MahjongWinChecker {

    // ����Ƿ���ƣ�4*3 + 2 ģʽ
    public boolean checkWin(int[] tiles) {
        // ���Ʋ���������
        int[] sortedTiles = Arrays.copyOf(tiles, tiles.length);
        Arrays.sort(sortedTiles);

        // ��������Ƿ�Ϊ14��
        if (sortedTiles.length != 14) {
            return false;
        }

        // �������п��ܵĶ�����Ϊ����
        for (int i = 0; i < sortedTiles.length - 1; i++) {
            if (sortedTiles[i] == sortedTiles[i + 1]) {
                // �������鲢�Ƴ�����
                List<Integer> remainingTiles = new ArrayList<>();
                for (int j = 0; j < sortedTiles.length; j++) {
                    if (j != i && j != i + 1) {
                        remainingTiles.add(sortedTiles[j]);
                    }
                }

                // ���ʣ�����ܷ����4����ӻ�˳��
                if (canFormGroups(remainingTiles)) {
                    return true;
                }
            }
        }

        return false;
    }

    // ���ʣ�����ܷ����4����ӻ�˳��
    private boolean canFormGroups(List<Integer> tiles) {
        if (tiles.isEmpty()) {
            return true;
        }

        // ��ȡ��ǰ��С����
        int minTile = tiles.get(0);

        // ������ɿ���
        if (tiles.size() >= 3 &&
                tiles.get(1) == minTile &&
                tiles.get(2) == minTile) {

            // �Ƴ�����
            List<Integer> newTiles = new ArrayList<>(tiles);
            newTiles.remove(0);
            newTiles.remove(0);
            newTiles.remove(0);

            // �ݹ���ʣ����
            if (canFormGroups(newTiles)) {
                return true;
            }
        }

        // �������˳��
        if (tiles.contains(minTile + 1) && tiles.contains(minTile + 2)) {
            // �Ƴ�˳��
            List<Integer> newTiles = new ArrayList<>(tiles);
            newTiles.remove(Integer.valueOf(minTile));
            newTiles.remove(Integer.valueOf(minTile + 1));
            newTiles.remove(Integer.valueOf(minTile + 2));

            // �ݹ���ʣ����
            if (canFormGroups(newTiles)) {
                return true;
            }
        }

        return false;
    }

    public static void main(String[] args) {
        MahjongWinChecker checker = new MahjongWinChecker();

        // ����������11123456778899 (����)
        int[] winTiles = {1, 1, 1, 2, 3, 4, 5, 6, 7, 7, 8, 8, 9, 9};
        System.out.println(checker.checkWin(winTiles)); // ���true

        // ����������12345678912345 (������)
        int[] loseTiles = {1, 2, 3, 4, 5, 6, 7, 8, 9, 1, 2, 3, 4, 5};
        System.out.println(checker.checkWin(loseTiles)); // ���false
    }
}