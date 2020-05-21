package 生命游戏;

import javax.swing.*;

public class CellControl extends JButton {
    private int[][] site;//细胞的坐标位置
    private int cell_num;

    CellControl(int cell_num){
        this.cell_num = cell_num;
        this.site = new int[cell_num][cell_num];
        for(int i=0;i<cell_num;i++)
            for(int j=0;j<cell_num;j++)
                site[i][j]=0;
    }

    //初始化
    void setSite(int i, int j) {
        site[i][j]=1;
    }

    //根据坐标对应值改变颜色
    int[][] update() {
        return site;
    }

    //刷新
    void flash(){
        for(int i=0;i<cell_num;i++)
            for(int j=0;j<cell_num;j++)
                site[i][j]=0;
    }

    //产生下一代算法
    void nextCells(){
        //用来遍历site的辅助数组
        int[][] help = new int[cell_num + 2][cell_num + 2];
        for(int i=0;i<cell_num;i++){
            for(int j=0;j<cell_num;j++){
                help[i+1][j+1]=site[i][j];
            }
        }
        //用来存储繁衍后的细胞分布的数组
        int[][] newSite = new int[cell_num + 2][cell_num + 2];
        for (int i = 0; i < cell_num; i++)
            for (int j = 0; j < cell_num; j++)
                switch (getNeighborCount(help, i+1, j+1)) {
                    case 2:
                        newSite[i+1][j+1] = site[i][j];//细胞状态保持不变
                        break;
                    case 3:
                        newSite[i+1][j+1] = 1; // 细胞活着
                        break;
                    default:
                        newSite[i+1][j+1] = 0; // 细胞死了
                }
        for (int i = 0; i < cell_num; i++)
            for (int j = 0; j < cell_num; j++)
                site[i][j] = newSite[i+1][j+1];
    }

    //获取细胞的邻居数量
    private int getNeighborCount(int[][] help, int x, int y) {
        int count = 0;
        for (int i = x - 1; i <= x + 1; i++)
            for (int j = y - 1; j <= y + 1; j++)
                count += help[i][j]; //如果邻居还活着，邻居数便会+1
        count -= help[x][y]; // 减去细胞本身
        return count;
    }
}
