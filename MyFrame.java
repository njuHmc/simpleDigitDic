import javax.swing.*;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;

public class MyFrame extends JFrame{

	private JPanel pUP = new JPanel();//上部面板
	private JPanel pDOWN = new JPanel();//下部面板
	private JLabel lWord = new JLabel("     单词      ");//提示输入单词的标签
	private JLabel right = new JLabel("     ");
	private JLabel down =new JLabel(" ");
	private JList<String> lAssociation = new JList<String>();//联想框
	private ListModel<String> lm;
	private JScrollPane sp = new JScrollPane();//滚动条panel
	private JTextArea mainPerform = new JTextArea();//主显框
	private JTextArea enterBox = new JTextArea(1,26);//输入框
	private JButton button = new JButton("搜索");//进入搜索的按钮
	private Dic dic;
	
	public MyFrame() throws FileNotFoundException
	{
		
		GraphicsEnvironment e =GraphicsEnvironment.getLocalGraphicsEnvironment();
		String[] fontnames = e.getAvailableFontFamilyNames();
		for(int i = 0; i < fontnames.length; i++)
			System.out.println(fontnames[i]);
		
		dic = new Dic();//读入字典
		
		lm = new DefaultComboBoxModel<String>(dic.getWord());//将字典list加入默认模式中
		lAssociation.setModel(lm);//在联想框里显示所有字典单词
		lAssociation.setPreferredSize(new Dimension(500, 31*dic.getNum()));//设置联想框显示框大小
		lAssociation.setBackground(new Color(240, 255, 255));//设置联想框背景色
//		lAssociation.setVisibleRowCount(31*dic.getNum());//设置联想框可显示行数
		lAssociation.setLayoutOrientation(JList.VERTICAL);//设置联想框是否多列显示多行显示
		
		sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);//在需要时显示横滚动条
		sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);//在需要时显示竖滚动条
		
		lAssociation.addListSelectionListener(new ListSelectionListener()//响应list中项目被单击选中事件
				{
					@Override
					public void valueChanged(ListSelectionEvent e)
					{
	                    JList<String> myList = (JList<String>) e.getSource();
	                    int index = myList.getSelectedIndex();//已选项的下标
	                    Object obj = myList.getModel().getElementAt(index);//取出数据
	                    if(obj!=null)
	                    	mainPerform.setText(obj.toString() + "\r\n" + dic.search(obj.toString()));
					}
				});
/*		lAssociation.addMouseListener(new MouseAdapter()//响应list中项目被双击选中事件
		{
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() == 2)
                {
                    JList<String> myList = (JList<String>) e.getSource();
                    int index = myList.getSelectedIndex();    //已选项的下标
                    Object obj = myList.getModel().getElementAt(index);  //取出数据
                    mainPerform.setText(obj.toString() + "\r\n" + dic.search(obj.toString()));
                }
            }
        });*/
/*		public Object getSelectedValue（）；//可以返回用户选择的项目的内容
		public int getSelectedIndex（）；//返回用户选择的项目的下标，从0开始
		public Object getSelectedValues（）
*/
	    sp.setPreferredSize(new Dimension(200, dic.getNum()));//设置滚动条要显示的大小
	    sp.setViewportView(lAssociation);//将单词list加入滚动条框中
//		lAssociation.setName("联想框");
//		lAssociation.setBounds(5, 5, 10, 120);
		
		pUP.setLayout(new FlowLayout(FlowLayout.LEFT, 30, 10));//上部面板布局
		
		mainPerform.setEditable(false);//主显框内容不可修改
		mainPerform.setLineWrap(true);
		mainPerform.setWrapStyleWord(true);
		mainPerform.setBackground(new Color(240, 255, 255));
		mainPerform.setSelectedTextColor(Color.WHITE);//被选中文字的颜色
		mainPerform.setSelectionColor(Color.BLACK);//被选中文字的背景色
		
		enterBox.getDocument().addDocumentListener(new DocumentListener()
				{

					@Override
					public void changedUpdate(DocumentEvent e) {
						// TODO
						if(enterBox.getText().length()==0)
							lm = new DefaultComboBoxModel<String>(dic.getWord());
						else
							lm = new DefaultComboBoxModel<String>(dic.association(enterBox.getText()));
						lAssociation.setModel(lm);
					}

					@Override
					public void insertUpdate(DocumentEvent e) {
						// TODO
						lm = new DefaultComboBoxModel<String>(dic.association(enterBox.getText()));
						lAssociation.setModel(lm);
					}

					@Override
					public void removeUpdate(DocumentEvent e) {
						// TODO
						if(enterBox.getText().length()==0)
							lm = new DefaultComboBoxModel<String>(dic.getWord());
						else
							lm = new DefaultComboBoxModel<String>(dic.association(enterBox.getText()));
						lAssociation.setModel(lm);
					}
				
				});
		
		lWord.setFont(new Font("华文行楷", 0, 30));
		mainPerform.setFont(new Font("楷体", 0, 35));
		enterBox.setFont(new Font("Serif", 0, 25));

		pUP.add(lWord);
		pUP.add(enterBox);
		
		button.setFont(new Font("华文新魏", 0, 30));
		button.setBackground(Color.white);
		button.setForeground(Color.blue);
		pUP.add(button);
		button.addActionListener(new ButtonListener());
		
		pUP.setBackground(new Color(187, 255, 255));
//		pUP.setForeground(Color.WHITE);
		
		pDOWN.setLayout(new BorderLayout(5, 5));
		pDOWN.add(pUP, BorderLayout.NORTH);
		
		lAssociation.setFont(new Font("Serif", 0, 20));
		right.setFont(new Font("Serif", 0, 20));
		down.setFont(new Font("Serif", 0, 20));
		
		pDOWN.add(right, BorderLayout.EAST);
		pDOWN.add(down, BorderLayout.SOUTH);
		pDOWN.add(sp, BorderLayout.WEST);
		pDOWN.add(mainPerform, BorderLayout.CENTER);
		
		pDOWN.setBackground(new Color(187, 255, 255));
		
		add(pDOWN, BorderLayout.CENTER);//将下部面板加入主框架中
	}


	class ButtonListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e)//响应搜索
		{
			String exp = dic.search(enterBox.getText());
			mainPerform.setText(enterBox.getText() + "\r\n" + exp);//可以实时用此语句更新文本内容
		}
		
	}
	
}
