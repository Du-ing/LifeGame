package 生命游戏;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GameMap extends JFrame {
    private final int length = 600;//界面尺寸
    private final int cell_long = 30;//细胞尺寸
    private final int cell_num = length / cell_long;//一列或一行细胞数量
    private JPanel backPanel, centerPanel, bottomPanel;
    private JButton[][] cells;//所有细胞
    private CellControl cellControl;//细胞控制器
    private int[][] site;//位置
    private JButton start, stop, flash;
    private Thread myThread;//线程
    private boolean running, isPause;//线程运行状态

    //构造函数，资源的初始化准备
    GameMap() {
        initCellControl();//添加细胞控制器
        addJPanel();//添加面板
        creatCells();//初始化细胞组
        addControl();//添加控制按钮

        this.setTitle("生命游戏");//标题
        this.setSize(length, length);//设置界面长宽
        this.setLocationRelativeTo(null);//设置界面位置
        this.setResizable(true);
        this.setVisible(true);//可见
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);//界面关闭时整个程序也关闭
    }

    //添加面板
    void addJPanel() {
        backPanel = new JPanel(new BorderLayout());
        centerPanel = new JPanel(new GridLayout(cell_num, cell_num,0,0));
        bottomPanel = new JPanel(new FlowLayout());
        this.setContentPane(backPanel);
        backPanel.add(centerPanel, "Center");
        backPanel.add(bottomPanel, "South");
    }

    //添加控制按钮
    void addControl() {
        start = new JButton("开始繁衍");
        start.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                //--------------------------------------------------------
                myThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        running = true;
                        isPause = false;
                        while(running&&!isPause){
                            cellControl.nextCells();//繁衍下一代
                            updateColor();//更新颜色
                            try {
                                myThread.sleep(500);
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                            running = isOver();//是否全部死亡
                        }
                        if(!running){
                            JFrame jf = new JFrame("提示");
                            jf.add(new JLabel("         所有细胞已死亡！"));
                            jf.setSize(100,100);
                            jf.setLocationRelativeTo(null);
                            jf.setVisible(true);
                        }
                    }
                });
                myThread.start();//开启线程
            }
        });

        stop = new JButton("暂停繁衍");
        stop.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                //--------------------------------------------------------
                isPause = true;
            }
        });

        flash = new JButton("刷新");
        flash.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                //--------------------------------------------------------
                cellControl.flash();//刷新
                updateColor();
            }
        });
        bottomPanel.add(start);
        bottomPanel.add(stop);
        bottomPanel.add(flash);
    }

    //初始化细胞
    void creatCells() {
        cells = new JButton[cell_num][cell_num];
        for (int i = 0; i < cell_num; i++) {
            for (int j = 0; j < cell_num; j++) {
                cells[i][j] = new JButton(); //按钮内容置空以表示细胞
                cells[i][j].setBackground(Color.WHITE); //初始时所有细胞均为白色，即死亡状态
                cellListener(cells[i][j], i, j);
                centerPanel.add(cells[i][j]);
            }
        }
    }

    //判断细胞是否已全部死亡
    boolean isOver(){
        boolean flag = false;
        for(int i=0;i<cell_num;i++){
            for(int j=0;j<cell_num;j++){
                if(site[i][j] == 1) //只要有一个还活着
                    flag = true;
            }
        }
        return flag;
    }

    //初始化细胞控制器
    void initCellControl(){
        cellControl = new CellControl(cell_num);
    }

    //给细胞按钮增加监听
    void cellListener(JButton cell, int i, int j) {
        cell.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                cellControl.setSite(i, j);
                updateColor();
            }
        });
    }

    //更新颜色
    void updateColor(){
        site = cellControl.update();
        for(int i=0;i<cell_num;i++){
            for(int j=0;j<cell_num;j++){
                if(site[i][j] == 1){
                    cells[i][j].setBackground(Color.blue);
                }
                else{
                    cells[i][j].setBackground(Color.white);
                }
            }
        }
    }
}
