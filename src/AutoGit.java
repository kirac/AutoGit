import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class AutoGit extends JFrame implements ActionListener {
    private JPanel mBranchPanel;
    private JPanel mCherryPickIdPanel;
    private JPanel mSummitPanel;

    private JLabel mBranchLabel;
    private JLabel mCherryPickIdLabel;

    private JButton mSummitBtn;

    private JTextField mBranchText;
    private JTextField mCherryPickIdText;

    private String saveDataName = "branch.data";
    private String savePathName = "path.data";

    public AutoGit() {
        mBranchPanel = new JPanel();
        mCherryPickIdPanel = new JPanel();
        mSummitPanel = new JPanel();


        mBranchLabel = new JLabel("添加的分支名[,]");
        mCherryPickIdLabel = new JLabel("Cherry-Pick-Id");

        mSummitBtn = new JButton("添加");
        mBranchText = new JTextField(20);
        try {
            String branchName = readData(saveDataName);
            mBranchText.setText(branchName);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

        savePersons(mBranchText.getText(), saveDataName);
        if ("".equals(mCherryPickIdText.getText()) || "".equals(mBranchText.getText())) {
            JOptionPane.showMessageDialog(null, "请输入分支名称和CherryPickId！！！", "消息", JOptionPane.WARNING_MESSAGE);
            return;
        }
        System.out.println(mCherryPickIdText.getText());
        String filePath = getFilePath();
        savePersons(filePath, savePathName);
        //获取完路径后，先切割分支名称
        String branchText = mBranchText.getText();
        String cherryPickId = mCherryPickIdText.getText();
        String[] branchArray = branchText.split(",");
        for (String branchName : branchArray) {
            writeTxtFile("\n" + "git checkout " + branchName + "\n"
                            + "git pull" + "\n"
                            + "git cherry-pick " + cherryPickId + "\n"
                            + "git commit" + "\n"
                            + "git push" + "\n"
                    , new File(filePath), "UTF-8");
        }
    }

    private void savePersons(String data, String saveDataName) {
        // 保存文件内容
        try {
            FileWriter writer = new FileWriter(saveDataName);
            writer.write(data);
            writer.close();
            System.out.println("对象保存完毕。");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 从文件中读取分支名称
    private String readData(String name) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(name));
        String line;
        StringBuilder stringBuilder = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
        }

        return stringBuilder.toString();
    }

    public String getFilePath() {

        JFileChooser fc = new JFileChooser();
        fc.setDialogType(JFileChooser.FILES_ONLY);
        fc.setDialogTitle("选择文件");
        fc.setMultiSelectionEnabled(false);
        try {
            String filePath = readData(savePathName);
            if (!"".equals(filePath)) {
                fc.setSelectedFile(new File(filePath));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        fc.showSaveDialog(fc);
        if (fc.getSelectedFile() == null) {
            return null;
        }
        return fc.getSelectedFile().getPath();
    }

    public boolean writeTxtFile(String content, File fileName, String encoding) {

        FileOutputStream fos = null;
        boolean result = false;
        try {
            if (!fileName.exists()) {
                fileName.createNewFile();//如果文件不存在，就创建该文件
                fos = new FileOutputStream(fileName);//首次写入获取
            } else {
                //如果文件已存在，那么就在文件末尾追加写入
                fos = new FileOutputStream(fileName, true);//这里构造方法多了一个参数true,表示在文件末尾追加写入
            }
            fos.write(content.getBytes(encoding));
            result = true;
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
