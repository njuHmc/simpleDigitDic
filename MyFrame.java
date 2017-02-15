import javax.swing.*;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;

public class MyFrame extends JFrame{

	private JPanel pUP = new JPanel();//�ϲ����
	private JPanel pDOWN = new JPanel();//�²����
	private JLabel lWord = new JLabel("     ����      ");//��ʾ���뵥�ʵı�ǩ
	private JLabel right = new JLabel("     ");
	private JLabel down =new JLabel(" ");
	private JList<String> lAssociation = new JList<String>();//�����
	private ListModel<String> lm;
	private JScrollPane sp = new JScrollPane();//������panel
	private JTextArea mainPerform = new JTextArea();//���Կ�
	private JTextArea enterBox = new JTextArea(1,26);//�����
	private JButton button = new JButton("����");//���������İ�ť
	private Dic dic;
	
	public MyFrame() throws FileNotFoundException
	{
		
		GraphicsEnvironment e =GraphicsEnvironment.getLocalGraphicsEnvironment();
		String[] fontnames = e.getAvailableFontFamilyNames();
		for(int i = 0; i < fontnames.length; i++)
			System.out.println(fontnames[i]);
		
		dic = new Dic();//�����ֵ�
		
		lm = new DefaultComboBoxModel<String>(dic.getWord());//���ֵ�list����Ĭ��ģʽ��
		lAssociation.setModel(lm);//�����������ʾ�����ֵ䵥��
		lAssociation.setPreferredSize(new Dimension(500, 31*dic.getNum()));//�����������ʾ���С
		lAssociation.setBackground(new Color(240, 255, 255));//��������򱳾�ɫ
//		lAssociation.setVisibleRowCount(31*dic.getNum());//������������ʾ����
		lAssociation.setLayoutOrientation(JList.VERTICAL);//����������Ƿ������ʾ������ʾ
		
		sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);//����Ҫʱ��ʾ�������
		sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);//����Ҫʱ��ʾ��������
		
		lAssociation.addListSelectionListener(new ListSelectionListener()//��Ӧlist����Ŀ������ѡ���¼�
				{
					@Override
					public void valueChanged(ListSelectionEvent e)
					{
	                    JList<String> myList = (JList<String>) e.getSource();
	                    int index = myList.getSelectedIndex();//��ѡ����±�
	                    Object obj = myList.getModel().getElementAt(index);//ȡ������
	                    if(obj!=null)
	                    	mainPerform.setText(obj.toString() + "\r\n" + dic.search(obj.toString()));
					}
				});
/*		lAssociation.addMouseListener(new MouseAdapter()//��Ӧlist����Ŀ��˫��ѡ���¼�
		{
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() == 2)
                {
                    JList<String> myList = (JList<String>) e.getSource();
                    int index = myList.getSelectedIndex();    //��ѡ����±�
                    Object obj = myList.getModel().getElementAt(index);  //ȡ������
                    mainPerform.setText(obj.toString() + "\r\n" + dic.search(obj.toString()));
                }
            }
        });*/
/*		public Object getSelectedValue������//���Է����û�ѡ�����Ŀ������
		public int getSelectedIndex������//�����û�ѡ�����Ŀ���±꣬��0��ʼ
		public Object getSelectedValues����
*/
	    sp.setPreferredSize(new Dimension(200, dic.getNum()));//���ù�����Ҫ��ʾ�Ĵ�С
	    sp.setViewportView(lAssociation);//������list�������������
//		lAssociation.setName("�����");
//		lAssociation.setBounds(5, 5, 10, 120);
		
		pUP.setLayout(new FlowLayout(FlowLayout.LEFT, 30, 10));//�ϲ���岼��
		
		mainPerform.setEditable(false);//���Կ����ݲ����޸�
		mainPerform.setLineWrap(true);
		mainPerform.setWrapStyleWord(true);
		mainPerform.setBackground(new Color(240, 255, 255));
		mainPerform.setSelectedTextColor(Color.WHITE);//��ѡ�����ֵ���ɫ
		mainPerform.setSelectionColor(Color.BLACK);//��ѡ�����ֵı���ɫ
		
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
		
		lWord.setFont(new Font("�����п�", 0, 30));
		mainPerform.setFont(new Font("����", 0, 35));
		enterBox.setFont(new Font("Serif", 0, 25));

		pUP.add(lWord);
		pUP.add(enterBox);
		
		button.setFont(new Font("������κ", 0, 30));
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
		
		add(pDOWN, BorderLayout.CENTER);//���²��������������
	}


	class ButtonListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e)//��Ӧ����
		{
			String exp = dic.search(enterBox.getText());
			mainPerform.setText(enterBox.getText() + "\r\n" + exp);//����ʵʱ�ô��������ı�����
		}
		
	}
	
}
