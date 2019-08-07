import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class AutoGit  extends JFrame implements ActionListener {
    private  JPanel mBranchPanel;
    private  JPanel mCherryPickIdPanel;
    private  JPanel mSummitPanel;

    private  JLabel mBranchLabel;
    private  JLabel mCherryPickIdLabel;

    private  JButton mSummitBtn;

    private  JTextField mBranchText;
    private  JTextField mCherryPickIdText;

    public AutoGit() {
        mBranchPanel = new JPanel();
        mCherryPickIdPanel = new JPanel();
        mSummitPanel = new JPanel();

        mBranchLabel = new JLabel("添加的分支名[,]");
        mCherryPickIdLabel = new JLabel("Cherry-Pick-Id");

        mSummitBtn = new JButton("添加");
        mBranchText = new JTextField(20);
        mCherryPickIdText = new JTextField(20);
        this.setLayout(new GridLayout(3, 1));

        // 加入各个组件
        mBranchPanel.add(mBranchLabel);
        mBranchPanel.add(mBranchText);

        mCherryPickIdPanel.add(mCherryPickIdLabel);
        mCherryPickIdPanel.add(mCherryPickIdText);

        mSummitPanel.add(mSummitBtn);


        // 加入到JFrame
        this.add(mBranchPanel);
        this.add(mCherryPickIdPanel);
        this.add(mSummitPanel);

        this.setSize(600, 200);
        this.setTitle("添加分支名与CherryPickId");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        mSummitBtn.addActionListener(this);
    }
    public static void main(String[] args) {
        new AutoGit();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if ("".equals(mCherryPickIdText.getText()) || "".equals(mBranchText.getText()))
        {
            JOptionPane.showMessageDialog(null,"请输入分支名称和CherryPickId！！！","消息",JOptionPane.WARNING_MESSAGE);
            return;
        }
        System.out.println(mCherryPickIdText.getText());
        String filePath = getFilePath();
        //获取完路径后，先切割分支名称
        String branchText = mBranchText.getText();
       String cherryPickId= mCherryPickIdText.getText();
        String[] branchArray = branchText.split(",");
        for (String branchName : branchArray) {
            writeTxtFile("\n"+"git checkout "+branchName+"\n"
                    +"git pull"+"\n"
                    +"git cherry-pick "+cherryPickId+"\n"
                    +"git commit"+"\n"
                    +"git push"+"\n"
                    ,new File(filePath),"UTF-8");
        }
    }
    public String getFilePath()
    {
        JFileChooser fc = new JFileChooser();
        fc.setDialogType(JFileChooser.FILES_ONLY);
        fc.setDialogTitle("选择文件");
        fc.setMultiSelectionEnabled(false);
        fc.showSaveDialog(fc);
        if (fc.getSelectedFile()==null) {
            return null;
        }
        return fc.getSelectedFile().getPath();
    }

    public  boolean writeTxtFile(String content, File fileName, String encoding) {

        FileOutputStream fos = null;
        boolean result=false;
        try {
            if(!fileName.exists()){
                fileName.createNewFile();//如果文件不存在，就创建该文件
                fos = new FileOutputStream(fileName);//首次写入获取
            }else{
                //如果文件已存在，那么就在文件末尾追加写入
                fos = new FileOutputStream(fileName,true);//这里构造方法多了一个参数true,表示在文件末尾追加写入
            }
            fos.write(content.getBytes(encoding));
            result=true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }
}
